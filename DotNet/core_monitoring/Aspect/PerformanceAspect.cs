using System;
using System.IO;
using System.Threading;


using log4net;
using DotNetGuru.AspectDNG.Joinpoints;

using Org.NMonitoring.Core.Store;
using Org.NMonitoring.Core.Dao;


namespace Org.NMonitoring.Core.Aspect
{
    public class PerformanceAspect
    {


        /** Log instance. */
        private static ILog sLog = LogManager.GetLogger("SynchroneDbWriter");

        /** Allow the parameter log. */
        protected static bool mLogParameter = false;

        /** Nom du group associé au pointcut. */
        protected static String mGroupName = "Default";



        public static object executionToLogInternal(OperationJoinPoint jp)
        {
            object tResult = null;
            StoreManager storeManager = StoreManager.getManager();

            Object[] tArgs = null;
            if (mLogParameter)
            {
                // On log les paramètres d'appel, de retour et les exceptions
                tArgs = jp.Arguments;
            }

            try
            {
                if (storeManager != null)
                {
                    storeManager.logBeginOfMethod(jp, tArgs, mGroupName);
                }
                else
                {
                    sLog.Error("executionToLogInternal Impossible de logger l'entrée de la methode");
                }
            }
            catch (Exception e) // TODO : FCH : MeasureException
            {
                sLog.Error("Unable to log", e);
            }



            // En cas d'exception le code est dans le trigger "after()throwing..."
            try
            {
                tResult = jp.Proceed();

                try
                {
                    if (storeManager != null)
                    {
                        if (mLogParameter)
                        {
                            storeManager.logEndOfMethodNormal(tResult);
                        }
                        else
                        {
                            storeManager.logEndOfMethodNormal(null);
                        }
                    }
                    else
                    {
                        sLog.Error("executionToLogInternal Impossible de logger la sortie de la methode");
                    }
                }
                catch (Exception e) // TODO : FCH : MeasureException
                {
                    sLog.Error("Unable to log", e);
                    //LogFactory.getLog(this.getClass()).error("Unable to log", e); FCH :cf PKE
                }
            }
            catch (Exception e)
            {
                try
                {
                    storeManager.logEndOfMethodWithException(e);
                }
                catch (Exception tT)
                {
                    sLog.Error("Unable to log execution Throwable");
                }
                throw (e);
            }


            return tResult;
        }

        /*
            after() throwing (Throwable t): executionToLogInternal() {
                
            }
         * */
    }
}
