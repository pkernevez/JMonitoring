package org.jmonitoring.core.persistence;

import java.io.Serializable;

public class MethodCallPK implements Serializable
{

    private static final long serialVersionUID = -329582789701661686L;

    private ExecutionFlowPO mFlow;

    private int mPosition;

    public MethodCallPK()
    {
    }

    public MethodCallPK(ExecutionFlowPO pFlow, int pPosition)
    {
        super();
        mFlow = pFlow;
        mPosition = pPosition;
    }

    public ExecutionFlowPO getFlow()
    {
        return mFlow;
    }

    public void setFlow(ExecutionFlowPO pFlow)
    {
        mFlow = pFlow;
    }

    public int getPosition()
    {
        return mPosition;
    }

    public void setPosition(int pPosition)
    {
        mPosition = pPosition;
    }

    public int hashCode()
    {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((mFlow == null) ? 0 : mFlow.hashCode());
        result = PRIME * result + mPosition;
        return result;
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final MethodCallPK other = (MethodCallPK) obj;
        if (mFlow == null)
        {
            if (other.mFlow != null)
                return false;
        } else if (!mFlow.equals(other.mFlow))
            return false;
        if (mPosition != other.mPosition)
            return false;
        return true;
    }

}
