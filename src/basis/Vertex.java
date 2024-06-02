package basis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/**
 * ClassName:basis.Vertex
 * Package:PACKAGE_NAME
 * Description:
 *
 * @date:2024/5/13 16:38
 * @author:shyboy
 */
public class Vertex {
    private String name;
    private HashSet<Vertex> preVSet;
    private HashSet<Vertex> nextVSet;
    public HashMap<Vertex,Integer> weight;

    public Vertex() {
        this.name = null;
        this.preVSet = new HashSet<>();
        this.nextVSet = new HashSet<>();
        this.weight = new HashMap<>();
    }

    public Vertex(String name) {
        this.name = name;
        this.preVSet = new HashSet<>();
        this.nextVSet = new HashSet<>();
        this.weight = new HashMap<>();
    }

    public Vertex(String name, HashSet<Vertex> preVSet, HashSet<Vertex> nextVSet, HashMap<Vertex, Integer> weight) {
        this.name = name;
        this.preVSet = preVSet;
        this.nextVSet = nextVSet;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashSet<Vertex> getPreVSet() {
        return preVSet;
    }

    public void setPreVSet(HashSet<Vertex> preVSet) {
        this.preVSet = preVSet;
    }

    public HashSet<Vertex> getNextVSet() {
        return nextVSet;
    }

    public void setNextVSet(HashSet<Vertex> nextVSet) {
        this.nextVSet = nextVSet;
    }

    public HashMap<Vertex, Integer> getWeight() {
        return weight;
    }

    public void setWeight(HashMap<Vertex, Integer> weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return Objects.equals(name, vertex.name) && Objects.equals(preVSet, vertex.preVSet) && Objects.equals(nextVSet, vertex.nextVSet) && Objects.equals(weight, vertex.weight);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
