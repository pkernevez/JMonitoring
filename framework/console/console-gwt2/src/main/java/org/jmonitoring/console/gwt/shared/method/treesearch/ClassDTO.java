package org.jmonitoring.console.gwt.shared.method.treesearch;

import java.io.Serializable;
import java.util.ArrayList;

public class ClassDTO implements Serializable
{
    private static final long serialVersionUID = -2018538621415786995L;

    private String name;

    private PackageDTO parent;

    private ArrayList<MethodDTO> methods = new ArrayList<MethodDTO>();

    public ClassDTO()
    {
    }

    /**
     * @param pName The short name of the class (not includes the package name)
     */
    public ClassDTO(PackageDTO pParent, String pName)
    {
        parent = pParent;
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

    public ArrayList<MethodDTO> getMethods()
    {
        return methods;
    }

    public void setMethods(ArrayList<MethodDTO> pMethods)
    {
        methods = pMethods;
    }

    public void addMethod(String pName, int pNbOccurence)
    {
        methods.add(new MethodDTO(this, pName, pNbOccurence));
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        ClassDTO other = (ClassDTO) obj;
        if (name == null)
        {
            if (other.name != null)
            {
                return false;
            }
        } else if (!name.equals(other.name))
        {
            return false;
        }
        return true;
    }

    public String getFullName()
    {
        return parent.getFullName() + name;
    }
}
