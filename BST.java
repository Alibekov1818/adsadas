package first;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BST<K extends Comparable<K>, V> {
    private Node root;

    private class Node {
        private K key;
        private V val;
        private Node left, right;

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    public void put(K key, V val) {
        if (get(key) != null) {
            return;
        }

        root = recursionAdd(root, key, val);
    }

    private Node recursionAdd(Node root, K key, V val) {
        if (root != null) {
            if (key.compareTo(root.key) >= 0) {
                root.right = recursionAdd(root.right, key, val);
            } else {
                root.left = recursionAdd(root.left, key, val);
            }

            return root;
        } else {
            return new Node(key, val);
        }

    }

    public V get(K key) {
        Node currentNode = root;

        while (currentNode != null && !currentNode.key.equals(key)) {
            currentNode = key.compareTo(currentNode.key) < 0 ? currentNode.left : currentNode.right;
        }

        return currentNode != null ? currentNode.val : null;
    }

    public void delete(K key) {
        boolean isInLeft = false;
        Node current = root;
        Node parent = root;

        while (current.key != key) {
            parent = current;

            if (current.key.compareTo(key) <= 0) {
                isInLeft = false;
                current = current.right;
            } else {
                isInLeft = true;
                current = current.left;
            }

            if (current != null) {
                continue;
            }

            return;
        }

        if (current.left == null && current.right == null) {
            if (current == root) {
                root = null;
            }
            if (isInLeft) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        } else {
            if (current.right == null) {
                if (current != root) {
                    if (isInLeft) {
                        parent.left = current.left;
                    } else {
                        parent.right = current.left;
                    }
                } else {
                    root = current.left;
                }
            } else if (current.left == null) {
                if (current != root) {
                    if (isInLeft) {
                        parent.left = current.right;
                    } else {
                        parent.right = current.right;
                    }
                } else {
                    root = current.right;
                }
            } else {
                Node temp = current;

                if (current != root) {
                    if (isInLeft) {
                        parent.left = temp;
                    } else {
                        parent.right = temp;
                    }
                } else {
                    root = temp;
                }
                temp.left = current.left;
            }
        }
    }

    public Iterable<K> iterator() {
        return (Iterable<K>) new BinarySearchTreeIterator<K>(root);
    }

    class BinarySearchTreeIterator<K extends Comparable<? super K>> implements Iterator<K> {

        private Stack<Node> stack = new Stack<>();
        private Node currentNode;
        private Node pending;

        BinarySearchTreeIterator(Node root) {
            if (root == null) {
                throw new IllegalArgumentException();
            }
            currentNode = root;
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            pending = stack.pop();
            currentNode = pending.right;
            while (currentNode != null) {
                stack.push(currentNode);
                currentNode = currentNode.left;
            }

            return (K) pending.key;
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        BST<Integer, Double> table = new BST<>();

        table.put(1, 11.0);
        table.put(2, 8.5);
        table.put(3, 7.4);

        System.out.println(table.get(10));
        table.delete(12);

    }
}
