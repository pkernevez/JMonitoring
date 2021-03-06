package org.jmonitoring.console.gwt.shared.method.treesearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TreeBuilder
{

    Map<String, TreeBuilder> subPackageBuilders;

    Map<String, ClassDTO> classes;

    PackageDTO myPackage;

    public TreeBuilder()
    {
        this(null, "");
    }

    public TreeBuilder(String pPackageName)
    {
        this(null, pPackageName);
    }

    public TreeBuilder(PackageDTO pParent, String pPackageName)
    {
        subPackageBuilders = new HashMap<String, TreeBuilder>();
        classes = new HashMap<String, ClassDTO>();
        myPackage = new PackageDTO(pParent, pPackageName);
    }

    private void addPart(String[] pPart, int pNbOccurence)
    {
        String tName = pPart[0];
        if (pPart.length == 2)
        {
            ClassDTO tClass = classes.get(tName);
            if (tClass == null)
            {
                tClass = new ClassDTO(myPackage, tName);
                classes.put(tName, tClass);
            }
            tClass.addMethod(pPart[1], pNbOccurence);
        } else
        {
            TreeBuilder tSubPackage = subPackageBuilders.get(tName);
            if (tSubPackage == null)
            {
                tSubPackage = new TreeBuilder(myPackage, tName);
                subPackageBuilders.put(tName, tSubPackage);
            }
            String[] tNewPart = new String[pPart.length - 1];
            System.arraycopy(pPart, 1, tNewPart, 0, pPart.length - 1);
            tSubPackage.addPart(tNewPart, pNbOccurence);
        }
    }

    public void addMethod(String pName, int pNbOccurence)
    {
        if (pName == null || pName.length() == 0 || !pName.contains("."))
        {
            throw new RuntimeException(
                                       "Invalid name, should contains at least a class and a name : \"$classs.$method\".");
        }
        addPart(pName.split("\\."), pNbOccurence);
    }

    public PackageDTO getRoot()
    {
        ArrayList<PackageDTO> tPackages = new ArrayList<PackageDTO>(subPackageBuilders.size());
        for (TreeBuilder tBuilder : subPackageBuilders.values())
        {
            tPackages.add(tBuilder.getRoot());
        }
        myPackage.setSubPackages(tPackages);
        myPackage.setClasses(new ArrayList<ClassDTO>(classes.values()));
        return myPackage;
    }
}
