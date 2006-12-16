using System;

namespace Org.NMonitoring.Core.Common
{
    public sealed class Util
    {
        /// <summary>
        /// Private Constructor to avoid default public one
        /// </summary>
        private Util()
        {
        }

        
        private static DateTime firstOfJanuary1970 = new DateTime(1970, 01, 01, 0, 0, 0, 0);

        static public long CurrentTimeMillis()
        {
            return DateToTimeMillis(DateTime.Now);
        }

        static public long DateToTimeMillis(DateTime date)
        {
            TimeSpan ellapsedTIme = (date - firstOfJanuary1970);
            return (long)ellapsedTIme.TotalMilliseconds;
        }

        static public DateTime TimeMillisToDate(long milliseconds)
        {
            return firstOfJanuary1970.AddMilliseconds(milliseconds); 
        }

    }
}
