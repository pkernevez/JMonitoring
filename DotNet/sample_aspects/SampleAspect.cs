using System;
using Org.NMonitoring.Core.Aspect;
using DotNetGuru.AspectDNG.Joinpoints;

namespace sample_aspects
{
    public class SampleAspect : Org.NMonitoring.Core.Aspect.PerformanceAspect 
    {
        public static object Inventory(OperationJoinPoint jp)
        {
            mGroupName = "GpeInventory";
            return Org.NMonitoring.Core.Aspect.PerformanceAspect.executionToLogInternal(jp);
        }

        public static object Item(OperationJoinPoint jp)
        {
            mGroupName = "GpeItem";
            return Org.NMonitoring.Core.Aspect.PerformanceAspect.executionToLogInternal(jp);
        }

        public static object ShoppingCart(OperationJoinPoint jp)
        {
            mGroupName = "GpeShoppingCart";
            return Org.NMonitoring.Core.Aspect.PerformanceAspect.executionToLogInternal(jp);
        }


        public static object ShoppingCartOperator(OperationJoinPoint jp)
        {
            mGroupName = "GpeShoppingCartOperator";
            return Org.NMonitoring.Core.Aspect.PerformanceAspect.executionToLogInternal(jp);
        }

        public static object SampleTarget(OperationJoinPoint jp)
        {
            mGroupName = "GpeSampleTarget";
            return Org.NMonitoring.Core.Aspect.PerformanceAspect.executionToLogInternal(jp);
        }

        public static object SimpleLogger(OperationJoinPoint jp)
        {
            mGroupName = "GpeSimpleLogger";
            return Org.NMonitoring.Core.Aspect.PerformanceAspect.executionToLogInternal(jp);
        }

    }
}
