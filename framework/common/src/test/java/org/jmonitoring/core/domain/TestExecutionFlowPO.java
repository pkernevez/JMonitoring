package org.jmonitoring.core.domain;

import junit.framework.TestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestExecutionFlowPO extends TestCase {
    public void testToString() {
        MethodCallPO tPoint = new MethodCallPO(null, TestExecutionFlowPO.class.getName(), "builNewFullFlow",
                "GrDefault", "[]");
        ExecutionFlowPO tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myJVM");
        assertEquals("ExecutionFlowPO FlowId=[-1]", tFlow.toString());
    }

}
