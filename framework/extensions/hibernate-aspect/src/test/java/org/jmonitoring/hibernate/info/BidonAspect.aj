package org.jmonitoring.hibernate.info;

import org.jmonitoring.agent.aspect.PerformanceAspect;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public aspect BidonAspect 
{

    pointcut filtre() : execution(* org.jmonitoring.hibernate.*.*.*2(..) );
    
    pointcut testBidon(): call(* org.jmonitoring.hibernate.*.*.*(..) )
        && !within(org.jmonitoring.hibernate.dao.InsertionHibernateDAO) 
        && !within( junit.framework.TestCase+ )
        && !cflow( filtre() );
    
    Object around() : testBidon() {
        System.out.println("Aspect...");
        return proceed();
    }
    
}
