package com.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class FileSystemCache<K extends Serializable, V extends Serializable> implements Cache<K, V> {
    private final Map<K, String> values;
    private Path tempDir;
    private int capacity;

    FileSystemCache() {
        try {
			this.tempDir = Files.createTempDirectory("cache");
			this.tempDir.toFile().deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        this.values = new ConcurrentHashMap<>();
    }

    FileSystemCache(int capacity) {
        try {
			this.tempDir = Files.createTempDirectory("cache");
			this.tempDir.toFile().deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        this.capacity = capacity;
        this.values = new ConcurrentHashMap<>(capacity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized V get(K key) {
        if (isValuePresent(key)) {
            String fileName = values.get(key);
            try (FileInputStream fileInputStream = new FileInputStream(new File(tempDir + File.separator + fileName));
            	ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                return (V) objectInputStream.readObject();
            } catch (ClassNotFoundException | IOException e) {
            	System.out.println("Can't read a file. FileName : "+fileName + " Error :"+e.getMessage());
            }
        }
        return null;
    }

    @Override
    public synchronized void put(K key, V value) {
        File tmpFile = null;
		try {
			tmpFile = Files.createTempFile(tempDir, "", "").toFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(tmpFile))) {
            outputStream.writeObject(value);
            outputStream.flush();
            values.put(key, tmpFile.getName());
        } catch (IOException e) {
            System.out.println("Can't write an object to a file " + tmpFile.getName() + ": " + e.getMessage());
        }
    }

    public boolean isValuePresent(K key) {
        return values.containsKey(key);
    }
    
    public boolean hasEmpty() {
        return values.size() < this.capacity;
    }

}
