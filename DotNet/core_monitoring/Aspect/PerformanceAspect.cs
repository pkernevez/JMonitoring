using System;
using System.IO;
using System.Threading;


using log4net;
using DotNetGuru.AspectDNG.Joinpoints;

using Org.NMonitoring.Core.Store;
using Org.NMonitoring.Core.Dao;
using Org.NMonitoring.Core.Common;


namespace Org.NMonitoring.Core.Aspect
{
    public class PerformanceAspect
    {
        /** Log instance. */
        private static ILog sLog = LogManager.GetLogger("SynchroneDbWriter");

        /** Allow the parameter log. */
        private static bool logParameter = false;
        protected static bool LogParameter
        {
            get { return logParameter; }
            set { logParameter = value; }
        }

        /** Nom du group associé au pointcut. */
        private static String groupName = "Default";
        protected static string GroupName
        {
            get { return groupName; }
            set { groupName = value; }
        }


        //Used to implement child classes
        protected PerformanceAspect()
        {
        }


        public static object ExecutionToLogInternal(OperationJoinPoint jp)
        {
            object result = null;

            if (jp == null)
            {
                sLog.Error("Unable to log a null joinpoint");
                return result;
            }

            StoreManager storeManager = StoreManager.GetManager();

            Object[] tArgs = null;
            if (logParameter)
            {
                // On log les paramètres d'appel, de retour et les exceptions
                tArgs = jp.Arguments;
            }

            try
            {
                if (storeManager != null)
                {
                    storeManager.LogBeginOfMethod(jp, tArgs, groupName);
                }
                else
                {
                    sLog.Error("executionToLogInternal Impossible de logger l'entrée de la methode");
                }
            }
            catch (NMonitoringException e) 
            {
                sLog.Error("NMonitoringException : Unable to log", e);
            }



            // En cas d'exception le code est dans le trigger "after()throwing..."
            try
            {
                result = jp.Proceed();

                try
                {
                    if (storeManager != null)
                    {
                        if (logParameter)
                        {
                            storeManager.LogEndOfMethodNormal(result);
                        }
                        else
                        {
                            storeManager.LogEndOfMethodNormal(null);
                        }
                    }
                    else
                    {
                        sLog.Error("executionToLogInternal Impossible de logger la sortie de la methode");
                    }
                }
                catch (NMonitoringException e)
                {
                    sLog.Error("NMonitoringException : Unable to log", e);
                    throw;
                    //LogFactory.getLog(this.getClass()).error("Unable to log", e); FCH :cf PKE
                }
            }
            catch (Exception externalException)
            {
                try
                {
                    storeManager.LogEndOfMethodWithException(externalException);
                }
                catch (NMonitoringException internalException)
                {
                    sLog.Error("NMonitoringException : Unable to log execution Throwable", internalException);
                    //LogFactory.getLog(this.getClass()).error("Unable to log", e); FCH :cf PKE
                    throw;
                }
                throw;
            }


            return result;
        }

        /*
            after() throwing (Throwable t): executionToLogInternal() {
                
            }
         * */
    }
}
