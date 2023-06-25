package com.github.doobo.map;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LinkedMultiValueMap<K, V> implements DoMultiValueMap<K, V>, Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private final Map<K, List<V>> targetMap;

    public LinkedMultiValueMap() {
        this.targetMap = new LinkedHashMap();
    }

    public LinkedMultiValueMap(int initialCapacity) {
        this.targetMap = new LinkedHashMap(initialCapacity);
    }

    public LinkedMultiValueMap(Map<K, List<V>> otherMap) {
        this.targetMap = new LinkedHashMap(otherMap);
    }

    public void add(K key, V value) {
        List<V> values = (List)this.targetMap.get(key);
        if (values == null) {
            values = new LinkedList();
            this.targetMap.put(key, values);
        }

        ((List)values).add(value);
    }

    public V getFirst(K key) {
        List<V> values = (List)this.targetMap.get(key);
        return values != null ? values.get(0) : null;
    }

    public void set(K key, V value) {
        List<V> values = new LinkedList();
        values.add(value);
        this.targetMap.put(key, values);
    }

    public void setAll(Map<K, V> values) {
        Iterator var2 = values.entrySet().iterator();

        while(var2.hasNext()) {
            Entry<K, V> entry = (Entry)var2.next();
            this.set(entry.getKey(), entry.getValue());
        }

    }

    public Map<K, V> toSingleValueMap() {
        LinkedHashMap<K, V> singleValueMap = new LinkedHashMap(this.targetMap.size());
        Iterator var2 = this.targetMap.entrySet().iterator();

        while(var2.hasNext()) {
            Entry<K, List<V>> entry = (Entry)var2.next();
            singleValueMap.put(entry.getKey(), (entry.getValue()).get(0));
        }
        return singleValueMap;
    }

    public int size() {
        return this.targetMap.size();
    }

    public boolean isEmpty() {
        return this.targetMap.isEmpty();
    }

    public boolean containsKey(Object key) {
        return this.targetMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.targetMap.containsValue(value);
    }

    public List<V> get(Object key) {
        return (List)this.targetMap.get(key);
    }

    public List<V> put(K key, List<V> value) {
        return (List)this.targetMap.put(key, value);
    }

    public List<V> remove(Object key) {
        return (List)this.targetMap.remove(key);
    }

    public void putAll(Map<? extends K, ? extends List<V>> map) {
        this.targetMap.putAll(map);
    }

    public void clear() {
        this.targetMap.clear();
    }

    public Set<K> keySet() {
        return this.targetMap.keySet();
    }

    public Collection<List<V>> values() {
        return this.targetMap.values();
    }

    public Set<Entry<K, List<V>>> entrySet() {
        return this.targetMap.entrySet();
    }

    public LinkedMultiValueMap<K, V> deepCopy() {
        LinkedMultiValueMap<K, V> copy = new LinkedMultiValueMap(this.targetMap.size());
        Iterator var2 = this.targetMap.entrySet().iterator();

        while(var2.hasNext()) {
            Entry<K, List<V>> entry = (Entry)var2.next();
            copy.put(entry.getKey(), (List)(new LinkedList((Collection)entry.getValue())));
        }

        return copy;
    }

    public LinkedMultiValueMap<K, V> clone() {
        return new LinkedMultiValueMap(this);
    }

    public boolean equals(Object obj) {
        return this.targetMap.equals(obj);
    }

    public int hashCode() {
        return this.targetMap.hashCode();
    }

    public String toString() {
        return this.targetMap.toString();
    }
}
