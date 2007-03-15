package org.jmonitoring.sample.testtraceparameter;

public class ClassToBeCall
{

    public String toBeCallWithParameter(int pInteger, String pString)
    {
        return "Toto";
    }

    public String toBeCallWithoutTrace(int pInteger, String pString)
    {
        return "Toto";
    }

    public String toBeCallWithParameterAndResult(int pInteger, String pString)
    {
        return "Toto";
    }

    public String toBeCallWithResult(int pInteger, String pString)
    {
        return "Toto";
    }

    public String toBeCallWithException(int pInteger, String pString)
    {
        throw new RuntimeException("Coucou");
    }

}
