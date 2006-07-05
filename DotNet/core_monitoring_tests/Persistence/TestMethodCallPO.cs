using System;
using System.Text;
using NUnit.Framework;

using org.nmonitoring.core.persistence;

namespace org.nmonitoring.core.persistence.tests
{
    [TestFixture]
    public class TestMethodCallPO
    {

        [Test]
        public void testUpdateChildrenWhenCreateWithParent()
        {
            MethodCallPO tParent = new MethodCallPO(null, this.GetType().FullName, "builNewFullFlow", "GrDefault",
                            new Object[0]);

            MethodCallPO tChild = new MethodCallPO(tParent, this.GetType().FullName, "builNewFullFlow2",
                            "GrChild1", new Object[0]);

            Assert.IsNull(tParent.getParentMethodCall());
            Assert.AreEqual(1, tParent.getChildren().Count);
            Assert.AreSame(tParent, tChild.getParentMethodCall());

            tParent.removeChildren(tChild);
            Assert.IsNull(tParent.getParentMethodCall());
            Assert.AreEqual(0, tParent.getChildren().Count);
            Assert.IsNull(tChild.getParentMethodCall());
        }

        [Test]
        public void testUpdateChildrenWhenAddParent()
        {
            MethodCallPO tParent = new MethodCallPO(null, this.GetType().FullName, "builNewFullFlow", "GrDefault",
                            new Object[0]);

            MethodCallPO tChild = new MethodCallPO(null, this.GetType().FullName, "builNewFullFlow2",
                            "GrChild1", new Object[0]);

            Assert.IsNull(tParent.getParentMethodCall());
            Assert.AreEqual(0, tParent.getChildren().Count);
            Assert.IsNull(tChild.getParentMethodCall());

            tChild.setParentMethodCall(tParent);
            Assert.IsNull(tParent.getParentMethodCall());
            Assert.AreEqual(1, tParent.getChildren().Count);
            Assert.AreSame(tParent, tChild.getParentMethodCall());

            tChild.setParentMethodCall(null);
            tChild.setParentMethodCall(null); // On teste avec null 2 fois...
            Assert.IsNull(tParent.getParentMethodCall());
            Assert.AreEqual(0, tParent.getChildren().Count);
            Assert.IsNull(tChild.getParentMethodCall());

            tChild.setParentMethodCall(tParent);
            Assert.IsNull(tParent.getParentMethodCall());
            Assert.AreEqual(1, tParent.getChildren().Count);
            Assert.AreSame(tParent, tChild.getParentMethodCall());
        }
    }
}
 