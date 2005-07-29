
/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

SELECT FLOW_ID, THREAD_NAME, JVM, DURATION,DATE_FORMAT(BEGIN_TIME, '%d/%m/%Y %H:%i:%s.%f') AS BEGIN_TIME2, DATE_FORMAT(END_TIME, '%d/%m/%Y %H:%i:%s.%f') AS END_TIME2 
FROM EXECUTION_FLOW  
WHERE THREAD_NAME LIKE '%';