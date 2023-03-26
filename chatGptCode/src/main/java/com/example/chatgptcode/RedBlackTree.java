package com.example.chatgptcode;

/**
 * @Author: ZDS
 * @Date:2023/3/26
 * @Desc:
 */
public class RedBlackTree<K extends Comparable<K>, V> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        K key;
        V value;
        Node left, right;
        int size;
        boolean color;

        Node(K key, V value, int size, boolean color) {
            this.key = key;
            this.value = value;
            this.size = size;
            this.color = color;
        }
    }

    private Node root;

    public RedBlackTree() {
        this.root = null;
    }

    public boolean isRed(Node node) {
        if (node == null) {
            return BLACK;
        }
        return node.color;
    }

    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) {
            return 0;
        }
        return node.size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key should not be null!");
        }

        root = put(root, key, value);
        root.color = BLACK;
    }

    private Node put(Node node, K key, V value) {
        if (node == null) {
            return new Node(key, value, 1, RED);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
        }

        // 维护红黑树性质
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key should not be null!");
        }

        Node node = get(root, key);
        return node == null ? null : node.value;
    }

    private Node get(Node node, K key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                return node;
            }
        }
        return null;
    }

    private Node rotateLeft(Node node) {
        Node x = node.right;
        node.right = x.left;
        x.left = node;
        x.color = node.color;
        node.color = RED;
        x.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;
        return x;
    }

    private Node rotateRight(Node node) {
        Node x = node.left;
        node.left = x.right;
        x.right = node;
        x.color = node.color;
        node.color = RED;
        x.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;
        return x;
    }

    private void flipColors(Node node) {
        node.color = RED;
        node.left.color = BLACK;
        node.right.color = BLACK;
    }

    public static void main(String[] args) {
        RedBlackTree<Integer, String> rbTree = new RedBlackTree<>();
        rbTree.put(3, "apple");
        rbTree.put(4, "orange");
        rbTree.put(1, "banana");
        rbTree.put(5, "pear");
        rbTree.put(6, "six");
        rbTree.put(2, "two");
        rbTree.put(0, "zero");

        System.out.println(rbTree.get(1));
        System.out.println(rbTree.get(2));
        System.out.println(rbTree.get(3));
        System.out.println(rbTree.get(4));
        System.out.println(rbTree.get(5));
        System.out.println(rbTree.get(6));
        System.out.println(rbTree.get(0));
    }
}