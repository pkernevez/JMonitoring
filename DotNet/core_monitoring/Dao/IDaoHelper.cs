using System;
using System.Data;
using System.Data.Common;

namespace Org.NMonitoring.Core.Dao
{
	/// <summary>
	/// Description résumée de Class1.
	/// </summary>
	public interface IDaoHelper
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
