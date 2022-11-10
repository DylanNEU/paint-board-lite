package paint.board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuItemActionListener implements ActionListener {

    private void openImage(File f) {
        // todo: openImage method needs a method to get draw panel listener.
        try {
            Main.mainWindow.getCanvas().setImage(ImageIO.read(f));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private BufferedImage createPanel(JPanel panel) {
        int width = panel.getWidth();
        int height = panel.getHeight();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        panel.print(g);
        return img;
    }

    private void saveImage(File f) {
        BufferedImage img = createPanel(Main.mainWindow.getCanvas());
        try {
            ImageIO.write(img, "png", f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = ((JMenuItem) e.getSource()).getName();
        if (name.equals("newFile")) {
            Main.mainWindow.createNewCanvas();
        } else if (name.equals("openFile")) {
            JFileChooser fileChooser = new JFileChooser(new File("."));
            // unfinished.
            // 今晚太困了，写到这吧。
            // 明天继续。
        }
    }
}
