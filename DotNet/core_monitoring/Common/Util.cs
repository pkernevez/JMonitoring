using System;
using System.Collections.Generic;
using System.Text;

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


        //static public long DateToTimeMillis(DateTime date)
        //{
            
        //    DateTime 
        //    TimeSpan.n
        //    ts.TotalMilliseconds;


        //    long millis = (date.Year - 1970)*
        //    return (long)date.TimeOfDay.TotalMilliseconds;

            
        //}
        
    }
}
