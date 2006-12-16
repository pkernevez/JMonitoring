using System;
using Org.NMonitoring.Core.Persistence;
using Org.NMonitoring.Core.Dao;
using Org.NMonitoring.Core.Common;

namespace Org.NMonitoring.Core.Store.Impl
{
    public sealed class SynchroneDBWriter : IStoreWriter
    {
        public void WriteExecutionFlow(ExecutionFlowPO executionFlow)
        {
            try
            {
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
