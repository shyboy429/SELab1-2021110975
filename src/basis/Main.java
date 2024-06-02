package basis;


import java.io.Console;

/**
 * ClassName:basis.Main
 * Package:PACKAGE_NAME
 * Description:
 *
 * @date:2024/5/13 21:00
 * @author:shyboy
 */
public class Main {
    public static void main(String[] args) {
        //功能1测试 生成有向图
        Graph graph = GraphGenerate.genGraph("t.txt");

        //功能2测试 展示有向图
//        new GraphVisualizer(graph,null);

        //功能3测试 查询桥接词
//        Bridge bridgeTest = new Bridge(graph);
////        String queryBridgeWordsRes = bridgeTest.queryBridgeWords("I", "BOY");
////        System.out.println(queryBridgeWordsRes);
////
//        //功能4测试 生成新文本
//        String generateNewTextRes = bridgeTest.generateNewText("I BOY");
//        System.out.println(generateNewTextRes);
//
//        //功能5测试 寻找最短路径
//        ShortestPath shortestPathTest = new ShortestPath(graph);
//        String shortestPathTestRes = shortestPathTest.calcShortestPath("Process", null);
//        System.out.println(shortestPathTestRes);
//
//        // 功能6测试 随机游走
//        RandomWalk walker = new RandomWalk(graph, console);
//        String randomWalkRes = walker.randomWalk();
//        walker.writeRandomWalkPathToFile(randomWalkRes);
    }
}
