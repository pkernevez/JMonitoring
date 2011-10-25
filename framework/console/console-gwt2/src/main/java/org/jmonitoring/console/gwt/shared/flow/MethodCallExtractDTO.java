package org.jmonitoring.console.gwt.shared.flow;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.Serializable;

public class MethodCallExtractDTO implements Serializable
{

    private static final long serialVersionUID = -497249515537353769L;

    /** Technical Id. */
    private String position;

    /** Position in the parent childlist. */
    private int childPosition;

    private int parentPosition;

    /** Link to the children nodes. */
    private MethodCallExtractDTO[] children = new MethodCallExtractDTO[0];

    /**
     * Time since the prev brother method call had finished. It's the time spend in the execution of the parent since
     * the end of the prev child.
     */
    private String timeFromPrevChild;

    /** Duration. */
    private String duration;

    /** Group name associated to this method call. */
    private String groupName;

    public MethodCallExtractDTO()
    {
    }

}
