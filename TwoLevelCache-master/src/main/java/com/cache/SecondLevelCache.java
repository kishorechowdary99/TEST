package com.cache;

import java.io.Serializable;

public class SecondLevelCache<K extends Serializable, V extends Serializable> implements Cache<K, V> {

	private final MemoryCache<K, V> memoryCache;
    private final FileSystemCache<K, V> fileSystemCache;

    public SecondLevelCache(final int memoryCapacity, final int fileCapacity) {
        this.memoryCache = new MemoryCache<>(memoryCapacity);
        this.fileSystemCache = new FileSystemCache<>(fileCapacity);
    }

    @Override
    public synchronized void put(K newKey, V newValue) {
        if (memoryCache.isValuePresent(newKey) || memoryCache.hasEmpty()) {
            memoryCache.put(newKey, newValue);
        } else if (fileSystemCache.isValuePresent(newKey) || fileSystemCache.hasEmpty()) {
            fileSystemCache.put(newKey, newValue);
        } else {
        	replaceObject(newKey,newValue);
        }
    }

    private void replaceObject(K key, V value) {
        if (memoryCache.isValuePresent(key)) {
            memoryCache.put(key, value);
        } else if (fileSystemCache.isValuePresent(key)) {
            fileSystemCache.put(key, value);
        }
    }
    
    @Override
    public synchronized V get(K key) {
        if (memoryCache.isValuePresent(key)) {
            return memoryCache.get(key);
        } else if (fileSystemCache.isValuePresent(key)) {
            return fileSystemCache.get(key);
        }
        return null;
    }

	public MemoryCache<K, V> getFirstLevelCache() {
		return memoryCache;
	}

	public FileSystemCache<K, V> getSecondLevelCache() {
		return fileSystemCache;
	}

	public boolean isValuePresent(K key) {
        return memoryCache.isValuePresent(key) || fileSystemCache.isValuePresent(key);
    }
    
    public synchronized boolean hasEmpty() {
        return memoryCache.hasEmpty() || fileSystemCache.hasEmpty();
    }
}
