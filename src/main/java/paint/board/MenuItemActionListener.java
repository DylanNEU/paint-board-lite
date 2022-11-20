package paint.board;

import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuItemActionListener implements ActionListener {

    private void openImage(File f) {
        try {
            var img = ImageIO.read(f);
            Main.mainWindow.getCanvas().setImage(img);
            Main.mainWindow.getCanvas().setSize(img.getWidth(), img.getHeight());
            Main.mainWindow.getCanvas().repaint();
            Main.mainWindow.setCanvasSizeLabel(img.getWidth(), img.getHeight());
            Dimension d = new Dimension(img.getWidth(), img.getHeight());
            Main.mainWindow.getCanvas().setMinimumSize(d);
            Main.mainWindow.getCanvas().setMaximumSize(d);
            Main.mainWindow.setSize(img.getWidth() + 200, img.getHeight() + 200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static BufferedImage createPanel(JPanel panel) {
        int width = panel.getWidth();
        int height = panel.getHeight();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        panel.print(g);
        return img;
    }

    public static void saveImage(File f) {
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
        switch (name) {
            case "newFile" -> Main.mainWindow.createNewCanvas();
            case "saveProject" -> {
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
            }
            case "openFile" -> {
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
            }
            case "saveFile" -> {
                JFileChooser fileChooser = new JFileChooser(new File(".\\export\\"));
                fileChooser.setFileFilter(new FileNameExtensionFilter("图片", "png", "jpg"));
                if (fileChooser.showSaveDialog(Main.mainWindow) == JFileChooser.APPROVE_OPTION) {
                    File file = new File(fileChooser.getSelectedFile() + ".png");
                    saveImage(file);
                }
            }
            case "Redo" -> Main.mainWindow.getCanvas().redo();
            case "Undo" -> Main.mainWindow.getCanvas().undo();
            default -> {
            }
        }
//        System.out.println(e);
    }
}
