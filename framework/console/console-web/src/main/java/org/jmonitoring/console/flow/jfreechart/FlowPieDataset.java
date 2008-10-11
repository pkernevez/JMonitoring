package org.jmonitoring.console.flow.jfreechart;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.general.DefaultPieDataset;

public class FlowPieDataset extends DefaultPieDataset {
    private static final long serialVersionUID = 4049643377807145267L;

    /** Storage for the data. */
    private List mGroupData = new ArrayList();

    /**
     * Return the name of the group.
     * 
     * @param pItem The index of the group.
     * @return The name of the group.
     */
    public String getGroupName(int pItem) {
        return (String) mGroupData.get(pItem);
    }

    /**
     * Sets the data value for a key.
     * 
     * @param pKey the key.
     * @param pValue the value.
     * @param pGroupName Name of the Group.
     */
    public void setValue(Comparable pKey, Number pValue, String pGroupName) {
        // mGroupData.add( pKey, pValue );
        super.setValue(pKey, pValue);
    }

    /**
     * Sets the data value for a key.
     * 
     * @param pKey the key.
     * @param pValue the value.
     * @param pGroupName Name of the Group.
     */
    public void setValue(Comparable pKey, double pValue, String pGroupName) {
        // mGroupName = pGroupName;
        super.setValue(pKey, new Double(pValue));
    }

}
