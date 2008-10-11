package org.jmonitoring.core.dto;

import java.util.Date;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class MethodCallFullExtractDTO {
    private int mFlowId;

    private String mThreadName;

    private long mDuration;

    private long mFlowDuration;

    private Date mBeginDate;

    private String mJvmName;

    private int mPosition;

    /**
     * @return Returns the beginDate.
     */
    public Date getBeginDate() {
        return mBeginDate;
    }

    /**
     * @param pBeginDate The beginDate to set.
     */
    public void setBeginDate(Date pBeginDate) {
        mBeginDate = pBeginDate;
    }

    /**
     * @return Returns the duration.
     */
    public long getDuration() {
        return mDuration;
    }

    /**
     * @param pDuration The duration to set.
     */
    public void setDuration(long pDuration) {
        mDuration = pDuration;
    }

    /**
     * @return Returns the flowId.
     */
    public int getFlowId() {
        return mFlowId;
    }

    /**
     * @param pFlowId The flowId to set.
     */
    public void setFlowId(int pFlowId) {
        mFlowId = pFlowId;
    }

    /**
     * @return Returns the id.
     */
    public int getPosition() {
        return mPosition;
    }

    /**
     * @param pPosition The id to set.
     */
    public void setPosition(int pPosition) {
        mPosition = pPosition;
    }

    /**
     * @return Returns the jvmName.
     */
    public String getJvmName() {
        return mJvmName;
    }

    /**
     * @param pJvmName The jvmName to set.
     */
    public void setJvmName(String pJvmName) {
        mJvmName = pJvmName;
    }

    /**
     * @return Returns the threadName.
     */
    public String getThreadName() {
        return mThreadName;
    }

    /**
     * @param pThreadName The threadName to set.
     */
    public void setThreadName(String pThreadName) {
        mThreadName = pThreadName;
    }

    /**
     * @return Returns the flowDuration.
     */
    public long getFlowDuration() {
        return mFlowDuration;
    }

    /**
     * @param pFlowDuration The flowDuration to set.
     */
    public void setFlowDuration(long pFlowDuration) {
        mFlowDuration = pFlowDuration;
    }
}
