using System;
using System.Threading;
using log4net;
using Org.NMonitoring.Core.Persistence;
using Org.NMonitoring.Core.Dao;

namespace Org.NMonitoring.Core.Store.Impl
{
    public sealed class AsynchroneDBWriter : IStoreWriter
    {
        private static ILog sLog = LogManager.GetLogger("SynchroneDbWriter");

        public void WriteExecutionFlow(ExecutionFlowPO executionFlow)
        {
            //TODO FCH : Parametrer
            System.Console.WriteLine("ASynchroneDbWriter:writeExecutionFlow Added new ExecutionFlow to List " + executionFlow);
            ThreadPool.SetMinThreads(1, 0);
            ThreadPool.SetMaxThreads(1, 0);

            ThreadPool.QueueUserWorkItem(new WaitCallback(AsynchroneDBWriter.AsynchroneWrite), executionFlow);
        }
        private static void AsynchroneWrite(Object data)
        {
            try
            {
                ExecutionFlowPO executionFlow = (ExecutionFlowPO)data;
                ExecutionFlowDao dao = new ExecutionFlowDao();
                dao.InsertFullExecutionFlow(executionFlow);
                sLog.Info("Added new ExecutionFlow to List " + executionFlow);
                System.Console.WriteLine("ASynchroneDbWriter:AsynchroneWrite Added new ExecutionFlow to List " + executionFlow);
            } catch (Exception internalException)
            {               
                //TODO FCH      
                sLog.Error("AsynchroneDBWriter::AsynchroneWrite UNABLE TO STORE Flow, "+ internalException);
            }

        }
    }
}

