using System.Data;
using System.Data.Hsql;
using Org.NMonitoring.Core.Common;
using Org.NMonitoring.Core.Common.Tests;
using Org.NMonitoring.Core.Dao;
using SharpHsql;

namespace Org.NMonitoring.Core.Tests.Common
{
    class InMemoryDBCreation
    {
        private static readonly string DB_NAME = "InMemoryDB";
        private static readonly string USER_ID = "sa";
        private static readonly string PWD = "";
        private static bool existingDB = false;
        
        public static void Create()
        {  
            //TODO : create foreign key constraint on METHOD_CALL table between INDEX_IN_FLOW and PARENT_INDEX_IN_FLOW
            if (!existingDB)
            {
                SharpHSqlDaoHelper.Initialize("Initial Catalog=" + DB_NAME + ";User Id=" + USER_ID + ";Pwd=" + PWD + ";");
                Factory<IDaoHelper> factory = Factory<IDaoHelper>.Instance;
                factory.TypeToCreate = typeof(SharpHSqlDaoHelper);
                SharpHsqlConnection conn = (SharpHsqlConnection) SharpHSqlDaoHelper.Instance.Connection;
                
                if (SharpHSqlDaoHelper.Instance.Connection.State == ConnectionState.Closed)
                    SharpHSqlDaoHelper.Instance.OpenConnection();

                SharpHsqlCommand createMethodCallTable = new
                    SharpHsqlCommand("CREATE TABLE METHOD_CALL( FLOW_ID int NOT NULL,INDEX_IN_FLOW int NOT NULL," +
                                         "PARAMETERS varchar(255) NULL,BEGIN_TIME numeric(19, 0) NULL," +
                                         "END_TIME numeric(19, 0) NULL, FULL_CLASS_NAME varchar(120) NULL," +
                                         "METHOD_NAME varchar(50) NULL, THROWABLE_CLASS_NAME varchar(120) NULL," +
                                         "THROWABLE_MESSAGE varchar(255) NULL, RESULT varchar(255) NULL," +
                                         "GROUP_NAME varchar(145) NULL, PARENT_INDEX_IN_FLOW int NULL," +
                                         "SUB_METH_INDEX int NULL," +
                                         "CONSTRAINT pk PRIMARY KEY " +
                                         "(" +
                                         "FLOW_ID," +
                                         "INDEX_IN_FLOW" +
                                         ")"+
                                         ")", 
                                     conn);

                createMethodCallTable.ExecuteNonQuery();

                
                SharpHsqlCommand createExecutionFlowTable = new
                    SharpHsqlCommand("CREATE TABLE EXECUTION_FLOW (ID int NOT NULL IDENTITY PRIMARY KEY," +
                                     "THREAD_NAME varchar(255) NULL,JVM varchar(255) NULL,BEGIN_TIME numeric(19, 0) NULL," +
                                     "END_TIME numeric(19, 0) NULL, BEGIN_TIME_AS_DATE datetime NULL," +
                                     "DURATION numeric(19, 0) NULL,FIRST_METHOD_CALL_INDEX_IN_FLOW int NULL,"+
                                     "CONSTRAINT fk FOREIGN KEY (ID,FIRST_METHOD_CALL_INDEX_IN_FLOW) REFERENCES "+
                                     "METHOD_CALL (FLOW_ID, INDEX_IN_FLOW))",
                                     conn);

                createExecutionFlowTable.ExecuteNonQuery();
                                                
                existingDB = true;
            }
        }
    }
}
