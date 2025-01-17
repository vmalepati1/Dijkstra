import java.io.File;
import java.io.IOException;
import java.util.*;

public class Vertex {
    private Vertex parent;
    private int size;
    private int vertextNum;

    public Vertex(int number){
        this.vertextNum = number;
        this.parent = this;
        this.size = 1;
    }

    public int getVertexNumber(){
        return vertextNum;
    }

    public Vertex getParent(){
        return parent;
    }

    public void updateParent(Vertex callMeDad){
        parent = callMeDad;
    }

    public int getSize(){
        return size;
    }
    public void updateSize(int newSize){
        size = newSize;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        if (!(object instanceof Vertex)) return false;
        Vertex vec = (Vertex) object;
        return vec.vertextNum == vertextNum;
    }

}
