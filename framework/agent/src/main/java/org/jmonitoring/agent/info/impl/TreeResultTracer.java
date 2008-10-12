package org.jmonitoring.agent.info.impl;

import org.jmonitoring.core.info.IResultTracer;

public class TreeResultTracer implements IResultTracer {

    public CharSequence convertToString(Object pTarget, Object pResultObject) {
        long tStartTime = System.currentTimeMillis();
        TreeTracerHelper tHelper = new TreeTracerHelper();
        StringBuilder tBuffer = new StringBuilder();
        tHelper.traceObjectTree(tBuffer, pResultObject);
        long tEndTime = System.currentTimeMillis();
        long tDuration = tEndTime - tStartTime;
        StringBuilder tResult = new StringBuilder();
        tResult.append("Tracing duration = ").append(tDuration).append(" ms\n");
        tResult.append("Max Depth = ").append(tHelper.getMaxDepth()).append("\n");
        tResult.append("Nb Entity = ").append(tHelper.getNbEntity()).append("\n");
        tResult.append("\nResult\n").append(tBuffer);
        return tResult;
    }

}
