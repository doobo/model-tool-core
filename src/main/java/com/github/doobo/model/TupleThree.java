package com.github.doobo.model;


import java.util.Objects;

/**
 * 三个元组类
 */
public class TupleThree<A, B, C> extends TupleTwo<A, B> {

    private final C third;

    public TupleThree(A one, B two, C third) {
        super(one, two);
        this.third = third;
    }

    public C getThird() {
        return third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TupleThree)) return false;
        if (!super.equals(o)) return false;
        TupleThree<?, ?, ?> that = (TupleThree<?, ?, ?>) o;
        return Objects.equals(third, that.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), third);
    }
}
