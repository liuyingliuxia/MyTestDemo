package com.example.chatgptcode;
public class KMPAlgorithm {
    public static int[] getNext(String pattern) {
        int[] next = new int[pattern.length()];
        next[0] = -1;
        int i = 0, j = -1;

        while (i < pattern.length() - 1) {
            if (j == -1 || pattern.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                next[i] = j;
            } else {
                j = next[j];
            }
        }
        return next;
    }

    public static int kmp(String str, String pattern) {
        int[] next = getNext(pattern);
        int i = 0, j = 0;

        while (i < str.length() && j < pattern.length()) {
            if (j == -1 || str.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            } else {
                j = next[j];
            }
        }

        if (j == pattern.length()) {
            return i - j;
        } else {
            return -1;
        }
    }

    public static void main(String[] args) {
        String str = "你好啊你好你是谁请问你的名字叫什么";
        String pattern = "啊";

        int index = kmp(str, pattern);
        if (index == -1) {
            System.out.println("No match.");
        } else {
            System.out.println("Pattern found at index " + index);
        }
    }
}