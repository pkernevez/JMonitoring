package org.jmonitoring.console.gwt.client.common;

public class AbstractPlaceTokenizer
{
    public final static String SEPARATOR = "&";

    private String[] tokens;

    private int currentPosition = 0;

    protected void tokenize(String pToken)
    {
        tokens = (pToken == null ? new String[0] : pToken.split(SEPARATOR));
        currentPosition = 0;
    }

    protected String nextToken()
    {
        if (currentPosition < tokens.length)
        {
            return tokens[currentPosition++];
        } else
        {
            return "";
        }
    }

    protected String getToken(String... pFields)
    {
        StringBuilder tBuilder = new StringBuilder();
        for (String tField : pFields)
        {
            tBuilder.append(tField == null ? "" : tField).append(SEPARATOR);
        }
        return (tBuilder.length() == 0 ? "" : tBuilder.substring(0, tBuilder.length() - 1));
    }

}
