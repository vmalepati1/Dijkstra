import java.io.File;
import java.io.IOException;
import java.util.*;

public class KruGraph {
    private Vertex[] vertexArr;
    private ArrayList<MyEdge> edgeArr;
    private int vertexCount;
    private int edgeCount;

    //Implement the constructor for KruGraph
    //The format of the input file is the same as the format of the input file in Dijkstra
    public KruGraph(String graph_file)throws IOException {
        File file = new File(graph_file);
        Scanner sc = new Scanner(file);
        vertexCount = sc.nextInt();
        edgeCount = sc.nextInt();
        edgeArr = new ArrayList<>();
        vertexArr = new Vertex[vertexCount];

        for(int i = 0; i < edgeCount; i ++){
            int begin = sc.nextInt() - 1;
            int end = sc.nextInt() - 1;
            int weight = sc.nextInt();
            addEgde(begin, end, weight);
        }
    }

    //Could be a helper function
    private void addEgde(int from, int to, int weight){
        edgeArr.add(new MyEdge(from, to, weight));
    }

    //Implement Kruskal with weighted union find algorithm
    public PriorityQueue<MyEdge> kruskalMST() {
        PriorityQueue<MyEdge> result = new PriorityQueue<>();

        Collections.sort(edgeArr);

        int i = 0;

        for (int v = 0; v < vertexCount; v++) {
            Vertex vertex = new Vertex(v);
            vertexArr[v] = vertex;
            vertexArr[v].updateParent(vertex);
        }

        while (result.size() < vertexCount - 1) {
            MyEdge next_edge = edgeArr.get(i++);

            Vertex x = find(vertexArr[next_edge.getS()]);
            Vertex y = find(vertexArr[next_edge.getD()]);

            // If including this edge does't cause cycle,
            // include it in result and increment the index
            // of result for next edge
            if (!(x.equals(y)))
            {
                result.add(next_edge);
                union(x, y);
            }
            // Else discard the next_edge
        }

        for (MyEdge e : result) {
            e.setSource(e.getS() + 1);
            e.setDestination(e.getD() + 1);
        }

        return result;
    }

    //Implement the recursion trick for the leaves to update the parent efficiently
    //Set it as static as always
    public static Vertex find(Vertex x){
        if (!(x.getParent().equals(x))) {
            x.updateParent(find(x.getParent()));
        }

        return x.getParent();
    }


    //This function should union two vertices when an edge is added to the MST
    //Return true when the edge can be picked in the MST
    //Otherwise return false
    //Set it as static as always
    public static void union(Vertex x, Vertex y){
        Vertex xroot = find(x);
        Vertex yroot = find(y);

        // Attach smaller rank tree under root of high rank tree
        // (Union by Rank)
        if (xroot.getSize() < yroot.getSize()) {
            xroot.updateParent(yroot);
            yroot.updateSize(yroot.getSize() + xroot.getSize());
        } else if (xroot.getSize() > yroot.getSize()) {
            yroot.updateParent(xroot);
            xroot.updateSize(xroot.getSize() + yroot.getSize());
            // If ranks are same, then make one as root and increment
            // its rank by one
        } else {
            xroot.updateParent(yroot);
            yroot.updateSize(yroot.getSize() + xroot.getSize());
        }
    }

    //This is what we expect for the output format
    //The test cases will follow this format
    public static void printGraph(PriorityQueue<MyEdge> edgeList){
        int turn = edgeList.size();
        for (int i = 0; i < turn; i++) {
            MyEdge edge = edgeList.poll();
            int source = edge.getS();
            int dest = edge.getD();
            if(source > dest){
                int temp = source;
                source = dest;
                dest = temp;
            }
            System.out.println("from: " + source + " to: " + dest + " weight: " + edge.getWeight());
        }
    }

    public static void main(String[] args) throws IOException {
        KruGraph graph = new KruGraph("C:\\Users\\Vikas Malepati\\Documents\\GitHub\\Dijkstra\\src\\localtest1.txt");
        printGraph(graph.kruskalMST());
    }

}
