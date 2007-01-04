using System;
using DotNetGuru.AspectDNG.Joinpoints;
using log4net;
using log4net.Config;
using Org.NMonitoring.Core.Configuration;

namespace Org.NMonitoring.Core.Common
{
    public class InternalLogger
    {

        #region Singleton
        /**
         * @return The Singleton.
         */
        private static InternalLogger instance;
        public static InternalLogger Instance
        {
            get
            {
                if (instance == null)
                {
                    instance = new InternalLogger();
                }
                return instance;
            }
        }
        #endregion Singleton


        protected InternalLogger()
        {
            //TODO : Supprimer
            ConfigurationManager confManager = ConfigurationManager.Instance;
            XmlConfigurator.Configure();

            ILog logger = LogManager.GetLogger("NMonitoring");
            logInfoEnable = logger.IsInfoEnabled;
         }

        private bool logInfoEnable;
        public  bool LogInfoEnable
        {
            get { return logInfoEnable; }
        }

        #region Insert Logging Code to each class
        private const string TYPE_TO_LOG = "//Type[not(match('Org.NMonitoring.Core.Aspect.*'))][not(match('Org.NMonitoring.Core.Common.*'))][not(match('Org.NMonitoring.Core.Dao.DaoHelper'))]";

        [Insert(TYPE_TO_LOG)]
        private static readonly ILog wLOG = LogManager.GetLogger("NMonitoring");

        [Insert(TYPE_TO_LOG)]
        private static bool wlogInfoEnable = Instance.LogInfoEnable;

        [AroundBody(TYPE_TO_LOG +  "/Method[not(match('get_*'))][not(match('set_*'))]")]    
        private static object LogIt(OperationJoinPoint jp)
        {
            String name = "";
            if (wlogInfoEnable)
            {       
                name = jp.TargetOperation.DeclaringType.FullName + "::" + jp.TargetOperationName;
                wLOG.Info("Entering " + name);
            }

            object result = jp.Proceed();

            if (wlogInfoEnable)
                wLOG.Info("Leaving " + name);
            
            return result;
        }
        #endregion Insert Logging Code to each class

       
    }
}
