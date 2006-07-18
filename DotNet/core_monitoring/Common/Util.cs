using System;

namespace Org.NMonitoring.Core.Common
{
    public class Util
    {
        private static DateTime premierJanvier1970 = new DateTime(1970, 01, 01, 0, 0, 0, 0);

        static public long CurrentTimeMillis()
        {
            return DateToTimeMillis(DateTime.Now);
        }

        static public long DateToTimeMillis(DateTime date)
        {
            TimeSpan ellapsedTIme = (date - premierJanvier1970);
            return (long)ellapsedTIme.TotalMilliseconds;
        }

        static public DateTime TimeMillisToDate(long milliseconds)
        {
            return premierJanvier1970.AddMilliseconds(milliseconds); 
        }
    }
}
