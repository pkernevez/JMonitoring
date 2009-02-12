package org.jmonitoring.core.info;

public interface IThrowableTracer {
    CharSequence convertToString(Throwable pException);
}
