import java.util.ArrayList;
import java.util.Locale;
import java.util.Queue;

public class TST {

    static int noNodes;
    static Node root;
    static ArrayList<BusStop> allBusStops;

    private static class Node {
        private char c;
        private Node left;
        private Node right;
        private Node middle;
        boolean isEnd;
        BusStop stop;

    }

    public TST(ArrayList<BusStop> allBusStops)
    {
        createTST(allBusStops);
        this.allBusStops = allBusStops;
    }

    private void createTST(ArrayList<BusStop> allBusStops)
    {
        root = new Node();

        //int numStops = allBusStops.size();
        for(int i = 0; i < allBusStops.size(); i++)
        {
            BusStop currentStop = allBusStops.get(i);
            if(currentStop != null)
            {
                String name = currentStop.stop_name;
                put(name,currentStop);
            }
        }
    }

    private void insertIntoTST(String word)
    {
        String upperCase = word.toUpperCase();
        root = insertIntoTST(root, upperCase.toCharArray(), 0);
    }

    private Node insertIntoTST(Node node, char[] word, int pointer)
    {
        if(word[pointer] < node.c)
        {
            node.left = insertIntoTST(node.left, word, pointer);
        }
        else if(word[pointer] > node.c)
        {
            node.right = insertIntoTST(node.right, word, pointer);
        }
        else
        {
            if(pointer + 1 < word.length)
            {
                node.middle = insertIntoTST(node.middle, word, pointer+1);
            }
            else
            {
                node.isEnd = true;
            }
        }
        return node;
    }

    public boolean exists(String word)
    {
        if(word == null || word.length() == 0)
        {
            return false;
        }
        String uppercase = word.toUpperCase();
        return search(root, uppercase.toCharArray(), 0);
    }

    private boolean search(Node node, char[] word, int pointer)
    {
        if (node == null)
        {
            return false;
        }

        if (word[pointer] < node.c)
        {
            return search(node.left, word, pointer);
        }
        else if (word[pointer] > node.c)
        {
            return search(node.right, word, pointer);
        }
        else
        {
            if (node.isEnd && pointer == word.length - 1)
            {
                return true;
            }
            else if (pointer == word.length - 1)
            {
                return false;
            }
            else
            {
                return search(node.middle, word, pointer + 1);
            }
        }
    }

    public static Node getPrefixNode(Node node, String prefix, int depth)
    {
        if (node == null || prefix == null || prefix.length() == 0)
        {
            return null;
        }

        char character = prefix.charAt(depth);
        if(character < node.c)
        {
            return getPrefixNode(node.left, prefix, depth);
        }
        else if (character > node.c)
        {
            return getPrefixNode(node.right, prefix, depth);
        }
        else if (depth < prefix.length() - 1)
        {
            return getPrefixNode(node.middle, prefix, depth + 1);
        }
        else
        {
            return node;
        }
    }

    // Traverse the tree from a given root and add words to the list
    public static void getWordsWithPrefix(Node root, StringBuilder prefix, ArrayList<String> matches) {
        if (root == null || matches == null || prefix == null || prefix.length() == 0)
        {
            return;
        }

        getWordsWithPrefix(root.left, prefix, matches);
        if (root.isEnd)
        {
            matches.add(prefix.toString() + root.c);
        }
        getWordsWithPrefix(root.middle, prefix.append(root.c), matches);
        prefix.deleteCharAt(prefix.length() - 1);
        getWordsWithPrefix(root.right, prefix, matches);
    }

    // returns all stop names that contains the prefix provided.
    public static ArrayList<String> findStops(String prefix) {
        ArrayList<String> matches = new ArrayList<String>();

        // find prefix root node
        Node prefixNode = getPrefixNode(root, prefix, 0);
        if (prefixNode == null) {
            return new ArrayList<String>();
        }
        if (prefixNode.isEnd) {
            matches.add(prefix);
        }

        // get all words starting from the prefix root node
        getWordsWithPrefix(prefixNode.middle, new StringBuilder(prefix), matches);

        return matches;
    }

    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }

    public BusStop get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node x = get(root, key, 0);
        if (x == null) return null;
        return x.stop;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.middle,   key, d+1);
        else                           return x;
    }


    public void put(String key, BusStop val) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (!contains(key)) noNodes++;
        else if(val == null) noNodes--;       // delete existing key
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, BusStop val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        if      (c < x.c)               x.left  = put(x.left,  key, val, d);
        else if (c > x.c)               x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1)  x.middle   = put(x.middle,   key, val, d+1);
        else                            x.stop   = val;
        return x;
    }


    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        Queue<String> queue = new Queue<String>();
        Node x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.stop != null) queue.enqueue(prefix);
        collect(x.middle, new StringBuilder(prefix), queue);
        return queue;
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left,  prefix, queue);
        if (x.stop != null) queue.enqueue(prefix.toString() + x.c);
        collect(x.middle,   prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, queue);
    }
}
class Stop {
    public int stopId;
    public String stopName, stopDesc;

    public Stop(int stopId, String stopName, String stopDesc) {
        String stopNameTmp;
        switch(stopName.charAt(0)) {
            case 'W':
                stopNameTmp = stopName.substring(9);
                stopNameTmp += " WB";
                this.stopName = stopNameTmp;
                break;

            case 'E':
                stopNameTmp = stopName.substring(9);
                stopNameTmp += " EB";
                this.stopName = stopNameTmp;
                break;

            case 'N':
                stopNameTmp = stopName.substring(10);
                stopNameTmp += " NB";
                this.stopName = stopNameTmp;
                break;

            case 'S':
                stopNameTmp = stopName.substring(10);
                stopNameTmp += " SB";
                this.stopName = stopNameTmp;
                break;

            default:
                this.stopName = stopName;
                break;
        }

        this.stopId = stopId;
        this.stopDesc = stopDesc;
    }

    public String printStopSingleLine() {
        return stopName + " " + stopId + " " + stopDesc;
    }

}







}
