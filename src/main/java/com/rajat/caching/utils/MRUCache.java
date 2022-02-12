package com.rajat.caching.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @param <T> Uses Generic type T
 *            This cache implements a MRU cache , Most Recent is removed first.
 */
@Slf4j
public class MRUCache<T> {
    private final int capacity;
    private int size;
    private Map<Integer, Node> mruMap;
    private CacheLinkedList internalQueue;

    // Constructor
    public MRUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        mruMap = new HashMap<>();
        this.internalQueue = new CacheLinkedList();
    }

    // Method to get the value from cache
    public T getValue(int id) {
        Node node = mruMap.get(id);
        if (node == null) {
            return null;
        }
        internalQueue.moveNodeToTop(node);
        log.info("Current Cache : ");
        internalQueue.printlist(internalQueue.head);
        return mruMap.get(id).value;
    }

    //*
    public void putValue(int id, T value) {

        // First try to get the data from current Cache
        Node currentNode = mruMap.get(id);

        // If key is already present
        if (currentNode != null) {
            currentNode.value = value;
            internalQueue.moveNodeToTop(currentNode);
            log.info("Current Cache : ");
            internalQueue.printlist(internalQueue.head);
        }

        // If size has reached the capacity
        if (size == capacity) {
            int topItemKey = internalQueue.getHeadKey();
            internalQueue.removeNodeFromTop();
            log.info("Current Cache : ");
            internalQueue.printlist(internalQueue.head);
            mruMap.remove(topItemKey);
            size--;
        }

        // Since old node has been deleted, time to create a new node
        Node newNode = new Node(id, value);
        internalQueue.addNodeAtHead(newNode);
        log.info("Current Cache : ");
        internalQueue.printlist(internalQueue.head);
        mruMap.put(id, newNode);
        size++;
    }

    public void printCache(){
        internalQueue.printlist(internalQueue.tail);
    }

    // Node inner class to bind these together
    public class Node {
        int key;
        T value;
        Node next, prev;

        public Node(int key, T value) {
            this.key = key;
            this.value = value;
            this.next = null;
            this.prev = null;
        }
    }

    // Doubly Linked list
    public class CacheLinkedList {
        private Node head, tail;

        public CacheLinkedList() {
            head = tail = null;
        }

        public int getBottomKey() {
            if (tail == null) {
                return 0;
            }
            return tail.key;
        }

        public int getHeadKey() {
            if (head == null) {
                return 0;
            }
            return head.key;
        }

        public void addNodeAtHead(Node node) {
            log.info("Adding element to list's head : Key " + node.key + " " + node.value.toString());
            // If list is already Empty
            if (tail == null) {
                head = tail = node;
                return;
            }
            // If Node is not empty
            node.next = head;
            head.prev = node;
            head = node;
        }

        public void removeNodeFromTop() {
            // Check if the list is not empty
            if (tail == null) {
                return;
            }
            log.info("Deleting key from list " + head.key);
            // Check if only 1 element left
            if (head == tail) {
                head = tail = null;
            } else {
                head = head.next;
                head.prev = null;
            }
        }

        public void moveNodeToTop(Node node) {
            log.info("Bringing node at head " + node.key + " " + node.value.toString());
            // Check if node is already head  , don't do anything
            if (node == head) {
                return;
            }
            // Step 1 - bring the node out of the list first
            // Check if the node is the last node
            if (node == tail) {
                tail = tail.prev;
                tail.next = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
            // Step 2 - Make the node as the top one
            node.prev = null;
            node.next = head;
            head.prev = node;
            head = node;
        }

        public void printlist(Node node) {
            Node tail = null;
            while (node != null) {
                System.out.print(node.key + " ");
                tail = node;
                node = node.next;
            }
            System.out.println();
        }
    }
}
