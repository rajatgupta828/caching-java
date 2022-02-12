package com.rajat.caching.utils;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class MFUCache<T> {

    // This hashmap will be used to store the Keys and Values
    HashMap<Integer, T> keyValues;

    // This hashmap will be used to store the Keys and Values
    HashMap<Integer, Integer> keyCounts;

    // This Hashmap will contain the Keys (which is the actual Counter) and LinkedHashSet (which includes Item list)
    HashMap<Integer, LinkedHashSet<Integer>> countToLruKeys;

    int capacity;
    int min = -1;
    int max = 0;

    // Constructor
    public MFUCache(int capacity) {
        this.capacity = capacity;
        this.keyValues = new HashMap<>();
        this.keyCounts = new HashMap<>();
        this.countToLruKeys = new HashMap<>();
        countToLruKeys.put(1,new LinkedHashSet<>());
    }

    /**
     * This method is being used to get the values from cache if present
     * @param id
     * @return
     */
    public T getValue(int id){
        // If key is not present in the Hashmap already
        if(!keyValues.containsKey(id)){
            return null;
        }

        // If key is present
        // Get the counts
        int current_count = keyCounts.get(id);
        // Increase the counter
        keyCounts.put(id,current_count+1);

        // remove the item related to current counter from the actual list
        countToLruKeys.get(current_count).remove(id);


        // If there is nothing in the current minimum bucket
        if(current_count == min && countToLruKeys.get(current_count).size() == 0){
            min++ ;
            // Check if max is smaller than the current minimum bucket
            if (max < min){
                max = min;
            }
        }

        // Add the keys to count key-counts map and add the userId to the hashset to maintain the order
        putCount(id,current_count+1);
        return keyValues.get(id);
    }

    /**
     * This method will be used to put the values into the cache for
     * @param id
     * @param value
     */
    public void putValue(int id, T value){
        // If capacity is 0 or negative, we can not insert
        if(capacity < 0){
            return;
        }

        // If key is already present in the Map, we need to update the value of the Item
        if(keyValues.containsKey(id)){
            // Update the values
            keyValues.put(id, value);
            // Get the old count of the key
            int current_count = keyCounts.get(id);
            // remove the key from current count
            countToLruKeys.get(current_count).remove(id);
            // Check the current minimum bucket
            if(current_count ==  min && countToLruKeys.get(current_count).size() == 0){
                min++;
                // Check if max is smaller than the current minimum bucket
                if (max < min){
                    max = min;
                }
            }
            // Add the keys to count key-counts map and add the userId to the hashset to maintain the order
            putCount(id,current_count+1);
            return;
        }

        // If key is not present we need to add the key and data to all the hashmaps and the keys
        // Check if the size of cache has not crossed the capacity
        if(keyValues.size() >= capacity){
            // evict LRU from the minimum count bucket
            evict(countToLruKeys.get(max).iterator().next());
            min = 1;
            putCount(id,min);
            keyValues.put(id,value);
        }
    }

    /**
     * This evict function does two things :
     * 1. Remove the key from LinkedHashSet corresponding to minCount
     * 2. Remove key from keyValues map
     * @param id
     */
    private void evict(int id) {
        countToLruKeys.get(max).remove(id);
        keyValues.remove(id);
    }

    /**
     *
     * @param id
     * @param count
     */
    public void putCount(int id, int count){
        keyCounts.put(id,count);
        countToLruKeys.computeIfAbsent(count, ignore -> new LinkedHashSet<>());
        countToLruKeys.get(count).add(id);
    }


}
