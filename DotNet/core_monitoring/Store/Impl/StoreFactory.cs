using System;

namespace Org.NMonitoring.Core.Store.Impl
{
    public sealed class StoreFactory
    {
        public StoreFactory()
        {
            //TODO FCH : Use a configuration parametrer
            writer =  new SynchroneDBWriter();
            //writer = new AsynchroneDbWriter();

        }

        private IStoreWriter writer;

        /**
         * Factory.
         * 
         * @return Une instance de <code>IStoreWriter</code> pour les logs en fonction du paramètrage.
         */
        public  IStoreWriter Writer
        {
            get
            {
                return writer;
            }

            /// <summary>
            /// Use this setter to force the store Manager 
            ///(it should be done only for testing purpose)
            /// </summary>
            /// <returns></returns>
            set
            {
                writer = value;
            }
        }
    }

}


