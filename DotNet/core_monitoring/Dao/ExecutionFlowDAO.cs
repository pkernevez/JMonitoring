using System;
using System.Data;
using System.Text;

using Org.NMonitoring.Core.Persistence;
using Org.NMonitoring.Core.Common;
using Org.NMonitoring.Core.Store;

namespace Org.NMonitoring.Core.Dao
{
    public class ExecutionFlowDao : IExecutionFlowWriter
    {

        private IDaoHelper _dao;

        public ExecutionFlowDao()
        {
            //TODO : Injecter la dépendance
            _dao = SqlDaoHelper.Instance;
        }

        public void InsertFullExecutionFlow(ExecutionFlowPO executionFlow)
        {
            _dao.Connection.Open();
            IDbTransaction trans = _dao.BeginTransaction();
            try
            {
                saveExecutionFlow(executionFlow);

                MethodCallPO tFirstMeth = executionFlow.FirstMethodCall;
                int totalIndex = saveRecursMethodCall(tFirstMeth, 0, 0);

                //relier le executionflaw avec le 1st method call
                SetFirstMethodCall(executionFlow, tFirstMeth);
                trans.Commit();
            }
            catch (Exception externalException)
            {
                trans.Rollback();
                throw new NMonitoringException("Unable to write Execution Flow",externalException);
            }
            finally
            {
                _dao.Connection.Close();
            }



        }




        private int saveRecursMethodCall(MethodCallPO currentMethodCall, int currentIndex, int totalIndex)
        {
            int newTotalIndex = totalIndex;
            if (currentMethodCall != null)
            {
                newTotalIndex++;
                saveMethodCall(currentMethodCall, currentIndex, newTotalIndex);
                
                int currentLocalIndex = 0;
                foreach (MethodCallPO childMethodCall in currentMethodCall.Children)
                {
                    newTotalIndex = saveRecursMethodCall(childMethodCall, currentLocalIndex, newTotalIndex);
                    currentLocalIndex++;
                }
            }
            return newTotalIndex;
        }

        private void saveMethodCall(MethodCallPO currentMethodCall, int currentIndex, int totalIndex)
        {
            //insertion bd
            //idparent(bd) = currentMethodCall.Parrent.Id;  (null si premier)
            //select @@id pour recuperer identifiant et updater l'objet + setId


            String sCommandText = @"INSERT INTO METHOD_CALL ([INDEX_IN_FLOW], [PARAMETERS], [BEGIN_TIME], [END_TIME], [FULL_CLASS_NAME], [METHOD_NAME], [THROWABLE_CLASS_NAME], [THROWABLE_MESSAGE], [RESULT], [GROUP_NAME], [PARENT_INDEX_IN_FLOW], [FLOW_ID], [SUB_METH_INDEX]) VALUES (@INDEX_IN_FLOW, @PARAMETERS, @BEGIN_TIME, @END_TIME, @FULL_CLASS_NAME, @METHOD_NAME, @THROWABLE_CLASS_NAME, @THROWABLE_MESSAGE, @RESULT, @GROUP_NAME, @PARENT_INDEX_IN_FLOW, @FLOW_ID, @SUB_METH_INDEX)";
            // ;SELECT  * FROM METHOD_CALL WHERE (INDEX_IN_FLOW = SCOPE_IDENTITY())";

            IDbCommand cmd = _dao.CreateCommand(sCommandText, CommandType.Text);
            cmd.Transaction = _dao.CurrentTransaction;

            cmd.Parameters.Add(_dao.CreateParameter("@INDEX_IN_FLOW", totalIndex));
            currentMethodCall.Id = totalIndex;
            cmd.Parameters.Add(_dao.CreateParameter("@PARAMETERS", currentMethodCall.Params.ToString()));
            cmd.Parameters.Add(_dao.CreateParameter("@BEGIN_TIME", currentMethodCall.BeginTime));
            cmd.Parameters.Add(_dao.CreateParameter("@END_TIME", currentMethodCall.EndTime));
            cmd.Parameters.Add(_dao.CreateParameter("@FULL_CLASS_NAME", currentMethodCall.ClassName));
            cmd.Parameters.Add(_dao.CreateParameter("@METHOD_NAME", currentMethodCall.MethodName));
            cmd.Parameters.Add(_dao.CreateParameter("@THROWABLE_CLASS_NAME", currentMethodCall.ThrowableClass));
            cmd.Parameters.Add(_dao.CreateParameter("@THROWABLE_MESSAGE", currentMethodCall.ThrowableMessage));
            cmd.Parameters.Add(_dao.CreateParameter("@RESULT", currentMethodCall.ReturnValue));
            cmd.Parameters.Add(_dao.CreateParameter("@GROUP_NAME", currentMethodCall.GroupName));
            //TODO FCH : Faire le test qui casse si on passe un mauvais ID
            if (currentMethodCall.Parent != null)
                cmd.Parameters.Add(_dao.CreateParameter("@PARENT_INDEX_IN_FLOW", currentMethodCall.Parent.Id));
            else
                cmd.Parameters.Add(_dao.CreateParameter("@PARENT_INDEX_IN_FLOW", null));
            //TODO FCH : Faire le test qui casse si on passe un mauvais ID
            cmd.Parameters.Add(_dao.CreateParameter("@FLOW_ID", currentMethodCall.Flow.Id));
            cmd.Parameters.Add(_dao.CreateParameter("@SUB_METH_INDEX", currentIndex));
            
            cmd.ExecuteNonQuery();

        }

        private void saveExecutionFlow(ExecutionFlowPO executionFlow)
        {
            if (executionFlow != null)
            {
                //insertion bd (sans 1st methodCall (existe pas)
                //select @@id pour recuperer identifiant et updater l'objet

                String sCommandText = @"INSERT INTO EXECUTION_FLOW ([THREAD_NAME], [JVM], [BEGIN_TIME], [END_TIME], [BEGIN_TIME_AS_DATE], [DURATION]) VALUES (@THREAD_NAME, @JVM, @BEGIN_TIME, @END_TIME, @BEGIN_TIME_AS_DATE, @DURATION);
SELECT SCOPE_IDENTITY()";
//SELECT ID, THREAD_NAME, JVM, BEGIN_TIME, END_TIME, BEGIN_TIME_AS_DATE, DURATION, FIRST_METHOD_CALL_INDEX_IN_FLOW FROM EXECUTION_FLOW WHERE (ID = SCOPE_IDENTITY())";

                IDbCommand cmd = _dao.CreateCommand(sCommandText, CommandType.Text);
                cmd.Transaction = _dao.CurrentTransaction;

                cmd.Parameters.Add(_dao.CreateParameter("@THREAD_NAME", executionFlow.ThreadName));
                cmd.Parameters.Add(_dao.CreateParameter("@JVM", executionFlow.ServerIdentifier));
                cmd.Parameters.Add(_dao.CreateParameter("@BEGIN_TIME", executionFlow.BeginTime));
                cmd.Parameters.Add(_dao.CreateParameter("@END_TIME", executionFlow.EndTime));
                cmd.Parameters.Add(_dao.CreateParameter("@BEGIN_TIME_AS_DATE", Org.NMonitoring.Core.Common.Util.TimeMillisToDate(executionFlow.BeginTime)));
                cmd.Parameters.Add(_dao.CreateParameter("@DURATION", executionFlow.Duration));
                //cmd.Parameters.Add(_dao.CreateParameter("@FIRST_METHOD_CALL_ID", null));

                //TODO FCH : Faire le test qui casse si on enleve cette ligne
                object tobject = cmd.ExecuteScalar();
                executionFlow.Id = decimal.ToInt32((decimal)tobject);
            }
        }

        private void SetFirstMethodCall(ExecutionFlowPO executionFlow, MethodCallPO firstMethodCall)
        {
            if ((executionFlow != null) && (firstMethodCall != null))
            {
                //updater le lien de pExecution Flow vers son fils (il est nul jusque la)

                String sCommandText = @"UPDATE EXECUTION_FLOW SET  [FIRST_METHOD_CALL_INDEX_IN_FLOW] = @FIRST_METHOD_CALL_INDEX_IN_FLOW WHERE (([ID] = @FLOW_ID))";
                IDbCommand cmd = _dao.CreateCommand(sCommandText, CommandType.Text);
                cmd.Transaction = _dao.CurrentTransaction;

                cmd.Parameters.Add(_dao.CreateParameter("@FLOW_ID", executionFlow.Id));
                //TODO FCH : Faire le test qui casse si on passe un mauvais ID
                cmd.Parameters.Add(_dao.CreateParameter("@FIRST_METHOD_CALL_INDEX_IN_FLOW", firstMethodCall.Id));

                cmd.ExecuteNonQuery();
                if (cmd.ExecuteNonQuery() != 1)
                    throw new NMonitoringException("The count of updated rows is not equal to 1");

            }
        }

    }
}


