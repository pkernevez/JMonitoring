using System;
using System.Data;
using System.Reflection;
using Org.NMonitoring.Core.Dao;
using Org.NMonitoring.Core.Persistence;

namespace Org.NMonitoring.Core.Common.Tests
{
    class UtilTest
    {
        public static void DeleteAllData()
        {
            IDaoHelper dao = Factory<IDaoHelper>.Instance.GetNewObject();
            String sCommandText = @"UPDATE EXECUTION_FLOW set FIRST_METHOD_CALL_INDEX_IN_FLOW = NULL;
                     UPDATE METHOD_CALL set PARENT_INDEX_IN_FLOW = NULL;
                     DELETE FROM METHOD_CALL;DELETE FROM EXECUTION_FLOW;";
                //@"UPDATE [METHOD_CALL] SET  [FLOW_ID] = -1;DELETE FROM [METHOD_CALL];DELETE FROM [EXECUTION_FLOW];";

            IDbCommand cmd = dao.CreateCommand(sCommandText, CommandType.Text);

            dao.Connection.Open();

            try
            {
                cmd.ExecuteNonQuery();
            }
            catch
            {
                dao.Connection.Close();
                throw;
            }
            finally
            {
                dao.Connection.Close();
            }
        }


        public static int CountMethods()
        {
            IDaoHelper dao = Factory<IDaoHelper>.Instance.GetNewObject();
            String sCommandText = @"SELECT COUNT(*) FROM METHOD_CALL;";
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
            IDaoHelper dao = Factory<IDaoHelper>.Instance.GetNewObject();
            String sCommandText = @"SELECT COUNT(*) FROM EXECUTION_FLOW;";
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
            //Warning : the constant value 100 is used in place of tStartTime
            ExecutionFlowPO tFlow;
            MethodCallPO tPoint;
            MethodCallPO tSubPoint;
            long tStartTime = Util.CurrentTimeMillis();

            tPoint = new MethodCallPO(null, "TestExecutionFlowDAO", "builNewFullFlow", "GrDefault", new ParameterInfo[0]);
            tPoint.BeginTime = tStartTime;

            tSubPoint = new MethodCallPO(tPoint, "TestExecutionFlowDAO", "builNewFullFlow2", "GrChild1", new ParameterInfo[0]);
            tSubPoint.BeginTime = (tStartTime + 2);
            tSubPoint.EndTime = (tStartTime + 5);

            tSubPoint = new MethodCallPO(tPoint, "TestExecutionFlowDAO", "builNewFullFlow3", "GrChild2", new ParameterInfo[0]);
            tSubPoint.BeginTime = (tStartTime + 8);
            tSubPoint.EndTime = (tStartTime + 13);

            tPoint.EndTime = (tStartTime + 20);
            tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myCLR");
            return tFlow;
        }
    }
}
