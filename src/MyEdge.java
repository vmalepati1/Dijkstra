import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.*;

public class MyEdge implements Comparable<MyEdge> {
    private int source;
    private int destination;
    private int weight;

    public MyEdge(int source, int destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public int getS(){
        return source;
    }
    public int getD(){
        return  destination;
    }

    public int getWeight(){
        return  weight;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(MyEdge o) {
        return Integer.compare(this.weight, o.weight);
    }

}
