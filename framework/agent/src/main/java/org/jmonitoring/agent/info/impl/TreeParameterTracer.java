package org.jmonitoring.agent.info.impl;

import org.jmonitoring.core.info.IParamaterTracer;

public class TreeParameterTracer extends Object implements IParamaterTracer {

    public CharSequence convertToString(Object pTarget, Object[] pParameterObjects) {
        long tStartTime = System.currentTimeMillis();
        StringBuilder[] tBuffer = new StringBuilder[pParameterObjects.length];
        TreeTracerHelper tHelper;
        int tMaxDepth = 0;
        int tNbEntity = 0;
        for (int i = 0; i < pParameterObjects.length; i++) {
            tBuffer[i] = new StringBuilder();
            tHelper = new TreeTracerHelper();
            tHelper.traceObjectTree(tBuffer[i], pParameterObjects[i]);
            tNbEntity = tNbEntity + tHelper.getNbEntity();
            tMaxDepth = Math.max(tMaxDepth, tHelper.getMaxDepth());
        }
        long tEndTime = System.currentTimeMillis();
        long tDuration = tEndTime - tStartTime;
        StringBuilder tResult = new StringBuilder();
        tResult.append("Tracing duration = ").append(tDuration).append(" ms\n");
        tResult.append("Max Depth = ").append(tMaxDepth).append("\n");
        tResult.append("Nb Entity = ").append(tNbEntity).append("\n");
        for (int i = 0; i < pParameterObjects.length; i++) {
            tResult.append("\nParameter n°").append(i + 1).append("\n").append(tBuffer[i]);
        }
        return tResult;
    }

}
