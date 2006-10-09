
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

create table `jmonitoring`.EXECUTION_FLOW (ID integer not null auto_increment, 
	THREAD_NAME varchar(255), 
	JVM varchar(255), 
	BEGIN_TIME bigint, 
	END_TIME bigint, 
	BEGIN_TIME_AS_DATE datetime, 
	DURATION bigint, 
	FIRST_METHOD_CALL_ID integer unique, 
	primary key (ID)) 
	type=InnoDB

create table `jmonitoring`.METHOD_CALL (ID integer not null auto_increment, 
	PARAMETERS varchar(255), 
	BEGIN_TIME bigint, 
	END_TIME bigint, 
	FULL_CLASS_NAME varchar(120), 
	METHOD_NAME varchar(50), 
	THROWABLE_CLASS_NAME varchar(120), 
	THROWABLE_MESSAGE varchar(255), 
	RESULT varchar(255), 
	GROUP_NAME varchar(145), 
	PARENT_ID integer, 
	CHILDREN_INDEX integer, 
	primary key (ID)) 
type=InnoDB

alter table `jmonitoring`.EXECUTION_FLOW 
	add index FK281CB2153BDDEB03 (FIRST_METHOD_CALL_ID), 
	add constraint FK281CB2153BDDEB03 foreign key (FIRST_METHOD_CALL_ID) references METHOD_CALL (ID)

alter table `jmonitoring`.METHOD_CALL 
	add index FK4ACE4AFCFBBE3D26 (PARENT_ID), 
	add constraint FK4ACE4AFCFBBE3D26 foreign key (PARENT_ID) references METHOD_CALL (ID)
