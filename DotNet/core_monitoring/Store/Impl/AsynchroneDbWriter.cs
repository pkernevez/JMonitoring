using System;
using System.Threading;
using Org.NMonitoring.Core.Persistence;
using Org.NMonitoring.Core.Dao;
using Org.NMonitoring.Core.Common;

namespace Org.NMonitoring.Core.Store.Impl
{
    public sealed class AsynchroneDBWriter : IStoreWriter
    {
        public void WriteExecutionFlow(ExecutionFlowPO executionFlow)
        {
            //TODO FCH : Parametrer
            ThreadPool.SetMinThreads(1, 0);
            ThreadPool.SetMaxThreads(2, 0);

            ThreadPool.QueueUserWorkItem(new WaitCallback(AsynchroneDBWriter.AsynchroneWrite), executionFlow);
        }
        private static void AsynchroneWrite(Object data)
        {
            try
            {
                ExecutionFlowPO executionFlow = (ExecutionFlowPO)data;
                ExecutionFlowDao dao = new ExecutionFlowDao();
                dao.InsertFullExecutionFlow(executionFlow);
            } 
            catch (Exception internalException)
            {
                throw new NMonitoringException("AsynchroneDBWriter::AsynchroneWrite UNABLE TO STORE Flow", internalException);
            }

        }
    }
}

