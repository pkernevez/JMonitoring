package org.jmonitoring.console.gwt.shared.method.treesearch;

import java.io.Serializable;
import java.util.ArrayList;

public class ClassDTO implements Serializable, HasName
{
    private static final long serialVersionUID = -2018538621415786995L;

    private String name;

    private ArrayList<String> methods = new ArrayList<String>();

    /**
     * @param pName The short name of the class (not includes the package name)
     */
    public ClassDTO(String pName)
    {
        name = pName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String pName)
    {
        name = pName;
    }

    public ArrayList<String> getMethods()
    {
        return methods;
    }

    public void setMethods(ArrayList<String> pMethods)
    {
        methods = pMethods;
    }

    public void addMethod(String pString)
    {
        methods.add(pString);
    }
}
