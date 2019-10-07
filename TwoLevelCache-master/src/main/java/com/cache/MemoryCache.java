package com.cache;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


class MemoryCache<K extends Serializable, V extends Serializable> implements Cache<K, V> {
    private final Map<K, V> values;
    private final int capacity;

    MemoryCache(int capacity) {
        this.capacity = capacity;
        this.values = new ConcurrentHashMap<>(capacity);
    }

    @Override
    public V get(K key) {
        return values.get(key);
    }

    @Override
    public void put(K key, V value) {
        values.put(key, value);
    }

    public boolean isValuePresent(K key) {
        return values.containsKey(key);
    }

    public boolean hasEmpty() {
        return values.size() < this.capacity;
    }

}
