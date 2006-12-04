using System;
using Org.NMonitoring.Core.Aspect;
using DotNetGuru.AspectDNG.Joinpoints;

namespace sample_aspects
{
    public class SampleAspect : Org.NMonitoring.Core.Aspect.PerformanceAspect 
    {
        public static object Inventory(OperationJoinPoint jp)
        {
            GroupName = "GpeInventory";
            LogParameter = true;

            return Org.NMonitoring.Core.Aspect.PerformanceAspect.ExecutionToLogInternal(jp);
        }

        public static object Item(OperationJoinPoint jp)
        {
            GroupName = "GpeItem";
            LogParameter = true;

            return Org.NMonitoring.Core.Aspect.PerformanceAspect.ExecutionToLogInternal(jp);
        }

        public static object ShoppingCart(OperationJoinPoint jp)
        {
            GroupName = "GpeShoppingCart";
            LogParameter = true;

            return Org.NMonitoring.Core.Aspect.PerformanceAspect.ExecutionToLogInternal(jp);
        }


        public static object ShoppingCartOperator(OperationJoinPoint jp)
        {
            GroupName = "GpeShoppingCartOperator";
            LogParameter = true;

            return Org.NMonitoring.Core.Aspect.PerformanceAspect.ExecutionToLogInternal(jp);
        }

        public static object SampleTarget(OperationJoinPoint jp)
        {
            GroupName = "GpeSampleTarget";
            LogParameter = true;

            return Org.NMonitoring.Core.Aspect.PerformanceAspect.ExecutionToLogInternal(jp);
        }

        public static object SimpleLogger(OperationJoinPoint jp)
        {
            GroupName = "GpeSimpleLogger";
            LogParameter = true;
            
            return Org.NMonitoring.Core.Aspect.PerformanceAspect.ExecutionToLogInternal(jp);
        }

    }
}
