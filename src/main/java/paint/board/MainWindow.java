package paint.board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
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
    private JButton button3;
    private JButton button4;
    private JButton undo;
    private JButton redo;
    private JButton button5;
    private JButton button6;
    private JLabel CursorPositionLabel;
    private JLabel canvasSizeLabel;
    private JButton curColor;
    private JButton lastColor;
    private JButton black;
    private JButton lightPurple;
    private JButton lightGreen;
    private JButton brightGrey;
    private JButton button13;
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
    private JPanel grey;
    private JButton moreColor;

    private JMenu file;
    private JMenu view;
    private JMenuItem openFile, newFile, saveFile;
    private JMenuItem full, half;
    private JMenuItem undoItem, redoItem;
    private JMenu edit;
    private MenuItemActionListener menuItemActionListener;

    public MainWindow() {
        super("轻画板");
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
    }

    public void setMousePosLabel(int x, int y) {
        this.CursorPositionLabel.setText(x + ", " + y);
    }

    public void setCanvasSizeLabel(int width, int height) {
        this.canvasSizeLabel.setText(width + "*" + height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: complete action-performed method
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
        }
        ((CanvasPanelListener) canvas).getShapes();
    }

    private void initBoard() {
        Font mainFont = new Font("Microsoft YaHei", Font.PLAIN, 12);
        file = new JMenu("文件");
        view = new JMenu("视图");
        edit = new JMenu("编辑");
        for (JMenu jMenu : Arrays.asList(this.view, this.edit, this.file)) {
            jMenu.setFont(mainFont);
        }
        newFile = new JMenuItem("新建");
        openFile = new JMenuItem("打开");
        saveFile = new JMenuItem("保存");
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        for (JMenuItem jMenuItem : Arrays.asList(this.newFile, this.openFile, this.saveFile)) {
            jMenuItem.setFont(mainFont);
            jMenuItem.addActionListener(menuItemActionListener);
        }
        newFile.setName("newFile");
        openFile.setName("openFile");
        saveFile.setName("saveFile");
        file.add(newFile);
        file.add(openFile);
        file.add(saveFile);


        mainMenuBar.add(file);


        pencil.addActionListener(this);
        eraser.addActionListener(this);
        line.addActionListener(this);
        moreColor.addActionListener(this);
        drag.addActionListener(this);

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

        JLabel widthLabel = new JLabel("宽度 (px):");
        widthLabel.setSize(75, 25);
        widthLabel.setLocation(25, 25);

        JLabel heightLabel = new JLabel("高度 (px):");
        heightLabel.setSize(75, 25);
        heightLabel.setLocation(25, 75);

        JTextField height = new JTextField();
        height.setLocation(100, 75);
        height.setSize(100, 25);

        JButton okay = new JButton("新建");
        okay.setSize(75, 25);
        okay.setLocation(250, 25);

        final int[] w = new int[1];
        final int[] h = new int[1];
        okay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int newCanvasWidth = Integer.parseInt(width.getText());
                    int newCanvasHeight = Integer.parseInt(height.getText());
                    newCanvasSettings.dispose();
                    w[0] = newCanvasWidth;
                    h[0] = newCanvasHeight;

                    ((CanvasPanelListener) canvas).changeCanvasPanelSize(newCanvasWidth, newCanvasHeight);
                } catch (NumberFormatException nfe) {
                                           JOptionPane.showMessageDialog(null,
                                                   "输入错误，请输入整数",
                                                   "ERROR",
                                                   JOptionPane.ERROR_MESSAGE);
                                       }
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

        Dimension d = new Dimension(w[0], h[0]);
        getCanvas().setSize(d);
        getCanvas().setMinimumSize(d);
        getCanvas().setMaximumSize(d);
        getCanvas().repaint();
        this.canvas.setSize(d);
        this.canvas.setBackground(Color.WHITE);
        this.setCanvasSizeLabel(w[0], h[0]);
        // todo: when the canvas gets bigger, the background cannot gets bigger at the same time.
        // confusing...


        // ---------- *** ----------
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
            // todo: there needs a label to display the position of cursor.
            CursorPositionLabel.setText("x: " + i + ", y: " + j);
        } else {
            CursorPositionLabel.setText("");
        }
    }
}
