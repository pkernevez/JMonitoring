package org.jmonitoring.hibernate.info;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public aspect BidonAspect
{

    pointcut filtre() : execution(* org.jmonitoring.core.*.*.*.*2(..) );
    
    pointcut testBidon(): call(* org.jmonitoring.core.*.*.*.*(..) )
        && !cflow( filtre() );
    
    Object around() : testBidon() {
        System.out.println("Aspect...");
        return proceed();
    }
    
}
