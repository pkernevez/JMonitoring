using System.Reflection;
using System.Threading;
using NUnit.Framework;
using Org.NMonitoring.Core.Common;
using Org.NMonitoring.Core.Common.Tests;
using Org.NMonitoring.Core.Persistence;
using Org.NMonitoring.Core.Store;
using Org.NMonitoring.Core.Tests.Common;

namespace Org.NMonitoring.Core.Dao.Tests
{
    [TestFixture]
    public class TestExecutionFlowDAO
    {
        private const int SLEEP_DURATION_FOR_ASYNC_WRITE = 1000;

        [TestFixtureSetUp]
        public void initialize()
        {
            InMemoryDBCreation.Create();
            UtilTest.DeleteAllData();
        }

        [Test]
        public void insertExecutionFlowWithNoMethodCallPO()
        {
             ExecutionFlowPO flow= new ExecutionFlowPO("TEST-main", null, "myCLR");
             IExecutionFlowWriter dao = new ExecutionFlowDao();
             int nbMethodsCallBeforeDao = UtilTest.CountMethods();
             dao.InsertFullExecutionFlow(flow);
             Thread.Sleep(SLEEP_DURATION_FOR_ASYNC_WRITE);
             int nbMethodsCallAfterDao = UtilTest.CountMethods();
             UtilTest.DeleteAllData();
             Assert.AreEqual(nbMethodsCallAfterDao, nbMethodsCallBeforeDao);
        }

        [Test]
        public void insertExecutionFlowWithOneMethodCallPOUsingConstructor()
        {
            long tStartTime = Util.CurrentTimeMillis();
            MethodCallPO    point=  new MethodCallPO(null, "TestExecutionFlowDAO", "builNewFlow", "GrDefault", new ParameterInfo[0]);
            point.BeginTime = tStartTime;
            point.EndTime = tStartTime + 2;

            ExecutionFlowPO flow = new ExecutionFlowPO("TEST-main", point, "myCLR");

            IExecutionFlowWriter dao = new ExecutionFlowDao();

            int nbMethodsCallBeforeDao = UtilTest.CountMethods();
            dao.InsertFullExecutionFlow(flow);
            Thread.Sleep(SLEEP_DURATION_FOR_ASYNC_WRITE);
            int nbMethodsCallAfterDao = UtilTest.CountMethods();
            int nbExpextedMethodsCall = 1;
            UtilTest.DeleteAllData();
            Assert.AreEqual(nbExpextedMethodsCall,nbMethodsCallAfterDao - nbMethodsCallBeforeDao);
        }


        [Test]
        public void insertExecutionFlowWithRecursiveMethodCallPO()
        {
            ExecutionFlowPO flow = UtilTest.buildNewFullFlow();
            IExecutionFlowWriter dao = new ExecutionFlowDao();
            int nbMethodsCallBeforeDao = UtilTest.CountMethods();
            dao.InsertFullExecutionFlow(flow);
            Thread.Sleep(SLEEP_DURATION_FOR_ASYNC_WRITE);
            int nbMethodsCallAfterDao = UtilTest.CountMethods();
            int nbExpextedMethodsCall = 3;
            UtilTest.DeleteAllData();
            Assert.AreEqual(nbExpextedMethodsCall, nbMethodsCallAfterDao - nbMethodsCallBeforeDao);
        }

        [Test]
        public void insertTwoExecutionFlowWithRecursiveMethodCallPO()
        {
            ExecutionFlowPO flow  = UtilTest.buildNewFullFlow();
            ExecutionFlowPO flow2 = UtilTest.buildNewFullFlow();
            IExecutionFlowWriter dao = new ExecutionFlowDao();
            int nbMethodsCallBeforeDao = UtilTest.CountMethods();
            dao.InsertFullExecutionFlow(flow);
            dao.InsertFullExecutionFlow(flow2);
            Thread.Sleep(SLEEP_DURATION_FOR_ASYNC_WRITE);
            int nbMethodsCallAfterDao = UtilTest.CountMethods();
            int nbExpextedMethodsCall = 6;
            UtilTest.DeleteAllData();
            Assert.AreEqual(nbExpextedMethodsCall, nbMethodsCallAfterDao - nbMethodsCallBeforeDao);
        }
    }
}
