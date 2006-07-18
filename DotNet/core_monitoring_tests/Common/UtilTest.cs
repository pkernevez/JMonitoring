using System;
using System.Data;
using Org.NMonitoring.Core.Persistence;
using Org.NMonitoring.Core.Dao;

namespace Org.NMonitoring.Core.Common.Tests
{
    class UtilTest
    {

        public static void DeleteAllData()
        {
            DaoHelper dao = DaoHelper.Instance;
            String sCommandText = @"UPDATE [jmonitoring].[EXECUTION_FLOW] SET  [FIRST_METHOD_CALL_ID] = NULL;DELETE FROM [jmonitoring].[METHOD_CALL];DELETE FROM [jmonitoring].[EXECUTION_FLOW];";
            IDbCommand cmd = dao.CreateCommand(sCommandText, CommandType.Text);

            dao.Connection.Open();

            try
            {
                cmd.ExecuteNonQuery();
            }
            finally
            {
                dao.Connection.Close();
            }
        }


        public static int CountMethods()
        {
            DaoHelper dao = DaoHelper.Instance;
            String sCommandText = @"SELECT COUNT(*) FROM [jmonitoring].[METHOD_CALL];";
            IDbCommand cmd = dao.CreateCommand(sCommandText, CommandType.Text);

            dao.Connection.Open();

            int count = 0;
            try
            {
                count = (int)cmd.ExecuteScalar();
            }
            finally
            {
                dao.Connection.Close();
            }

            return count;
        }

        public static int CountFlows()
        {
            DaoHelper dao = DaoHelper.Instance;
            String sCommandText = @"SELECT COUNT(*) FROM [jmonitoring].[EXECUTION_FLOW];";
            IDbCommand cmd = dao.CreateCommand(sCommandText, CommandType.Text);

            dao.Connection.Open();

            int count = 0;
            try
            {
                count = (int)cmd.ExecuteScalar();
            }
            finally
            {
                dao.Connection.Close();
            }

            return count;
        }

        public static ExecutionFlowPO buildNewEmptyFlow()
        {
            return new ExecutionFlowPO("TEST-main", null, "myCLR");
        }


        public static ExecutionFlowPO buildNewFullFlow()
        {
            ExecutionFlowPO tFlow;
            MethodCallPO tPoint;
            MethodCallPO tSubPoint;
            long tStartTime = Common.Util.CurrentTimeMillis();

            tPoint = new MethodCallPO(null, "TestExecutionFlowDAO", "builNewFullFlow", "GrDefault", new System.Reflection.ParameterInfo[0]);
            tPoint.BeginTime = tStartTime;

            tSubPoint = new MethodCallPO(tPoint, "TestExecutionFlowDAO", "builNewFullFlow2", "GrChild1", new System.Reflection.ParameterInfo[0]);
            tSubPoint.BeginTime = (tStartTime + 2);
            tSubPoint.EndTime = (tStartTime + 5);

            tSubPoint = new MethodCallPO(tPoint, "TestExecutionFlowDAO", "builNewFullFlow3", "GrChild2", new System.Reflection.ParameterInfo[0]);
            tSubPoint.BeginTime = (tStartTime + 8);
            tSubPoint.EndTime = (tStartTime + 13);

            tPoint.EndTime = (tStartTime + 20);
            tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myCLR");
            return tFlow;
        }
    }
}
