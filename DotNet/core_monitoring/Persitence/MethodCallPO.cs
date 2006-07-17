using System;
using System.Collections;
using System.Text;
using log4net;

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
        private int mId = -1;
        public int Id
        {
            get { return mId; }
            set { mId = value; }
        }
        /** Lien sur le père de ce point dans la hierachie d'appel. */
        private MethodCallPO mParent;
        public MethodCallPO Parent
        {
            get { return mParent; }
            set { setParentMethodCall(value); }
        }
    

         private IList mChildren = new ArrayList();
         public IList Children
         {
             get { return mChildren;}
             set { mChildren = value;}
         }
     

         /** Représentation sous forme de <code>String</code> des paramètres passés lors de l'appel à la méthode. */
        private String mParams;
        public String Params
        {
            get { return mParams; }
            set { mParams = value; }
        }
        /** Date/Heure de début d'appel de la méthode. */
        private long mBeginTime;
        public long BeginTime
        {
            get { return mBeginTime; }
            set { mBeginTime = value; }
        }
        /** Date/Heure de fin d'appel de la méthode. */
        private long mEndTime;
        public long EndTime
        {
            get { return mEndTime; }
            set { mEndTime = value; }
        }
        /** Nom de la classe sur laquelle est fait l'appel de la méthode. */
        private String mClassName;
        public String ClassName
        {
            get { return mClassName; }
            set { mClassName = value; }
        }
        /** Nom de la méthode associée à ce point de mesure. */
        private String mMethodName;
        public String MethodName
        {
            get { return mMethodName; }
            set { mMethodName = value; }
        }
        /** Exception qui est stockée si l'exécution associée à ce point est levée durant son exécution. */
        private String mThrowableClass = null;
        public String ThrowableClass
        {
            get { return mThrowableClass; }
            set { mThrowableClass = value; }
        }
        private String mThrowableMessage = null;
        public String ThrowableMessage
        {
            get { return mThrowableMessage; }
            set { mThrowableMessage = value; }
        }
        /** Valeur de retour si la méthode associée à ce point est autre que 'void' . */
        private String mReturnValue = null;
        public String ReturnValue
        {
            get { return mReturnValue; }
            set { mReturnValue = value; }
        }
        /** Nom du group associé au point de mesure. */
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
        public MethodCallPO(MethodCallPO pParent, String pClassName, String pMethodName, String pGroupName, Object[] pParams)
        {
            if (pParent != null)
            { // On chaine la hierachie
                pParent.addChildren(this);
            }
            mClassName = pClassName;
            mMethodName = pMethodName;
            mBeginTime = Org.NMonitoring.Core.Common.Util.CurrentTimeMillis();
            mParams = getParamsAsString(pParams, pClassName, pMethodName);
            mGroupName = pGroupName;
        }

        private String getParamsAsString(Object [] pParams, String pClassName, String pMethodName)
        {

            /*
             *                 StringBuilder sb = new StringBuilder();
                for (int iParam = 0; iParam < currentMethodCall.Params.Length; iParam++)
                {
                    sb.Append(currentMethodCall.Params.Get
                }
             * */
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
            catch (Exception tT)
            {
                sLog.Error("Unable to getArguments of class=[" + pClassName + "] and method=[" + pMethodName + "]", tT);
            }
            return buffer.ToString();

        }

        public void addChildren(MethodCallPO pChild)
        {
            mChildren.Add(pChild);
            pChild.mParent = this;
        }

        public void removeChildren(MethodCallPO pChild)
        {
            mChildren.Remove(pChild);
            pChild.mParent = null;

        }


        /**
         * @param pParent The mParent to set.
         */
        private void setParentMethodCall(MethodCallPO pParent)
        {
            if (pParent == null)
            {
                if (this.mParent != null)
                {
                    this.mParent.mChildren.Remove(this);
                }
            }
            else
            {
                pParent.mChildren.Add(this);
            }
            mParent = pParent;
        }

        public void setFlowRecusivly(ExecutionFlowPO pFlowPO)
        {
            mFlow = pFlowPO;
            MethodCallPO curMeth;
            foreach (Object it in mChildren)            
            {
                curMeth = (MethodCallPO)it;
                curMeth.setFlowRecusivly(pFlowPO);
            }

        }
    }

}



