package paint.board;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class XMLHandler {

    private final SAXParser parser;
    ShapeDefaultHandler sdh;

    public XMLHandler() throws ParserConfigurationException, SAXException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        parser = spf.newSAXParser();
        sdh = new ShapeDefaultHandler();
    }

    public void parseXML(File f) {
        try {
            parser.parse(f, sdh);
        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveProject(File f) {
        StringBuilder sbf = new StringBuilder();
        sbf.append("<canvas w=\"").append(Main.mainWindow.getCanvas().getWidth()).append("\" h=\"");
        sbf.append(Main.mainWindow.getCanvas().getHeight()).append("\" >");
        BufferedWriter bwr;
        try {
            bwr = new BufferedWriter(new FileWriter(f));
            bwr.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            bwr.newLine();
            bwr.write(String.valueOf(sbf));
            bwr.newLine();
            for (Shape s : Main.mainWindow.getCanvas().getShapes()) {
                bwr.write("    " + s.toXML());
                bwr.newLine();
            }
            bwr.write("</canvas>");
            bwr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
