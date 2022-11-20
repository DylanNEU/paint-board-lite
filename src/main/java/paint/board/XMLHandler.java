package paint.board;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class XMLHandler {

    public XMLHandler(File f) throws ParserConfigurationException, SAXException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser parser = spf.newSAXParser();

        ShapeDefaultHandler sdh = new ShapeDefaultHandler();
        try {
            parser.parse(f, sdh);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
