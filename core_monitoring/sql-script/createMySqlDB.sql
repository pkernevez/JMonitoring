
#/***************************************************************************
# * Copyright 2005 Philippe Kernevez All rights reserved.                   *
# * Please look at license.txt for more license detail.                     *
# **************************************************************************/

Update `method_execution` set PARENT_ID=NULL;
Delete from `method_execution`;
delete FROM `execution_flow`;

DROP TABLE IF EXISTS `monitoring`.`method_execution`;
DROP TABLE IF EXISTS `monitoring`.`execution_flow`;

CREATE TABLE `execution_flow` (
  `ID` int(8) unsigned NOT NULL auto_increment,
  `THREAD_NAME` varchar(60) NOT NULL default '',
  `DURATION` bigint(20) unsigned NOT NULL default '0',
  `BEGIN_TIME_AS_DATE` datetime NOT NULL default '0000-00-00 00:00:00',
  `END_TIME` bigint(20) unsigned NOT NULL default '0',
  `JVM` varchar(45) NOT NULL default 'Kerny''s Babasse',
  `BEGIN_TIME` bigint(20) unsigned NOT NULL default '0',
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Entry point of execution';


CREATE TABLE `method_execution` (
  `FLOW_ID` int(8) unsigned NOT NULL default '0',
  `SEQUENCE_ID` int(8) unsigned NOT NULL default '0',
  `FULL_CLASS_NAME` varchar(120) NOT NULL default '',
  `METHOD_NAME` varchar(50) NOT NULL default '',
  `DURATION` bigint(20) unsigned NOT NULL default '0',
  `BEGIN_TIME` bigint(20) unsigned NOT NULL default '0',
  `END_TIME` bigint(20) unsigned NOT NULL default '0',
  `PARAMETERS` text,
  `RESULT` text default NULL,
  `THROWABLE_CLASS_NAME` varchar(120) default NULL,
  `THROWABLE_MESSAGE` text default NULL,
  `PARENT_ID` int(8) unsigned default '0',
  `RETURN_TYPE` varchar(10) NOT NULL default '',
  `GROUP_NAME` varchar(45) NOT NULL default 'Default',
  PRIMARY KEY  (`FLOW_ID`,`SEQUENCE_ID`),
  KEY `FK_method_execution_PARENT` (`FLOW_ID`,`PARENT_ID`),
  KEY `Index_ClassMethodName` TYPE BTREE (`FULL_CLASS_NAME`,`METHOD_NAME`),
  CONSTRAINT `FK_FLOW_REF` FOREIGN KEY (`FLOW_ID`) REFERENCES `execution_flow` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 4096 kB';
#  Remove constrainte for batch mode CONSTRAINT `FK_method_execution_PARENT` FOREIGN KEY (`FLOW_ID`, `PARENT_ID`) REFERENCES `method_execution` (`FLOW_ID`, `SEQUENCE_ID`)
