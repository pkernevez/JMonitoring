package org.jmonitoring.hibernate.info;

import org.jmonitoring.core.aspects.PerformanceAspect;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public aspect HibernateApiAspect extends PerformanceAspect
{

    public pointcut executionToLog() : 
        (
           call( * org.hibernate.Session+.*(..))
           
           || call( * org.hibernate.Query+.executeUpdate())
           || call( * org.hibernate.Query+.iterate())
           || call( * org.hibernate.Query+.list())
           || call( * org.hibernate.Query+.scroll(..))
           || call( * org.hibernate.Query+.uniqueResult())
           
           || call( * org.hibernate.Criteria.list())
           || call( * org.hibernate.Criteria.scroll(..))
           || call( * org.hibernate.Criteria.uniqueResult())
        ) && !within(org.hibernate.*) && !within(org.hibernate.*.*) 
          && !within(org.hibernate.*.*.*) && !within(org.jmonitoring.core.dao.PersistanceTestCase+);

    public HibernateApiAspect()
    {
        mGroupName = "Hibernate";
    }
}
