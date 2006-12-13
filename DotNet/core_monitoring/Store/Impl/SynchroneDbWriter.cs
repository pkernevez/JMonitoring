using System;
using log4net;
using Org.NMonitoring.Core.Persistence;
using Org.NMonitoring.Core.Dao;

namespace Org.NMonitoring.Core.Store.Impl
{
    public sealed class SynchroneDBWriter : IStoreWriter
    {
        private static ILog sLog = LogManager.GetLogger("SynchroneDbWriter");

        public void WriteExecutionFlow(ExecutionFlowPO executionFlow)
        {
            try
            {
                ExecutionFlowDao dao = new ExecutionFlowDao();

                System.Console.WriteLine("PKE Avant SynchroneDbWriter:writeExecutionFlow Added new ExecutionFlow to List ");
                dao.InsertFullExecutionFlow(executionFlow);
                System.Console.WriteLine("SynchroneDbWriter:writeExecutionFlow Added new ExecutionFlow to List " + executionFlow);
                sLog.Info("Added new ExecutionFlow to List " + executionFlow);
            } catch (Exception internalException)
            {               
                //TODO FCH      
                  System.Console.WriteLine("AsynchroneDBWriter::AsynchroneWrite UNABLE TO STORE Flow, "+ internalException);
                sLog.Error("AsynchroneDBWriter::AsynchroneWrite UNABLE TO STORE Flow, "+ internalException);
            }

        }
    }
}