package org.jmonitoring.core.info;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class TestTreeTracerHelper extends TestCase
{

    public class Mother
    {
        public List mChildren1 = new ArrayList();

        public Child1 mChild1;

        public Child2[] mChildren2;

        public Map mChildren2Bis = new HashMap();

        public Date mBidon = new Date();

        public Date getBidon()
        {
            return mBidon;
        }

        public void setBidon(Date pBidon)
        {
            mBidon = pBidon;
        }

        public Child1 getChild1()
        {
            return mChild1;
        }

        public void setChild1(Child1 pChild1)
        {
            mChild1 = pChild1;
        }

        public List getChildren1()
        {
            return mChildren1;
        }

        public void setChildren1(List pChildren1)
        {
            mChildren1 = pChildren1;
        }

        public Child2[] getChildren2()
        {
            return mChildren2;
        }

        public void setChildren2(Child2[] pChildren2)
        {
            mChildren2 = pChildren2;
        }

        public Map getChildren2Bis()
        {
            return mChildren2Bis;
        }

        public void setChildren2Bis(Map pChildren2Bis)
        {
            mChildren2Bis = pChildren2Bis;
        }

    }

    public class Child1
    {
        public List mChildren2 = new ArrayList();

        public List getChildren2()
        {
            return mChildren2;
        }

        public void setChildren2(List pChildren2)
        {
            mChildren2 = pChildren2;
        }

        public Child1()
        {
        }

        public Child1(Child2 pChild2)
        {
            mChildren2.add(pChild2);
        }
    }

    public class Child2
    {
    }

    public void testTraceObjectTree()
    {
        Mother tMother = new Mother();
        tMother.mChild1 = new Child1(new Child2());
        tMother.mChild1.mChildren2.add(new Child2());
        tMother.mChildren1.add(new Child1(new Child2()));
        tMother.mChildren1.add(new Child1());

        StringBuffer tBuffer = new StringBuffer();
        TreeTracerHelper.traceObjectTree(tBuffer, tMother);
        String tExpectedResultL1 = Mother.class.getName() + "\n";
        String tExpectedResultL2 = "   |-- getChild1 --> " + Child1.class.getName() + "\n";
        String tExpectedResultL3 = "   |  |-- getChildren2 --> " + Child2.class.getName();
        String tExpectedResultL4 = "   |-- getChildren1 --> " + Child1.class.getName() + "\n";
        
    }

}
