using System;
using NUnit.Framework;
using Org.NMonitoring.Core.Persistence;
using Org.NMonitoring.Core.Store.Impl;

using Org.NMonitoring.Core.Common.Tests;
using Org.NMonitoring.Core.Dao;

using System.Data.Hsql;


namespace Org.NMonitoring.Core.Store.Tests
{
    [TestFixture]
    public class TestASynchroneDbWriter
    {

        [TestFixtureSetUp]
        public void initialize()
        {
            SqlDaoHelper.Initialize(Configuration.ConfigurationManager.Instance.ConnexionString);
            //SqlDaoHelper.Initialize(System.Data.SqlClient.HSqlClientFactory.Instance, Configuration.ConfigurationManager.Instance.ConnexionString);
            UtilTest.DeleteAllData();
        }


        [Test]
        public void insertExecutionFlowWithRecursiveMethodCallPO()
        {

            ExecutionFlowPO flow = UtilTest.buildNewFullFlow();

            int nbMethodsCallBeforeDao = UtilTest.CountMethods();

            AsynchroneDBWriter writer = new AsynchroneDBWriter(new ExecutionFlowDao());
            writer.WriteExecutionFlow(flow);
            System.Threading.Thread.Sleep(5000);
            int nbMethodsCallAfterDao = UtilTest.CountMethods();

            int nbExpextedMethodsCall = 3;

            UtilTest.DeleteAllData();
            Assert.AreEqual(nbExpextedMethodsCall, nbMethodsCallAfterDao - nbMethodsCallBeforeDao);
        }
    }
}
