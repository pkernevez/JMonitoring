using System;
using System.Threading;

namespace Org.NMonitoring.SampleTarget
{
    class MultiThreadSampleTarget
    {
        private const int SLEEP_TEST_TIME = 20000;

        static void Main(string[] args)
        {
            ThreadPool.SetMinThreads(10, 10);
            ThreadPool.SetMaxThreads(10, 10);
            System.Console.WriteLine("Start running Threads");
            for (int i = 0; i < 20; i++)
                ThreadPool.QueueUserWorkItem(new WaitCallback(SampleTarget.MyMain));
            System.Console.WriteLine("Stop running Threads");
            Console.WriteLine("Wait for log to be dumped (is asynchronous)");
            try
            {
                //Empirique
                Thread.Sleep(SLEEP_TEST_TIME);
            }
            catch (ThreadInterruptedException e)
            {
                Console.WriteLine(e.StackTrace.ToString());
            }
            System.Console.WriteLine("End Main");
        }
    }
}
