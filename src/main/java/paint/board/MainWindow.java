package paint.board;

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import static paint.board.BasicTools.*;

public class MainWindow extends JFrame implements ActionListener {

    private JPanel mainPanel;

    private JMenuBar mainMenuBar;
    private JPanel tools;
    private JPanel stretcher;
    private JPanel canvas;
    private JButton pencil;
    private JButton eraser;
    private JButton line;
    private JButton drag;
    private JPanel BasicTools;
    private JButton straw;
    private JButton text;
    private final String[] defaultList = new String[]{
            "---简单形状---",
            "直线",
            "铅笔",
            "矩形",
            "三角形",
            "圆形",
            " ",
            "---多边形---",
            "三角形",
            "五边形",
            "六边形",
            " ",
            "---圆形---",
            "圆形",
            " ",
            "---特殊形状---",
            "四角星",
            "五角星",
            "五边形",
            "六边形"
    };
    private JButton save;
    private JButton fullScreen;
    private JButton open;
    private JLabel CursorPositionLabel;
    private JLabel canvasSizeLabel;
    private JButton curColor;
    private JButton lastColor;
    private JButton black;
    private JButton lightPurple;
    private JButton lightGreen;
    private JButton brightGray;
    private JButton create;
    private JButton lightBlue;
    private JButton white;
    private JButton green;
    private JButton blue;
    private JButton darkGrey;
    private JButton red;
    private JButton lightOrange;
    private JButton yellow;
    private JButton magenta;
    private JButton cyan;
    private JButton lightGreenBlue;
    private JButton gray;
    private JButton moreColor;
    private JPanel color;
    private JTextField search;
    private JPanel shapesPanel;
    private JButton searchBtn;
    private JButton clearBtn;
    private JPanel searchField;

    private JMenu file;
    private JMenu view;
    private JMenuItem openFile, newFile, saveFile;
    private JMenuItem full, half;
    private JMenuItem undoItem, redoItem;
    private JMenu edit;
    private MenuItemActionListener menuItemActionListener;
    private JList<String> shapeList;
    private JRadioButton fill;
    private JLabel curToolLabel;
    private JSpinner stroke;
    private JSpinner slider1;
    private JMenuItem redoM;
    private JMenuItem undoM;
    private JMenuItem saveProject;

    public MainWindow() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        super("轻画板");
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        setSize(1200, 800);
        setLocation(500, 400);
        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuItemActionListener = new MenuItemActionListener();
        // 画板初始化
        initBoard();
        Dimension temp = canvas.getSize();
        setCanvasSizeLabel(temp.width, temp.height);
        setMousePosLabel(0, 0);
        pencil.requestFocus();
        setVisible(true);
        shapeList.setAutoscrolls(true);

        search.setToolTipText("支持模糊匹配");
        search.setText("搜索形状...");
        search.setForeground(Color.GRAY);

        searchBtn.addActionListener(e -> {
            String key = search.getText();
            String[] res = (key.length() > 0 && !key.equals("搜索形状...")) ? fuzzyMatching(key) : defaultList;
            shapeList.setListData(res);
        });

        clearBtn.addActionListener(e -> {
            shapeList.setListData(defaultList);
            if (!search.getText().equals("搜索形状...")) {
                search.setText("搜索形状...");
                search.setForeground(Color.GRAY);
            }
        });

        shapeList.addListSelectionListener(e -> {
            if (!shapeList.getValueIsAdjusting()) {    //设置只有释放鼠标时才触发
                switch (shapeList.getSelectedValue()) {
                    case "直线" -> ((CanvasPanelListener) canvas).setTool(LINE);
                    case "矩形" -> ((CanvasPanelListener) canvas).setTool(RECTANGLE);
                    case "三角形" -> ((CanvasPanelListener) canvas).setTool(TRIANGLE);
                    case "六边形" -> ((CanvasPanelListener) canvas).setTool(HEXAGON);
                    case "五边形" -> ((CanvasPanelListener) canvas).setTool(PENTAGON);
                    case "圆形" -> ((CanvasPanelListener) canvas).setTool(ELLIPTICAL);
                    case "五角星" -> ((CanvasPanelListener) canvas).setTool(PENTAGRAM);
                    case "四角星" -> ((CanvasPanelListener) canvas).setTool(COMPASS);
                    case "铅笔" -> ((CanvasPanelListener) canvas).setTool(PENCIL);
                    default -> ((CanvasPanelListener) canvas).setTool(NULL);
                }
                setCurToolLabel();
            }
        });

        save.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(new File(".\\export\\"));
            fileChooser.setFileFilter(new FileNameExtensionFilter("画板", "pdml"));
            if (fileChooser.showSaveDialog(Main.mainWindow) == JFileChooser.APPROVE_OPTION) {
                var s = fileChooser.getSelectedFile();
                File f = new File(s + (s.toString().contains(".pdml") ? "" : ".pdml"));
                try {
                    XMLHandler xml = new XMLHandler();
                    xml.saveProject(f);
                } catch (ParserConfigurationException | SAXException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        create.addActionListener(e -> createNewCanvas());
        search.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (search.getText().equals("搜索形状...")) {
                    search.setText("");
                    search.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (search.getText().equals("")) {
                    search.setText("搜索形状...");
                    search.setForeground(Color.GRAY);
                }
            }
        });
        fullScreen.addActionListener(e -> {
            JFrame frame = new JFrame();
            frame.setLayout(new GridBagLayout());
            JFrame note = new JFrame();
            note.setLayout(new GridBagLayout());
            Toolkit kit = Toolkit.getDefaultToolkit(); //定义工具包
            Dimension screenSize = kit.getScreenSize(); //获取屏幕的尺寸
            note.setLocation(screenSize.width / 2 - 100, 0);
            note.setAlwaysOnTop(true);
            JLabel l = new JLabel("鼠标点击任意位置退出全屏显示");
            l.setForeground(Color.WHITE);
            note.add(l);
            note.getContentPane().setBackground(Color.DARK_GRAY);
            note.setSize(new Dimension(200, 50));
            note.setResizable(false);
            note.setUndecorated(true);
            note.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            note.setVisible(true);
            BufferedImage img = MenuItemActionListener.createPanel(canvas);
            frame.add(new JLabel(new ImageIcon(img)));
            frame.getContentPane().setBackground(Color.BLACK);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//            GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
            frame.setMaximumSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize());
            frame.setMinimumSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize());
            frame.setResizable(false);    //不能改变大小
            frame.setUndecorated(true);    //不要边框
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    frame.setVisible(false);
                    frame.dispose();
                    note.setVisible(false);
                    note.dispose();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
            frame.setVisible(true);
        });

        open.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(new File(".\\export\\"));
            fileChooser.setFileFilter(new FileNameExtensionFilter("画板", "pdml"));
            if (fileChooser.showOpenDialog(Main.mainWindow) == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                try {
                    XMLHandler xml = new XMLHandler();
                    xml.parseXML(f);
                } catch (ParserConfigurationException | SAXException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        SpinnerNumberModel snm = new SpinnerNumberModel(1, 1, 10, 1);
        stroke.setModel(snm);
        stroke.addChangeListener(e -> {
            int n = (int) stroke.getValue();
            ((CanvasPanelListener) canvas).setStroke(n);
        });
    }

    public void setMousePosLabel(int x, int y) {
        this.CursorPositionLabel.setText(x + ", " + y);
    }

    public void setCanvasSizeLabel(int width, int height) {
        this.canvasSizeLabel.setText(width + "*" + height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == moreColor) {
            displayColorChooser();
        } else if (e.getSource() == pencil) {
            ((CanvasPanelListener) canvas).setTool(PENCIL);
        } else if (e.getSource() == eraser) {
            ((CanvasPanelListener) canvas).setTool(ERASER);
        } else if (e.getSource() == line) {
            ((CanvasPanelListener) canvas).setTool(LINE);
        } else if (e.getSource() == drag) {
            ((CanvasPanelListener) canvas).setTool(DRAG);
        } else if (e.getSource() == straw) {
            ((CanvasPanelListener) canvas).setTool(STRAW);
        } else if (e.getSource() == text) {
            ((CanvasPanelListener) canvas).setTool(TEXT);
        } else if (e.getSource() == black) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            ((CanvasPanelListener) canvas).setColor(Pigment.black);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        } else if (e.getSource() == white) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            ((CanvasPanelListener) canvas).setColor(Pigment.white);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        } else if (e.getSource() == darkGrey) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            ((CanvasPanelListener) canvas).setColor(Pigment.darkGray);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        } else if (e.getSource() == brightGray) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            ((CanvasPanelListener) canvas).setColor(Pigment.brightGray);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        } else if (e.getSource() == gray) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            ((CanvasPanelListener) canvas).setColor(Pigment.gray);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        } else if (e.getSource() == red) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            ((CanvasPanelListener) canvas).setColor(Pigment.red);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        } else if (e.getSource() == green) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            ((CanvasPanelListener) canvas).setColor(Pigment.green);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        } else if (e.getSource() == blue) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            ((CanvasPanelListener) canvas).setColor(Pigment.blue);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        } else if (e.getSource() == yellow) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            ((CanvasPanelListener) canvas).setColor(Pigment.yellow);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        } else if (e.getSource() == magenta) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            ((CanvasPanelListener) canvas).setColor(Pigment.magenta);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        } else if (e.getSource() == cyan) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            ((CanvasPanelListener) canvas).setColor(Pigment.cyan);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        } else if (e.getSource() == lightGreenBlue) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            ((CanvasPanelListener) canvas).setColor(Pigment.lightGreenBlue);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        } else if (e.getSource() == lightPurple) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            ((CanvasPanelListener) canvas).setColor(Pigment.lightPurple);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        } else if (e.getSource() == lightOrange) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            ((CanvasPanelListener) canvas).setColor(Pigment.lightOrange);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        } else if (e.getSource() == lightGreen) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            ((CanvasPanelListener) canvas).setColor(Pigment.lightGreen);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        } else if (e.getSource() == lightBlue) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            ((CanvasPanelListener) canvas).setColor(Pigment.lightBlue);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        } else if (e.getSource() == fill) {
            Main.mainWindow.getCanvas().setTransparent(!fill.isSelected());
        }
//        ((CanvasPanelListener) canvas).getShapes();
        setCurToolLabel();
    }

    private void initBoard() {
        Font mainFont = new Font("Microsoft YaHei", Font.PLAIN, 12);
        file = new JMenu("文件");
        view = new JMenu("视图");
        edit = new JMenu("编辑");
        for (JMenu jMenu : Arrays.asList(this.view, this.edit, this.file)) {
            jMenu.setFont(mainFont);
        }
        saveProject = new JMenuItem("保存");
        newFile = new JMenuItem("新建");
        openFile = new JMenuItem("打开");
        saveFile = new JMenuItem("导出");
        saveProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        for (JMenuItem jMenuItem : Arrays.asList(this.newFile, this.openFile, this.saveFile, this.saveProject)) {
            jMenuItem.setFont(mainFont);
            jMenuItem.addActionListener(menuItemActionListener);
        }
        newFile.setName("newFile");
        saveProject.setName("saveProject");
        openFile.setName("openFile");
        saveFile.setName("saveFile");
        file.add(newFile);
        file.add(openFile);
        file.add(saveProject);
        file.add(saveFile);
        undoM = new JMenuItem("撤销");
        redoM = new JMenuItem("重做");
        undoM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        redoM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        for (JMenuItem jMenuItem : Arrays.asList(this.redoM, this.undoM)) {
            jMenuItem.setFont(mainFont);
            jMenuItem.addActionListener(menuItemActionListener);
        }
        undoM.setName("Undo");
        redoM.setName("Redo");
        edit.add(undoM);
        edit.add(redoM);


        mainMenuBar.add(file);
        mainMenuBar.add(edit);


        setActionLis(pencil, eraser, line, moreColor, drag, black, darkGrey, gray, brightGray, white);
        setActionLis(red, green, blue, yellow, magenta, cyan, lightGreenBlue, lightGreen, lightBlue, lightOrange);
        lightPurple.addActionListener(this);
        fill.addActionListener(this);
        straw.addActionListener(this);
        text.addActionListener(this);

        pencil.setIcon(new ImageIcon("./assets/pencil.png"));
        eraser.setIcon(new ImageIcon("./assets/eraser.png"));
        drag.setIcon(new ImageIcon("./assets/move.png"));
        line.setIcon(new ImageIcon("./assets/line.png"));
        text.setIcon(new ImageIcon("./assets/text.png"));
        straw.setIcon(new ImageIcon("./assets/straw.png"));
        searchBtn.setIcon(new ImageIcon("./assets/search.png"));
        clearBtn.setIcon(new ImageIcon("./assets/close.png"));
        save.setIcon(new ImageIcon("./assets/save.png"));
        open.setIcon(new ImageIcon("./assets/folder.png"));
        create.setIcon(new ImageIcon("./assets/add.png"));
        fullScreen.setIcon(new ImageIcon("./assets/full.png"));

        shapeList.setListData(defaultList);
        setCurToolLabel();
    }

    private void setActionLis(JButton red, JButton green, JButton blue, JButton yellow, JButton magenta, JButton cyan, JButton lightGreenBlue, JButton lightGreen, JButton lightBlue, JButton lightOrange) {
        red.addActionListener(this);
        green.addActionListener(this);
        blue.addActionListener(this);
        yellow.addActionListener(this);
        magenta.addActionListener(this);
        cyan.addActionListener(this);
        lightGreenBlue.addActionListener(this);
        lightGreen.addActionListener(this);
        lightBlue.addActionListener(this);
        lightOrange.addActionListener(this);
    }

    private JPanel generateSearchedShapeMenu(String key) {
        JPanel res = new JPanel();
        Dimension d = new Dimension(120, 200);
        Dimension inf = new Dimension(120, 9999);
        res.setSize(d);
        res.setAutoscrolls(true);
        res.setMinimumSize(d);
        res.setMaximumSize(inf);
        String[] s = fuzzyMatching(key);
        JList<String> searchResult = new JList<>(s.length == 0 ? new String[]{"暂无结果"} : s);
        res.add(searchResult);
        return res;
    }

    private String[] fuzzyMatching(String key) {
        HashSet<String> res = new HashSet<>();
        String[] existShapes = new String[]{"五角星", "直线", "三角形", "矩形", "六边形", "圆形", "五边形", "四角星"};
        for (var c : key.toCharArray()) {
            for (var s : existShapes) {
                if (s.contains(CharBuffer.wrap(new char[]{c}))) {
                    res.add(s);
                }
            }
        }
        String[] res_ = new String[]{};
        return res.isEmpty() ? new String[]{"暂无结果"} : res.toArray(res_);
    }

    public void createNewCanvas() {
        JFrame newCanvasSettings = new JFrame();
        newCanvasSettings.setTitle("新建画布");
        newCanvasSettings.setSize(350, 200);
        newCanvasSettings.setResizable(false);
        newCanvasSettings.setLayout(null);

        newCanvasSettings.setLocation(500, 400);
        newCanvasSettings.setVisible(true);

        JTextField width = new JTextField();
        width.setSize(100, 25);
        width.setLocation(100, 25);
        width.setText("800");

        JLabel widthLabel = new JLabel("宽度 (px):");
        widthLabel.setSize(75, 25);
        widthLabel.setLocation(25, 25);

        JLabel heightLabel = new JLabel("高度 (px):");
        heightLabel.setSize(75, 25);
        heightLabel.setLocation(25, 75);

        JTextField height = new JTextField();
        height.setLocation(100, 75);
        height.setSize(100, 25);
        height.setText("600");

        JButton okay = new JButton("新建");
        okay.setSize(75, 25);
        okay.setLocation(250, 25);

        final int[] w = new int[1];
        final int[] h = new int[1];
        okay.addActionListener(e -> {
                    try {
                        int newCanvasWidth = Integer.parseInt(width.getText());
                        int newCanvasHeight = Integer.parseInt(height.getText());
                        newCanvasSettings.dispose();
                        w[0] = newCanvasWidth;
                        h[0] = newCanvasHeight;

                        ((CanvasPanelListener) canvas).changeCanvasPanelSize(newCanvasWidth, newCanvasHeight);
                        Dimension d = new Dimension(w[0], h[0]);
                        getCanvas().setBICanvas(d);

                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(null,
                                "输入错误，请输入整数",
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
        );

        JButton cancel = new JButton("取消");
        cancel.setSize(75, 25);
        cancel.setLocation(250, 75);
        cancel.addActionListener(e -> newCanvasSettings.dispose()
        );
        ArrayList<Component> focusOrderList = new ArrayList<>();
        focusOrderList.add(heightLabel);
        focusOrderList.add(widthLabel);
        focusOrderList.add(width);
        focusOrderList.add(height);
        focusOrderList.add(okay);
        focusOrderList.add(cancel);

        Vector<Component> vector = new Vector<>();
        vector.add(heightLabel);
        vector.add(widthLabel);
        vector.add(width);
        vector.add(height);
        vector.add(okay);
        vector.add(cancel);
        newCanvasSettings.setFocusTraversalPolicy(new NewFileDialogFocusTraversalPolicy(vector));
        newCanvasSettings.add(heightLabel);
        newCanvasSettings.add(widthLabel);
        newCanvasSettings.add(width);
        newCanvasSettings.add(height);
        newCanvasSettings.add(okay);
        newCanvasSettings.add(cancel);
    }

    private void createUIComponents() {
        canvas = new CanvasPanelListener();
    }

    public CanvasPanelListener getCanvas() {
        return (CanvasPanelListener) canvas;
    }

    public JPanel getStretcher() {
        return stretcher;
    }

    public void setLastColor(Color color) {
        lastColor.setBackground(color);
    }

    public void setCurColor(Color color) {
        curColor.setBackground(color);
    }

    private void displayColorChooser() {
        Color c = JColorChooser.showDialog(this, "选择颜色", new Color(0x68A4FF));
        if (!(c == null)) {
            setLastColor(((CanvasPanelListener) canvas).getCurrentColor());
            Main.mainWindow.getCanvas().setColor(c);
            setCurColor(((CanvasPanelListener) canvas).getCurrentColor());
        }
    }

    public void setMousePositionLabel(int i, int j) {
        if (CanvasPanelListener.isInCanvas) {
            CursorPositionLabel.setText("x: " + i + ", y: " + j);
        } else {
            CursorPositionLabel.setText("");
        }
    }

    public void setCurToolLabel() {
        switch (((CanvasPanelListener) canvas).getActTool()) {
            case NULL -> curToolLabel.setText("未选择工具");
            case PENCIL -> curToolLabel.setText("铅笔");
            case LINE -> curToolLabel.setText("直线");
            case ERASER -> curToolLabel.setText("橡皮");
            case TEXT -> curToolLabel.setText("文本工具");
            case STRAW -> curToolLabel.setText("吸管");
            case DRAG -> curToolLabel.setText("拖拽");
            case TRIANGLE -> curToolLabel.setText("三角形");
            case RECTANGLE -> curToolLabel.setText("矩形");
            case PENTAGON -> curToolLabel.setText("五边形");
            case HEXAGON -> curToolLabel.setText("六边形");
            case ELLIPTICAL -> curToolLabel.setText("圆形");
            case COMPASS -> curToolLabel.setText("四角星");
            case PENTAGRAM -> curToolLabel.setText("五角星");
            default -> curToolLabel.setText("");
        }
    }
}
