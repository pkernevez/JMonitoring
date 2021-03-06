using System;
using System.Globalization;
using System.Threading;
using DotNetGuru.AspectDNG.Joinpoints;
using Org.NMonitoring.Core.Common;
using Org.NMonitoring.Core.Configuration;
using Org.NMonitoring.Core.Persistence;

namespace Org.NMonitoring.Core.Store
{
    public class StoreManager
    {
		#region ThreadStatic Singleton
		[ThreadStatic]
		private static StoreManager storeManager;

        public static StoreManager GetManager()
        {
            if (storeManager == null)
            {
                try
                {                   
                    storeManager = new StoreManager();
                }
                catch (Exception externalException)
                {
                    // Impossible de laisser remonter l'erreur car elle se confond avec l'erreur
                    // de la m�thode fonctionelle invoqu�e.
                    throw new NMonitoringException("Unable to create a new StoreManager ", externalException);
                }
            }


            return storeManager;
        }
        #endregion ThreadStatic Singleton

        private MethodCallPO currentLogPoint;


        private IStoreWriter storeWriter;

        /**
         * Default constructor.
         * 
         */
        protected StoreManager()            
        {
            //Create the Configuration Manager (to initialized it)
            ConfigurationManager confManager = ConfigurationManager.Instance; 

            storeWriter = Factory<IStoreWriter>.Instance.GetNewObject();
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
			if (currentLogPoint == null)
			{ // Premier appel du Thread
				currentLogPoint = new MethodCallPO(null, className, operationName, groupName, args);
			}
			else
			{
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
  
            // To limit call to toString on business object, that could be expensive
            String tResultAsString = EndMethod(currentLogPoint, result);
            if (currentLogPoint.Parent == null)
            { // Dernier appel du Thread
                String threadName = Thread.CurrentThread.GetHashCode().ToString();
                String threadName2 = Thread.CurrentThread.GetHashCode().ToString(CultureInfo.CurrentCulture.NumberFormat);
                if (Thread.CurrentThread.Name != null)
                    threadName += " (" + Thread.CurrentThread.Name + ")";

                ExecutionFlowPO tFlow = new ExecutionFlowPO(threadName, currentLogPoint, ConfigurationManager.getServerName());

                storeWriter.WriteExecutionFlow(tFlow);

                currentLogPoint = null;
            }
            else
            {
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

            if (exception == null)
            { // On ne logue pas le d�tail
                EndMethodWithException(currentLogPoint, null, null);
            }
            else
            {
                externalExceptionMessage = exception.Message;
                EndMethodWithException(currentLogPoint, exception.GetType().FullName, externalExceptionMessage);
            }

            if (currentLogPoint.Parent == null)
            { // Dernier appel du Thread
				
                //String threadName = System.Threading.Thread.CurrentThread.ManagedThreadId.ToString();
				String threadName = Thread.CurrentThread.GetHashCode().ToString();
                if (Thread.CurrentThread.Name != null)
                    threadName += " (" + Thread.CurrentThread.Name + ")";

                ExecutionFlowPO tFlow = new ExecutionFlowPO(threadName, currentLogPoint, ConfigurationManager.getServerName());
                storeWriter.WriteExecutionFlow(tFlow);
                currentLogPoint = null;
            }
            else
            {
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
            methodCall.EndTime = Util.CurrentTimeMillis();
            
            if (returnValue != null)
            {
                try
                {
                    returnValueAsString = returnValue.ToString();
                    methodCall.ReturnValue = returnValueAsString;
                }
                catch (Exception externalException)                
                {
                    throw new NMonitoringException("Unable to trace return value of call.",externalException);
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
            methodCall.EndTime =  Util.CurrentTimeMillis();
            methodCall.ThrowableClass =  exceptionClassName;
            methodCall.ThrowableMessage =  exceptionMessage;
        }
    }
}

  
