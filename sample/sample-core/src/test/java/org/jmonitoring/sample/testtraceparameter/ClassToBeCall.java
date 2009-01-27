package org.jmonitoring.sample.testtraceparameter;

import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.jmonitoring.sample.persistence.SpringSampleConfigurationUtil;

public class ClassToBeCall
{

    public String toBeCallWithParameter(int pInteger, String pString)
    {
        return "Toto";
    }

    public String toBeCallOther(int pInteger, String pString)
    {
        String tResult = toBeCallWithoutTrace();
        try
        {
            SessionFactory tFactory = (SessionFactory) SpringSampleConfigurationUtil.getBean("sessionFactory");
            tFactory.getCurrentSession().save(this);
        } catch (MappingException e)
        {
            // Nothing to to
        }
        return tResult;
    }

    public String toBeCallWithoutTrace()
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
