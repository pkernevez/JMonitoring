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
            SqlDaoHelper.Initialize(Configuration.ConfigurationManager.Instance.ConnexionString);
            //TODO : Configure Factories
            UtilTest.DeleteAllData();
        }


        [Test]
        public void insertExecutionFlowWithRecursiveMethodCallPO()
        {
          
            ExecutionFlowPO flow = UtilTest.buildNewFullFlow();
            
            int nbMethodsCallBeforeDao = UtilTest.CountMethods();

            SynchroneDBWriter writer = new SynchroneDBWriter();
            writer.WriteExecutionFlow(flow);

            int nbMethodsCallAfterDao = UtilTest.CountMethods();
            
            int nbExpextedMethodsCall = 3;

            UtilTest.DeleteAllData();
            Assert.AreEqual(nbExpextedMethodsCall, nbMethodsCallAfterDao - nbMethodsCallBeforeDao);
        }
    }
}
