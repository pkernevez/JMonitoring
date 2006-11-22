package org.jmonitoring.core.info.impl;

import org.jmonitoring.core.info.IResultTracer;

public class TreeResultTracer implements IResultTracer
{

    public String convertToString(Object pResultObject)
    {
        long tStartTime = System.currentTimeMillis();
        TreeTracerHelper tHelper = new TreeTracerHelper();
        StringBuffer tBuffer = new StringBuffer();
        tHelper.traceObjectTree(tBuffer, pResultObject);
        long tEndTime = System.currentTimeMillis();
        long tDuration = tEndTime - tStartTime;
        StringBuffer tResult = new StringBuffer();
        tResult.append("Tracing duration = ").append(tDuration).append(" ms\n");
        tResult.append("Max Depth = ").append(tHelper.getMaxDepth()).append("\n");
        tResult.append("Nb Entity = ").append(tHelper.getNbEntity()).append("\n");
        tResult.append("\nResult\n").append(tBuffer);
        return tResult.toString();
    }

}
