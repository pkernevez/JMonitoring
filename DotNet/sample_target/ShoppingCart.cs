using System;
using System.Collections;
using System.Threading;

namespace Org.NMonitoring.SampleTarget
{
    public class ShoppingCart
    {

        private const int TEMPO7 = 9;
        private const int TEMPO6 = 11;
        private const int TEMPO5 = 17;
        private const int TEMPO4 = 16;
        private const int TEMPO3 = 15;
        private const int TEMPO2 = 13;
        private const int TEMPO = 7;
        private IList mItems = new ArrayList();

        /**
         * For the Sample 
         * @param pItem For the Sample
         */
        public void addItem(Item pItem)
        {
            Org.NMonitoring.SampleTargetLibrary.SimpleLogger.Instance.log("ShoppingCart:addItem");

            try
            {
                Thread.Sleep(TEMPO);
            }
            catch (ThreadInterruptedException e)
            {
                // @todo Auto-generated catch block
                e.StackTrace.ToString();
            }
            if ("3".Equals(pItem.getID()))
            {
                throw new Exception("Pour declancher une exception sur item3");
            }
            //*
            mItems.Add(pItem);


/***********************************
            Connection tCon = new MockConnection();
            PreparedStatement tState = null;
            try
            {
                tState = tCon.prepareStatement("lk");
                try
                {
                    Thread.Sleep(TEMPO7);
                }
                catch (InterruptedException e)
                {
                    // @todo Auto-generated catch block
                    e.printStackTrace();
                }
                tState.setString(0, "Param0");
                try
                {
                    Thread.Sleep(TEMPO6);
                }
                catch (ThreadInterruptedException e)
                {
                    // @todo Auto-generated catch block
                    e.StackTrace.ToString();
                }
                tState.setInt(1, 0);
                tState.executeQuery();
            }
            catch (SQLException e)
            {
                throw new RuntimeException("Impossible de créer un statement !", e);
            }
            finally
            {
                if (tState != null)
                {
                    try
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
                        tState.close();
                    }
                    catch (SQLException e)
                    {
                        throw new RuntimeException("Impossible de fermer un statement !", e);

                    }
                }
            }
            ********************************/
        }
 

        /**
         * For the Sample
         * @param pItem For the Sample
         */
        public void removeItem(Item pItem)
        {
            Org.NMonitoring.SampleTargetLibrary.SimpleLogger.Instance.log("ShoppingCart:removeItem");

            try
            {
                Thread.Sleep(TEMPO3);
            }
            catch (ThreadInterruptedException e)
            {
                // @todo Auto-generated catch block
                e.StackTrace.ToString();
            }
            mItems.Remove(pItem);
        }

        /**
         * For the Sample
         *
         */
        public void empty()
        {
            try
            {
                Thread.Sleep(TEMPO4);
            }
            catch (ThreadInterruptedException e)
            {
                // @todo Auto-generated catch block
                e.StackTrace.ToString();
            }
            mItems.Clear();
        }

        /**
         * For the Sample
         * @return For the Sample
         */
        public float totalValue()
        {
            // unimplemented... free!
            try
            {
                Thread.Sleep(TEMPO5);
            }
            catch (ThreadInterruptedException e)
            {
                // @todo Auto-generated catch block
                e.StackTrace.ToString();
            }
            return 0;
        }
    }
}