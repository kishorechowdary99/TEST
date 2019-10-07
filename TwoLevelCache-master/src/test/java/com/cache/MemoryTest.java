package com.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

public class MemoryTest {
    private static final String VALUE = "value";

    private MemoryCache<Integer, String> memoryCache;

    @Before
    public void init() {
        memoryCache = new MemoryCache<>(3);
    }

    @Test
    public void shouldNotGetObjectFromCacheIfNotExistsTest() {
        memoryCache.put(0, VALUE);
        assertEquals(VALUE, memoryCache.get(0));
        assertNull(memoryCache.get(111));
    }


    @Test
    public void isValuePresentTest() {
        assertFalse(memoryCache.isValuePresent(0));

        memoryCache.put(0, VALUE);
        assertTrue(memoryCache.isValuePresent(0));
    }

    @Test
    public void isEmptyTest() {
        memoryCache = new MemoryCache<>(4);

        IntStream.range(0, 3).forEach(i -> memoryCache.put(i, "String " + i));

        assertTrue(memoryCache.hasEmpty());
        memoryCache.put(4, "String");
        assertFalse(memoryCache.hasEmpty());
    }
}