package com.ck.smartsearch.customstructlayer;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Trie {

    private final TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    // O(L) - Insert a word into the Trie
    public void insert(String word) {
        TrieNode current = root;
        for (char ch : word.toLowerCase().toCharArray()) {
            current.getChildren().putIfAbsent(ch, new TrieNode());
            current = current.getChildren().get(ch);
        }
        current.setEndOfWord(true);
    }

    // O(L + N) - Find all words starting with a prefix
    public List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode current = root;

        // 1. Navigate to the node representing the prefix
        for (char ch : prefix.toLowerCase().toCharArray()) {
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return results; // Prefix not found
            }
            current = node;
        }

        // 2. Perform DFS/Recursion to find all complete words from this point
        findAllWords(current, prefix.toLowerCase(), results);
        return results;
    }

    // Helper for Depth First Search
    private void findAllWords(TrieNode node, String currentWord, List<String> results) {
        if (node.isEndOfWord()) {
            results.add(currentWord);
        }

        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            findAllWords(entry.getValue(), currentWord + entry.getKey(), results);
        }
    }
}