package org.jmonitoring.console.gwt.client.common;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.junit.Test;

public class AbstractPlaceTokenizerTest extends TestCase
{

    @Test
    public void testTokenizeSimple()
    {
        AbstractPlaceTokenizer tPlaceToken = new AbstractPlaceTokenizer();
        tPlaceToken.tokenize("param1=value1");
        assertEquals(1, tPlaceToken.getParams().size());
        assertEquals("value1", tPlaceToken.getParam("param1"));
    }

    @Test
    public void testTokenizeMultiple()
    {
        AbstractPlaceTokenizer tPlaceToken = new AbstractPlaceTokenizer();
        tPlaceToken.tokenize("param1=value1&param2=value2");
        assertEquals(2, tPlaceToken.getParams().size());
        assertEquals("value1", tPlaceToken.getParam("param1"));
        assertEquals("value2", tPlaceToken.getParam("param2"));
    }

    @Test
    public void testTokenizeWithEmpty()
    {
        AbstractPlaceTokenizer tPlaceToken = new AbstractPlaceTokenizer();
        tPlaceToken.tokenize("param1=value1&param2=");
        assertEquals(1, tPlaceToken.getParams().size());
        assertEquals("value1", tPlaceToken.getParam("param1"));
        tPlaceToken.tokenize("param1=value1&param2=&param3=value3");
        assertEquals(2, tPlaceToken.getParams().size());
        assertEquals("value1", tPlaceToken.getParam("param1"));
        assertNull(tPlaceToken.getParam("param2"));
        assertEquals("value3", tPlaceToken.getParam("param3"));
    }

    @Test
    public void testTokenizeWithOrder()
    {
        AbstractPlaceTokenizer tPlaceToken = new AbstractPlaceTokenizer();
        tPlaceToken.tokenize("a=1&b=2&f=3&w=3&we=7&poi=4&hjk=weqterq");
        assertEquals(7, tPlaceToken.getParams().size());
        Iterator<Entry<String, String>> tIt = tPlaceToken.getParams().iterator();
        checkEntry(tIt.next(), "a", "1");
        checkEntry(tIt.next(), "b", "2");
        checkEntry(tIt.next(), "f", "3");
        checkEntry(tIt.next(), "w", "3");
        checkEntry(tIt.next(), "we", "7");
        checkEntry(tIt.next(), "poi", "4");
        checkEntry(tIt.next(), "hjk", "weqterq");
    }

    private void checkEntry(Entry<String, String> pEntry, String pExpectedKey, String pExpectedValue)
    {
        assertEquals(pExpectedKey, pEntry.getKey());
        assertEquals(pExpectedValue, pEntry.getValue());
    }

    @Test
    public void testTokenizeWithDuplicate()
    {
        AbstractPlaceTokenizer tPlaceToken = new AbstractPlaceTokenizer();
        try
        {
            tPlaceToken.tokenize("param1=value1&param2=value2&param1=value3");
            fail("Should not pass !");
        } catch (RuntimeException pException)
        {
            assertEquals("Invalid token, the same parameter is set twice", pException.getMessage());
        }
    }

    @Test
    public void testGetTokenSimple()
    {
        AbstractPlaceTokenizer tPlaceToken = new AbstractPlaceTokenizer();
        assertEquals("", tPlaceToken.getToken(null));
        LinkedHashMap<String, String> tParams = new LinkedHashMap<String, String>();
        assertEquals("", tPlaceToken.getToken(tParams));
        tPlaceToken.addParam("param1", "value1");
        assertEquals("param1=value1", tPlaceToken.getToken(tParams));
    }

    @Test
    public void testGetTokenMultiple()
    {
        AbstractPlaceTokenizer tPlaceToken = new AbstractPlaceTokenizer();
        LinkedHashMap<String, String> tParams = new LinkedHashMap<String, String>();
        tParams.put("param1", "value1");
        tParams.put("param2", "value2");
        assertEquals("param1=value1&param2=value2", tPlaceToken.getToken(tParams));
    }

    @Test
    public void testGetTokenWithEmpty()
    {
        AbstractPlaceTokenizer tPlaceToken = new AbstractPlaceTokenizer();
        LinkedHashMap<String, String> tParams = new LinkedHashMap<String, String>();
        tParams.put("param1", "");
        tParams.put("param2", null);
        assertEquals("", tPlaceToken.getToken(tParams));
        tParams.put("", "value3");
        tParams.put(null, "value3");
        assertEquals("", tPlaceToken.getToken(tParams));
        tParams.put("param2", "value4");
        assertEquals("param2=value4", tPlaceToken.getToken(tParams));
    }

    @Test
    public void testGetTokenWithOrder()
    {
        AbstractPlaceTokenizer tPlaceToken = new AbstractPlaceTokenizer();
        tPlaceToken.addParam("a", "1");
        tPlaceToken.addParam("b", "2");
        tPlaceToken.addParamInt("f", 3);
        tPlaceToken.addParam("w", "3");
        tPlaceToken.addParamInt("we", 7);
        tPlaceToken.addParam("poi", "4");
        tPlaceToken.addParam("hjk", "weqterq");
        assertEquals("a=1&b=2&f=3&w=3&we=7&poi=4&hjk=weqterq", tPlaceToken.getToken());
    }

}
