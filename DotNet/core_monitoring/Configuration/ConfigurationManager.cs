using System;
using Org.NMonitoring.Core.Common;

namespace Org.NMonitoring.Core.Configuration
{
    public class ConfigurationManager
    {
        private static ConfigurationManager _Instance;

        #region ConnexionString
        private string _ConnexionString;
        public string ConnexionString
        {
            get { return _ConnexionString; }
            set { _ConnexionString = value; }
        }
        #endregion ConnexionString

        private const string _CONNECTION_STRING_NAME = "jmonitoringConnectionString";


        private ConfigurationManager()
        {
            System.Configuration.Configuration config =
               System.Configuration.ConfigurationManager.OpenExeConfiguration(
               System.Configuration.ConfigurationUserLevel.None);
            try
            {
                ConnexionString = config.ConnectionStrings.ConnectionStrings[_CONNECTION_STRING_NAME].ConnectionString;
            }
            catch
            {
                String message = "Connection String \"";
                message += _CONNECTION_STRING_NAME;
                message += "\" could not be found in file \"";
                message += config.FilePath + "\"";
                throw new NMonitoringException(message);     
            }
        }


        /**
         * Factory's method for the <code>Configuration</code>. This method is not synchrnoized because there isn't any
         * trouble if we load the configuration twice.
         * 
         * @return The Singleton.
         */
        public static ConfigurationManager Instance
        {
            get
            {

                if (_Instance == null)
                {
                    _Instance = new ConfigurationManager();
                }
                return _Instance;
            }
        }

        static internal string getServerName()
        {
            //TODO FCH : Lire dans le fichier de configuration
            return ("DOTNET CLR");
        }
    }
}