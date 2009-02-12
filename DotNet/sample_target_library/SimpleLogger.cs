using System;
using System.Threading;

namespace Org.NMonitoring.SampleTargetLibrary
{
    public class SimpleLogger
    {
        private static SimpleLogger mInstance;
        public static SimpleLogger Instance
        {
            get
            {
                if (mInstance == null)
                    mInstance = new SimpleLogger();
                return mInstance;
            }
        }

        SimpleLogger()
        {
        }

        public void log(String message)
        {
            String logMessage = "Thread ";
            logMessage += Thread.CurrentThread.ManagedThreadId.ToString();
            if (Thread.CurrentThread.Name != null)
                logMessage += " (" + Thread.CurrentThread.Name + ")";
            logMessage +=" : ";
            logMessage += message;
            Console.WriteLine(logMessage);
        }
    }
}
