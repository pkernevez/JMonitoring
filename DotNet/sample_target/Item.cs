using System;
using System.Threading;

namespace Org.NMonitoring.SampleTarget
{
    public class Item
    {

        private const int TEMPO2 = 4;
        private const int TEMPO = 3;
        private String mId;

        private float mPrice;

        /**
         * For the Sample
         * @param pId For the Sample
         * @param pPrice For the Sample
         */
        public Item(String pId, float pPrice)
        {
            mId = pId;
            mPrice = pPrice;
            try
            {
                Thread.Sleep(TEMPO2);
            }
            catch (ThreadInterruptedException e)
            {
                // @todo Auto-generated catch block
                e.StackTrace.ToString();
            }
        }

        /**
         * For the Sample
         * @return For the Sample
         */
        public String getID()
        {
            return mId;
        }

        /**
         * For the Sample
         * @return For the Sample
         */
        public float getPrice()
        {
            try
            {
                Thread.Sleep(2);
            }
            catch (ThreadInterruptedException e)
            {
                // @todo Auto-generated catch block
                e.StackTrace.ToString();
            }
            return mPrice;
        }

        /**
         * For the Sample
         * @return For the Sample
         */
        public String toString()
        {
            try
            {
                Thread.Sleep(TEMPO);
            }
            catch (ThreadInterruptedException e)
            {
                // @todo Auto-generated catch block
                e.StackTrace.ToString();
            }
            return "Item: " + mId;
        }

    }
}
