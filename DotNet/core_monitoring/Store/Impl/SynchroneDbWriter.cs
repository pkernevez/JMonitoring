using System;
using log4net;
using Org.NMonitoring.Core.Persistence;

namespace Org.NMonitoring.Core.Store.Impl
{
    public sealed class SynchroneDbWriter : IStoreWriter
    {
        private static ILog sLog = LogManager.GetLogger("SynchroneDbWriter");

        public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
        {
            //TODO FCH
            //            sExecutor.execute(getAsynchroneLogTask(pExecutionFlow));

            sLog.Info("Added new ExecutionFlow to List " + pExecutionFlow);
        }
    }
}