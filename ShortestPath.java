import java.util.*;

public class ShortestPath {
    double distance[];
    boolean visited[];
    boolean fail = false;
    EdgeWeightedGraph graph;
    HashMap<Integer, ArrayList<EdgeJourney>> adjList;
    int[][] history;
    int destination_index;

    ShortestPath(EdgeWeightedGraph graph, int start_id)
    {
        this.graph = graph;
        this.adjList = graph.adjacencyList;

    }

    double getShortestDistBetween(int start_id, int destination_id)
    {
        HashMap<Integer, Double> shortestPaths = shortestDistBetween(start_id, destination_id);
        return shortestPaths.get(destination_id);
    }

    ArrayList<Integer> getStopSequenceForTrip(int start_id, int destination_id)
    {
        ArrayList<Integer> stops = new ArrayList<>();
        boolean complete = false;
        int prev_stop = destination_id;

        for(int i = destination_index; i >= 0 && !complete; i--)
        {
            if(history[1][i] == prev_stop)
            {
                stops.add(history[0][i]);
                prev_stop = history[0][i];
            }
            if(prev_stop == start_id)
            {
                complete = true;
            }
        }
        return stops;
    }

    double getPriceForTrip(int start_id, int destination_id)
    {
        double price = 0;
        return price;
    }







    /*
    public int minimumDistance(double[] distance, boolean[] visited)
    {
        double min = Double.MAX_VALUE;
        int index = -1;
        for(int i = 0; i < visited.length; i++)
        {
            if(!visited[i] && distance[i] <= min)
            {
                min = distance[i];
                index = i;
            }
        }
        return index;
    }

    public void relax(EdgeJourney edge, EdgeWeightedGraph graph)
    {
        int start = graph.findStopIndex(edge.from_stop_id);
        int destination = graph.findStopIndex(edge.to_stop_id);
        if(distance[destination] > distance[start] + edge.edgeWeight)
        {
            distance[destination] = distance[start] + edge.edgeWeight;
        }
    }
   */

    //this function returns the shortest distance array from the node passed in to every other node in the graph
    public HashMap<Integer, Double> shortestDistBetween(int start, int destination)
    {
        HashMap<Integer, Boolean> visited = new HashMap<>(graph.noVertices);
        //boolean[] visited = new boolean[graph.noVertices];
        HashMap<Integer, Double> distance = new HashMap<>(graph.noVertices);
        //this will record [from stop][to stop] visted
        history = new int [2][graph.noVertices];
        int count = 0;

        //all nodes are unvisited and have an infinite value
        for(int i = 0; i < distance.size(); i++)
        {
            visited.put(graph.allBusStops.get(i).stop_id, false);
            //these are lengthy but to explain, we're getting each bus stop and putting the distance to this bus stop as max value and bool as false
            distance.put(graph.allBusStops.get(i).stop_id, Double.MAX_VALUE);
        }

        //distance from a node to itself is 0
        distance.put(start, 0.0);

        //this priority queue stores the edges and is sorted by the distances
        PriorityQueue<EdgeJourney> queue2 = new PriorityQueue<EdgeJourney>(graph.noVertices, new Comparator<EdgeJourney>() {
            @Override
            public int compare(EdgeJourney edge1, EdgeJourney edge2) {
                if(edge1.edgeWeight < edge2.edgeWeight)
                {
                    return -1;
                }
                if(edge1.edgeWeight > edge2.edgeWeight)
                {
                    return 1;
                }
                return 0;
            }
        });

        //get the adjacency list for the starting node
        int from = start;
        List<EdgeJourney> edges = adjList.get(from);
        //add all the edges into the priority queue that is sorted by edge weighting
        for(int i = 0; edges != null && i < edges.size(); i++)
        {
            queue2.add(edges.get(i));
        }

        while(!queue2.isEmpty())
        {
            //take out the edge that has the smallest distance
            EdgeJourney currentEdge = queue2.poll();
            from = currentEdge.from_stop_id;
            int to = currentEdge.to_stop_id;

            //if we haven't visited already, mark as visited, check if the distance taken to get here is less than before
            if(!visited.get(to))
            {
                if(to == destination)
                {
                    destination_index = count;
                }
                history[0][count] = from;
                history[1][count] = to;
                visited.replace(to, true);
                if(distance.get(graph.allBusStops.get(from).stop_id) + currentEdge.edgeWeight < distance.get(graph.allBusStops.get(to).stop_id))
                {
                    distance.replace(graph.allBusStops.get(to).stop_id, distance.get(graph.allBusStops.get(from).stop_id) + currentEdge.edgeWeight);
                }

                //add all the new edges from the node we just visited to the priority queue
                from = to;
                edges = adjList.get(graph.allBusStops.get(from).stop_id);
                for(int i = 0; edges != null && i < edges.size() && from != start; i++)
                {
                    //if the edge goes to the starting node, don't take this edge
                    //if we've already visited this node, do not take this edge
                    if(edges.get(i).to_stop_id != start && !visited.get(edges.get(i).to_stop_id))
                    {
                        //get length from start node to the node
                        double soFar = distance.get(graph.allBusStops.get(from).stop_id);
                        soFar += edges.get(i).edgeWeight;
                        EdgeJourney edgeForPQ = new EdgeJourney(start, edges.get(i).to_stop_id, 1, soFar);
                        queue2.add(edgeForPQ);
                    }
                }
            }
        }
        return distance;
    }
}
