package basis;

import java.util.ArrayList;

/**
 * ClassName:basis.Bridge
 * Package:PACKAGE_NAME
 * Description:
 *
 * @date:2024/5/13 20:36
 * @author:shyboy
 */
public class Bridge {
    private Graph graph;

    public Bridge() {
        this.graph = new Graph();
    }

    public Bridge(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public String queryBridgeWords(String word1, String word2) {
        ArrayList<Vertex> vertices = graph.getVertices();
        boolean word1Found = false;
        boolean word2Found = false;
        // 检查 word1 和 word2 是否存在于图中
        for (Vertex v : vertices) {
            if (v.getName().equals(word1)) {
                word1Found = true;
            }
            if (v.getName().equals(word2)) {
                word2Found = true;
            }
        }
        // 如果 word1 或 word2 不存在于图中，则返回相应消息
        if (!word1Found && !word2Found) {
            return "No \"" + word1 + "\" and \"" + word2 + "\" in the graph!";
        } else if (!word1Found) {
            return "No \"" + word1 + "\" in the graph!";
        } else if (!word2Found) {
            return "No \"" + word2 + "\" in the graph!";
        }
        // 检查是否存在桥接词
        ArrayList<String> bridgeWords = new ArrayList<>();
        for (Vertex v : vertices) {
            for (Vertex preVertex : v.getPreVSet()) {
                if (preVertex.getName().equals(word1)) {
                    for (Vertex nextVertex : v.getNextVSet()) {
                        if (nextVertex.getName().equals(word2)) {
                            bridgeWords.add(v.getName());
                        }
                    }
                }
            }
        }
        // 如果不存在桥接词，则返回相应消息
        if (bridgeWords.isEmpty()) {
            return "No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!";
        }
        // 构建输出字符串
        StringBuilder result = new StringBuilder("The bridge words from \"" + word1 + "\" to \"" + word2 + "\" are: " +
                "\"");
        for (int i = 0; i < bridgeWords.size(); i++) {
            result.append(bridgeWords.get(i));
            if (i < bridgeWords.size() - 1) {
                result.append("\", \"");
            }
        }
        result.append("\".");
        return result.toString();
    }

    private String privateGenerateNewText(String inputText) {
        String[] words = inputText.split("\\s+"); // 分割输入文本中的单词
        StringBuilder newText = new StringBuilder();
        // 遍历输入文本的单词
        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];
            newText.append(word1); // 将当前单词添加到新文本中
            // 检查 word1 和 word2 之间是否存在桥接词
            String bridgeWord = privateQueryBridgeWords(word1, word2);
            if (bridgeWord != null) {
                // 如果存在桥接词，将其插入到新文本中
                newText.append(" ").append(bridgeWord).append(" ");
            } else {
                // 如果不存在桥接词，在当前单词后面添加一个空格
                newText.append(" ");
            }
        }
        // 将最后一个单词添加到新文本中
        newText.append(words[words.length - 1]);
        return newText.toString();
    }
    public String generateNewText(String inputText) {
        String t1 = privateGenerateNewText(inputText);
        String t2 = privateGenerateNewText(t1);
        while(!t1.equals(t2)){
            t1 = t2;
            t2 = privateGenerateNewText(t1);
        }
        return t1;
    }
    private String privateQueryBridgeWords(String word1, String word2) {
        ArrayList<Vertex> vertices = graph.getVertices();
        // 检查是否存在桥接词
        for (Vertex v : vertices) {
            for (Vertex preVertex : v.getPreVSet()) {
                if (preVertex.getName().equals(word1)) {
                    for (Vertex nextVertex : v.getNextVSet()) {
                        if (nextVertex.getName().equals(word2)) {
                            return v.getName();
                        }
                    }
                }
            }
        }
        return null;
    }
}
