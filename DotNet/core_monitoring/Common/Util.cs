using System;
using System.Collections.Generic;
using System.Text;

namespace Org.NMonitoring.Core.Common
{
    public class Util
    {
        static public long CurrentTimeMillis()
        {
            return (long)DateTime.Now.TimeOfDay.TotalMilliseconds;
        }
    }
}
