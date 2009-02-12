using System;
using System.Threading;
using Org.NMonitoring.Core.Common;
using Org.NMonitoring.Core.Persistence;

namespace Org.NMonitoring.Core.Store.Impl
{
    public sealed class AsynchroneDBWriter : IStoreWriter
    {        
        public AsynchroneDBWriter()
        {  
        }

        public void WriteExecutionFlow(ExecutionFlowPO executionFlow)
        {
            //TODO FCH : Parametrer le nombre de thread
            //Let the old IOC parameters as they were.
            int unUsed, minIOC, maxIOC;
            ThreadPool.GetMinThreads(out unUsed, out minIOC);
            ThreadPool.GetMaxThreads(out unUsed, out maxIOC);
            ThreadPool.SetMinThreads(1, minIOC);
            ThreadPool.SetMaxThreads(1, maxIOC); // Use only 1 thread
          
            ThreadPool.QueueUserWorkItem(new WaitCallback(AsynchroneWrite), executionFlow);
        }
        private static void AsynchroneWrite(Object data)
        {
            try
            {
                IExecutionFlowWriter ExecutionflowWriter= Factory<IExecutionFlowWriter>.Instance.GetNewObject();
                ExecutionflowWriter.InsertFullExecutionFlow((ExecutionFlowPO)data);               
            } 
            catch (Exception internalException)
            {
                throw new NMonitoringException("AsynchroneDBWriter::AsynchroneWrite UNABLE TO STORE Flow", internalException);
            }

        }
    }
}

