using System;
using System.Data;
using System.Text;

using Org.NMonitoring.Core.Persistence;

namespace Org.NMonitoring.Core.Dao
{
    public class ExecutionFlowDAO
    {

        private IDaoHelper _dao;

        public ExecutionFlowDAO()
        {
            //TODO : Injecter la dépendance
            _dao = DaoHelper.Instance;
        }

        public void insertFullExecutionFlow(ExecutionFlowPO executionFlow)
        {
            _dao.Connection.Open();
            IDbTransaction trans = _dao.BeginTransaction();
            try
            {
                saveExecutionFlow(executionFlow);

                MethodCallPO tFirstMeth = executionFlow.FirstMethodCall;
                saveRecursMethodCall(tFirstMeth, 0);

                //relier le executionflaw avec le 1st method call
                setFirstMethodCall(executionFlow, tFirstMeth);
                trans.Commit();
            }
            catch (Exception e)
            {
                trans.Rollback();
            }
            finally
            {
                _dao.Connection.Close();
            }



        }




        private void saveRecursMethodCall(MethodCallPO currentMethodCall, int currentIndex)
        {
            if (currentMethodCall != null)
            {
                saveMethodCall(currentMethodCall, currentIndex);

                int currentLocalIndex = 0;
                foreach (MethodCallPO childMethodCall in currentMethodCall.Children)
                {
                    saveRecursMethodCall(childMethodCall, currentLocalIndex);
                    currentLocalIndex++;
                }
            }
        }

        private void saveMethodCall(MethodCallPO currentMethodCall, int currentIndex)
        {
            //insertion bd
            //idparent(bd) = currentMethodCall.Parrent.Id;  (null si premier)
            //select @@id pour recuperer identifiant et updater l'objet + setId


            String sCommandText = @"INSERT INTO METHOD_CALL ([PARAMETERS], [BEGIN_TIME], [END_TIME], [FULL_CLASS_NAME], [METHOD_NAME], [THROWABLE_CLASS_NAME], [THROWABLE_MESSAGE], [RESULT], [GROUP_NAME], [PARENT_ID], [FLOW_ID], [SUB_METH_INDEX]) VALUES (@PARAMETERS, @BEGIN_TIME, @END_TIME, @FULL_CLASS_NAME, @METHOD_NAME, @THROWABLE_CLASS_NAME, @THROWABLE_MESSAGE, @RESULT, @GROUP_NAME, @PARENT_ID, @FLOW_ID, @SUB_METH_INDEX); SELECT ID, PARAMETERS, BEGIN_TIME, END_TIME, FULL_CLASS_NAME, METHOD_NAME, THROWABLE_CLASS_NAME, THROWABLE_MESSAGE, RESULT, GROUP_NAME, PARENT_ID, FLOW_ID, SUB_METH_INDEX FROM METHOD_CALL WHERE (ID = SCOPE_IDENTITY())";

            IDbCommand cmd = _dao.CreateCommand(sCommandText, CommandType.Text);
            cmd.Transaction = _dao.CurrentTransaction;

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
                cmd.Parameters.Add(_dao.CreateParameter("@PARENT_ID", currentMethodCall.Parent.Id));
            else
                cmd.Parameters.Add(_dao.CreateParameter("@PARENT_ID", null));
            //TODO FCH : Faire le test qui casse si on passe un mauvais ID
            cmd.Parameters.Add(_dao.CreateParameter("@FLOW_ID", currentMethodCall.Flow.Id));
            cmd.Parameters.Add(_dao.CreateParameter("@SUB_METH_INDEX", currentIndex));


            //TODO FCH : Faire le test qui casse si on enleve cette ligne
            currentMethodCall.Id = (int)cmd.ExecuteScalar();

        }

        private void saveExecutionFlow(ExecutionFlowPO executionFlow)
        {
            if (executionFlow != null)
            {
                //insertion bd (sans 1st methodCall (existe pas)
                //select @@id pour recuperer identifiant et updater l'objet

                String sCommandText = @"INSERT INTO EXECUTION_FLOW ([THREAD_NAME], [JVM], [BEGIN_TIME], [END_TIME], [BEGIN_TIME_AS_DATE], [DURATION]) VALUES (@THREAD_NAME, @JVM, @BEGIN_TIME, @END_TIME, @BEGIN_TIME_AS_DATE, @DURATION);
SELECT ID, THREAD_NAME, JVM, BEGIN_TIME, END_TIME, BEGIN_TIME_AS_DATE, DURATION, FIRST_METHOD_CALL_ID FROM EXECUTION_FLOW WHERE (ID = SCOPE_IDENTITY())";

                IDbCommand cmd = _dao.CreateCommand(sCommandText, CommandType.Text);
                cmd.Transaction = _dao.CurrentTransaction;

                cmd.Parameters.Add(_dao.CreateParameter("@THREAD_NAME", executionFlow.ThreadName));
                cmd.Parameters.Add(_dao.CreateParameter("@JVM", executionFlow.ServerIdentifier));
                cmd.Parameters.Add(_dao.CreateParameter("@BEGIN_TIME", executionFlow.BeginTime));
                cmd.Parameters.Add(_dao.CreateParameter("@END_TIME", executionFlow.EndTime));
                //TODO FCH  : Remplacer DataTime.Now() par la valeur
                cmd.Parameters.Add(_dao.CreateParameter("@BEGIN_TIME_AS_DATE", Org.NMonitoring.Core.Common.Util.TimeMillisToDate(executionFlow.BeginTime)));
                cmd.Parameters.Add(_dao.CreateParameter("@DURATION", executionFlow.Duration));
                //cmd.Parameters.Add(_dao.CreateParameter("@FIRST_METHOD_CALL_ID", null));

                //TODO FCH : Faire le test qui casse si on enleve cette ligne
                executionFlow.Id = (int)cmd.ExecuteScalar();
                System.Console.WriteLine("ExecutionFlowDAO:saveExecutionFlow");

            }
        }

        private void setFirstMethodCall(ExecutionFlowPO executionFlow, MethodCallPO firstMethodCall)
        {
            if ((executionFlow != null) && (firstMethodCall != null))
            {
                //updater le lien de pExecution Flow vers son fils (il est nul jusque la)

                String sCommandText = @"UPDATE EXECUTION_FLOW SET  [FIRST_METHOD_CALL_ID] = @FIRST_METHOD_CALL_ID WHERE (([ID] = @FLOW_ID))";
                IDbCommand cmd = _dao.CreateCommand(sCommandText, CommandType.Text);
                cmd.Transaction = _dao.CurrentTransaction;

                cmd.Parameters.Add(_dao.CreateParameter("@FLOW_ID", executionFlow.Id));
                //TODO FCH : Faire le test qui casse si on passe un mauvais ID
                cmd.Parameters.Add(_dao.CreateParameter("@FIRST_METHOD_CALL_ID", firstMethodCall.Id));

                cmd.ExecuteNonQuery();
                if (cmd.ExecuteNonQuery() != 1)
                    throw new Exception("The count of updated rows is not equal to 1");

            }
        }

    }
}


