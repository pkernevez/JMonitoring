package org.jmonitoring.sample;

import junit.framework.TestCase;

import org.jmonitoring.agent.sql.info.SqlExecutionAspect;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestAspect extends TestCase {
    public void testInc() {
        assertFalse(SqlExecutionAspect.ajc$cflowCounter$0.isValid());
        SqlExecutionAspect.ajc$cflowCounter$0.inc();
        SqlExecutionAspect.ajc$cflowCounter$0.dec();
        assertFalse(SqlExecutionAspect.ajc$cflowCounter$0.isValid());
        SqlExecutionAspect.ajc$cflowCounter$0.inc();
        assertTrue(SqlExecutionAspect.ajc$cflowCounter$0.isValid());
        SqlExecutionAspect.ajc$cflowCounter$0.inc();
        SqlExecutionAspect.ajc$cflowCounter$0.inc();
        assertTrue(SqlExecutionAspect.ajc$cflowCounter$0.isValid());
        SqlExecutionAspect.ajc$cflowCounter$0.dec();
        SqlExecutionAspect.ajc$cflowCounter$0.dec();
        assertTrue(SqlExecutionAspect.ajc$cflowCounter$0.isValid());
        SqlExecutionAspect.ajc$cflowCounter$0.dec();
        assertFalse(SqlExecutionAspect.ajc$cflowCounter$0.isValid());

    }

}
