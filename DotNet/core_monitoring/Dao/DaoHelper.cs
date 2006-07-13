using System;
using System.Data;
using System.Data.Common;
using System.Data.SqlClient;

namespace Org.NMonitoring.Core.Dao
{
    public class DaoHelper
    {
        private static bool _initialized = false;
        /*
        public String ConnectionString
        {
            get 
            { 
                return Instance.Connection.ConnectionString; 
            }
            set
            {
                Instance.Connection.ConnectionString = value;
            }
        }
        */
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

        private DbProviderFactory _dbFactory;
        public DbProviderFactory DbFactory
        {
            get 
            {
                return _dbFactory; 
            }
            set { _dbFactory = value; }
        }

        private IDbConnection _connection = null;
        public IDbConnection Connection
        {
            get { return _connection; }
        }

        public static void Initialize(DbProviderFactory dbFactory, 
                                      string connectionString)
        {
            Instance.DbFactory = dbFactory;
            Instance._connection = dbFactory.CreateConnection();
            Instance._connection.ConnectionString = connectionString;
            _initialized = true;
        }

        public DbParameter CreateParameter(String name, Object value)
        {
            if (!_initialized)
                throw new Exception("Please call Initialize() before");
            DbParameter parameter = DbFactory.CreateParameter();
            parameter.ParameterName = name;
            if (value == null)
            {
                parameter.Value = DBNull.Value;
            }
            else
                parameter.Value = value;
            return parameter;
        }

        public DbParameter CreateParameter(string parameterName, DbType dbType,
                                            int size, ParameterDirection direction,
                                            string sourceColumn, DataRowVersion sourceVersion,
                                            bool sourceColumnNullMapping, object value)
        {
            DbParameter parameter = this.CreateParameter(parameterName, value);
            parameter.DbType = dbType;
            parameter.Direction = direction;
            parameter.SourceColumn = sourceColumn;
            parameter.SourceVersion = sourceVersion;
            parameter.SourceColumnNullMapping = sourceColumnNullMapping;
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
            if (!_initialized)
                throw new Exception("Please call Initialize() before");
            IDbCommand cmd = DbFactory.CreateCommand();
            cmd.Connection = Instance.Connection;
            return cmd;
        }

        private DaoHelper()
        {
        }
    }
}
