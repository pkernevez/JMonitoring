package org.jmonitoring.agent.store.impl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

import org.jmonitoring.agent.store.Filter;
import org.jmonitoring.core.domain.MethodCallPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This filter is not really a filter as it will always kept measure, but it's will be use to understand why some
 * measure some aspect intercept calls without any context. It will be use during the definition of all the aspect to
 * add some missing high level aspect. For example if you use the <code>GenericDriver</code>, it may intercept some call
 * without any context. This filter will help you to understand which aspect you may add, like an
 * <code>javax.http.Filter</code> or an asynchronous interceptor like <code>javax.jms.MessageListener</code> ou
 * <code>org.quartz.Job</code>. This filter will log the current stack trace to understand why the corresponding code
 * has been ran.
 * 
 * @author pke
 * 
 */
public class OutOfContextFilter implements Filter
{

    static Logger sLog = LoggerFactory.getLogger(OutOfContextFilter.class);

    public boolean keep(MethodCallPO pCurrent)
    {
        if (pCurrent.getParentMethodCall() == null)
        {
            sLog.info(buildMessage());
        }
        return true;
    }

    private String buildMessage()
    {
        Exception tException = new Exception();
        ByteArrayOutputStream tStream = new ByteArrayOutputStream();
        PrintStream tPrintStream = new PrintStream(tStream);;
        tException.printStackTrace(tPrintStream);
        String tStack = new String(tStream.toByteArray());
        tStack = tStack.substring(tStack.indexOf('\n') + 1);
        return "A MethodCall has been intercept without context, the stack trace is :\n" + tStack;
    }

}
