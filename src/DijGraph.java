import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DijGraph {

    static int MAXWEIGHT = 10000000;//The weight of edge will not exceed this number
    private Node[] nodeArr;//The vertices set in the graph
    private int nodeCount;//number of total vertices
    private int edgeCount;//number of total edges

    //Two option for the DijGraph constructor
    //Option 0 is used to build graph with for part 1: implementation for Dijkstra
    //Option 1 is used to build graph with for part 2: simple application of Dijkstra
    public DijGraph(String graph_file, int option)throws IOException{
        if (option == 0){
            File file = new File(graph_file);
            Scanner sc = new Scanner(file);
            nodeCount = sc.nextInt();
            edgeCount = sc.nextInt();
            nodeArr = new Node[nodeCount + 1];
            for(int i =0; i < nodeCount + 1; i ++){
                if(i != 0) {
                    nodeArr[i] = new Node(i);
                }
            }
            for(int i = 0;i < edgeCount; i ++){
                int begin = sc.nextInt();
                int end = sc.nextInt();
                int weight = sc.nextInt();
                nodeArr[begin].addEdge(end, weight);
                nodeArr[end].addEdge(begin,weight);
            }
        }
        else if (option == 1){
            File file = new File(graph_file);
            Scanner sc = new Scanner(file);
            nodeCount = sc.nextInt();
            edgeCount = sc.nextInt();
            nodeArr = new Node[nodeCount + 1];
            for(int i =0; i < nodeCount + 1; i ++){
                if(i != 0){
                    nodeArr[i]= new Node(i, sc.next());
                }
            }
            for(int i = 0;i < edgeCount; i ++){
                String begin = sc.next();
                String end = sc.next();
                int weight = sc.nextInt();
                Node beginNode = findByName(begin);
                Node endNode = findByName(end);
                beginNode.addEdge(endNode.getNodeNumber(), weight);
                endNode.addEdge(beginNode.getNodeNumber(),weight);
            }
        }

    }

    // Finding the single source shortest distances by implementing dijkstra.
    // Using min heap to find the next smallest target
    public  Dist[] dijkstra(int source) {
        int V = nodeCount;
        int[] dist = new int[V];
        Dist[] result = new Dist[V + 1];

        Dist[] heap = new Dist[V];
        int size = 0;

        for (int v = 0; v < V; ++v) {
            dist[v] = Integer.MAX_VALUE;
            heap[v] = new Dist(v, dist[v]);
        }

        source--;

        // Make dist value of src vertex as 0 so that it is extracted first
        heap[source] = new Dist(source, dist[source]);
        dist[source] = 0;
        decreaseKey(heap, source, dist[source]);

        // Initially size of min heap is equal to V
        size = V;

        // In the following loop, min heap contains all nodes
        // whose shortest distance is not yet finalized.
        while (size > 0)
        {
            // Extract the vertex with minimum distance value
            Dist minHeapNode = extractMin(heap, size);
            size--;

            int u = minHeapNode.getNodeNumber(); // Store the extracted vertex number

            // Traverse through all adjacent vertices of u (the extracted
            // vertex) and update their distance values

            for (Map.Entry<Integer, Integer> integerIntegerEntry : nodeArr[u + 1].getEdges().entrySet()) {
                int v = (int) ((Map.Entry) integerIntegerEntry).getKey() - 1;
                int weight = (int) ((Map.Entry) integerIntegerEntry).getValue();

                // If shortest distance to v is not finalized yet, and distance to v
                // through u is less than its previously calculated distance
                if (find(heap, v) < size && dist[u] != Integer.MAX_VALUE &&
                        weight + dist[u] < dist[v]) {
                    dist[v] = dist[u] + weight;

                    // update distance value in min heap also
                    decreaseKey(heap, v, dist[v]);
                }
            }
        }

        for (int i = 1; i < V + 1; i++) {
            result[i] = new Dist(i, dist[i - 1]);
        }

        return result;
    }

    //Find the vertex by the location name
    public Node findByName(String name){
        for (int x =1; x < nodeCount + 1; x++){
            if(nodeArr[x].getLocation().equals(name)){
                return nodeArr[x];
            }
        }
        return null;
    }

    // Generic function to find the index of an element in an object array
    private static int find(Dist[] a, int v)
    {
        for (int i = 0; i < a.length; i++)
            if (a[i].getNodeNumber() == v)
                return i;

        return -1;
    }

    private static void swimUp(Dist[] array, int i) {
        // Swim up while the complete tree is not heapified
        while (i > 0 && array[(i - 1) / 2] != null && array[i].getDist() < array[(i - 1) / 2].getDist()) {
            // Swap this node with its parent
            swap(array, i, (i - 1) / 2);

            // move to parent index
            i = (i - 1) / 2;
        }
    }

    //Implement insertion in min heap
    //first insert the element to the end of the heap
    //then swim up the element if necessary
    //Set it as static as always
    public static void insert(Dist[] arr, Dist value, int index){
        if (index >= arr.length) {
            return;
        }

        arr[index] = value;
        swimUp(arr, index);
    }

    public static void swap(Dist[] arr, int index1, int index2){
        Dist temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;
    }

    private static void minHeapify(Dist[] array, int idx, int size) {
        int smallest, left, right;
        smallest = idx;
        left = 2 * idx + 1;
        right = 2 * idx + 2;

        if (left < size && array[left].getDist() < array[smallest].getDist())
            smallest = left;

        if (right < size && array[right].getDist() < array[smallest].getDist())
            smallest = right;

        if (smallest != idx) {
            // The nodes to be swapped in min heap
            Dist smallestNode = array[smallest];
            Dist idxNode = array[idx];

            // Swap nodes
            swap(array, smallest, idx);

            minHeapify(array, smallest, size);
        }
    }

    //Extract the minimum element in the min heap
    //replace the last element with the root
    //then do minheapify
    //Set it as static as always
    public static Dist extractMin(Dist[] arr, int size) {
        if (size == 0) {
            return null;
        }

        // Store the root node
        Dist root = arr[0];

        // Replace root node with last node
        Dist lastNode = arr[size - 1];
        arr[0] = lastNode;

        // Reduce heap size and heapify root
        --size;
        minHeapify(arr, 0, size);

        return root;
    }

    private static void decreaseKey(Dist[] array, int v, int dist) {
        // Get the index of v in heap array
        int i = find(array, v);

        // Get the node and update its dist value
        array[i].updateDist(dist);

        swimUp(array, i);
    }

    //This will print the shortest distance result
    //The output format will be what we expect to pass the test cases
    public static void printResult(Dist[] result, int source){
        for(int x = 1;  x < result.length; x++){
            if(x != source){
                System.out.println(result[x].getNodeNumber() + " " +result[x].getDist());
            }
        }
    }

    public static void main(String[] args)throws IOException {
        DijGraph graph = new DijGraph("C:\\Users\\Vikas Malepati\\Documents\\GitHub\\Dijkstra\\src\\localtest1.txt", 0);
        Dist[] result  = graph.dijkstra(1);
        printResult(result, 1);
    }
}
