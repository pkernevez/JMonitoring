package org.jmonitoring.agent.info.impl;

import org.jmonitoring.core.info.IThrowableTracer;

public class DefaultExceptionTracer implements IThrowableTracer {

    public CharSequence convertToString(Throwable pException) {
        return (pException == null ? "" : pException.getMessage());
    }

}
