using System;
using NUnit.Framework;
using Org.NMonitoring.Core.Persistence;
using Org.NMonitoring.Core.Store.Impl;

using Org.NMonitoring.Core.Common.Tests;
using Org.NMonitoring.Core.Dao;


namespace Org.NMonitoring.Core.Store.Tests
{
    [TestFixture]
    public class TestSynchroneDbWriter
    {

        [TestFixtureSetUp]
        public void initialize()
        {
            DaoHelper.Initialize(System.Data.SqlClient.SqlClientFactory.Instance,
                   "Data Source=VIRTUALPODE\\JMONITORING;Initial Catalog=jmonitoring;User ID=jmonitoring;Password=jmonitoring");
            UtilTest.DeleteAllData();
        }


        [Test]
        public void insertExecutionFlowWithRecursiveMethodCallPO()
        {
          
            ExecutionFlowPO flow = UtilTest.buildNewFullFlow();
            
            int nbMethodsCallBeforeDao = UtilTest.CountMethods();

            SynchroneDbWriter writer = new SynchroneDbWriter();
            writer.writeExecutionFlow(flow);

            int nbMethodsCallAfterDao = UtilTest.CountMethods();
            
            int nbExpextedMethodsCall = 3;

            UtilTest.DeleteAllData();
            Assert.AreEqual(nbExpextedMethodsCall, nbMethodsCallAfterDao - nbMethodsCallBeforeDao);
        }
    }
}
