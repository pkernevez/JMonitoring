using System;
using System.Collections;
using System.Text;
using log4net;

using Org.NMonitoring.Core.Common;

namespace Org.NMonitoring.Core.Persistence
{
    public class MethodCallPO
    {

        private static ILog sLog = LogManager.GetLogger("MethodCallPO");

        /** Flow Technical Id. */
        private ExecutionFlowPO mFlow;
        public ExecutionFlowPO Flow
        {
            get { return mFlow; }
            set { mFlow = value; }
        }


        /** Technical Id. */
        private int id = -1;
        public int Id
        {
            get { return id; }
            set { id = value; }
        }
        /** Lien sur le p�re de ce point dans la hierachie d'appel. */
        private MethodCallPO parent;
        public MethodCallPO Parent
        {
            get { return parent; }
            set { SetParentMethodCall(value); }
        }
    

         private IList mChildren = new ArrayList();
         public IList Children
         {
             get { return mChildren;}
         }
     

         /** Repr�sentation sous forme de <code>String</code> des param�tres pass�s lors de l'appel � la m�thode. */
        private String mParams;
        public String Params
        {
            get { return mParams; }
            set { mParams = value; }
        }
        /** Date/Heure de d�but d'appel de la m�thode. */
        private long mBeginTime;
        public long BeginTime
        {
            get { return mBeginTime; }
            set { mBeginTime = value; }
        }
        /** Date/Heure de fin d'appel de la m�thode. */
        private long mEndTime;
        public long EndTime
        {
            get { return mEndTime; }
            set { mEndTime = value; }
        }
        /** Nom de la classe sur laquelle est fait l'appel de la m�thode. */
        private String mClassName;
        public String ClassName
        {
            get { return mClassName; }
            set { mClassName = value; }
        }
        /** Nom de la m�thode associ�e � ce point de mesure. */
        private String mMethodName;
        public String MethodName
        {
            get { return mMethodName; }
            set { mMethodName = value; }
        }
        /** Exception qui est stock�e si l'ex�cution associ�e � ce point est lev�e durant son ex�cution. */
        private String mThrowableClass;
        public String ThrowableClass
        {
            get { return mThrowableClass; }
            set { mThrowableClass = value; }
        }
        private String mThrowableMessage;
        public String ThrowableMessage
        {
            get { return mThrowableMessage; }
            set { mThrowableMessage = value; }
        }
        /** Valeur de retour si la m�thode associ�e � ce point est autre que 'void' . */
        private String mReturnValue;
        public String ReturnValue
        {
            get { return mReturnValue; }
            set { mReturnValue = value; }
        }
        /** Nom du group associ� au point de mesure. */
        private String mGroupName;
        public String GroupName
        {
            get { return mGroupName; }
            set { mGroupName = value; }
        }


        /**
         * 
         * @param pParent The <code>MethodCallPO</code> from which we made the call to the current
         *        <code>MethodCallPO</code>.
         * @param pClassName The name of the <code>Class</code> on which we call the statement associated with this
         *        <code>MethodCallDTO</code>.
         * @param pMethodName The method name of the statement associated with this <code>MethodCallDTO</code>.
         * @param pGroupName The name of the group associated to this <code>MethodCallDTO</code>.
         * @param pParams The parameters passed to the method <code>pMethodName</code>.
         */
        public MethodCallPO(MethodCallPO parent, String className, String methodName, String groupName, Object[] parameters)
        {
            if (parent != null)
            { // On chaine la hierachie
                parent.AddChildren(this);
            }
            mClassName = className;
            mMethodName = methodName;
            mBeginTime = Org.NMonitoring.Core.Common.Util.CurrentTimeMillis();
            mParams = getParamsAsString(parameters, className, methodName);
            mGroupName = groupName;
        }

        private static String getParamsAsString(Object [] pParams, String pClassName, String pMethodName)
        {

            StringBuilder buffer = new StringBuilder();
            try
            {
                if (pParams != null)
                {
                    bool tFistTime = true;
                    buffer.Append("[");
                    for (int i = 0; i < pParams.Length; i++)
                    {
                        if (!tFistTime)
                        {
                            buffer.Append(", ");
                        }
                        buffer.Append("" + pParams.GetValue(i));
                        tFistTime = false;
                    }
                    buffer.Append("]");
                }
            }
            catch (Exception externalException)
            {
                String message = "Unable to get arguments of class=[" + pClassName + "] and method=[" + pMethodName + "]";
                sLog.Error(message, externalException);
                throw new NMonitoringException(message,externalException);
            }
            return buffer.ToString();

        }

        public void AddChildren(MethodCallPO child)
        {
            if (child != null)
            {
                mChildren.Add(child);
                child.parent = this;
            }
        }

        public void RemoveChildren(MethodCallPO child)
        {
            if (child != null)
            {
                mChildren.Remove(child);
                child.parent = null;
            }
        }


        /**
         * @param pParent The mParent to set.
         */
        private void SetParentMethodCall(MethodCallPO parent)
        {
            if (parent == null)
            {
                if (this.parent != null)
                {
                    this.parent.mChildren.Remove(this);
                }
            }
            else
            {
                parent.mChildren.Add(this);
            }
            this.parent = parent;
        }

        public void SetFlowRecusivly(ExecutionFlowPO flowPO)
        {
            mFlow = flowPO;
            MethodCallPO curMeth;
            foreach (Object it in mChildren)            
            {
                curMeth = (MethodCallPO)it;
                curMeth.SetFlowRecusivly(flowPO);
            }

        }
    }

}



