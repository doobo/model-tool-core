package com.github.doobo.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * 权重元素模型
 */
public class WeightElement<T extends Serializable> implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private String uniqueKey;

    private int count = 1;

    private int min;

    private int max;

    private transient T t;

    public WeightElement() {
        this.uniqueKey = UUID.randomUUID().toString();
    }

    public WeightElement(T t, int count) {
        this.uniqueKey = UUID.randomUUID().toString();
        this.t = t;
        this.count = count;
    }

    public WeightElement(int count) {
        this.count = count;
    }

    public WeightElement(T t) {
        this.t = t;
    }

    public int getCount() {
        return this.count;
    }

    public WeightElement<T> setCount(int count) {
        this.count = count;
        return this;
    }

    public int getMin() {
        return this.min;
    }

    public WeightElement<T> setMin(int min) {
        this.min = min;
        return this;
    }

    public int getMax() {
        return this.max;
    }

    public WeightElement<T> setMax(int max) {
        this.max = max;
        return this;
    }

    public String getUniqueKey() {
        return this.uniqueKey;
    }

    public T getT() {
        return this.t;
    }

    public WeightElement<T> setT(T t) {
        this.t = t;
        return this;
    }

    public static <T extends Serializable> WeightElement<T> newInstance(WeightElement<T> policy) {
        WeightElement<T> copyPolicy = new WeightElement<>();
        copyPolicy.setT(policy.getT()).setMax(policy.getMax()).setMin(policy.getMin()).setCount(policy.getCount());
        return copyPolicy;
    }
}
