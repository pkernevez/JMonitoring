using System;
using System.Threading;
using System.Text;

//using Org.NMonitoring.SampleTarget;

namespace Org.NMonitoring.SampleTarget
{
    class SampleTarget
    {

        private const int SLEEP_TEST_TIME = 200;
        private const int NB3 = 32;
        private const int NB2 = 31;
        private const int NB1 = 30;

        static void Main(string[] args)
        {
            MyMain(null);
            Console.WriteLine("Wait for log to be dumped (is asynchronous)");
            Thread.Sleep(10000); 
            Console.WriteLine("Press any key to end");
            Console.ReadLine();
         }

        public static void MyMain(Object data)
        {
            new SampleTarget().Run();
            // On attend pour être sur de l'insertion
            try
            {
                //Empirique
                Thread.Sleep(SLEEP_TEST_TIME);
            }
            catch (ThreadInterruptedException e)
            {
                Console.WriteLine(e.StackTrace.ToString());
            }
        }


        public void Run()
        {
            Console.WriteLine("Start");
            Inventory inventory = new Inventory();

            Item item1 = new Item("1", NB1);

            Item item2 = new Item("2", NB2);

            Item item3 = new Item("3", NB3);

            inventory.addItem(item1);

            inventory.addItem(item2);

            inventory.addItem(item3);
            ShoppingCart sc = new ShoppingCart();

            ShoppingCartOperator.addShoppingCartItem(sc, inventory, item1);

            ShoppingCartOperator.addShoppingCartItem(sc, inventory, item2);

            try
            {
                ShoppingCartOperator.addShoppingCartItem(sc, inventory, item3);
            }
            catch 
            {
                // C'est juste pour tester la remontée d'exception
                Console.WriteLine("");
            }
            Console.WriteLine("End");
        }
    }
}
