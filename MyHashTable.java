package hashtable;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MyHashTable<K, V> {

    private Stream<HashNode<K, V>> apply(int i) {
        return Stream.iterate(chainArray[i], Objects::nonNull, element -> element.next);
    }

    private class HashNode<K, V> {
        private K key;
        private V value;
        private HashNode<K, V> next;

        public HashNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "{" + key + " " + value + "}";
        }
    }

    private HashNode<K, V>[] chainArray;
    private int M = 11;
    private int size;

    public MyHashTable() {
        chainArray = new HashNode[M];
    }

    public MyHashTable(int M) {
        this.M = M;
        chainArray = new HashNode[M];
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % M;
    }

    public void put(K key, V value) {
        if (key == null) {
            return;
        }

        int bucket = hash(key);

        if (chainArray[bucket] == null) {
            chainArray[bucket] = new HashNode<>(key, value);
        } else {
            HashNode<K, V> currentNode = chainArray[bucket];

            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }

            currentNode.next = new HashNode<>(key, value);
        }

        size++;
    }

    public V get(K key) {
        int bucket = hash(key);

        if (chainArray[bucket] == null) {
            return null;
        }
        HashNode<K, V> currentNode = chainArray[bucket];

        while (currentNode.next != null && !currentNode.key.equals(key)) {
            currentNode = currentNode.next;
        }

        return currentNode.value;

    }

    public V remove(K key) {
        int bucket = hash(key);

        if (chainArray[bucket] == null) {
            return null;
        }

        if (!chainArray[bucket].key.equals(key)) {
            HashNode<K, V> previousNode = chainArray[bucket];
            HashNode<K, V> temporaryNode = previousNode.next;

            while (temporaryNode != null && !temporaryNode.key.equals(key)) {
                temporaryNode = temporaryNode.next;
                previousNode = temporaryNode;
            }

            if (temporaryNode != null) {
                previousNode.next = temporaryNode.next;
                size--;
            }

            return null;
        } else {
            HashNode<K, V> currentNode = chainArray[bucket];
            chainArray[bucket] = chainArray[bucket].next;
            size--;

            return currentNode.value;
        }

    }

    public boolean contains(V value) {
        return IntStream.iterate(chainArray.length - 1, key -> key > 0, key -> key - 1)
                .mapToObj(this::apply)
                .flatMap(Function.identity())
                .anyMatch(element -> element.value.equals(value));
    }

    public K getKey(V value) {
        return Arrays.stream(chainArray)
                .filter(node -> node.value.equals(value))
                .findFirst()
                .map(node -> node.key)
                .orElse(null);

    }

    public static void main(String[] args) {
        MyHashTable<Integer, Integer> table = new MyHashTable<>();

        table.put(2, 10);
        table.put(4, 20);
        table.put(3, 2);

        table.remove(3);

        System.out.println(table.getKey(2));
        System.out.println(table.get(5));
        System.out.println(table.contains(4));

    }
}
