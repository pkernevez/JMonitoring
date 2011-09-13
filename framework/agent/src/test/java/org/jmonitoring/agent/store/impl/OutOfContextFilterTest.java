package org.jmonitoring.agent.store.impl;

import static org.junit.Assert.*;

import org.jmonitoring.core.domain.MethodCallPO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OutOfContextFilterTest
{

    @Mock
    private Logger sLog;

    private OutOfContextFilter outOfContextFilter;

    @Before
    public void createOutOfContextFilter() throws Exception
    {
        outOfContextFilter = new OutOfContextFilter();
        outOfContextFilter.sLog = sLog;
    }

    @Test
        public void testKeep()
        {
           outOfContextFilter.keep(new MethodCallPO());
            verify(sLog).info( startsWith("A MethodCall has been intercept without context, the stack trace is :\n"));
            verify(sLog).info( contains("org.jmonitoring.agent.store.impl.OutOfContextFilterTest.testKeep"));
        }

}
