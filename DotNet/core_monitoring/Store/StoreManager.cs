using System;

using Org.NMonitoring.Core.Common;
using Org.NMonitoring.Core.Persistence;
using Org.NMonitoring.Core.Configuration;
using Org.NMonitoring.Core.Store.Impl;
using Org.NMonitoring.Core.Dao;

using log4net;

using DotNetGuru.AspectDNG.Joinpoints;


namespace Org.NMonitoring.Core.Store
{
    public class StoreManager
    {

		#region ThreadStatic Singleton
		[ThreadStatic]
		private static StoreManager storeManager;

		public static StoreManager GetManager()
		{
			try
			{
				if (storeManager == null)
				{
                    DaoHelper.Initialize(System.Data.SqlClient.SqlClientFactory.Instance, ConfigurationManager.Instance.ConnexionString);                           
                    storeManager = new StoreManager();
				}

			}
			catch (Exception e)
			{
				// Impossible de laisser remonter l'erreur car elle se confond avec l'erreur
				// de la méthode fonctionelle invoquée.
				sLog.Error("Impossible d'instancier un logger pour tracer les appels", e);
			}

			return storeManager;
		}
        #endregion ThreadStatic Singleton

        private MethodCallPO currentLogPoint;

        /** <code>CommonsLog</code> instance. */
        static ILog sLog = LogManager.GetLogger("StoreManager");

        private IStoreWriter storeWriter;

        /**
         * Default constructor.
         * 
         */
        protected StoreManager()
            : this(new StoreFactory().Writer)
        {
            Console.WriteLine("StoreManager::StoreManager(Vide)");
        }

        /**
         * Constructor for testing purpose.
         * 
         * @param pStoreWriter The <code>IStoreWriter</code> to use.
         */
        protected StoreManager(IStoreWriter storeWriter)
        {
            this.storeWriter = storeWriter;
            Console.WriteLine("StoreManager::StoreManager(IStoreWriter,Configuration )");
        }

        /**
         * Trace a method with its arguments.
         * 
         * @param joinPoint The method signature.
         * @param args The method arguments.
         * @param groupName The name of the group associated with this <code>MethodCallDTO</code>.
         */

        public void LogBeginOfMethod(OperationJoinPoint joinPoint, Object [] args, String groupName)
        {
            if (joinPoint == null)
            {
                throw new NMonitoringException("No jointPoint to log");
            }
            string operationName = joinPoint.TargetOperationName;
            string classeName = joinPoint.TargetOperation.ReflectedType.FullName; // Name ?

			LogBeginOfMethod(classeName,operationName,args,groupName);
        }


		public void LogBeginOfMethod(string className, string operationName, Object [] args, String groupName)
		{
			Console.WriteLine("StoreManager::logBeginOfMethod()");

			if (currentLogPoint == null)
			{ // Premier appel du Thread
				if (sLog.IsDebugEnabled)
				{
					//sLog.Debug("logBeginOfMethod First Time" + opJoinPoint.ToString());
				}
				currentLogPoint = new MethodCallPO(null, className, operationName, groupName, args);
			}
			else
			{
				if (sLog.IsDebugEnabled)
				{
					//sLog.Debug("logBeginOfMethod Any Time" + opJoinPoint.ToString());
				}
				MethodCallPO tOldPoint = currentLogPoint;
				currentLogPoint = new MethodCallPO(tOldPoint, className, operationName,  groupName, args);
			}
		}

        /**
         * Trace the result of a method ended normally.
         * 
         * @param result The result of the execution of the method.
         */
        public void LogEndOfMethodNormal(Object result)
        {
            Console.WriteLine("StoreManager::logEndOfMethodNormal()");

            // To limit call to toString on business object, that could be expensive
            String tResultAsString = EndMethod(currentLogPoint, result);
            if (currentLogPoint.Parent == null)
            { // Dernier appel du Thread
                if (sLog.IsDebugEnabled)
                {
                    sLog.Debug("logEndOfMethodNormal Last Time" + tResultAsString);
                }
                String threadName = System.Threading.Thread.CurrentThread.GetHashCode().ToString();
                String threadName2 = System.Threading.Thread.CurrentThread.GetHashCode().ToString(System.Globalization.CultureInfo.CurrentCulture.NumberFormat);
                if (System.Threading.Thread.CurrentThread.Name != null)
                    threadName += " (" + System.Threading.Thread.CurrentThread.Name + ")";

                Console.WriteLine("StoreManager::logEndOfMethodNormal(Dernier Appel)");
                ExecutionFlowPO tFlow = new ExecutionFlowPO(threadName, currentLogPoint, ConfigurationManager.getServerName());
                try
                {
                    sLog.Debug("StoreManager::storeWriter.writeExecutionFlow(Dernier Appel)");
                    storeWriter.WriteExecutionFlow(tFlow);
                }
                catch (NMonitoringException internalException)
                {               
                    //TODO FCH      
                    sLog.Error("StoreManager::storeWriter.writeExecutionFlow UNABLE TO STORE Flow, "+ internalException);
                }
                currentLogPoint = null;
            }
            else
            {
                if (sLog.IsDebugEnabled)
                {
                    sLog.Debug("logEndOfMethodNormal Any Time" + tResultAsString);
                }
                currentLogPoint = currentLogPoint.Parent;
            }
        }

        /**
         * Trace the <code>Exception</code> thrown during its execution.
         * 
         * @param exception The <code>Exception</code> to trace.
         */
        public void LogEndOfMethodWithException(Exception exception)
        {
            string externalExceptionMessage = "";
            if (sLog.IsDebugEnabled)
            {
                sLog.Debug("logEndOfMethodWithException " + (exception == null ? "" : exception.Message));
            }
            if (exception == null)
            { // On ne logue pas le détail
                EndMethodWithException(currentLogPoint, null, null);
            }
            else
            {
                externalExceptionMessage = exception.Message;
                EndMethodWithException(currentLogPoint, exception.GetType().FullName, externalExceptionMessage);
            }

            if (currentLogPoint.Parent == null)
            { // Dernier appel du Thread
                if (sLog.IsDebugEnabled)
                {
                    sLog.Debug("logEndOfMethodWithException Last Time" + externalExceptionMessage);
                }
				
                //String threadName = System.Threading.Thread.CurrentThread.ManagedThreadId.ToString();
				String threadName = System.Threading.Thread.CurrentThread.GetHashCode().ToString();
                if (System.Threading.Thread.CurrentThread.Name != null)
                    threadName += " (" + System.Threading.Thread.CurrentThread.Name + ")";

                ExecutionFlowPO tFlow = new ExecutionFlowPO(threadName, currentLogPoint, ConfigurationManager.getServerName());
                Console.WriteLine("PKE Log avec Exception");
                storeWriter.WriteExecutionFlow(tFlow);
                currentLogPoint = null;
            }
            else
            {
                if (sLog.IsDebugEnabled)
                {
                    sLog.Debug("logEndOfMethodWithException Any Time" + (exception == null ? "" : exception.Message));
                }
                currentLogPoint = currentLogPoint.Parent;
            }

        }

        /**
         * Define the return value of the method associated with this <code>MethodCallDTO</code> when it didn't throw a
         * <code>Throwable</code>.
         * 
         * @param methodCall the current methodcall to manage.
         * @param returnValue The return value of the method.
         */
        public static String EndMethod(MethodCallPO methodCall, Object returnValue)
        {
            if (methodCall == null)
            {
                throw new NMonitoringException("No methodCall to log");
            }

            String returnValueAsString = null;
            methodCall.EndTime = Org.NMonitoring.Core.Common.Util.CurrentTimeMillis();
            
            if (returnValue != null)
            {
                try
                {
                    returnValueAsString = returnValue.ToString();
                    methodCall.ReturnValue = returnValueAsString;
                }
                catch (NMonitoringException internalException)
                {
                    sLog.Error("Unable to trace return value of call.", internalException);
                }
            }
            return returnValueAsString;
        }

        /**
         * Define the <code>Throwable</code> thrown by the method associated with this <code>MethodCallDTO</code>.
         * 
         * @param methodCall the current methodcall to manage.
         * @param exceptionClassName The name of the <code>Class</code> of the <code>Exception</code>.
         * @param exceptionMessage The message of the <code>Exception</code>.
         */
        private static void EndMethodWithException(MethodCallPO methodCall, String exceptionClassName, String exceptionMessage)
        {
            methodCall.EndTime =  Org.NMonitoring.Core.Common.Util.CurrentTimeMillis();
            methodCall.ThrowableClass =  exceptionClassName;
            methodCall.ThrowableMessage =  exceptionMessage;
        }
    }
}

  
