package ui;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import basis.ShortestPath;
import basis.Bridge;

import javafx.scene.control.TextArea;
import basis.*;
import javafx.fxml.FXML;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * 主窗口控制类
 *
 * @author XJL YSJ
 * @version 1.2.0
 * @date 2017-09-15
 */
public class BaseWindowController {
    private Graph graph;
    private GraphVisualizer printer;
    private String content;

    @FXML
    private MenuBar menuBar;                //菜单栏
    @FXML
    private MenuItem saveMenuItem;        //“另存为”菜单项

    @FXML
    private Button textButton;            //“查看源文本”按钮
    @FXML
    private Button showButton;            //“展示有向图”按钮
    @FXML
    private Button queryButton;            //“查询桥接词”按钮
    @FXML
    private Button generateButton;        //“生成新文本”按钮
    @FXML
    private Button pathButton;            //“求最短路径”按钮
    @FXML
    private Button walkButton;            //“随机游走”按钮

    @FXML
    private ScrollPane canvasContainer;    //画布面板的容器面板
    @FXML
    private AnchorPane canvasPane;        //画布面板，用于画有向图
    @FXML
    private TextArea console;                //控制台，用于显示各种信息
    @FXML
    private StackPane stackPane;            //控制按钮面板的容器


    private File dataFile;                        //源文本文件对象


    /**
     * “打开”菜单项被点击时的事件处理方法
     */
    @FXML
    protected void handleOpenMenuItemClicked(ActionEvent e) {
        Stage stage = (Stage) menuBar.getScene().getWindow(); // 获取当前窗口
        FileChooser fileChooser = new FileChooser(); // 文件选择器

        fileChooser.setTitle("打开文件"); // 设置窗口标题
        fileChooser.setInitialDirectory(new File("C:\\Users\\86139\\Desktop\\SELAB1")); // 设置初始路径
        // 设置文件格式过滤器
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("文本文档", "*.txt"),
                new FileChooser.ExtensionFilter("所有文件", "*.*"));
        File file = fileChooser.showOpenDialog(stage); // 打开文件选择对话框
        dataFile = file; // 记录选择的文件对象
        if (file != null) {
            try (Scanner scan = new Scanner(file)) {
                String content = scan.useDelimiter("\\Z").next(); // 读取文件全部内容
                this.content = content;
                console.setText(content); // 将内容显示到控制台
                String filePath = file.getAbsolutePath(); // 获取用户选择的文件路径
                Graph graph = GraphGenerate.genGraph(filePath); // 使用选择的文件路径生成图
                this.graph = graph;
//                GraphVisualizer printer = new GraphVisualizer(graph);
//                this.printer = printer;
                showButton.setDisable(false);
            } catch (FileNotFoundException err) {
                err.printStackTrace();
            }
            //成功生成有向图后，各功能控制按钮可用
            if (graph != null) {
                showButton.setDisable(false);
                queryButton.setDisable(false);
                generateButton.setDisable(false);
                pathButton.setDisable(false);
                walkButton.setDisable(false);
            }
        }
    }

    /**
     * “另存为”菜单项被点击时的事件处理方法
     */
    @FXML
    protected void handleSaveMenuItemClicked(ActionEvent e) {
        Stage stage = (Stage) menuBar.getScene().getWindow();//文件另存为窗口
        FileChooser fileChooser = new FileChooser();        //文件另存为

        fileChooser.setTitle("保存图片");    //设置窗口标题
        fileChooser.setInitialDirectory(new File(System.getProperty("C:\\Users\\86139\\Desktop")));        //设置初始路径
        //设置文件格式过滤器
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG图像", "*.png"),
                new FileChooser.ExtensionFilter("JPG图像", "*.jpg"),
                new FileChooser.ExtensionFilter("GIF图像", "*.gif"));
        File file = fileChooser.showSaveDialog(stage);    //以“另存为模式”打开窗口，并使用file记录选择保存的文件对象
        if (file != null) {
            String name = file.getName();    //获取文件保存路径
            String extendName = name.substring(name.lastIndexOf('.') + 1);    //获取文件拓展名
            WritableImage image = new WritableImage((int) canvasPane.getWidth(), (int) canvasPane.getHeight());
            //初始化可写入图片
            try {
                //根据文件拓展名选择不同的图片保存格式
                switch (extendName) {
                    case "png":
                        SnapshotParameters spPNG = new SnapshotParameters();
                        spPNG.setFill(Color.WHITE);    //设置填充色
                        //以png格式将图片写入文件
                        ImageIO.write(SwingFXUtils.fromFXImage(canvasPane.snapshot(spPNG, image), null), "png", file);
                        break;
                    case "jpg":
                        SnapshotParameters spJPG = new SnapshotParameters();
                        spJPG.setFill(Color.WHITE);
                        ImageIO.write(SwingFXUtils.fromFXImage(canvasPane.snapshot(spJPG, image), null), "jpg", file);
                        break;
                    case "gif":
                        SnapshotParameters spGIF = new SnapshotParameters();
                        spGIF.setFill(Color.WHITE);
                        ImageIO.write(SwingFXUtils.fromFXImage(canvasPane.snapshot(spGIF, image), null), "gif", file);
                        break;
                    default:
                        break;
                }
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
    }

    /**
     * “关闭”菜单项被点击时的事件处理方法
     */
    @FXML
    protected void handleCloseMenuItemClicked(ActionEvent e) {
        Stage stage = (Stage) menuBar.getScene().getWindow();    //获取主窗口
        stage.close();    //关闭主窗口
    }

    @FXML
    protected void handleShowButtonClicked(MouseEvent e) {

        GraphVisualizer.showDirectGraph(this.graph);
//        GraphVisualizer printer = new GraphVisualizer(graph,null);
//        this.printer = printer;
//        // 创建一个 ImageView 对象用于展示图片
//        ImageView exportedImage = new ImageView();
//
//        // 加载图片
//        Image image = new Image("file:C:\\Users\\water_boy\\Desktop\\SELAB1\\src\\mid.svg");
//
//        // 设置图片到 ImageView
//        exportedImage.setImage(image);
//
//        // 将 ImageView 添加到布局中
//        // 假设 canvasPane 是你的 AnchorPane 对象
//        canvasPane.getChildren().add(exportedImage);
//        //canvasPane.getChildren().clear();
//
//        //展示有向图
//        saveMenuItem.setDisable(false);        //有向图被画出后，“另存为”菜单项可用
    }
    //查询桥接词
    @FXML
    protected void handleQueryButtonClicked(MouseEvent e) throws Exception {
        GridPane prePane = (GridPane)stackPane.getChildren().get(0);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PathPane.fxml"));
        GridPane pane = loader.load();
        TextField word1TF = (TextField)loader.getNamespace().get("word1TextField");
        TextField word2TF = (TextField)loader.getNamespace().get("word2TextField");
        Button returnBT = (Button)loader.getNamespace().get("returnButton");
        Button yesBT = (Button)loader.getNamespace().get("yesButton");
        //“返回”按钮被点击时，重新显示控制按钮面板
        returnBT.setOnMouseClicked(event -> {
            stackPane.getChildren().remove(pane);
            stackPane.getChildren().add(prePane);
        });
        //“确定”按钮被点击时，求最短路径并显示
        yesBT.setOnMouseClicked(event -> {

            String word1 = word1TF.getText().trim();
            String word2 = word2TF.getText().trim();
            if(word1.equals("")){
                word1 = null;
            }
            if(word2.equals("")){
                word2 = null;
            }
            Bridge bri = new Bridge(this.graph);
            String result = bri.queryBridgeWords(word1,word2);

            console.setText(result);

        });

        stackPane.getChildren().remove(prePane);
        stackPane.getChildren().add(pane);
    }
    //生成新文本
    @FXML
    protected void handleGenerateButtonClicked(MouseEvent e) throws Exception {
        GridPane prePane = (GridPane)stackPane.getChildren().get(0);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gentext.fxml"));
        GridPane pane = loader.load();
        TextField word1TF = (TextField)loader.getNamespace().get("word1TextField");
        Button returnBT = (Button)loader.getNamespace().get("returnButton");
        Button yesBT = (Button)loader.getNamespace().get("yesButton");
        //“返回”按钮被点击时，重新显示控制按钮面板
        returnBT.setOnMouseClicked(event -> {
            stackPane.getChildren().remove(pane);
            stackPane.getChildren().add(prePane);
        });
        //“确定”按钮被点击时，求最短路径并显示
        yesBT.setOnMouseClicked(event -> {

            String word1 = word1TF.getText().trim();


            Bridge bri = new Bridge(this.graph);
            String result = bri.generateNewText(word1);

            console.setText(result);

        });

        stackPane.getChildren().remove(prePane);
        stackPane.getChildren().add(pane);


    }
    //最短路径
    @FXML
    protected void handlePathButtonClicked(MouseEvent e) throws Exception {
        GridPane prePane = (GridPane)stackPane.getChildren().get(0);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PathPane.fxml"));
        GridPane pane = loader.load();
        TextField word1TF = (TextField)loader.getNamespace().get("word1TextField");
        TextField word2TF = (TextField)loader.getNamespace().get("word2TextField");
        Button returnBT = (Button)loader.getNamespace().get("returnButton");
        Button yesBT = (Button)loader.getNamespace().get("yesButton");
        //“返回”按钮被点击时，重新显示控制按钮面板
        returnBT.setOnMouseClicked(event -> {
            stackPane.getChildren().remove(pane);
            stackPane.getChildren().add(prePane);
        });
        //“确定”按钮被点击时，求最短路径并显示
        yesBT.setOnMouseClicked(event -> {

            String word1 = word1TF.getText().trim();
            String word2 = word2TF.getText().trim();
            if(word1.equals("")){
                word1 = null;
            }
            if(word2.equals("")){
                word2 = null;
            }
            ShortestPath sp = new ShortestPath(this.graph);
            String result = sp.calcShortestPath(word1,word2);

            console.setText(result);
            // 按照换行符分割每一行
            String[] lines = result.split("\n");

            // 创建一个二维数组列表
            List<List<String>> twoDimensionalArray = new ArrayList<>();
            // 遍历每一行，按照'->'分割，并将结果添加到二维数组列表中
            for (String line : lines) {
                String[] parts = line.split("->");
                List<String> row = new ArrayList<>();
                for (String part : parts) {
                    row.add(part);
                }
                twoDimensionalArray.add(row);
            }

            GraphVisualizer printer = new GraphVisualizer(graph,twoDimensionalArray);
        });

        stackPane.getChildren().remove(prePane);
        stackPane.getChildren().add(pane);
    }


    @FXML
    protected void handleWalkButtonClicked(MouseEvent e) throws Exception {
        RandomWalk walker = new RandomWalk(this.graph, this.console);
        String randomWalkRes = walker.randomWalk();
        walker.writeRandomWalkPathToFile(randomWalkRes);
    }
}

