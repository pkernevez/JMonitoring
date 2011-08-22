package org.jmonitoring.console.gwt.shared.flow;

import java.io.Serializable;

import com.google.gwt.user.client.ui.TextBox;

public class FlowSearchRequestDTO implements Serializable
{
    private static final long serialVersionUID = -9173009879031466320L;

    private String mThread;

    private String mMinDuration;

    private String mGroup;

    private String mBeginDate;

    private String mFirstMeasureClassName;

    private String mFirstMeasureMethodName;

    public String getThread()
    {
        return mThread;
    }

    public void setThread(String thread)
    {
        mThread = thread;
    }

    public String getMinDuration()
    {
        return mMinDuration;
    }

    public void setMinDuration(String minDuration)
    {
        mMinDuration = minDuration;
    }

    public String getGroup()
    {
        return mGroup;
    }

    public void setGroup(String group)
    {
        mGroup = group;
    }

    public String getBeginDate()
    {
        return mBeginDate;
    }

    public void setBeginDate(String beginDate)
    {
        mBeginDate = beginDate;
    }

    public String getFirstMeasureClassName()
    {
        return mFirstMeasureClassName;
    }

    public void setFirstMeasureClassName(String firstMeasureClassName)
    {
        mFirstMeasureClassName = firstMeasureClassName;
    }

    public String getFirstMeasureMethodName()
    {
        return mFirstMeasureMethodName;
    }

    public void setFirstMeasureMethodName(String firstMeasureMethodName)
    {
        mFirstMeasureMethodName = firstMeasureMethodName;
    }

}
