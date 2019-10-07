package com.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;


public class SecondLevelCacheTest {
    private static final String VALUE1 = "value1";
    private static final String VALUE2 = "value2";

    private SecondLevelCache<Integer, String> secondLevelCache;

    @Before
    public void init() {
        secondLevelCache = new SecondLevelCache<>(1, 1);
    }
    @Test
    public void shouldNotGetObjectFromCacheIfNotExistsTest() {
        secondLevelCache.put(0, VALUE1);
        assertEquals(VALUE1, secondLevelCache.get(0));
        assertNull(secondLevelCache.get(111));
    }


    @Test
    public void shouldPutObjectIntoCacheWhenFirstLevelHasEmptyPlaceTest() {
        assertTrue(secondLevelCache.getFirstLevelCache().hasEmpty());
        secondLevelCache.put(0, VALUE1);
        assertEquals(VALUE1, secondLevelCache.get(0));
        assertEquals(VALUE1, secondLevelCache.getFirstLevelCache().get(0));
        assertFalse(secondLevelCache.getSecondLevelCache().isValuePresent(0));
    }

    @Test
    public void shouldPutObjectIntoCacheWhenSecondLevelHasEmptyPlaceTest() {
        IntStream.range(0, 1).forEach(i -> secondLevelCache.put(i, "String " + i));

        assertFalse(secondLevelCache.getFirstLevelCache().hasEmpty());
        assertTrue(secondLevelCache.getSecondLevelCache().hasEmpty());

        secondLevelCache.put(2, VALUE2);

        assertEquals(VALUE2, secondLevelCache.get(2));
        assertEquals(VALUE2, secondLevelCache.getSecondLevelCache().get(2));
    }

    @Test
    public void isValuePresentTest() {
        assertFalse(secondLevelCache.isValuePresent(0));

        secondLevelCache.put(0, VALUE1);
        assertTrue(secondLevelCache.isValuePresent(0));
    }

    @Test
    public void isEmptyPlaceTest() {
        assertFalse(secondLevelCache.isValuePresent(0));
        secondLevelCache.put(0, VALUE1);
        assertTrue(secondLevelCache.hasEmpty());

        secondLevelCache.put(1, VALUE2);
        assertFalse(secondLevelCache.hasEmpty());
    }

}