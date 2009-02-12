using System;
using System.Text;

namespace Org.NMonitoring.Core.Persistence
{
    public class ExecutionFlowPO
    {
        /** Thread name. */
        private String threadName;
        public String ThreadName
        {
            get { return threadName; }
            set { threadName = value; }
        }

        /** Name of the 'Server' or server. */
        private String serverIdentifier;
        public String ServerIdentifier
        {
            get { return serverIdentifier; }
            set { serverIdentifier = value; }
        }

        /** Begin datetime. */
        private long beginTime;
        public long BeginTime
        {
            get { return beginTime; }
            set { beginTime = value; }
        }

        /** End datetime. */
        private long endTime;
        public long EndTime
        {
            get { return endTime; }
            set { endTime = value; }
        }

        /** Technical identifier. */
        private int id = -1;
        public int Id
        {
            get { return id; }
            set { id = value; }
        }
        /** First method call of this flow. */
        private MethodCallPO firstMethodCall;
        public MethodCallPO FirstMethodCall
        {
            get { return firstMethodCall; }
            //set { mFirstMethodCall = value; }
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
        public ExecutionFlowPO(String threadName, MethodCallPO firstMeasure, String serverIdentifier)
        {
            this.threadName = threadName;
            this.serverIdentifier = serverIdentifier;
            this.firstMethodCall = firstMeasure;
            if (this.firstMethodCall != null)
            {
                this.beginTime = firstMethodCall.BeginTime;
                this.endTime = firstMethodCall.EndTime;
                this.firstMethodCall.SetFlowRecusivly(this);
            }
        }


        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        override public String ToString()
        {
            StringBuilder buffer = new StringBuilder();
            buffer.Append("ExecutionFlowPO FlowId=[").Append(id).Append("] ");
            return base.ToString();
        }

    }
}
