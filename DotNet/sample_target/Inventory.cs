using System;
using System.Threading;
using System.Collections;

namespace Org.NMonitoring.SampleTarget
{
    public class Inventory
    {

        private const int TEMPO = 5;
        private IList mItems = new ArrayList();

        /**
         * For the Sample.
         * @param pItem For the Sample
         */
        public void addItem(Item pItem)
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
            mItems.Add(pItem);
        }

        /**
         * For the Sample
         * @param pItem For the Sample
         */
        public void removeItem(Item pItem)
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
            mItems.Remove(pItem);
        }

    }
}