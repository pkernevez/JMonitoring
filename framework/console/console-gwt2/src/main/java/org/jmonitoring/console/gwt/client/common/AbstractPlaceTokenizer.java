package org.jmonitoring.console.gwt.client.common;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

public class AbstractPlaceTokenizer
{
    public final static String SEPARATOR = "&";

    private LinkedHashMap<String, String> params;

    protected void tokenize(String pToken)
    {
        params = new LinkedHashMap<String, String>();
        String[] tTokens = (pToken == null ? new String[0] : pToken.split(SEPARATOR));
        for (String tString : tTokens)
        {
            int tPos = tString.indexOf('=');
            if (tPos < tString.length() - 1)
            {
                addParam(tString.substring(0, tPos), tString.substring(tPos + 1));
            }
        }
    }

    public void addParam(String pKey, String pValue)
    {
        params = (params != null ? params : new LinkedHashMap<String, String>());
        String tOldValue = params.put(pKey, pValue);
        if (tOldValue != null)
        {
            throw new RuntimeException("Invalid token, the same parameter is set twice");
        }

    }

    public String getParam(String pKey)
    {
        return params.get(pKey);
    }

    public int getParamInt(String pKey)
    {
        return Integer.parseInt(params.get(pKey));
    }

    protected AbstractPlaceTokenizer addParamInt(String pKey, int pValue)
    {
        addParam(pKey, String.valueOf(pValue));
        return this;
    }

    protected long getParamLong(String pKey)
    {
        return Long.parseLong(getParam(pKey));
    }

    protected AbstractPlaceTokenizer addParamLong(String pKey, long pValue)
    {
        addParam(pKey, String.valueOf(pValue));
        return this;
    }

    public String getToken()
    {
        if (params == null)
        {
            return "";
        }
        StringBuilder tBuilder = new StringBuilder();
        for (Entry<String, String> tEntry : params.entrySet())
        {
            if (tEntry.getKey() != null && tEntry.getKey().length() > 0 && tEntry.getValue() != null
                && tEntry.getValue().length() > 0)
            {
                tBuilder.append(tEntry.getKey()).append("=").append(tEntry.getValue()).append(SEPARATOR);
            }
        }
        return (tBuilder.length() == 0 ? "" : tBuilder.substring(0, tBuilder.length() - 1));
    }

    protected String getToken(LinkedHashMap<String, String> pMap)
    {
        params = pMap;
        return getToken();
    }

    public Set<Entry<String, String>> getParams()
    {
        return params.entrySet();
    }

}
