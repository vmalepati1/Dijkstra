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
        int[][] permutations = {
                {1, 2, 3},
                {1, 3, 2},
                {2, 1, 3},
                {2, 3, 1},
                {3, 1, 2},
                {3, 2, 1}
        };

        double max = 0;

        for (int[] p : permutations) {
            int firstStop = customer[p[0] - 1].getNodeNumber();
            int secondStop = customer[p[1] - 1].getNodeNumber();
            int thirdStop = customer[p[2] - 1].getNodeNumber();

            double firstOrder = order[p[0] - 1];
            double secondOrder = order[p[1] - 1];
            double thirdOrder = order[p[2] - 1];

            int firstTrip = getShortestDistance(restaurant.getNodeNumber(), firstStop);
            int secondTrip = firstTrip + getShortestDistance(firstStop, secondStop);
            int thirdTrip = secondTrip + getShortestDistance(secondStop, thirdStop);

            double firstPercent = (slope * firstTrip + intercept) / 100.0;
            double secondPercent = (slope * secondTrip + intercept) / 100.0;
            double thirdPercent = (slope * thirdTrip + intercept) / 100.0;

            double current = firstPercent * firstOrder + secondPercent * secondOrder + thirdPercent * thirdOrder;

            if (current > max) {
                max = current;
            }
        }

        return max;
    }

}
