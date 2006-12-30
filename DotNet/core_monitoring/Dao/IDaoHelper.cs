using System;
using System.Data;
using System.Data.Common;

namespace Org.NMonitoring.Core.Dao
{
	public interface IDaoHelper : IDisposable
	{
		IDbConnection Connection 
		{
			get;
		}
		IDbTransaction CurrentTransaction 
		{
			get;
		}

		IDbDataParameter CreateParameter(String name, Object value);
		IDbDataParameter CreateParameter(string parameterName, DbType dbType,
												int size, ParameterDirection direction,
												string sourceColumn, DataRowVersion sourceVersion,
												object value);
		IDbCommand CreateCommand(String commandText, CommandType commandType);
		IDbCommand CreateCommand();
		IDbTransaction BeginTransaction();
    }
}
