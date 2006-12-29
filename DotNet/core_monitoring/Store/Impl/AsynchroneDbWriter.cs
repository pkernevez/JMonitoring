using System;
using System.Threading;
using Org.NMonitoring.Core.Persistence;
using Org.NMonitoring.Core.Dao;
using Org.NMonitoring.Core.Common;

namespace Org.NMonitoring.Core.Store.Impl
{
    public sealed class AsynchroneDBWriter : IStoreWriter
    {
        private IExecutionFlowWriter dao;

        private struct ThreadData
        {
            public ExecutionFlowPO flow;
            public IExecutionFlowWriter dao;
        }
        
        public AsynchroneDBWriter(IExecutionFlowWriter dao)
        {
            this.dao = dao;    
        }

        public void WriteExecutionFlow(ExecutionFlowPO executionFlow)
        {
            //TODO FCH : Parametrer
            ThreadPool.SetMinThreads(1, 0);
            ThreadPool.SetMaxThreads(2, 0);

            ThreadData data = new ThreadData();
            data.flow = executionFlow;
            data.dao = dao;
            
            ThreadPool.QueueUserWorkItem(new WaitCallback(AsynchroneDBWriter.AsynchroneWrite), data);
        }
        private static void AsynchroneWrite(Object data)
        {
            try
            {
                ThreadData threadData = (ThreadData)data;
                threadData.dao.InsertFullExecutionFlow(threadData.flow);
            } 
            catch (Exception internalException)
            {
                throw new NMonitoringException("AsynchroneDBWriter::AsynchroneWrite UNABLE TO STORE Flow", internalException);
            }

        }
    }
}

