package org.jmonitoring.console.gwt.shared.flow;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.ui.TextBox;

public class FlowSearchRequestDTO implements Serializable
{
    private static final long serialVersionUID = -9173009879031466320L;

    private String mThread;

    private String mMinDuration;

    private String mGroup;

    private Date mBeginDate;

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

    public Date getBeginDate()
    {
        return mBeginDate;
    }

    public void setBeginDate(Date beginDate)
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
