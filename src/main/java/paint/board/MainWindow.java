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

public class MainWindow extends JFrame implements ActionListener {

    private JPanel mainPanel;
    private JPanel tools;
    private JPanel stretcher;
    private JPanel canvas;
    private JButton pencil;
    private JButton eraser;
    private JButton button1;
    private JButton button2;
    private JPanel BasicTools;
    private JButton button3;
    private JButton button4;
    private JButton undo;
    private JButton redo;
    private JButton button5;
    private JButton button6;

    private JMenu file;
    private JMenu view;
    private JMenuItem openFile, newFile, saveFile;
    private JMenuItem full, half;
    private JMenuItem quash, recover;
    private JMenu edit;
    private final MenuItemActionListener menuItemActionListener = new MenuItemActionListener();

    public MainWindow() {
        super("轻画板");
        setSize(1200, 800);
        setLocation(500, 400);
        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 画板初始化
        initBoard();
        Dimension temp = canvas.getSize();
//        setCanvasSizeLabel(temp.width, temp.height);
//        setMousePosLabel(0, 0);
        pencil.requestFocus();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: complete action-performed method

    }

    private void initBoard() {
        Font mainFont = new Font("Microsoft YaHei", Font.PLAIN, 12);
        for (JMenu jMenu : Arrays.asList(this.view, this.edit, this.file)) {
            jMenu.setFont(mainFont);
        }
        file = new JMenu("文件");
        view = new JMenu("视图");
        edit = new JMenu("编辑");
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


    }

    public void createNewCanvas() {
        JFrame newCanvasSettings = new JFrame();
        newCanvasSettings.setTitle("新建画布");
        newCanvasSettings.setSize(350, 200);
        newCanvasSettings.setResizable(false);
        newCanvasSettings.setLayout(null);
        newCanvasSettings.pack();

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
        okay.addActionListener(new ActionListener() {
                                   public void actionPerformed(ActionEvent e) {
                                       try {
                                           int newDrawPanelWidth = Integer.parseInt(width.getText());
                                           int newDrawPanelHeight = Integer.parseInt(height.getText());
                                           newCanvasSettings.dispose();

                                           ((CanvasPanelListener) canvas).changeCanvasPanelSize(newDrawPanelWidth, newDrawPanelHeight);
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
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public CanvasPanelListener getCanvas() {
        return (CanvasPanelListener) canvas;
    }

    public JPanel getStretcher() {
        return stretcher;
    }

    public void setLastColor(Color color) {
        // todo: there is a btn to show the last color.
        // lastColor.setBackground(color);
    }

    public void setCurColor(Color color) {
        // todo: there is  a btn to show the current color.
    }

    public void setMousePositionLabel(int i, int j) {
        if (CanvasPanelListener.isInCanvas) {
            // todo: there needs a label to display the position of cursor.
            // mosePositionLabel.setText(i + ", " + y + "px");
        } else {
            // mosePositionLabel.setText("");
        }
    }
}
