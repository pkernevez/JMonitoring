using System;
using System.Text;
using NUnit.Framework;

using Org.NMonitoring.Core.Persistence;

namespace Org.NMonitoring.Core.Persistence.Tests
{
    [TestFixture]
    public class TestMethodCallPO
    {

        [Test]
        public void testUpdateChildrenWhenCreateWithParent()
        {
            MethodCallPO tParent = new MethodCallPO(null, this.GetType().FullName, "builNewFullFlow", "GrDefault",
                            new System.Reflection.ParameterInfo[0]);

            MethodCallPO tChild = new MethodCallPO(tParent, this.GetType().FullName, "builNewFullFlow2",
                            "GrChild1", new System.Reflection.ParameterInfo[0]);

            Assert.IsNull(tParent.Parent);
            Assert.AreEqual(1, tParent.Children.Count);
            Assert.AreSame(tParent, tChild.Parent);

            tParent.RemoveChildren(tChild);
            Assert.IsNull(tParent.Parent);
            Assert.AreEqual(0, tParent.Children.Count);
            Assert.IsNull(tChild.Parent);
        }

        [Test]
        public void testUpdateChildrenWhenAddParent()
        {
           MethodCallPO tParent = new MethodCallPO(null, this.GetType().FullName, "builNewFullFlow", "GrDefault",
                            new System.Reflection.ParameterInfo[0]);

            MethodCallPO tChild = new MethodCallPO(null, this.GetType().FullName, "builNewFullFlow2",
                            "GrChild1", new System.Reflection.ParameterInfo[0]);

            Assert.IsNull(tParent.Parent);
            Assert.AreEqual(0, tParent.Children.Count);
            Assert.IsNull(tChild.Parent);

            tChild.Parent=tParent;
            Assert.IsNull(tParent.Parent);
            Assert.AreEqual(1, tParent.Children.Count);
            Assert.AreSame(tParent, tChild.Parent);

            tChild.Parent=null;
            tChild.Parent=null; // On teste avec null 2 fois...
            Assert.IsNull(tParent.Parent);
            Assert.AreEqual(0, tParent.Children.Count);
            Assert.IsNull(tChild.Parent);

            tChild.Parent=tParent;
            Assert.IsNull(tParent.Parent);
            Assert.AreEqual(1, tParent.Children.Count);
            Assert.AreSame(tParent, tChild.Parent);
        }
    }
}
 