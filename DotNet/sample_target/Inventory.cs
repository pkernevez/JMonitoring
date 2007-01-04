using System.Collections;
using System.Threading;
using Org.NMonitoring.SampleTargetLibrary;

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
            SimpleLogger.Instance.log("Inventory:addItem");

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
            SimpleLogger.Instance.log("Inventory:RemoveItem");
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