using System;
using System.Threading;
using log4net;
using Org.NMonitoring.Core.Persistence;
using Org.NMonitoring.Core.Dao;

namespace Org.NMonitoring.Core.Store.Impl
{
    public sealed class AsynchroneDbWriter : IStoreWriter
    {
        private static ILog sLog = LogManager.GetLogger("SynchroneDbWriter");

        public void writeExecutionFlow(ExecutionFlowPO executionFlow)
        {
            //TODO FCH : Parametrer
            System.Console.WriteLine("SynchroneDbWriter:writeExecutionFlow Added new ExecutionFlow to List " + executionFlow);
            ThreadPool.SetMinThreads(1, 0);
            ThreadPool.SetMaxThreads(1, 0);

            ThreadPool.QueueUserWorkItem(new WaitCallback(AsynchroneDbWriter.AsynchroneWrite), executionFlow);
        }
        private static void AsynchroneWrite(Object data)
        {
            ExecutionFlowPO executionFlow = (ExecutionFlowPO)data;
            ExecutionFlowDAO dao = new ExecutionFlowDAO();
            dao.insertFullExecutionFlow(executionFlow);
            sLog.Info("Added new ExecutionFlow to List " + executionFlow);
            System.Console.WriteLine("ASynchroneDbWriter:AsynchroneWrite Added new ExecutionFlow to List " + executionFlow);

        }
    }
}

