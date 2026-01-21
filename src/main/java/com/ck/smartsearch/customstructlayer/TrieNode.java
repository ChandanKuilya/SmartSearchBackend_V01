package com.ck.smartsearch.customstructlayer;
import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
public class TrieNode {
    // Each node maps a character to the next TrieNode
    private final Map<Character, TrieNode> children = new HashMap<>();

    // Marks the end of a complete product name
    private boolean isEndOfWord = false;

    public void setEndOfWord(boolean endOfWord) {
        isEndOfWord = endOfWord;
    }
}