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
        private static ILog sLog = LogManager.GetLogger("PerformanceAspect");

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
            StoreManager storeManager = null;
            try
            {

                if (jp == null)
                {
                    sLog.Error("Unable to log a null joinpoint");
                    return result;
                }

                storeManager = StoreManager.GetManager();

                Object[] tArgs = null;
                if (logParameter)
                {
                    // On log les paramètres d'appel, de retour et les exceptions
                    tArgs = jp.Arguments;
                }


                if (storeManager != null)
                {
                    storeManager.LogBeginOfMethod(jp, tArgs, groupName);
                }
                else
                {
                    //sLog.Error("executionToLogInternal Impossible de logger l'entrée de la methode");
                }
            }

            //Impossible de laisser remonter une erreur de NMonitoring vers l'appli !
            // TODO : Voir s'il ne faudrait pas courcircuiter l'appel a LogEndOfMethod 
            //dans le cas ou on arrive pas à logger le debut
            catch (NMonitoringException e)
            {
                sLog.Error("NMonitoringException : Unable to log", e);
            }
            catch (Exception e)
            {
                sLog.Error("Unknown Exception : Unable to log", e);
            }

            try
            {
                //Execution du code à auditer
                result = jp.Proceed();

                //Fin normale (sans exception) du code audité
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
                //Impossible de laisser remonter une erreur de NMonitoring vars l'appli !
                catch (NMonitoringException e)
                {
                    sLog.Error("NMonitoringException : Unable to log", e);
                }
                catch (Exception e)
                {
                    sLog.Error("Unknown Exception : Unable to log", e);
                }
            }
            //Une exception s'est produite dans l'application auditée
            catch (Exception externalException)
            {
                try
                {
                    //In log la fin de la methode en marquant l'exception
                    storeManager.LogEndOfMethodWithException(externalException);
                }
                //Impossible de laisser remonter une erreur de NMonitoring vars l'appli !
                catch (NMonitoringException internalException)
                {
                    sLog.Error("NMonitoringException : Unable to log execution Throwable", internalException);
                }
                catch (Exception e)
                {
                    sLog.Error("Unknown Exception : Unable to log", e);
                }

                //On propage l'exception issue de l'application auditée
                throw;
            }


            return result;
        }
    }
}
