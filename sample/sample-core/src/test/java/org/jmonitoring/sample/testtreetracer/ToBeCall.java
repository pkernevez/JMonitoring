package org.jmonitoring.sample.testtreetracer;

public class ToBeCall {
    public static class Mother {
        private Child mChild1;

        private Child mChild2;

        public Child getChild1() {
            return mChild1;
        }

        public void setChild1(Child pChild1) {
            mChild1 = pChild1;
        }

        public Child getChild2() {
            return mChild2;
        }

        public void setChild2(Child pChild2) {
            mChild2 = pChild2;
        }

    }

    public static class Child {

    }

    public void callWithParam(Mother pMother, Child pChild) {
    }

    public Mother callWithReturn() {
        Mother tMother = new Mother();
        tMother.setChild1(new Child());
        tMother.setChild2(new Child());
        return tMother;
    }

    public static void callStaticMethod(Mother pMother) {

    }
}
