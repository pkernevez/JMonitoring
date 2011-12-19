package org.jmonitoring.console.gwt.shared.method.treesearch;

import java.io.Serializable;

public class MethodDTO implements Serializable
{
    private static final long serialVersionUID = -6252310822107875117L;

    private String name;

    private int nbOccurence;

    private ClassDTO parent;

    public MethodDTO()
    {
    }

    public MethodDTO(ClassDTO pParent, String pName, int pNbOccurence)
    {
        parent = pParent;
        name = pName;
        nbOccurence = pNbOccurence;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String pName)
    {
        name = pName;
    }

    public int getNbOccurence()
    {
        return nbOccurence;
    }

    public void setNbOccurence(int pNbOccurence)
    {
        nbOccurence = pNbOccurence;
    }

    public String getFullClassName()
    {
        return parent.getFullName();
    }

}
