using System;
using System.Threading;

namespace Org.NMonitoring.SampleTarget
{
    class MultiThreadSampleTarget
    {
        static void Main(string[] args)
        {
            ThreadPool.SetMinThreads(5, 5);
            ThreadPool.SetMaxThreads(10, 10);
            System.Console.WriteLine("Start running Threads");
            for (int i = 0; i < 20; i++)
                ThreadPool.QueueUserWorkItem(new WaitCallback(SampleTarget.MyMain));
            System.Console.WriteLine("Stop running Threads");
            Console.WriteLine("Wait for log to be dumped (is asynchronous)");
            Thread.Sleep(60000); //Wait for log to be dumped
            System.Console.WriteLine("End Main");
        }
    }
}
