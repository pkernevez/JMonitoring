using System;
using System.Runtime.Serialization;

namespace Org.NMonitoring.Core.Common
{
    [Serializable]
    public sealed class NMonitoringException : Exception
    {
        public NMonitoringException()
        {
        }

        public NMonitoringException(String message)
            : base(message)
        {
        }
        public NMonitoringException(String message, Exception innerException)
            :base(message,innerException)
        {
        }

        private NMonitoringException(SerializationInfo info, StreamingContext context)
            :base(info,context)
        {
        }
    }
}
