using System;
using Org.NMonitoring.Core.Persistence;
using Org.NMonitoring.Core.Dao;
using Org.NMonitoring.Core.Common;

namespace Org.NMonitoring.Core.Store.Impl
{
    public sealed class SynchroneDBWriter : IStoreWriter
    {
        private IExecutionFlowWriter dao;

        public SynchroneDBWriter(IExecutionFlowWriter dao)
        {
            this.dao = dao;    
        }
        
        public void WriteExecutionFlow(ExecutionFlowPO executionFlow)
        {
            try
            {
                dao.InsertFullExecutionFlow(executionFlow);
              }
            catch (Exception internalException)
            {
                throw new NMonitoringException("AsynchroneDBWriter::AsynchroneWrite UNABLE TO STORE Flow", internalException);
            }


        }
    }
}
