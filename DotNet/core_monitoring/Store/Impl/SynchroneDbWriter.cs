using System;
using Org.NMonitoring.Core.Common;
using Org.NMonitoring.Core.Persistence;

namespace Org.NMonitoring.Core.Store.Impl
{
    public sealed class SynchroneDBWriter : IStoreWriter
    {

        public SynchroneDBWriter()
        {   
        }
        
        public void WriteExecutionFlow(ExecutionFlowPO executionFlow)
        {
            try
            {
                IExecutionFlowWriter ExecutionflowWriter = Factory<IExecutionFlowWriter>.Instance.GetNewObject();
                ExecutionflowWriter.InsertFullExecutionFlow(executionFlow);
              }
            catch (Exception internalException)
            {
                throw new NMonitoringException("AsynchroneDBWriter::AsynchroneWrite UNABLE TO STORE Flow", internalException);
            }


        }
    }
}
