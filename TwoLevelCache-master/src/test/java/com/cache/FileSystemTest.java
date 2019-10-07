package com.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;


public class FileSystemTest {
    private static final String VALUE = "value";

    private FileSystemCache<Integer, String> fileSystemCache;

    @Before
    public void init() {
        fileSystemCache = new FileSystemCache<>();
    }

    @Test
    public void shouldNotGetObjectFromCacheIfNotExistsTest() {
        fileSystemCache.put(0, VALUE);
        assertEquals(VALUE, fileSystemCache.get(0));
        assertNull(fileSystemCache.get(111));
    }

    @Test
    public void isValuePresentTest() {
        assertFalse(fileSystemCache.isValuePresent(0));

        fileSystemCache.put(0, VALUE);
        assertTrue(fileSystemCache.isValuePresent(0));
    }

    @Test
    public void isEmptyTest() {
        fileSystemCache = new FileSystemCache<>(4);
        IntStream.range(0, 3).forEach(i -> fileSystemCache.put(i, "String " + i));
        assertTrue(fileSystemCache.hasEmpty());
        fileSystemCache.put(4, "String");
        assertFalse(fileSystemCache.hasEmpty());
    }

}
