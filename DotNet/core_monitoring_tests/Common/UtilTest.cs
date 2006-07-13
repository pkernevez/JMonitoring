using System;
using System.Data;
using Org.NMonitoring.Core.Persistence;
using Org.NMonitoring.Core.Dao;
using Org.NMonitoring.Core.Dao.NMonitoringDataSetTableAdapters;

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
            METHOD_CALLTableAdapter mcAdapter = new METHOD_CALLTableAdapter();
            NMonitoringDataSet.METHOD_CALLDataTable methodCalls = mcAdapter.GetData();
            return methodCalls.Rows.Count;
        }

        public static int CountFlows()
        {
            EXECUTION_FLOWTableAdapter flowAdapter = new EXECUTION_FLOWTableAdapter();
            NMonitoringDataSet.EXECUTION_FLOWDataTable flows = flowAdapter.GetData();
            return flows.Rows.Count;
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
            tPoint.setBeginTime(tStartTime);

            tSubPoint = new MethodCallPO(tPoint, "TestExecutionFlowDAO", "builNewFullFlow2", "GrChild1", new System.Reflection.ParameterInfo[0]);
            tSubPoint.setBeginTime(tStartTime + 2);
            tSubPoint.setEndTime(tStartTime + 5);

            tSubPoint = new MethodCallPO(tPoint, "TestExecutionFlowDAO", "builNewFullFlow3", "GrChild2", new System.Reflection.ParameterInfo[0]);
            tSubPoint.setBeginTime(tStartTime + 8);
            tSubPoint.setEndTime(tStartTime + 13);

            tPoint.setEndTime(tStartTime + 20);
            tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myCLR");
            return tFlow;
        }
    }
}
