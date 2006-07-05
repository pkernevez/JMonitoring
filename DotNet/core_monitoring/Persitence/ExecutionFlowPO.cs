using System;
using System.Collections;
using System.Text;


namespace org.nmonitoring.core.persistence
{
    public class ExecutionFlowPO
    {
        /** Thread name. */
        private String mThreadName;
        public String ThreadName
        {
            get { return mThreadName; }
            set { mThreadName = value; }
        }

        /** Name of the 'Server' or server. */
        private String mServerIdentifier;
        public String ServerIdentifier
        {
            get { return mServerIdentifier; }
            set { mServerIdentifier = value; }
        }

        /** Begin datetime. */
        private long mBeginTime;
        public long BeginTime
        {
            get { return mBeginTime; }
            set { mBeginTime = value; }
        }

        /** End datetime. */
        private long mEndTime;
        public long EndTime
        {
            get { return mEndTime; }
            set { mEndTime = value; }
        }

        /** Technical identifier. */
        private int mId = -1;
        public int Id
        {
            get { return mId; }
            set { mId = value; }
        }
        /** First method call of this flow. */
        private MethodCallPO mFirstMethodCall;
        public MethodCallPO FirstMethodCall
        {
            get { return mFirstMethodCall; }
            set { mFirstMethodCall = value; }
        }
        
        //The duration of the first measure execution in milliseconds.
        public long Duration
        {
            get { return EndTime - BeginTime; }
        }
        
      
        /// <summary>
        /// <param name="pThreadName">The name of the Thread of this flow</param>
        /// <param name="pFirstMeasure">First <code>MeasurePoint</code> of this flow.</param>
        /// <param name="pServerIdentifier">The identifier of this Server.</param>
        /// </summary>
        public ExecutionFlowPO(String pThreadName, MethodCallPO pFirstMeasure, String pServerIdentifier)
        {
            mThreadName = pThreadName;
            mFirstMethodCall = pFirstMeasure;
            mServerIdentifier = pServerIdentifier;
            mBeginTime = mFirstMethodCall.getBeginTime();
            mEndTime = mFirstMethodCall.getEndTime();
            mFirstMethodCall.setFlowRecusivly(this);
        }


        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        override public String ToString()
        {
            StringBuilder buffer = new StringBuilder();
            buffer.Append("ExecutionFlowPO FlowId=[").Append(mId).Append("] ");
            return base.ToString();
        }

    }
}
