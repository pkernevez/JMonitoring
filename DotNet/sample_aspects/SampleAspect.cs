using DotNetGuru.AspectDNG.Joinpoints;
using Org.NMonitoring.Core.Aspect;

namespace sample_aspects
{
    public class SampleAspect : PerformanceAspect 
    {
        public static object Inventory(OperationJoinPoint jp)
        {
            GroupName = "GpeInventory";
            LogParameter = true;

            return ExecutionToLogInternal(jp);
        }

        public static object Item(OperationJoinPoint jp)
        {
            GroupName = "GpeItem";
            LogParameter = true;

            return ExecutionToLogInternal(jp);
        }

        public static object ShoppingCart(OperationJoinPoint jp)
        {
            GroupName = "GpeShoppingCart";
            LogParameter = true;

            return ExecutionToLogInternal(jp);
        }


        public static object ShoppingCartOperator(OperationJoinPoint jp)
        {
            GroupName = "GpeShoppingCartOperator";
            LogParameter = true;

            return ExecutionToLogInternal(jp);
        }

        public static object SampleTarget(OperationJoinPoint jp)
        {
            GroupName = "GpeSampleTarget";
            LogParameter = true;

            return ExecutionToLogInternal(jp);
        }

        public static object SimpleLogger(OperationJoinPoint jp)
        {
            GroupName = "GpeSimpleLogger";
            LogParameter = true;
            
            return ExecutionToLogInternal(jp);
        }

    }
}
