using NUnit.Framework;

namespace Org.NMonitoring.Core.Common.Tests
{
    [TestFixture]
    public class TestFactory
    {
        interface AnInterface
        {
        }

        interface AnOtherInterface
        {
        }

        class AClassImplementingAnInterface : AnInterface
        {
        }

        class AClassNotImplementingAnInterface
        {
        }

        [Test]
        [ExpectedException(typeof(NMonitoringException))]
        public void CouldNotSetTypeToCreateWithANonImplementingClass()
        {
           Factory<AnInterface> factory = Factory<AnInterface>.Instance;
           factory.TypeToCreate = typeof(AClassNotImplementingAnInterface);
        }

        [Test]      
        public void CouldSetTypeToCreateWithAnImplementingClass()
        {
            Factory<AnInterface> factory = Factory<AnInterface>.Instance;
            factory.TypeToCreate = typeof(AClassImplementingAnInterface);
            Assert.AreEqual(typeof(AClassImplementingAnInterface), factory.TypeToCreate);
        }

        [Test]
        [ExpectedException(typeof(NMonitoringException))]
        public void CouldNotCreateAFactoryForNonInterfaceTypes()
        {
            Factory<AClassImplementingAnInterface> factory = Factory<AClassImplementingAnInterface>.Instance;
        }

        [Test]
        [ExpectedException(typeof(NMonitoringException))]
        public void TypeToCreateMustBeSet()
        {
            Factory<AnOtherInterface> factory = Factory<AnOtherInterface>.Instance;
            AnOtherInterface anOtherInterface = factory.GetNewObject();
        }
    }
}
