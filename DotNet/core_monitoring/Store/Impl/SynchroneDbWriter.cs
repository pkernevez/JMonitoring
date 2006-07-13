using System;
using log4net;
using Org.NMonitoring.Core.Persistence;
using Org.NMonitoring.Core.Dao;

namespace Org.NMonitoring.Core.Store.Impl
{
    public sealed class SynchroneDbWriter : IStoreWriter
    {
        private static ILog sLog = LogManager.GetLogger("SynchroneDbWriter");

        public void writeExecutionFlow(ExecutionFlowPO executionFlow)
        {
            ExecutionFlowDAO dao = new ExecutionFlowDAO();

            dao.insertFullExecutionFlow(executionFlow);
            System.Console.WriteLine("SynchroneDbWriter:writeExecutionFlow Added new ExecutionFlow to List " + executionFlow);
            sLog.Info("Added new ExecutionFlow to List " + executionFlow);

        }
    }
}