using System;
using System.Collections.Generic;
using System.Text;

namespace Org.NMonitoring.Core.Common
{
    public class Factory<T>
    {

        #region Singleton
        private static Factory<T> instance;
        public static Factory<T> Instance
        {
            get
            {
                if (instance == null)
                {
                    instance = new Factory<T>();
                }
                return instance;
            }
        }
        #endregion Singleton

        protected Factory()
        {
            interfaceType = typeof(T);
            if (!interfaceType.IsInterface)
                throw new NMonitoringException(interfaceType.Name + "must be an Interface");
        }

        public T GetNewObject()
        {
            if (typeToCreate == null)
                throw new NMonitoringException("Don't know how to create a " + interfaceType.Name + ". TypeToCreate must be set");
            return (T) Activator.CreateInstance(TypeToCreate);
        }

        private Type typeToCreate;
        private Type interfaceType;

        public Type TypeToCreate
        {
            get
            {
                return typeToCreate;
            }
            set
            {
                if (value.GetInterface(interfaceType.Name) == null)
                    throw new NMonitoringException("Type " + value.Name + " doesn't implement interface " + interfaceType.Name);
                typeToCreate = value;
            }
        }
    }
}
