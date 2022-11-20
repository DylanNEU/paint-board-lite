package paint.board;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.*;
import java.util.Objects;

import static paint.board.Main.mainWindow;

public class ShapeDefaultHandler extends DefaultHandler {

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
//        System.out.println("解析xml文档开始");
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
//        System.out.println("解析xml文档结束");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if ("canvas".equals(qName)) {
            //获取所有的属性
            int count = attributes.getLength();//属性的个数
            //循环获取每个属性
            for (int i = 0; i < count; i++) {
                String attName = attributes.getQName(i);//属性名称
                String attValue = attributes.getValue(i);//属性值
                System.out.println("属性名称:" + attName + "\t属性值为:" + attValue);
            }
        }
        if ("canvas".equals(qName)) {
            int width = Integer.parseInt(attributes.getValue(0));
            int height = Integer.parseInt(attributes.getValue(1));
            mainWindow.getCanvas().changeCanvasPanelSize(width, height);
            Dimension d = new Dimension(width, height);
            mainWindow.getCanvas().setBICanvas(d);
            System.out.println("canvas - w:" + width + ", h:" + height);
        } else if ("shape".equals(qName)) {
            String shape = "";
            if (Objects.equals(attributes.getQName(0), "type")) shape = attributes.getValue(0);
            int x1 = Integer.parseInt(attributes.getValue(1));
            int y1 = Integer.parseInt(attributes.getValue(2));
            int x2 = Integer.parseInt(attributes.getValue(3));
            int y2 = Integer.parseInt(attributes.getValue(4));
            String[] sc1 = attributes.getValue(5).split(",");
            int s_stroke = Integer.parseInt(attributes.getValue(6));
            String[] sc2 = attributes.getValue(7).split(",");
            Color c1 = new Color(Integer.parseInt(sc1[0]), Integer.parseInt(sc1[1]), Integer.parseInt(sc1[2]));
            Color c2 = new Color(Integer.parseInt(sc2[0]), Integer.parseInt(sc2[1]), Integer.parseInt(sc2[2]));
            BasicStroke stroke = new BasicStroke(s_stroke);
            boolean isTrans = Boolean.parseBoolean(attributes.getValue(8));
            switch (shape) {
                case "line" ->
                        mainWindow.getCanvas().addNewShape(new Shape(x1, y1, x2, y2, c1, stroke, BasicTools.LINE, c2, isTrans));
                case "rectangle" ->
                        mainWindow.getCanvas().addNewShape(new Shape(x1, y1, x2, y2, c1, stroke, BasicTools.RECTANGLE, c2, isTrans));
                case "elliptical" ->
                        mainWindow.getCanvas().addNewShape(new Shape(x1, y1, x2, y2, c1, stroke, BasicTools.ELLIPTICAL, c2, isTrans));
                case "pentagon" ->
                        mainWindow.getCanvas().addNewShape(new Shape(x1, y1, x2, y2, c1, stroke, BasicTools.PENTAGON, c2, isTrans));
                case "pentagram" ->
                        mainWindow.getCanvas().addNewShape(new Shape(x1, y1, x2, y2, c1, stroke, BasicTools.PENTAGRAM, c2, isTrans));
                case "compass" ->
                        mainWindow.getCanvas().addNewShape(new Shape(x1, y1, x2, y2, c1, stroke, BasicTools.COMPASS, c2, isTrans));
                case "hexagon" ->
                        mainWindow.getCanvas().addNewShape(new Shape(x1, y1, x2, y2, c1, stroke, BasicTools.HEXAGON, c2, isTrans));
                case "triangle" ->
                        mainWindow.getCanvas().addNewShape(new Shape(x1, y1, x2, y2, c1, stroke, BasicTools.TRIANGLE, c2, isTrans));
            }
        } else if ("text".equals(qName)) {
            int x1 = Integer.parseInt(attributes.getValue(0));
            int y1 = Integer.parseInt(attributes.getValue(1));
            String fontName = attributes.getValue(2);
            int fontSize = Integer.parseInt(attributes.getValue(3));
            String message = attributes.getValue(4);
            Font font = new Font(fontName, Font.PLAIN, fontSize);
            String[] sc1 = attributes.getValue(5).split(",");
            Color c1 = new Color(Integer.parseInt(sc1[0]), Integer.parseInt(sc1[1]), Integer.parseInt(sc1[2]));
            mainWindow.getCanvas().addNewShape(new Shape(x1, y1, font, c1, new BasicStroke(2), BasicTools.TEXT, message));
        }
    }
}
