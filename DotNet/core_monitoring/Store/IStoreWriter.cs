using System;

using Org.NMonitoring.Core.Persistence;

namespace Org.NMonitoring.Core.Store
{
    public interface IStoreWriter
    {
        void writeExecutionFlow(ExecutionFlowPO pExecutionFlow);
    }
}
