using System;
using System.Data;
using System.Data.Common;
using System.Data.SqlClient;

namespace Org.NMonitoring.Core.Dao
{
    public class SqlDaoHelper : IDaoHelper
    {
        private static bool mInitialized = false;
        private static string mConnectionString;

        [ThreadStatic]
        private static SqlDaoHelper _instance;
        public static SqlDaoHelper Instance
        {
            get
            {
                if (_instance == null)
                    _instance = new SqlDaoHelper();
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
		

        public static void Initialize(string connectionString)
        {
            mConnectionString = connectionString;
            mInitialized = true;
        }

        public IDbDataParameter CreateParameter(String name, Object value)
        {
            if (!mInitialized)
                throw new Exception("Please call Initialize() before");
            IDbDataParameter parameter = new SqlParameter();
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
            IDbCommand cmd = new SqlCommand();
            cmd.Connection = this.Connection;
            return cmd;
        }

		public IDbTransaction BeginTransaction()
		{
			 _transaction = this.Connection.BeginTransaction();
			return	_transaction;		
		}

        public SqlDaoHelper()
        {
            if (!mInitialized)
                throw new Exception("Please call Initialize() before");
            this._connection = new SqlConnection();
            this._connection.ConnectionString = mConnectionString;
        }

		public void Dispose ()
		{
			if (this._connection != null)
			{
				this._connection.Close();
				this._connection.Dispose();
			}	
		}
    }
}
