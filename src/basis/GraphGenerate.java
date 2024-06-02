package basis;

import java.io.*;
import java.util.Scanner;

/**
 * ClassName:basis.GraphGenerate
 * Package:PACKAGE_NAME
 * Description:
 *
 * @date:2024/5/13 17:15
 * @author:shyboy
 */
public class GraphGenerate {
    /**
     * 功能1：生成有向图
     * (对外使用)
     *
     * @param fileName
     * @return basis.Graph
     */
    public static Graph genGraph(String fileName) {
        GraphGenerate.filePreprocessing(fileName);
        return GraphGenerate.privateGenGraph(fileName + ".txt");
    }

    /**
     * 预处理文件 去除符号等
     *
     * @param fileName
     */
    private static void filePreprocessing(String fileName) {
        // 输入文件和输出文件路径
        String inputFilePath = fileName;
        String outputFilePath = fileName + ".txt";
        try {
            // 创建输入流
            FileReader fileReader = new FileReader(inputFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            // 创建输出流
            FileWriter fileWriter = new FileWriter(outputFilePath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            // 读取输入文件内容并处理
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // 将标点符号替换为空格，非字母字符忽略
                String processedLine1 = line.replaceAll("[^a-zA-Z ,.!$%:]", "");
                String processedLine2 = processedLine1.replaceAll("[,.!$%:]", " ");
                // 将处理后的行写入输出文件
                bufferedWriter.write(processedLine2);
                bufferedWriter.newLine(); // 写入换行符
            }
            // 关闭流
            bufferedReader.close();
            bufferedWriter.close();
            //System.out.println("文件处理完成。");
        } catch (IOException e) {
            System.err.println("文件处理出错: " + e.getMessage());
        }
    }

    /**
     * 根据改写后的文件路径生成有向图
     *
     * @param fileName
     * @return basis.Graph
     */
    private static Graph privateGenGraph(String fileName) {
        Graph graph = new Graph();
        try {
            Scanner scan = new Scanner(new FileInputStream(fileName));
            String preName = null;
            String curName = null;
            if (scan.hasNext()) {
                preName = scan.next();
            }
            if (preName != null) {
                graph.addVertex(preName);
            }
            while (scan.hasNext()) {
                curName = scan.next();
                if (curName != null) {
                    graph.addVertex(curName);
                    graph.addEdge(preName, curName);
                    preName = curName;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return graph;
    }
}


