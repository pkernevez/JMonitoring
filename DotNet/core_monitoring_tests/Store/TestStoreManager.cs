using NUnit.Framework;

namespace Org.NMonitoring.Core.Tests.Store
{
    [TestFixture]
    public class TestStoreManager
    {
        [Test]
        public void TestLogging()
        {
            //tester la m�thode LogEndOfMethodNormal (conversion no thread)
            Assert.Ignore("A Implementer");
        }

        [Test]
        public void TestLogEndOfMethodWhenExceptionOccursInLogBeginOfMethod()
        {
            //tester si l'ecriture se deroule bien qd un erreur survient dans l'empilement de la m�thode
            Assert.Ignore("A Implementer");
        }
    }
}
