using System;
using System.Data;
using NUnit.Framework;

using Org.NMonitoring.Core.Dao;
using Org.NMonitoring.Core.Persistence;

using Org.NMonitoring.Core.Common.Tests;

namespace Org.NMonitoring.Core.Dao.Tests
{
    [TestFixture]
    public class TestExecutionFlowDAO
    {
        [TestFixtureSetUp]
        public void initialize()
        {
            DaoHelper.Initialize(System.Data.SqlClient.SqlClientFactory.Instance,
                   "Data Source=VIRTUALPODE\\JMONITORING;Initial Catalog=jmonitoring;User ID=jmonitoring;Password=jmonitoring");
            UtilTest.DeleteAllData();
        }

        [Test]
        public void insertExecutionFlowWithNoMethodCallPO()
        {

             ExecutionFlowPO flow= new ExecutionFlowPO("TEST-main", null, "myCLR");
             ExecutionFlowDAO dao = new ExecutionFlowDAO();
             int nbMethodsCallBeforeDao = UtilTest.CountMethods();
             dao.insertFullExecutionFlow(flow);
             int nbMethodsCallAfterDao = UtilTest.CountMethods();
             UtilTest.DeleteAllData();
             Assert.AreEqual(nbMethodsCallAfterDao, nbMethodsCallBeforeDao);
        }

        [Test]
        public void insertExecutionFlowWithOneMethodCallPOUsingConstructor()
        {

            
            long tStartTime = Common.Util.CurrentTimeMillis();
            MethodCallPO    point=  new MethodCallPO(null, "TestExecutionFlowDAO", "builNewFlow", "GrDefault", new System.Reflection.ParameterInfo[0]);
            point.BeginTime = tStartTime;
            point.EndTime = tStartTime + 2;

            ExecutionFlowPO flow = new ExecutionFlowPO("TEST-main", point, "myCLR");

            ExecutionFlowDAO dao = new ExecutionFlowDAO();

            int nbMethodsCallBeforeDao = UtilTest.CountMethods();
            dao.insertFullExecutionFlow(flow);
            int nbMethodsCallAfterDao = UtilTest.CountMethods();
            int nbExpextedMethodsCall = 1;
            UtilTest.DeleteAllData();
            Assert.AreEqual(nbExpextedMethodsCall,nbMethodsCallAfterDao - nbMethodsCallBeforeDao);
        }


        [Test]
        public void insertExecutionFlowWithRecursiveMethodCallPO()
        {
            ExecutionFlowPO flow = UtilTest.buildNewFullFlow();
            ExecutionFlowDAO dao = new ExecutionFlowDAO();
            int nbMethodsCallBeforeDao = UtilTest.CountMethods();
            dao.insertFullExecutionFlow(flow);
            int nbMethodsCallAfterDao = UtilTest.CountMethods();
            int nbExpextedMethodsCall = 3;
            UtilTest.DeleteAllData();
            Assert.AreEqual(nbExpextedMethodsCall, nbMethodsCallAfterDao - nbMethodsCallBeforeDao);
        }
    
    }
}
