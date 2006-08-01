using System;

using Org.NMonitoring.Core.Persistence;
using Org.NMonitoring.Core.Configuration;
using Org.NMonitoring.Core.Store.Impl;
using Org.NMonitoring.Core.Dao;

using log4net;

using DotNetGuru.AspectDNG.Joinpoints;

/*
 * 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.Signature;
import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.MethodCallPO;
import org.jmonitoring.core.store.impl.StoreFactory;*/

namespace Org.NMonitoring.Core.Store
{
    public class StoreManager
    {

		#region ThreadStatic Singleton
		[ThreadStatic]
		private static StoreManager mStoreManager = null;

		public static StoreManager getManager()
		{
			try
			{
				if (mStoreManager == null)
				{
                    DaoHelper.Initialize(System.Data.SqlClient.SqlClientFactory.Instance,
                           "Data Source=.\\SQLEXPRESS;Initial Catalog=jmonitoring;User ID=jmonitoring;Password=jmonitoring");
                    mStoreManager = new StoreManager();
				}

			}
			catch (Exception e)
			{
				// Impossible de laisser remonter l'erreur car elle se confond avec l'erreur
				// de la méthode fonctionelle invoquée.
				sLog.Error("Impossible d'instancier un logger pour tracer les appels", e);
			}

			return mStoreManager;
		}
		#endregion ThreadStatic Singleton

        private MethodCallPO mCurrentLogPoint;

        /** <code>CommonsLog</code> instance. */
        static ILog sLog = LogManager.GetLogger("StoreManager");

        private IStoreWriter mStoreWriter;


        private Configuration.Configuration mConfiguration;

        /**
         * Default constructor.
         * 
         * @param pConfiguration The configuration instance to use.
         */
        protected StoreManager()  : this(StoreFactory.getWriter(), Configuration.Configuration.getInstance())
        {
            Console.WriteLine("StoreManager::StoreManager(Vide)");
        }

        /**
         * Constructor for testing purpose.
         * 
         * @param pStoreWriter The <code>IStoreWriter</code> to use.
         * @param pConfiguration The configuration instance to use.
         */
        protected StoreManager(IStoreWriter pStoreWriter, Configuration.Configuration pConfiguration)
        {
            mConfiguration = pConfiguration;
            mStoreWriter = pStoreWriter;
            Console.WriteLine("StoreManager::StoreManager(IStoreWriter,Configuration )");
        }

        /**
         * Trace a method with its arguments.
         * 
         * @param pSignature The method signature.
         * @param pArgs The method arguments.
         * @param pGroupName The name of the group associated with this <code>MethodCallDTO</code>.
         */

        public void logBeginOfMethod(OperationJoinPoint opJoinPoint, Object [] pArgs, String pGroupName)
        {
            string operationName = opJoinPoint.TargetOperationName;
            string classeName = opJoinPoint.TargetOperation.ReflectedType.FullName; // Name ?

			logBeginOfMethod(classeName,operationName,pArgs,pGroupName);
        }


		public void logBeginOfMethod(string className, string operationName, Object [] pArgs, String pGroupName)
		{
			Console.WriteLine("StoreManager::logBeginOfMethod()");

			if (mCurrentLogPoint == null)
			{ // Premier appel du Thread
				if (sLog.IsDebugEnabled)
				{
					//sLog.Debug("logBeginOfMethod First Time" + opJoinPoint.ToString());
				}
				mCurrentLogPoint = new MethodCallPO(null, className, operationName, pGroupName, pArgs);
			}
			else
			{
				if (sLog.IsDebugEnabled)
				{
					//sLog.Debug("logBeginOfMethod Any Time" + opJoinPoint.ToString());
				}
				MethodCallPO tOldPoint = mCurrentLogPoint;
				mCurrentLogPoint = new MethodCallPO(tOldPoint, className, operationName,  pGroupName, pArgs);
			}
		}

        /**
         * Trace the result of a method ended normally.
         * 
         * @param pResult The result of the execution of the method.
         */
        public void logEndOfMethodNormal(Object pResult)
        {
            Console.WriteLine("StoreManager::logEndOfMethodNormal()");

            // To limit call to toString on business object, that could be expensive
            String tResultAsString = endMethod(mCurrentLogPoint, pResult);
            if (mCurrentLogPoint.Parent == null)
            { // Dernier appel du Thread
                if (sLog.IsDebugEnabled)
                {
                    sLog.Debug("logEndOfMethodNormal Last Time" + tResultAsString);
                }
                //String threadName = System.Threading.Thread.CurrentThread.ManagedThreadId.ToString();
				String threadName = System.Threading.Thread.CurrentThread.GetHashCode().ToString();
                if (System.Threading.Thread.CurrentThread.Name != null)
                    threadName += " (" + System.Threading.Thread.CurrentThread.Name + ")";

                Console.WriteLine("StoreManager::logEndOfMethodNormal(Dernier Appel)");
                ExecutionFlowPO tFlow = new ExecutionFlowPO(threadName, mCurrentLogPoint, mConfiguration.getServerName());
                try
                {
                    Console.WriteLine("StoreManager::mStoreWriter.writeExecutionFlow(Dernier Appel)");
                    mStoreWriter.writeExecutionFlow(tFlow);
					//System.Threading.Thread.Sleep(2000);
                }
                catch (Exception e)
                {
                    Console.WriteLine("StoreManager::logEndOfMethodNormal Exception : " + e.Message);
                }
                mCurrentLogPoint = null;
            }
            else
            {
                if (sLog.IsDebugEnabled)
                {
                    sLog.Debug("logEndOfMethodNormal Any Time" + tResultAsString);
                }
                mCurrentLogPoint = mCurrentLogPoint.Parent;
            }
        }

        /**
         * Trace the <code>Exception</code> thrown during its execution.
         * 
         * @param pException The <code>Exception</code> to trace.
         */
        public void logEndOfMethodWithException(Exception pException)
        {
            if (sLog.IsDebugEnabled)
            {
                sLog.Debug("logEndOfMethodWithException " + (pException == null ? "" : pException.Message));
            }
            if (pException == null)
            { // On ne logue pas le détail
                endMethodWithException(mCurrentLogPoint, null, null);
            }
            else
            {
                endMethodWithException(mCurrentLogPoint, pException.GetType().FullName, pException.Message);
            }

            if (mCurrentLogPoint.Parent == null)
            { // Dernier appel du Thread
                if (sLog.IsDebugEnabled)
                {
                    sLog.Debug("logEndOfMethodWithException Last Time" + pException.Message);
                }
				
                //String threadName = System.Threading.Thread.CurrentThread.ManagedThreadId.ToString();
				String threadName = System.Threading.Thread.CurrentThread.GetHashCode().ToString();
                if (System.Threading.Thread.CurrentThread.Name != null)
                    threadName += " (" + System.Threading.Thread.CurrentThread.Name + ")";

                ExecutionFlowPO tFlow = new ExecutionFlowPO(threadName, mCurrentLogPoint, mConfiguration.getServerName());
                mStoreWriter.writeExecutionFlow(tFlow);
                mCurrentLogPoint = null;
            }
            else
            {
                if (sLog.IsDebugEnabled)
                {
                    sLog.Debug("logEndOfMethodWithException Any Time" + (pException == null ? "" : pException.Message));
                }
                mCurrentLogPoint = mCurrentLogPoint.Parent;
            }

        }

        /**
         * Define the return value of the method associated with this <code>MethodCallDTO</code> when it didn't throw a
         * <code>Throwable</code>.
         * 
         * @param pMethodCall the current methodcall to manage.
         * @param pReturnValue The return value of the method.
         */
        public String endMethod(MethodCallPO pMethodCall, Object pReturnValue)
        {
            String tReturnValueAsString = null;
            pMethodCall.EndTime = Org.NMonitoring.Core.Common.Util.CurrentTimeMillis();
            
            if (pReturnValue != null)
            {
                try
                {
                    tReturnValueAsString = pReturnValue.ToString();
                    pMethodCall.ReturnValue = tReturnValueAsString;
                }
                catch (Exception tT)
                {
                    sLog.Error("Unable to trace return value of call.", tT);
                }
            }
            return tReturnValueAsString;
        }

        /**
         * Define the <code>Throwable</code> thrown by the method associated with this <code>MethodCallDTO</code>.
         * 
         * @param pMethodCall the current methodcall to manage.
         * @param pExceptionClassName The name of the <code>Class</code> of the <code>Exception</code>.
         * @param pExceptionMessage The message of the <code>Exception</code>.
         */
        private void endMethodWithException(MethodCallPO pMethodCall, String pExceptionClassName, String pExceptionMessage)
        {
            pMethodCall.EndTime =  Org.NMonitoring.Core.Common.Util.CurrentTimeMillis();
            pMethodCall.ThrowableClass =  pExceptionClassName;
            pMethodCall.ThrowableMessage =  pExceptionMessage;
        }
    }
}

  
