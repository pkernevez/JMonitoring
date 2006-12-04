using System;
using NUnit.Framework;
using Org.NMonitoring.Core.Configuration;

namespace Org.NMonitoring.Core.Configuration.Tests
{
    [TestFixture]
    public class TestConfiguration
    {
        [Test]
        public void CreateConfigurationInstance()
        {
            Assert.AreNotEqual(null, ConfigurationManager.Instance);
        }
    }
}
