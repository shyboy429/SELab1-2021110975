package basis;

import java.util.ArrayList;

/**
 * ClassName:basis.Graph
 * Package:PACKAGE_NAME
 * Description:
 *
 * @date:2024/5/13 16:48
 * @author:shyboy
 */
public class Graph {
    private ArrayList<Vertex> vertices;

    public Graph() {
        this.vertices = new ArrayList<>();
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
    }

    public void addVertex(String name) {
        for (Vertex v : vertices) {
            if (v.getName().equals(name)) {
                return;
            }
        }
        vertices.add(new Vertex(name));
    }

    public void addEdge(String preName, String curName) {
        Vertex pre = new Vertex();
        Vertex cur = new Vertex();
        for (Vertex v : vertices) {
            if (v.getName().equals(preName)) {
                pre = v;
            }
            if (v.getName().equals(curName)) {
                cur = v;
            }
        }
        if (pre.getNextVSet().contains(cur)) {
            int weight = pre.weight.get(cur);
            pre.weight.replace(cur, 1 + weight);
        } else {
            pre.getNextVSet().add(cur);
            cur.getPreVSet().add(pre);
            pre.getWeight().put(cur, 1);
        }
    }

    @Override
    public String toString() {
        String result = "";
        for (Vertex v : vertices) {
            result += v.toString() + ": " + v.getNextVSet().toString() + "\n";
        }
        return result;
    }

    @Override
    public int hashCode() {
        int code = 0;
        for (Vertex v : vertices) {
            code += v.hashCode();
        }
        return code;
    }
}
