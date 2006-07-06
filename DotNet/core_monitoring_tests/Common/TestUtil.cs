using System;
using NUnit.Framework;
using Org.NMonitoring.Core.Common;

namespace Org.NMonitoring.Core.Common.Tests
{
    [TestFixture]
    public class TestUtil
    {
        /*
        [Test]
        public void TimeEllapsedBetweenTwoCurrentTime()
        {
            long expected = 5;

            long startTime = Util.CurrentTimeMillis();
            System.Threading.Thread.Sleep((int)expected);
            long endTime = Util.CurrentTimeMillis();

            TimeSpan ts = new TimeSpan(endTime - startTime);
            long actual = (long) ts.TotalMilliseconds;
            Assert.AreEqual(expected, actual);
        }
         */

        [Test]
        public void ConvertDateToTimeMillis()
        {
            //Ellapsed Time in millisecond to reference date is zero
            DateTime date = new DateTime(1970, 01, 01, 0, 0, 0);
            long expected = 0;
            Assert.AreEqual(0, expected - Util.DateToTimeMillis(date), "Ellapsed Time in millisecond to reference date is " + expected.ToString());

            //Ellapsed Time in millisecond to reference date + 1 hour is 3600000
            date = new DateTime(1970, 01, 01, 1, 0, 0);
            expected = 3600000;
            Assert.AreEqual(0, expected - Util.DateToTimeMillis(date), "Ellapsed Time in millisecond to reference date + 1 hour is " + expected.ToString());

            //Ellapsed Time in millisecond to reference date + 1 year is 31536000000
            date = new DateTime(1971, 01, 01, 0, 0, 0);
            expected = 31536000000;
            Assert.AreEqual(0, expected - Util.DateToTimeMillis(date), "Ellapsed Time in millisecond to reference date + 1 year is " + expected.ToString());

            //Ellapsed Time in millisecond to a day in 01.2006
            date = new DateTime(2006, 01, 01, 01, 02, 03);
            expected = 1136077323000;
            Assert.AreEqual(0, expected - Util.DateToTimeMillis(date), "Ellapsed Time in millisecond to a day in 01.2006 is " + expected.ToString());
        }
    }
}
