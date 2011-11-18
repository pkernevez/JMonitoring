package org.jmonitoring.console.gwt.shared.method;

import java.io.Serializable;

public class MethodCallDistributionDTO implements Serializable
{
    private static final long serialVersionUID = -3517045932991784784L;

    private String className;

    private String methodName;

    private String nbOccurences;

    private String minDuration;

    private String avgDuration;

    private String maxDuration;

    private String devianceDuration;

    private String map;

    private String interval;

    public String getFullName()
    {
        return className + "." + methodName + "(...)";
    }

    public String getMinDuration()
    {
        return minDuration;
    }

    public void setMinDuration(String pMinDuration)
    {
        minDuration = pMinDuration;
    }

    public String getAvgDuration()
    {
        return avgDuration;
    }

    public void setAvgDuration(String pAvgDuration)
    {
        avgDuration = pAvgDuration;
    }

    public String getMaxDuration()
    {
        return maxDuration;
    }

    public void setMaxDuration(String pMaxDuration)
    {
        maxDuration = pMaxDuration;
    }

    public String getStdDeviationDuration()
    {
        return devianceDuration;
    }

    public void setDevianceDuration(String pDevianceDuration)
    {
        devianceDuration = pDevianceDuration;
    }

    public String getNbOccurences()
    {
        return nbOccurences;
    }

    public void setNbOccurences(String pNbOccurences)
    {
        nbOccurences = pNbOccurences;
    }

    public String getMap()
    {
        return map;
    }

    public void setMap(String pMap)
    {
        map = pMap;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String pClassName)
    {
        className = pClassName;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public void setMethodName(String pMethodName)
    {
        methodName = pMethodName;
    }

    public void setInterval(String pInterval)
    {
        interval = pInterval;

    }

    public String getInterval()
    {
        return interval;

    }

}
