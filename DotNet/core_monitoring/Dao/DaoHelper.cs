using System;
using System.Data;
using System.Data.Common;
using System.Data.SqlClient;
using Org.NMonitoring.Core.Common;

namespace Org.NMonitoring.Core.Dao
{
    public class DaoHelper : IDaoHelper

    {
        private static bool DefaultClassParametersInitialized = false;
        private static string connectionString;
        private static DbProviderFactory dbFactory;

        [ThreadStatic]
        private static DaoHelper _instance;
        public static DaoHelper Instance
        {
            get
            {
                if (_instance == null)
                    _instance = new DaoHelper();
                return _instance;
            }
        }

        private IDbConnection _connection = null;
        public IDbConnection Connection
        {
            get { return _connection; }
        }

        IDbTransaction _transaction = null;
        public IDbTransaction CurrentTransaction
        {
            get { return _transaction; }
        }
		
        /// <summary>
        /// Initialize class parameters
        /// </summary>
        /// <param name="dbFactoryToUse">The DBFactory to use (SQLServer or other)</param>
        /// <param name="connectionStringToUse">The Connection String (depending of which DBFactory is used)</param>
        public static void Initialize(DbProviderFactory dbFactoryToUse, 
                               string connectionstringToUse)
        {
            dbFactory         = dbFactoryToUse;
            connectionString = connectionstringToUse;
            DefaultClassParametersInitialized = true;
        }
        
       
        public IDbDataParameter CreateParameter(String name, Object value)
        {
            if (!DefaultClassParametersInitialized)
                throw new NMonitoringException("Please call Initialize() before");
            IDbDataParameter parameter = dbFactory.CreateParameter();
            parameter.ParameterName = name;
            if (value == null)
            {
                parameter.Value = DBNull.Value;
            }
            else
                parameter.Value = value;
            return parameter;
        }


        public IDbDataParameter CreateParameter(string parameterName, DbType dbType,
                                            int size, ParameterDirection direction,
                                            string sourceColumn, DataRowVersion sourceVersion,
                                            object value)
        {
            IDbDataParameter parameter = this.CreateParameter(parameterName, value);
            parameter.DbType = dbType;
            parameter.Direction = direction;
            parameter.SourceColumn = sourceColumn;
            parameter.SourceVersion = sourceVersion;
            parameter.Size = size;

            return parameter;
        }

        public IDbCommand CreateCommand(String commandText, 
                                        CommandType commandType)
        {
            IDbCommand cmd = CreateCommand();
            cmd.CommandText = commandText;
            cmd.CommandType = commandType;
            return cmd;
        }
        public IDbCommand CreateCommand()
        {
            IDbCommand cmd = dbFactory.CreateCommand();
            cmd.Connection = this.Connection;
            return cmd;
        }

        public IDbTransaction BeginTransaction()
        {
            _transaction = this.Connection.BeginTransaction();
            return _transaction;
        }

        private DaoHelper()
        {
            if (!DefaultClassParametersInitialized)
            {
                //Use default paramaters if not Explici
                throw new NMonitoringException("Please call Initialize() before");
            }
            this._connection = dbFactory.CreateConnection();
            this._connection.ConnectionString = connectionString;
        }
    }
}
