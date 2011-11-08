package org.jmonitoring.console.gwt.shared.method;

import java.io.Serializable;

public class MethodCallDistributionDTO implements Serializable
{
    private static final long serialVersionUID = -3517045932991784784L;

    private String fullName;

    private String nbOccurences;

    private String minDuration;

    private String avgDuration;

    private String maxDuration;

    private String devianceDuration;

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String pFullName)
    {
        fullName = pFullName;
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

    public String getDevianceDuration()
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

}
