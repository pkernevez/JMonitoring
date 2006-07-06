using System;
using System.Collections.Generic;
using System.Text;

using  Org.NMonitoring.Core.Persistence;

namespace Org.NMonitoring.Core.Dao
{
    public class ExecutionFlowDAO
    {
        public void insertFullExecutionFlow(ExecutionFlowPO pExecutionFlow)
        {
            saveExecutionFlow(pExecutionFlow);

            MethodCallPO tFirstMeth = pExecutionFlow.FirstMethodCall;
            saveRecursMethodCall(tFirstMeth, 0);

            //relier le executionflaw avec le 1st method call
            setFirstMethodCall(pExecutionFlow,tFirstMeth);
        }

        //Test avec buildNewFullFlow()


        private void saveRecursMethodCall(/*long idParent,*/  MethodCallPO currentMethodCall, int currentIndex)
        {
            saveMethodCall(currentMethodCall,currentIndex);

            int currentLocalIndex=0;
            foreach(MethodCallPO childMethodCall in currentMethodCall.Children)
            {
                saveRecursMethodCall(childMethodCall, currentLocalIndex);
                currentLocalIndex++;
            }
            
        }

        private void saveMethodCall(MethodCallPO currentMethodCall, int currentIndex)
        {
            //insertion bd
            //idparent(bd) = currentMethodCall.Parrent.Id;  (null si premier)
            //select @@id pour recuperer identifiant et updater l'objet + setId
        }

        private void saveExecutionFlow(ExecutionFlowPO pExecutionFlow)
        {
            //insertion bd (sans 1st methodCall (existe pas)
            //select @@id pour recuperer identifiant et updater l'objet
        }

        private void setFirstMethodCall (ExecutionFlowPO pExecutionFlow, MethodCallPO currentMethodCall)
        {
            //updater le lien de pExecution Flow vers son fils (il est nul jusque la)
        }


    }
}
