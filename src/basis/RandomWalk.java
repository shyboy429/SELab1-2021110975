package basis;

import javafx.scene.control.TextArea;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class RandomWalk extends JFrame {
    private ArrayList<Vertex> vertices;  // 有向图的顶点列表
    private boolean stopped;             // 用于标注是否停止
    private TextArea console;
    private boolean suspended;           // 用于标注线程是否暂停
    private StringBuilder result;        // 存储随机游走结果的字符串构造器

    public RandomWalk(Graph graph, TextArea console) {
        this.vertices = graph.getVertices();
        this.stopped = false;
        this.suspended = false;
        this.result = new StringBuilder();
        this.console = console;
        setTitle("Random Walk Control");

        // 创建继续按钮
        JButton continueButton = new JButton("继续");
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resumeWalk();
            }
        });
        // 创建暂停按钮
        JButton stopButton = new JButton("暂停");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suspendWalk();
            }
        });

        // 美化按钮
        continueButton.setBackground(new Color(20, 196, 254)); // 豆沙绿背景
        continueButton.setForeground(Color.BLACK); // 白色文字
        continueButton.setFocusPainted(false); // 去除按钮焦点框
        continueButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // 设置按钮内边距
        continueButton.setPreferredSize(new Dimension(200, 80));

        stopButton.setBackground(new Color(199, 236, 204)); // 豆沙绿背景
        stopButton.setForeground(Color.BLACK); // 白色文字
        stopButton.setFocusPainted(false); // 去除按钮焦点框
        stopButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // 设置按钮内边距
        stopButton.setPreferredSize(new Dimension(200, 80));

        // 设置布局并添加按钮
        setLayout(new BorderLayout());
        add(stopButton, BorderLayout.SOUTH);
        add(continueButton, BorderLayout.NORTH);
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 关闭窗口时只关闭当前窗口，不退出整个应用程序
        setLocationRelativeTo(null); // 将窗口设置在屏幕中央
        setVisible(true);
    }

    /**
     * 线程暂停
     */
    public synchronized void suspendWalk() {
        suspended = true;
    }

    /**
     * 线程继续
     */
    public synchronized void resumeWalk() {
        suspended = false;
        notify();
    }

    public String randomWalk() {
        if (vertices == null || vertices.isEmpty()) {
            return "The vertex list is empty or null.";
        }
        HashMap<Vertex, HashSet<Vertex>> walkedVertices = new HashMap<>();
        for (Vertex v : vertices) {
            walkedVertices.put(v, new HashSet<>());
        }
        Vertex pre = vertices.get(new Random().nextInt(vertices.size()));
        Vertex next;
        result.append(pre.getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("随机游走开始:\n" + pre.getName());

        while (!stopped) {
            synchronized (this) {
                while (suspended) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                Thread.sleep(1000);  // 模拟每步的时间间隔
            } catch (InterruptedException e) {
                e.printStackTrace();
                return result.toString();
            }

            next = randomSelect(pre.getNextVSet());
            if (next == null) {
                result.append("\n当前节点无后继结点可游走！");
                System.out.print("\n当前节点无后继结点可游走！");
                break;
            }
            if (!suspended) {
                result.append("->").append(next.getName());
                System.out.print("->" + next.getName());
                if (walkedVertices.get(pre).contains(next)) {
                    result.append("\n游走经过重复边,停止!");
                    System.out.print("\n游走经过重复边,停止!");
                    break;
                }
                walkedVertices.get(pre).add(next);
                pre = next;
            }
        }
        console.setText(result.toString());
        return result.toString();
    }

    public Vertex randomSelect(Set<Vertex> set) {
        if (set == null || set.size() == 0) {
            return null;
        }
        int size = set.size();
        int random = new Random().nextInt(size);
        ArrayList<Vertex> list = new ArrayList<>(set);
        return list.get(random);
    }

    public void writeRandomWalkPathToFile(String randomWalkPath) {
        // 获取当前项目的默认目录
        String currentDirectory = System.getProperty("user.dir");
        // 创建path.txt文件路径
        File file = new File(currentDirectory, "path.txt");
        // 使用BufferedWriter写入文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(randomWalkPath);
            System.out.println("\n写入磁盘成功！路径: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


//package basis; /**
// * ClassName:basis.RandomWalk
// * Package:PACKAGE_NAME
// * Description:
// *
// * @date:2024/5/14 10:38
// * @author:shyboy
// */
//
//import java.awt.*;
//import java.awt.event.*;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Random;
//import java.util.Scanner;
//import java.util.Set;
//import javax.swing.JFrame;
//import javax.swing.*;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.util.*;
//
//public class RandomWalk extends JFrame {
//    private ArrayList<Vertex> vertices;  // 有向图的顶点列表
//    private boolean stopped;             // 用于标注是否停止
//    private boolean suspended;            //用于标注线程是否暂停
//    private StringBuilder result;        // 存储随机游走结果的字符串构造器
//
//    private int isOver = 0;
//
//    public RandomWalk(Graph graph) {
//        this.vertices = graph.getVertices();
//        this.stopped = false;
//        this.result = new StringBuilder();
//        setTitle("Random Walk");
//
//        // 创建继续按钮
//        JButton continueButton = new JButton("继续");
//        continueButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                resume();
//            }
//        });
//        // 创建暂停按钮
//        JButton stopButton = new JButton("暂停");
//        stopButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                suspend();
//            }
//        });
//
//        // 美化按钮
//        continueButton.setBackground(new Color(20, 196, 254)); // 豆沙绿背景
//        continueButton.setForeground(Color.BLACK); // 白色文字
//        continueButton.setFocusPainted(false); // 去除按钮焦点框
//        continueButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // 设置按钮内边距
//        continueButton.setPreferredSize(new Dimension(200, 80));
//
//        stopButton.setBackground(new Color(199, 236, 204)); // 豆沙绿背景
//        stopButton.setForeground(Color.BLACK); // 白色文字
//        stopButton.setFocusPainted(false); // 去除按钮焦点框
//        stopButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // 设置按钮内边距
//        stopButton.setPreferredSize(new Dimension(200, 80));
//        // 设置布局并添加按钮
//        setLayout(new BorderLayout());
//        add(stopButton, BorderLayout.SOUTH);
//        add(continueButton, BorderLayout.NORTH);
//        setSize(300, 200);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 关闭窗口时只关闭当前窗口，不退出整个应用程序
//        setVisible(true);
//    }
//
//    public void stop() {
//        this.stopped = true;
//    }
//
//    /**
//     * 线程暂停
//     */
//    public void suspend() {
//        suspended = true;
//    }
//
//    /**
//     * 线程继续
//     */
//    public synchronized void resume() {
//        suspended = false;
//        notify();
//    }
//
//    public String randomWalk() {
//        if (vertices == null || vertices.isEmpty()) {
//            return "The vertex list is empty or null.";
//        }
//        HashMap<Vertex, HashSet<Vertex>> walkedVertices = new HashMap<>();
//        for (Vertex v : vertices) {
//            walkedVertices.put(v, new HashSet<>());
//        }
//        Vertex pre = vertices.get(new Random().nextInt(vertices.size()));
//        Vertex next;
//        result.append(pre.getName());
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.print("随机游走开始:\n" + pre.getName());
//        while (!stopped) {
//            try {
//                Thread.sleep(1000);  // 模拟每步的时间间隔
//                while (suspended) {
//                    wait();
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                return result.toString();
//            }
//
//            next = randomSelect(pre.getNextVSet());
//            if (next == null) {
//                result.append("\n当前节点无后继结点可游走！");
//                System.out.println("\n当前节点无后继结点可游走！");
//                break;
//            }
//            if (!stopped) {
//                result.append("->").append(next.getName());
//                System.out.print("->" + next.getName());
//                if (walkedVertices.get(pre).contains(next)) {
//                    result.append("\n游走经过重复边,停止!");
//                    System.out.println("\n游走经过重复边,停止!");
//                    break;
//                }
//                walkedVertices.get(pre).add(next);
//                pre = next;
//            }
//        }
//        // 确保用户输入线程已结束
////        try {
////            System.out.println("*****1*****");
////            userInputThread.join();
////            System.out.println("*****2*****");
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//        return result.toString();
//    }
//
//    public Vertex randomSelect(HashSet<Vertex> set) {
//        if (set == null || set.size() == 0) {
//            return null;
//        }
//        int size = set.size();
//        int random = new Random().nextInt(size);
//        ArrayList<Vertex> list = new ArrayList<>(set);
//        return list.get(random);
//    }
//}
