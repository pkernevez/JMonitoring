using Org.NMonitoring.Core.Persistence;

namespace Org.NMonitoring.Core.Store
{
    public interface IExecutionFlowWriter
    {
        void InsertFullExecutionFlow(ExecutionFlowPO executionFlow);
    }
}