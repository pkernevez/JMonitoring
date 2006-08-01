using System;

namespace Org.NMonitoring.Core.Store.Impl
{
    public sealed class StoreFactory
    {
        private StoreFactory()
        {
        }

        /**
         * Factory.
         * 
         * @return Une instance de <code>IStoreWriter</code> pour les logs en fonction du param�trage.
         */
        public static IStoreWriter getWriter()
        {
            //TODO FCH : Parametrer
            return new SynchroneDbWriter();
            //return new AsynchroneDbWriter();
        }
    }

}


