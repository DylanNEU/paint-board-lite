package paint.board;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class TextDialog extends JDialog implements ActionListener, ChangeListener {

    public static final int APPLY_OPTION = 1;
    public static final int CANCEL_OPTION = 0;
    JTextField preview;
    JTextField input;
    JButton ok;
    JButton cancel;
    JComboBox<String> fontBox;
    JComboBox<String> sizeBox;
    int fontSize;
    Font font;
    String text;
    String[] fontList;
    String[] fontSizeList = {"12", "15", "18", "21", "24", "27", "30", "32", "36", "38", "40", "42", "44", "46", "48", "50", "52", "53", "54", "55"};
    private int userResponse;

    public TextDialog(Frame owner) {
        super(owner);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        fontList = ge.getAvailableFontFamilyNames();
        sizeBox = new JComboBox<>(fontSizeList);
        fontBox = new JComboBox<>(fontList);
        preview = new JTextField("你好世界HelloWorld!");
        preview.setHorizontalAlignment(SwingConstants.CENTER);
        preview.setFont(new Font("宋体", Font.PLAIN, 25));
        preview.setPreferredSize(new Dimension(220, 60));
        preview.setEditable(false);

        ok = new JButton("确定");
        cancel = new JButton("取消");
        ok.setPreferredSize(cancel.getPreferredSize());

        input = new JTextField("你好世界HelloWorld!");
        input.setFont(new Font("宋体", Font.PLAIN, 25));
        input.setPreferredSize(new Dimension(220, 60));
        input.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        });

        sizeBox.addActionListener(e -> update());
        fontBox.addActionListener(e -> update());
        ok.addActionListener(this);
        cancel.addActionListener(this);
        input.addActionListener(this);

        JPanel p0 = new JPanel();
        p0.add(input);
        p0.setBorder(new TitledBorder(new EtchedBorder(), "Text"));

        JPanel p1 = new JPanel();
        p1.add(fontBox);
        p1.setBorder(new TitledBorder(new EtchedBorder(), "Font Family"));

        JPanel p2 = new JPanel(); // use FlowLayout
        p2.add(sizeBox);
        p2.setBorder(new TitledBorder(new EtchedBorder(), "Font Size"));
        p2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel p3 = new JPanel();
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));
        p3.add(preview);
        p3.setPreferredSize(new Dimension(250, 60));
        p3.setMaximumSize(new Dimension(250, 60));
        p3.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel p4 = new JPanel();
        p4.add(ok);
        p4.add(cancel);
        p4.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(p0);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(p1);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(p2);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(p3);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(p4);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.setContentPane(p);
        this.pack();
    }

    public void update() {
        text = input.getText();
        fontSize = Integer.parseInt((String) Objects.requireNonNull(sizeBox.getSelectedItem()));
        font = new Font((String) fontBox.getSelectedItem(), Font.PLAIN, fontSize);
        preview.setFont(font);
        preview.setText(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            userResponse = APPLY_OPTION;
            update();
            this.setVisible(false);
        } else if (e.getSource() == cancel) {
            userResponse = CANCEL_OPTION;
            this.setVisible(false);
        } else {
            update();
        }
    }

    public int showCustomDialog(Frame f) {
        this.setLocationRelativeTo(f);

        this.setVisible(true);

        return userResponse;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        update();
    }

    public int getFontSize() {
        return fontSize;
    }

    @Override
    public Font getFont() {
        return font;
    }

    public String getText() {
        return text;
    }
}
