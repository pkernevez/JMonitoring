package org.jmonitoring.console.gwt.shared.method.treesearch;

import java.io.Serializable;
import java.util.ArrayList;

public class PackageDTO implements Serializable, HasName
{
    private static final long serialVersionUID = 7913081024289332085L;

    private ArrayList<PackageDTO> subPackages;

    private ArrayList<ClassDTO> classes;

    private String name;

    public PackageDTO(String pName)
    {
        this(pName, new ArrayList<PackageDTO>(), new ArrayList<ClassDTO>());
    }

    public PackageDTO(String pName, ArrayList<PackageDTO> pSubPackages, ArrayList<ClassDTO> pClasses)
    {
        name = pName;
        subPackages = pSubPackages;
        classes = pClasses;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String pName)
    {
        name = pName;
    }

    public ArrayList<PackageDTO> getSubPackages()
    {
        return subPackages;
    }

    public void setSubPackages(ArrayList<PackageDTO> pSubPackages)
    {
        subPackages = pSubPackages;
    }

    public ArrayList<ClassDTO> getClasses()
    {
        return classes;
    }

    public void setClasses(ArrayList<ClassDTO> pClasses)
    {
        classes = pClasses;
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
        PackageDTO other = (PackageDTO) obj;
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

}
