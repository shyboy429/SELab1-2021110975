package basis;

import java.util.*;

/**
 * ClassName:shortestPath
 * Package:PACKAGE_NAME
 * Description:
 *
 * @date:2024/5/13 21:32
 * @author:shyboy
 */
public class ShortestPath {
    private Graph graph;

    public ShortestPath() {
        this.graph = new Graph();
    }

    public ShortestPath(Graph graph) {
        this.graph = graph;
    }

    /**
     * 计算最短路径
     *
     * @param word1
     * @param word2
     * @return String
     */
    public String calcShortestPath(String word1, String word2) {
        ArrayList<Vertex> vertices = graph.getVertices();
        if (word1 == null && word2 == null) {
            return "无效的起点和终点";
        } else if (word1 == null) {
            StringBuilder word2ToAllPath = new StringBuilder();
            for (Vertex v : vertices) {
                if (!v.getName().equals(word2)) {
                    String word2ToVPath = calcShortestPath(word2, v.getName());
                    word2ToAllPath.append("To ").append(v.getName()).append(" : ").append("\n").append(word2ToVPath).append("\n\n");
                }
            }
            word2ToAllPath.delete(word2ToAllPath.length() - 2, word2ToAllPath.length());
            return word2ToAllPath.toString();
        } else if (word2 == null) {
            StringBuilder word1ToAllPath = new StringBuilder();
            for (Vertex v : vertices) {
                if (!v.getName().equals(word1)) {
                    String word1ToVPath = calcShortestPath(word1, v.getName());
                    word1ToAllPath.append("To ").append(v.getName()).append(" : ").append("\n").append(word1ToVPath).append("\n\n");
                }
            }
            word1ToAllPath.delete(word1ToAllPath.length() - 2, word1ToAllPath.length());
            return word1ToAllPath.toString();
        }
        // 获取起点和终点节点对象
        Vertex start = getVertexByName(vertices, word1);
        Vertex end = getVertexByName(vertices, word2);
        // Dijkstra 算法初始化
        Map<Vertex, Integer> dist = new HashMap<>();
        Map<Vertex, List<Vertex>> prevs = new HashMap<>();
        PriorityQueue<Vertex> pq = new PriorityQueue<>((a, b) -> dist.get(a) - dist.get(b));
        for (Vertex v : vertices) {
            dist.put(v, Integer.MAX_VALUE);
            prevs.put(v, new ArrayList<>());
        }
        dist.put(start, 0);
        pq.offer(start);

        // Dijkstra 算法
        while (!pq.isEmpty()) {
            // 从优先队列中取出距离最小的节点
            Vertex u = pq.poll();
            // 如果取出的节点是目标节点，跳出循环
            if (u.equals(end)) {
                break;
            }
            // 遍历当前节点的相邻节点
            for (Vertex v : u.getNextVSet()) {
                // 计算经当前节点到相邻节点的距离
                int alt = dist.get(u) + u.weight.get(v);
                // 如果新计算的距离比原先记录的距离小
                if (alt < dist.get(v)) {
                    // 更新最短距离
                    dist.put(v, alt);
                    // 更新前驱节点列表为当前节点
                    prevs.get(v).clear();
                    prevs.get(v).add(u);
                    // 将相邻节点加入优先队列
                    pq.offer(v);
                }
                // 如果新计算的距离与原先记录的距离相等
                else if (alt == dist.get(v)) {
                    // 将当前节点加入前驱节点列表
                    prevs.get(v).add(u);
                }
            }
        }
        //构建所有最短路径字符串
        Set<String> paths = new HashSet<>();
        buildShortestPaths(start, end, new Stack<>(), prevs, paths);
        Set<String> reversedPaths = reversePathStrings(paths);
        return String.join("\n", reversedPaths);
    }

    /**
     * 递归构建最短路径
     *
     * @param start 起始节点
     * @param end   目标节点
     * @param path  当前路径
     * @param prevs 前驱节点列表映射
     * @param paths 最短路径集合
     */
    private void buildShortestPaths(Vertex start, Vertex end, Stack<Vertex> path, Map<Vertex, List<Vertex>> prevs,
                                    Set<String> paths) {
        // 将当前节点加入路径
        path.push(end);
        // 如果当前节点是起始节点，则构建完整路径并加入最短路径集合
        if (start.equals(end)) {
            StringBuilder sb = new StringBuilder();
            for (Vertex v : path) {
                sb.append(v.getName());
                if (v != path.peek()) {
                    sb.append("->");
                }
            }
            paths.add(sb.toString());
        } else {
            // 遍历当前节点的前驱节点列表，递归构建路径
            for (Vertex prev : prevs.get(end)) {
                buildShortestPaths(start, prev, path, prevs, paths);
            }
        }
        // 回溯，将当前节点弹出路径
        path.pop();
    }

    // 根据节点名称获取节点对象的辅助方法
    private Vertex getVertexByName(ArrayList<Vertex> vertices, String name) {
        for (Vertex v : vertices) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    public static Set<String> reversePathStrings(Set<String> paths) {
        Set<String> reversedPaths = new HashSet<>();
        for (String path : paths) {
            String[] nodes = path.split("->");
            StringBuilder sb = new StringBuilder();
            for (int i = nodes.length - 1; i >= 0; i--) {
                sb.append(nodes[i]);
                if (i > 0) {
                    sb.append("->");
                }
            }
            reversedPaths.add(sb.toString());
        }
        return reversedPaths;
    }
}
