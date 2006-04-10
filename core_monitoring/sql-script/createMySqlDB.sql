
#/***************************************************************************
# * Copyright 2005 Philippe Kernevez All rights reserved.                   *
# * Please look at license.txt for more license detail.                     *
# **************************************************************************/

GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,ALTER,INDEX 
    on jmonitoring.* TO jmonitoring@'%' IDENTIFIED BY 'jmonitoringpw';
flush privileges;
create database jmonitoring;

Update `METHOD_CALL` set PARENT_ID=NULL;
Delete from `METHOD_CALL`;
delete FROM `execution_flow`;

DROP TABLE IF EXISTS `jmonitoring`.`method_call`;
DROP TABLE IF EXISTS `jmonitoring`.`execution_flow`;

CREATE TABLE `jmonitoring`.`execution_flow` (
  `ID` int(8) unsigned NOT NULL auto_increment,
  `THREAD_NAME` varchar(60) NOT NULL default '',
  `DURATION` bigint(20) unsigned NOT NULL default '0',
  `BEGIN_TIME_AS_DATE` datetime NOT NULL default '0000-00-00 00:00:00',
  `END_TIME` bigint(20) unsigned NOT NULL default '0',
  `JVM` varchar(45) NOT NULL default 'Kerny''s Babasse',
  `BEGIN_TIME` bigint(20) unsigned NOT NULL default '0',
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Entry point of execution';


CREATE TABLE `jmonitoring`.`method_call` (
  `EXECUTION_FLOW_ID` int(8) unsigned NOT NULL default '0',
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
  PRIMARY KEY  (`EXECUTION_FLOW_ID`,`SEQUENCE_ID`),
  KEY `FK_method_call_PARENT` (`EXECUTION_FLOW_ID`,`PARENT_ID`),
  KEY `Index_ClassMethodName` TYPE BTREE (`FULL_CLASS_NAME`,`METHOD_NAME`),
  CONSTRAINT `FK_FLOW_REF` FOREIGN KEY (`EXECUTION_FLOW_ID`) REFERENCES `execution_flow` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 4096 kB';

#  Remove constrainte for batch mode CONSTRAINT `FK_method_call_PARENT` FOREIGN KEY (`EXECUTION_FLOW_ID`, `PARENT_ID`) REFERENCES `method_call` (`FLOW_ID`, `SEQUENCE_ID`)
