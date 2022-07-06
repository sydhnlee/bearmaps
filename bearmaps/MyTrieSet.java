package bearmaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTrieSet implements TrieSet61BL {

    private class Node {
        private boolean isWord;
        private Map<Character, Node> children;

        public Node(boolean isWord) {
            children = new HashMap<>();
            this.isWord = isWord;
        }
    }

    private Node dummy;
    public MyTrieSet() {
        dummy = new Node(false);
    }

    @Override
    public void clear() {
        dummy.children.clear();
        dummy.isWord = false;
    }

    @Override
    public boolean contains(String key) {
        Node current = dummy;
        if (key.length() == 0) {
            if (current.children.containsKey(Character.MIN_VALUE)) {
                return true;
            }
        }
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (current.children.containsKey(c)) {
                current = current.children.get(c);
            }
            else {
                return false;
            }
        }
        if (current.isWord) {
            return true;
        }
        return false;
    }

    @Override
    public void add(String key) {
        Node curr = dummy;
        if (key.length() == 0) {
            curr.children.put(Character.MIN_VALUE, new Node(true));
            return;
        }
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.children.containsKey(c)) {
                curr.children.put(c, new Node(false));
            }
            curr = curr.children.get(c);
        }
        curr.isWord = true;
    }

    @Override
    public List<String> keysWithPrefix(String prefix) {
        List<String> words = new ArrayList<>();
        Node current = dummy;

        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (!current.children.containsKey(c)) {
                return null;
            }
            current = current.children.get(c);
        }

        keysWithPrefixHelper(prefix, current, words);
        return words;
    }

    private void keysWithPrefixHelper(String prefix, Node end, List<String> words) {
        if (end == null) {
            return;
        }
        else if (end.isWord) {
            words.add(prefix);
        }
        for (char c : end.children.keySet()) {
            keysWithPrefixHelper(prefix + c, end.children.get(c), words);
        }
    }
    @Override
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException("Unsupported");
    }

    public static void main(String[] args) {
        MyTrieSet s = new MyTrieSet();
        s.add("");
        System.out.println(s.contains(""));
        MyTrieSet t = new MyTrieSet();
        t.add("hello 1");
        t.add("hello");
        t.add("hi");
        t.add("help");
        t.add("zebra");
        t.add("hello 2");
        t.add("hello!!");
        t.keysWithPrefix("hello").forEach(word -> System.out.println(word));
        System.out.println(t.contains("hel"));
    }
}
