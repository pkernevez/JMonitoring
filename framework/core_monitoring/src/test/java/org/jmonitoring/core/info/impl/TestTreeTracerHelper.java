package org.jmonitoring.core.info.impl;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;

public class TestTreeTracerHelper extends TestCase
{

    public static class Mother
    {
        private List mChildren1 = new ArrayList();

        private Child1 mChild1;

        private Child2[] mChildren2;

        private Map mChildren2Bis = new HashMap();

        private Set mChildren2Ter = new HashSet();

        private Child2[] mChildren2Qua;

        private Date mBidon = new Date();

        public static Mother getInstance()
        {
            return new Mother();
        }

        public Date getBidon()
        {
            return mBidon;
        }

        public void setBidon(Date pBidon)
        {
            mBidon = pBidon;
        }

        public Child1 getChild1()
        {
            return mChild1;
        }

        public void setChild1(Child1 pChild1)
        {
            mChild1 = pChild1;
        }

        public List getChildren1()
        {
            return mChildren1;
        }

        public void setChildren1(List pChildren1)
        {
            mChildren1 = pChildren1;
        }

        public Child2[] getChildren2()
        {
            return mChildren2;
        }

        public void setChildren2(Child2[] pChildren2)
        {
            mChildren2 = pChildren2;
        }

        public Map getChildren2Bis()
        {
            return mChildren2Bis;
        }

        public void setChildren2Bis(Map pChildren2Bis)
        {
            mChildren2Bis = pChildren2Bis;
        }

        public Child2[] getChildren2Qua()
        {
            return mChildren2Qua;
        }

        public void setChildren2Qua(Child2[] pChildren2Qua)
        {
            mChildren2Qua = pChildren2Qua;
        }

        public Set getChildren2Ter()
        {
            return mChildren2Ter;
        }

        public void setChildren2Ter(Set pChildren2Ter)
        {
            mChildren2Ter = pChildren2Ter;
        }

    }

    public static class Child1
    {
        private List mChildren2 = new ArrayList();

        private Set mChildren2Bis = new HashSet();

        public List getChildren2()
        {
            return mChildren2;
        }

        public void setChildren2(List pChildren2)
        {
            mChildren2 = pChildren2;
        }

        public Child1()
        {
        }

        public Child1(Child2 pChild2)
        {
            mChildren2.add(pChild2);
        }

        public Set getChildren2Bis()
        {
            return mChildren2Bis;
        }

        public void setChildren2Bis(Set pChidren2Bis)
        {
            mChildren2Bis = pChidren2Bis;
        }
    }

    public static class Child2
    {
        private Child2 mChild2;

        public ClassLoader getMyClassLoader()
        {
            return this.getClass().getClassLoader();
        }

        protected Child2 getChildProtected()
        {
            return mChild2;
        }

        public Child2 getChild2()
        {
            return mChild2;
        }

        public void setChild2(Child2 pChild2)
        {
            mChild2 = pChild2;
        }
    }

    /**
     * This test that 2 differents empty Set does print [ALREADY DONE] due to the implementation of the 'equals' and
     * 'hashcode' method of AbstractSet
     * 
     */
    public void testNotAlreadyDone()
    {
        List tList = new ArrayList();
        tList.add(new Child1(new Child2()));
        tList.add(new Child1());

        StringBuffer tBuffer = new StringBuffer();
        TreeTracerHelper tHelper = new TreeTracerHelper();
        tHelper.traceObjectTree(tBuffer, tList);

        tBuffer.append("\n \n");
        assertEquals(-1, tBuffer.indexOf("[ALREADY DONE!]"));
        assertEquals(3, tHelper.getNbEntity());
        assertEquals(2, tHelper.getMaxDepth());
    }

    public void testTraceObjectTree()
    {
        Mother tMother = new Mother();
        tMother.mChild1 = new Child1();
        tMother.mChild1.mChildren2.add(new Child2());
        tMother.mChild1.mChildren2.add(new Child2());
        tMother.mChildren1.add(new Child1(new Child2()));
        tMother.mChildren1.add(new Child1());
        Child2 tChild2 = new Child2();
        tChild2.setChild2(new Child2());
        tMother.setChildren2Qua(new Child2[] {tChild2, new Child2(), new Child2() });

        StringBuffer tBuffer = new StringBuffer();
        TreeTracerHelper tHelper = new TreeTracerHelper();
        tHelper.traceObjectTree(tBuffer, tMother);
        String tExpectedResultL01 = Mother.class.getName();
        String tExpectedResultL02 = "\n  |-- getChild1 --> " + Child1.class.getName();
        String tExpectedResultL03 = "\n  |  |-- getChildren2 --> " + List.class.getName();
        String tExpectedResultL04 = "\n  |  |  |-- pos1 --> " + Child2.class.getName();
        String tExpectedResultL05 = "\n  |  |  |-- pos2 --> " + Child2.class.getName();
        String tExpectedResultL06 = "\n  |  |-- getChildren2Bis --> " + Set.class.getName();
        String tExpectedResultL07 = "\n  |-- getChildren1 --> " + List.class.getName();
        String tExpectedResultL08 = "\n  |  |-- pos1 --> " + Child1.class.getName();
        String tExpectedResultL09 = "\n  |  |              |-- getChildren2 --> " + List.class.getName();
        String tExpectedResultL10 = "\n  |  |              |  |-- pos1 --> " + Child2.class.getName();
        String tExpectedResultL11 = "\n  |  |              |-- getChildren2Bis --> " + Set.class.getName();
        String tExpectedResultL12 = "\n  |  |-- pos2 --> " + Child1.class.getName();
        String tExpectedResultL13 = "\n  |  |              |-- getChildren2 --> " + List.class.getName();
        String tExpectedResultL14 = "\n  |  |              |-- getChildren2Bis --> " + Set.class.getName();
        String tExpectedResultL15 = "\n  |-- getChildren2Bis --> " + Map.class.getName();
        String tExpectedResultL16 = "\n  |-- getChildren2Qua --> " + Array.class.getName();
        String tExpectedResultL17 = "\n  |  |-- pos1 --> " + Child2.class.getName();
        String tExpectedResultL18 = "\n  |  |              |-- getChild2 --> " + Child2.class.getName();
        String tExpectedResultL19 = "\n  |  |-- pos2 --> " + Child2.class.getName();
        String tExpectedResultL20 = "\n  |  |-- pos3 --> " + Child2.class.getName();
        String tExpectedResultL21 = "\n  |-- getChildren2Ter --> " + Set.class.getName();

        System.out.println("---------------------------");
        String tResultString = tBuffer.toString();
        System.out.println(tResultString);
        assertEquals(21, StringUtils.countMatches(tResultString, "\n"));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL01));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL02));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL03));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL04));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL05));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL06));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL07));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL08));
        assertEquals(2, StringUtils.countMatches(tResultString, tExpectedResultL09));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL10));
        assertEquals(2, StringUtils.countMatches(tResultString, tExpectedResultL11));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL12));
        assertEquals(2, StringUtils.countMatches(tResultString, tExpectedResultL13));
        assertEquals(2, StringUtils.countMatches(tResultString, tExpectedResultL14));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL15));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL16));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL17));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL18));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL19));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL20));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL21));
        assertEquals(11, tHelper.getNbEntity());
        assertEquals(3, tHelper.getMaxDepth());
    }

    public void testGetListOfGetters()
    {
        List tMeth = TreeTracerHelper.getListOfGetters(Mother.class);
        assertEquals(6, tMeth.size());

        tMeth = TreeTracerHelper.getListOfGetters(Child1.class);
        assertEquals(2, tMeth.size());

        tMeth = TreeTracerHelper.getListOfGetters(Child2.class);
        assertEquals(1, tMeth.size());
    }

    public void testTraceList()
    {
        List tList = new ArrayList();
        tList.add(new Child2());
        tList.add(new Child2());
        tList.add(new Child2());
        StringBuffer tBuffer = new StringBuffer();
        TreeTracerHelper tHelper = new TreeTracerHelper();
        tHelper.traceList("", tBuffer, tList);
        String tExpectedResultL1 = List.class.getName();
        String tExpectedResultL2 = "  |-- pos1 --> " + Child2.class.getName();
        String tExpectedResultL3 = "  |-- pos2 --> " + Child2.class.getName();
        String tExpectedResultL4 = "  |-- pos3 --> " + Child2.class.getName();

        tBuffer.append("\n \n");
        StringTokenizer tTok = new StringTokenizer(tBuffer.toString(), "\n");
        assertEquals(tExpectedResultL1, tTok.nextToken());
        assertEquals(tExpectedResultL2, tTok.nextToken());
        assertEquals(tExpectedResultL3, tTok.nextToken());
        assertEquals(tExpectedResultL4, tTok.nextToken());
        assertEquals(1, tHelper.getMaxDepth());
        assertEquals(3, tHelper.getNbEntity());
    }

    public void testTraceSet()
    {
        Set tSet = new HashSet();
        tSet.add(new Child2());
        tSet.add(new Child2());
        tSet.add(new Child2());
        StringBuffer tBuffer = new StringBuffer();
        TreeTracerHelper tHelper = new TreeTracerHelper();
        tHelper.traceSet("", tBuffer, tSet);
        String tExpectedResultL1 = Set.class.getName();
        String tExpectedResultL2 = "  |-- pos1 --> " + Child2.class.getName();
        String tExpectedResultL3 = "  |-- pos2 --> " + Child2.class.getName();
        String tExpectedResultL4 = "  |-- pos3 --> " + Child2.class.getName();

        tBuffer.append("\n \n");
        StringTokenizer tTok = new StringTokenizer(tBuffer.toString(), "\n");
        assertEquals(tExpectedResultL1, tTok.nextToken());
        assertEquals(tExpectedResultL2, tTok.nextToken());
        assertEquals(tExpectedResultL3, tTok.nextToken());
        assertEquals(tExpectedResultL4, tTok.nextToken());

        assertEquals(1, tHelper.getMaxDepth());
        assertEquals(3, tHelper.getNbEntity());

    }

    public void testTraceArray()
    {
        Object tArray = Array.newInstance(Child2.class, 3);
        Array.set(tArray, 0, new Child2());
        Array.set(tArray, 1, new Child2());
        Array.set(tArray, 2, new Child2());

        StringBuffer tBuffer = new StringBuffer();
        TreeTracerHelper tHelper = new TreeTracerHelper();
        tHelper.traceArray("", tBuffer, tArray);
        String tExpectedResultL1 = Array.class.getName();
        String tExpectedResultL2 = "  |-- pos1 --> " + Child2.class.getName();
        String tExpectedResultL3 = "  |-- pos2 --> " + Child2.class.getName();
        String tExpectedResultL4 = "  |-- pos3 --> " + Child2.class.getName();

        tBuffer.append("\n \n");
        StringTokenizer tTok = new StringTokenizer(tBuffer.toString(), "\n");
        System.out.println(tExpectedResultL1 + "\n" + tExpectedResultL2 + "\n" + tExpectedResultL3 + "\n"
            + tExpectedResultL4);
        System.out.println("---------------------------");
        System.out.println(tBuffer.toString());
        assertEquals(tExpectedResultL1, tTok.nextToken());
        assertEquals(tExpectedResultL2, tTok.nextToken());
        assertEquals(tExpectedResultL3, tTok.nextToken());
        assertEquals(tExpectedResultL4, tTok.nextToken());

        assertEquals(1, tHelper.getMaxDepth());
        assertEquals(3, tHelper.getNbEntity());
    }

    public void testTraceMap()
    {
        Map tMap = new HashMap();
        tMap.put("key1", new Child2());
        tMap.put("key2", new Child2());
        tMap.put("key3", new Child2());
        StringBuffer tBuffer = new StringBuffer();
        TreeTracerHelper tHelper = new TreeTracerHelper();
        tHelper.traceMap("", tBuffer, tMap);
        String tExpectedResultL1 = Map.class.getName();
        String tExpectedResultL2 = "  |-- pos1 --> " + Child2.class.getName();
        String tExpectedResultL3 = "  |-- pos2 --> " + Child2.class.getName();
        String tExpectedResultL4 = "  |-- pos3 --> " + Child2.class.getName();

        tBuffer.append("\n \n");
        StringTokenizer tTok = new StringTokenizer(tBuffer.toString(), "\n");
        assertEquals(tExpectedResultL1, tTok.nextToken());
        assertEquals(tExpectedResultL2, tTok.nextToken());
        assertEquals(tExpectedResultL3, tTok.nextToken());
        assertEquals(tExpectedResultL4, tTok.nextToken());

        assertEquals(1, tHelper.getMaxDepth());
        assertEquals(3, tHelper.getNbEntity());
    }

    public void testTraceCircularyTree()
    {
        Child2 tChild = new Child2();
        Child2 tChild2 = new Child2();
        tChild.setChild2(tChild2);
        tChild2.setChild2(tChild);
        StringBuffer tBuffer = new StringBuffer();
        TreeTracerHelper tHelper = new TreeTracerHelper();
        tHelper.traceObjectTree(tBuffer, tChild);
        String tExpectedResultL1 = Child2.class.getName();
        String tExpectedResultL2 = "  |-- getChild2 --> " + Child2.class.getName();
        String tExpectedResultL3 = "  |  |-- getChild2 --> [ALREADY DONE!] " + Child2.class.getName();

        tBuffer.append("\n \n");
        StringTokenizer tTok = new StringTokenizer(tBuffer.toString(), "\n");
        assertEquals(tExpectedResultL1, tTok.nextToken());
        assertEquals(tExpectedResultL2, tTok.nextToken());
        assertEquals(tExpectedResultL3, tTok.nextToken());
        assertEquals(2, tHelper.getMaxDepth());
        assertEquals(2, tHelper.getNbEntity());
    }

    public void testTraceCircularyTreeSet()
    {
        Set tSet = new HashSet();
        Child1 tChild = new Child1();
        tSet.add(tChild);
        tChild.setChildren2Bis(tSet);
        StringBuffer tBuffer = new StringBuffer();
        TreeTracerHelper tHelper = new TreeTracerHelper();
        tHelper.traceSet("", tBuffer, tSet);
        String tExpectedResultL1 = Set.class.getName();
        String tExpectedResultL2 = "  |-- pos1 --> " + Child1.class.getName();
        String tExpectedResultL3 = "  |              |-- getChildren2 --> " + List.class.getName();
        String tExpectedResultL4 = "  |              |-- getChildren2Bis --> " + Set.class.getName();
        String tExpectedResultL5 = "  |              |  |-- pos1 --> [ALREADY DONE!] " + Child1.class.getName();
        tBuffer.append("\n \n");
        StringTokenizer tTok = new StringTokenizer(tBuffer.toString(), "\n");
        assertEquals(tExpectedResultL1, tTok.nextToken());
        assertEquals(tExpectedResultL2, tTok.nextToken());
        assertEquals(tExpectedResultL3, tTok.nextToken());
        assertEquals(tExpectedResultL4, tTok.nextToken());
        assertEquals(tExpectedResultL5, tTok.nextToken());

        assertEquals(1, tHelper.getMaxDepth());
        assertEquals(1, tHelper.getNbEntity());
    }

    public void testTraceCircularyTreeMap()
    {
        Mother tMother = new Mother();
        Map tMap = new HashMap();
        tMap.put("key1", tMother);
        tMother.setChildren2Bis(tMap);
        StringBuffer tBuffer = new StringBuffer();
        TreeTracerHelper tHelper = new TreeTracerHelper();
        tHelper.traceMap("", tBuffer, tMap);
        String tExpectedResultL1 = Map.class.getName() + "\n";
        String tExpectedResultL2 = "  |-- pos1 --> " + Mother.class.getName() + "\n";
        String tExpectedResultL3 = "  |              |-- getChildren1 --> " + List.class.getName() + "\n";
        String tExpectedResultL4 = "  |              |-- getChildren2Bis --> [ALREADY DONE!] " + Map.class.getName()
            + "\n";
        String tExpectedResultL5 = "  |              |-- getChildren2Ter --> " + Set.class.getName() + "\n";
        String tResultString = tBuffer.toString();
        assertEquals(5, StringUtils.countMatches(tResultString, "\n"));
        assertEquals(2, StringUtils.countMatches(tResultString, tExpectedResultL1));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL2));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL3));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL4));
        assertEquals(1, StringUtils.countMatches(tResultString, tExpectedResultL5));

        assertEquals(1, tHelper.getMaxDepth());
        assertEquals(1, tHelper.getNbEntity());
    }

    public void testTraceCircularyTreeList()
    {
        List tList = new ArrayList();
        Child1 tChild = new Child1();
        tList.add(tChild);
        tChild.setChildren2(tList);
        StringBuffer tBuffer = new StringBuffer();
        TreeTracerHelper tHelper = new TreeTracerHelper();
        tHelper.traceList("", tBuffer, tList);
        String tExpectedResultL1 = List.class.getName();
        String tExpectedResultL2 = "  |-- pos1 --> " + Child1.class.getName();
        String tExpectedResultL3 = "  |              |-- getChildren2 --> [ALREADY DONE!] " + List.class.getName();

        tBuffer.append("\n \n");
        StringTokenizer tTok = new StringTokenizer(tBuffer.toString(), "\n");
        assertEquals(tExpectedResultL1, tTok.nextToken());
        assertEquals(tExpectedResultL2, tTok.nextToken());
        assertEquals(tExpectedResultL3, tTok.nextToken());

        assertEquals(1, tHelper.getMaxDepth());
        assertEquals(1, tHelper.getNbEntity());
    }

    public void testGetSpaceInsteadOfInt()
    {
        assertEquals(" ", TreeTracerHelper.getSpaceInsteadOfInt(0));
        assertEquals(" ", TreeTracerHelper.getSpaceInsteadOfInt(9));
        assertEquals("  ", TreeTracerHelper.getSpaceInsteadOfInt(31));
        assertEquals("  ", TreeTracerHelper.getSpaceInsteadOfInt(99));
        assertEquals("   ", TreeTracerHelper.getSpaceInsteadOfInt(100));
        assertEquals("   ", TreeTracerHelper.getSpaceInsteadOfInt(999));
        assertEquals("    ", TreeTracerHelper.getSpaceInsteadOfInt(4444));
    }

    public void testModifier()
    {
        int curModifier = Modifier.FINAL;
        assertEquals(0, Modifier.STATIC & curModifier);
        curModifier = curModifier + Modifier.PUBLIC;
        assertEquals(0, Modifier.STATIC & curModifier);
        curModifier = curModifier + Modifier.TRANSIENT;
        assertEquals(0, Modifier.STATIC & curModifier);
        assertTrue((Modifier.FINAL & curModifier) > 0);
        curModifier = curModifier + Modifier.STATIC;
        assertTrue((Modifier.STATIC & curModifier) > 0);

    }

}
