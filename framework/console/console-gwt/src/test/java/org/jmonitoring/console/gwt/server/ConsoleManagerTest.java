package org.jmonitoring.console.gwt.server;

import static org.junit.Assert.*;

import java.util.List;

import org.jmonitoring.console.gwt.client.dto.StatMapAreaDTO;
import org.junit.Test;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class ConsoleManagerTest
{

    @Test
    public void testParseMap()
    {
        String tMap =
            "<MAP NAME=\"chart\">"
                + "<AREA SHAPE=\"RECT\" COORDS=\"59,52,70,406\" title=\"durations (ms): (0, 1)\" href=\"MethodCallListIn.do?className=java.sql.Statement&methodName=executeQuery&durationMin=0&durationMax=1\">"
                + "<AREA SHAPE=\"RECT\" COORDS=\"70,407,81,408\" title=\"durations (ms): (1, 0)\" href=\"MethodCallListIn.do?className=java.sql.Statement&methodName=executeQuery&durationMin=1&durationMax=2\">"
                + "<AREA SHAPE=\"RECT\" COORDS=\"81,407,92,408\" title=\"durations (ms): (2, 0)\" href=\"MethodCallListIn.do?className=java.sql.Statement&methodName=executeQuery&durationMin=2&durationMax=3\">"
                + "<AREA SHAPE=\"RECT\" COORDS=\"92,407,103,408\" title=\"durations (ms): (3, 0)\" href=\"MethodCallListIn.do?className=java.sql.Statement&methodName=executeQuery&durationMin=3&durationMax=4\">"
                + "</MAP>";
        List<StatMapAreaDTO> tMapDto = ConsoleManager.parseMap(tMap);
        assertEquals(4, tMapDto.size());
        int curPos = 0;
        assertEquals("59,52,70,406", tMapDto.get(curPos).getCoordinate());
        assertEquals(0, tMapDto.get(curPos).getDurationMin());
        assertEquals(1, tMapDto.get(curPos).getDurationMax());
        assertEquals("70,407,81,408", tMapDto.get(++curPos).getCoordinate());
        assertEquals(1, tMapDto.get(curPos).getDurationMin());
        assertEquals(2, tMapDto.get(curPos).getDurationMax());
        assertEquals("81,407,92,408", tMapDto.get(++curPos).getCoordinate());
        assertEquals(2, tMapDto.get(curPos).getDurationMin());
        assertEquals(3, tMapDto.get(curPos).getDurationMax());
        assertEquals("92,407,103,408", tMapDto.get(++curPos).getCoordinate());
        assertEquals(3, tMapDto.get(curPos).getDurationMin());
        assertEquals(4, tMapDto.get(curPos).getDurationMax());
    }
}
