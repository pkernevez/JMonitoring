package org.jmonitoring.agent.info.impl;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class TreeTracerHelper
{
    private static Log sLog = LogFactory.getLog(TreeTracerHelper.class);

    private static Map<Class<?>, List<Method>> sListOfGetterCache = new Hashtable<Class<?>, List<Method>>();

    /**
     * The list of all entities that have already been traced by this instance. This is for preventing infinite loops.
     */
    private final Set<Object> mObjectAlreadyTrace = new HashSet<Object>();

    private int mMaxDepth = 0;

    private int mCurrentDepth = 0;

    public int getMaxDepth()
    {
        return mMaxDepth;
    }

    private int mNbEntity = 0;

    public int getNbEntity()
    {
        return mNbEntity;
    }

    public void traceObjectTree(StringBuilder pBuffer, Object pObject)
    {
        traceObjectTree("", pBuffer, pObject);
    }

    private void traceObjectTree(String pIndent, StringBuilder pBuffer, Object pObject)
    {
        if (pObject != null)
        {
            if (pObject instanceof List)
            {
                traceList(pIndent, pBuffer, (List<?>) pObject);
            } else if (pObject instanceof Set)
            {
                traceSet(pIndent, pBuffer, (Set<?>) pObject);
            } else if (pObject instanceof Map)
            {
                traceMap(pIndent, pBuffer, (Map<?, ?>) pObject);
            } else if (pObject instanceof Collection)
            {
                sLog.error("Collection type not yet implemented... Class=[" + pObject.getClass().getName());
            } else if (pObject.getClass().isArray())
            {
                traceArray(pIndent, pBuffer, pObject);
            } else
            {
                traceBizObject(pIndent, pBuffer, pObject);
            }
        }
    }

    void traceArray(String pIndent, StringBuilder pBuffer, Object pArray)
    {
        String tNewIndent;
        if (mObjectAlreadyTrace.contains(pArray))
        {
            pBuffer.append("[ALREADY DONE!] " + Array.class.getName() + "\n");
        } else
        {
            pBuffer.append(Array.class.getName() + "\n");
            mObjectAlreadyTrace.add(pArray);
            for (int i = 0; i < Array.getLength(pArray); i++)
            {
                pBuffer.append(pIndent + "  |-- pos" + (i + 1) + " --> ");
                tNewIndent = pIndent + "  |" + getSpaceInsteadOfInt(i + 1) + "           ";
                traceObjectTree(tNewIndent, pBuffer, Array.get(pArray, i));
            }
        }
    }

    void traceList(String pIndent, StringBuilder pBuffer, List<?> pList)
    {
        String tNewIndent;
        if (mObjectAlreadyTrace.contains(pList))
        {
            pBuffer.append("[ALREADY DONE!] " + List.class.getName() + "\n");
        } else
        {
            pBuffer.append(List.class.getName() + "\n");
            mObjectAlreadyTrace.add(pList);
            for (int i = 0; i < pList.size(); i++)
            {
                pBuffer.append(pIndent + "  |-- pos" + (i + 1) + " --> ");
                tNewIndent = pIndent + "  |" + getSpaceInsteadOfInt(i + 1) + "           ";
                traceObjectTree(tNewIndent, pBuffer, pList.get(i));
            }
        }
    }

    void traceSet(String pIndent, StringBuilder pBuffer, Set<?> pSet)
    {
        String tNewIndent;
        pBuffer.append(Set.class.getName() + "\n");
        mObjectAlreadyTrace.add(pSet);
        int i = 0;
        for (Object tObject : pSet)
        {
            pBuffer.append(pIndent + "  |-- pos" + (i + 1) + " --> ");
            tNewIndent = pIndent + "  |" + getSpaceInsteadOfInt(i + 1) + "           ";
            traceObjectTree(tNewIndent, pBuffer, tObject);
            i++;
        }
    }

    void traceMap(String pIndent, StringBuilder pBuffer, Map<?, ?> pMap)
    {
        String tNewIndent;
        if (mObjectAlreadyTrace.contains(pMap))
        {
            pBuffer.append("[ALREADY DONE!] " + Map.class.getName() + "\n");
        } else
        {
            pBuffer.append(Map.class.getName() + "\n");
            mObjectAlreadyTrace.add(pMap);
            int i = 0;
            for (Object tObject : pMap.values())
            {
                pBuffer.append(pIndent + "  |-- pos" + (i + 1) + " --> ");
                tNewIndent = pIndent + "  |" + getSpaceInsteadOfInt(i + 1) + "           ";
                traceObjectTree(tNewIndent, pBuffer, tObject);
                i++;
            }
        }
    }

    // TODO make an optimization with static final field for the 10 first value and computation for others
    static String getSpaceInsteadOfInt(int pI)
    {
        int tSize = ("" + pI).length();
        StringBuilder tBuf = new StringBuilder();
        for (int i = 0; i < tSize; i++)
        {
            tBuf.append(" ");
        }
        return tBuf.toString();
    }

    void traceBizObject(String pIndent, StringBuilder pBuffer, Object pObject)
    {
        if (mObjectAlreadyTrace.contains(pObject))
        {
            pBuffer.append("[ALREADY DONE!] " + pObject.getClass().getName() + "\n");
        } else
        {
            mNbEntity++;
            mCurrentDepth++;
            mMaxDepth = Math.max(mCurrentDepth, mMaxDepth);
            mObjectAlreadyTrace.add(pObject);
            pBuffer.append(pObject.getClass().getName() + "\n");
            String tNewIndent = pIndent + "  |";
            String tLocalIndent;
            List<Method> tListOfGetters = getListOfGetters(pObject.getClass());
            Object curChild;
            for (Method tMeth : tListOfGetters)
            {
                try
                {
                    tLocalIndent = tNewIndent;
                    curChild = tMeth.invoke(pObject, new Object[0]);
                    if (curChild != null)
                    {
                        pBuffer.append(tLocalIndent + "-- " + tMeth.getName() + " --> ");
                        traceObjectTree(tLocalIndent, pBuffer, curChild);
                    }
                } catch (IllegalArgumentException e)
                {
                    sLog.warn("Unable to trace the Object =[" + pObject + "] for Method=[" + tMeth + "]", e);
                } catch (IllegalAccessException e)
                {
                    sLog.warn("Unable to trace the Object =[" + pObject + "] for Method=[" + tMeth + "]", e);
                } catch (InvocationTargetException e)
                {
                    sLog.warn("Unable to trace the Object =[" + pObject + "] for Method=[" + tMeth + "]", e);
                }
            }
            mCurrentDepth--;
        }
    }

    /**
     * Return the list of getter for the specified class.
     * 
     * @param pClass The class for which we need accessors.
     * @return The list of accessors <code>method</code>.
     */
    static List<Method> getListOfGetters(Class<?> pClass)
    {
        List<Method> tResult = sListOfGetterCache.get(pClass);

        if (tResult == null)
        {
            Method[] tMethods = pClass.getMethods();
            tResult = new ArrayList<Method>();
            Method curMethod;
            for (int i = 0; i < tMethods.length; i++)
            {
                curMethod = tMethods[i];
                if ((curMethod.getModifiers() & Modifier.STATIC) == 0)
                    if (curMethod.getName().startsWith("get"))
                    {
                        Class<?> tReturnClass = curMethod.getReturnType();
                        if (!tReturnClass.isPrimitive() && curMethod.getParameterTypes().length == 0)
                        {
                            boolean tIsBizClass = !tReturnClass.getName().startsWith("sun.") && !tReturnClass.getName()
                                            .startsWith("java");
                            tIsBizClass = tIsBizClass || (Collection.class.isAssignableFrom(tReturnClass) || Map.class
                                            .isAssignableFrom(tReturnClass) || Object.class.equals(tReturnClass));
                            if (tIsBizClass)
                            {
                                tResult.add(curMethod);
                            }
                        }
                    }
            }
            sListOfGetterCache.put(pClass, tResult);
        }
        return tResult;
    }

}
