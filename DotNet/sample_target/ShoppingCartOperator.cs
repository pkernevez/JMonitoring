using System.Threading;

namespace Org.NMonitoring.SampleTarget
{
    public sealed class ShoppingCartOperator
    {
        private const int TEMPO2 = 19;
        private const int TEMPO = 18;
        private ShoppingCartOperator()
        {
        }
        /**
         * For the Sample
         * @param pSc For the Sample
         * @param pInventory For the Sample
         * @param pItem For the Sample
         */
        public static void addShoppingCartItem(ShoppingCart pSc, Inventory pInventory, Item pItem)
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
            pInventory.removeItem(pItem);

            pSc.addItem(pItem);
            

        }

        /**
         * For the Sample
         * @param pSc For the Sample
         * @param pInventory For the Sample
         * @param pItem For the Sample
         */
        public static void removeShoppingCartItem(ShoppingCart pSc, Inventory pInventory, Item pItem)
        {
            try
            {
                Thread.Sleep(TEMPO2);
            }
            catch (ThreadInterruptedException e)
            {
                // @todo Auto-generated catch block
                e.StackTrace.ToString();
            }
            pSc.removeItem(pItem);

            pInventory.addItem(pItem);
        }
    }
}