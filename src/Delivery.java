import java.util.*;
import java.util.stream.Collectors;


public class Delivery {
    private DijGraph westLafayette;//The graph
    private Node restaurant;//The vertex that the driver start
    private Node[] customer;//The vertices that the driver need to pass through
    private double slope;//Tip percentage function slope
    private double intercept;//Tip percentage function intercept
    private double [] order;//The order amount from each customer
    public Delivery (DijGraph graph,Node restaurant, Node[] customer, double slope, double intercept, double[] order){
        this.westLafayette = graph;
        this.restaurant = restaurant;
        this.customer = customer;
        this.slope = slope;
        this.intercept  = intercept;
        this.order = order;
    }

    private int getShortestDistance(int i, int j) {
        Dist[] dists = westLafayette.dijkstra(i);
        return dists[j].getDist();
    }

    public static int find(Node[] a, String locationNmae)
    {
        for (int i = 0; i < a.length; i++)
            if (a[i].getLocation().equals(locationNmae))
                return i;

        return -1;
    }

    //Finding the best path that the driver can earn most tips
    //Each time the driver only picks up three orders
    //Picking up N orders and find the maximum tips will be NP-hard
    public double bestPath(){
        int n = westLafayette.getNodeCount() + 1;
        int[][] d = new int[n][n];

        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {
                d[i][j] = Integer.MAX_VALUE;
            }
        }

        for (int k = 1; k < n; k++) {
            for (int i = 1; i < n; i++) {
                for (int j = 1; j < n; j++) {
                    d[i][j] = Math.min(getShortestDistance(i, j),
                            getShortestDistance(i, k) + getShortestDistance(k, j));
                }
            }
        }

        int shortest = Integer.MAX_VALUE;
        List<Integer> winningSequence = null;

        List<Integer> l = Arrays.stream(customer).map(Node::getNodeNumber).collect(Collectors.toList());

        PermutationIterable<Integer> permutationIterable1 = new PermutationIterable<Integer>(l, new IndicesWalker<Integer>(l.size()));

        for (List<Integer> p : permutationIterable1) {
            int current = d[restaurant.getNodeNumber()][p.get(0)] + d[p.get(0)][p.get(1)] + d[p.get(1)][p.get(2)];

            if (current < shortest) {
                shortest = current;
                winningSequence = new ArrayList<>(p);
            }
        }

        assert winningSequence != null;

        int firstStop = winningSequence.get(0);
        int secondStop = winningSequence.get(1);
        int thirdStop = winningSequence.get(2);

        double firstOrder = order[find(customer, westLafayette.getNodeArr()[firstStop].getLocation())];
        double secondOrder = order[find(customer, westLafayette.getNodeArr()[secondStop].getLocation())];
        double thirdOrder = order[find(customer, westLafayette.getNodeArr()[thirdStop].getLocation())];

        int firstTrip = getShortestDistance(restaurant.getNodeNumber(), winningSequence.get(0));
        int secondTrip = getShortestDistance(winningSequence.get(0), winningSequence.get(1));
        int thirdTrip = getShortestDistance(winningSequence.get(1), winningSequence.get(2));

        double firstPercent = (slope * firstTrip + intercept) / 100;
        double secondPercent = (slope * secondTrip + intercept) / 100;
        double thirdPercent = (slope * thirdTrip + intercept) / 100;

        return firstPercent * firstOrder + secondPercent * secondOrder + thirdPercent * thirdOrder;
    }

}
