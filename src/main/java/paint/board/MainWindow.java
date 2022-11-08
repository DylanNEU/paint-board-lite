package paint.board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;

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

    private JMenu file = new JMenu("文件"), view = new JMenu("视图"), edit = new JMenu("编辑");
    private JMenuItem openFile, newFile, saveFile;
    private JMenuItem full, half;
    private JMenuItem quash, recover;
    private MenuItemActionListener menuItemActionListener = new MenuItemActionListener();

    public MainWindow() {
        super("轻画板");
        setSize(1200, 800);
        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 画板初始化
        initBoard();
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
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public JPanel getCanvas() {
        return canvas;
    }
}
