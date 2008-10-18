package org.jmonitoring.agent.info.impl;

import org.jmonitoring.core.info.IResultTracer;

public class ToStringResultTracer implements IResultTracer {
    private int mMaxLength = -1;

    public ToStringResultTracer() {
    }

    /**
     * Limit return value lenght to ensure the String lenght compatibily with SqlType.
     * 
     * @param pMaxLength The max lenght of the String.
     */
    public ToStringResultTracer(int pMaxLength) {
        mMaxLength = pMaxLength;
    }

    public CharSequence convertToString(Object pTarget, Object pResultObject) {
        String tResult = (pResultObject == null ? "" : pResultObject.toString());
        boolean tNeedToBeCut = (mMaxLength > -1) && (tResult.length() > mMaxLength);
        return (tNeedToBeCut ? tResult.substring(0, mMaxLength) : tResult);
    }
}
