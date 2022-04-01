import java.util.*;

public class TST<T> {

    static int noNodes;
    Node root;
    ArrayList<BusStop> possibleValues;
    //HashSet<BusStop> searchResults;
    static ArrayList<BusStop> allBusStops;
    EdgeWeightedGraph graph;

    private static class Node {
        private char c;
        private BusStop value;
        private Node left;
        private Node right;
        private Node middle;
        boolean isEnd;
        //BusStop stop;

        public Node() {
            this.left = null;
            this.middle = null;
            this.right = null;
        }

        public Node(char c) {
            this.left = null;
            this.middle = null;
            this.right = null;
            this.c = c;
        }


    }

    public TST(EdgeWeightedGraph graph) {
        this.graph = graph;
        createTST(graph.allBusStops);
        this.allBusStops = allBusStops;
    }

    private void createTST(HashMap<Integer, BusStop> allBusStops) {
        root = new Node();

        //int numStops = allBusStops.size();
        for (int i = graph.minStopNo; i <= graph.maxStopNo; i++) {
            if(allBusStops.get(i) != null)
            {
                BusStop currentStop = allBusStops.get(i);
                if (currentStop != null) { //redundant
                    String name = currentStop.stop_name;
                    System.out.println(currentStop.stop_id +  " Added stop " + name );
                    put(name, currentStop);
                }
            }
        }
    }

    public ArrayList<BusStop> contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        if (get(key) != null)
        {
            return get(key);
        }
        return null;
    }

    public ArrayList<BusStop> get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node x = get(root, key, 0);
        if (x == null) return null;

        possibleValues = new ArrayList<>();
        //searchResults = new HashSet<>();
        //searchResults.add(root.value);
        //findChildVals(root.middle);
        if (x.value != null) possibleValues.add((x.value));
        //findChildVals(x.middle);

        return possibleValues;
        //return searchResults;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        //if(d == key.length()-1)
        //{
          //  return x;
        //}
        char c = key.charAt(d);
        if (c < x.c) return get(x.left, key, d);
        else if (c > x.c) return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.middle, key, d + 1);
        else return x;
    }

    private void findChildVals(Node x) {
        if (x != null) {
            findChildVals(x.left);
            findChildVals(x.middle);
            findChildVals(x.right);

            if (x.value != null) {
                possibleValues.add(x.value);
                //searchResults.add(x.value);
            }
        }
    }


    public void put(String key, BusStop val) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (contains(key) != null) noNodes++;
        else if (val == null) noNodes--;       // delete existing key
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, BusStop val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        if (c < x.c) x.left = put(x.left, key, val, d);
        else if (c > x.c) x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1) x.middle = put(x.middle, key, val, d + 1);
        else x.value = val;
        return x;
    }

    /*
    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        Queue<String> queue = new Queue<String>();
        Node x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.value != null) queue.add(prefix);
        collect(x.middle, new StringBuilder(prefix), queue);
        return queue;
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left,  prefix, queue);
        if (x.value != null) queue.add(prefix.toString() + x.c);
        collect(x.middle,   prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, queue);
    }
     */

    public void printPossibleValues()
    {
        for(int i = 0; i < possibleValues.size(); i++)
        {
            possibleValues.get(i).tellMeAboutThisBusStop();
        }
    }

}









