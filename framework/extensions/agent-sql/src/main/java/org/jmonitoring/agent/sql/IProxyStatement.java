package org.jmonitoring.agent.sql;

import java.sql.Statement;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public interface IProxyStatement {
    Statement getRealStatement();

    CharSequence getTrace();
}
