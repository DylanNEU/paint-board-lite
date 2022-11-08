package paint.board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class CanvasPanelListener extends JPanel implements MouseListener, MouseMotionListener {

    public static boolean isInCanvas = false;
    private int x1, x2, y1, y2;
    private boolean dragged = false;
    private Color currentColor;
    private Color lastColor;
    private int group;
    private boolean isTransparent;
    private BasicStroke stroke = new BasicStroke(2);
    private Stack<Shape> shapes;
    private Stack<Shape> removed;
    private Stack<Shape> preview;
    private BufferedImage canvas;
    private Graphics2D graphics2D;
    // todo: add Text Dialog window and so on.
//    private TextDialog textDialog;
    private BasicTools actTool;
    //    private ArcStatus drawStatus = ArcStatus.NOT_DRAWING;
//    private ArcStatus direction = ArcStatus.NO_DIRECTION;
    private Dimension center, startPoint;
    //    private int radius;
//    private Vector3 benchmark;
    private Rectangle rectangle;

    public CanvasPanelListener(int x, int y) {
        setSize(x, y);
        Dimension d = new Dimension(x, y);
        setMaximumSize(d);
        setMinimumSize(d);
        setBorder(BorderFactory.createLineBorder(Color.lightGray));
        setBackground(Color.white);

        addMouseListener(this);
        addMouseMotionListener(this);
        requestFocus();
        actTool = BasicTools.PENCIL;
        currentColor = Color.BLACK;
        lastColor = Color.WHITE;
//        todo: complete text dialog.
//        textDialog = new TextDialog(Main.mainWindow);
        this.shapes = new Stack<>();
        this.removed = new Stack<>();
        this.preview = new Stack<>();
        this.group = 1;
        this.isTransparent = true;
    }

    public void changeCanvasPanelSize(int w, int h) {
        shapes.clear();
        removed.clear();
        preview.clear();

        Dimension d = new Dimension(w, h);
        setSize(d);
        setMaximumSize(d);
        setMinimumSize(d);
        repaint();
        // todo: method-set canvas size label in main window.
        // Main.mainWindow.setCanvasSizeLabel(w, h);
    }

    public void paintComponent(Graphics g) {
        //System.out.println(activeTool.toString() + "pc");
        Dimension dimension = Main.mainWindow.getCanvas().getSize();
        if (canvas == null) {
            canvas = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
            graphics2D = canvas.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }
        g.drawImage(canvas, 0, 0, null);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Shape s : shapes) {
            g2.setColor(s.getColor());
            g2.setStroke(s.getStroke());

            if (s.getShape() == BasicTools.LINE) {
                g2.drawLine(s.getX1(), s.getY1(), s.getX2(), s.getY2());
            } else if (s.getShape() == BasicTools.RECTANGLE) {
                g2.drawRect(s.getX1(), s.getY1(), s.getX2(), s.getY2());
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillRect(s.getX1(), s.getY1(), s.getX2(), s.getY2());
                }
            } else if (s.getShape() == BasicTools.ELLIPTICAL) {
                g2.drawOval(s.getX1(), s.getY1(), s.getX2(), s.getY2());
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillOval(s.getX1(), s.getY1(), s.getX2(), s.getY2());
                }
            } else if (s.getShape() == BasicTools.PENTAGON) {
                g2.drawPolygon(s.getPosX(), s.getPosY(), 5);
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillPolygon(s.getPosX(), s.getPosY(), 5);
                }
            } else if (s.getShape() == BasicTools.HEXAGON) {
                g2.drawPolygon(s.getPosX(), s.getPosY(), 6);
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillPolygon(s.getPosX(), s.getPosY(), 6);
                }
            } else if (s.getShape() == BasicTools.TRIANGLE) {
                g2.drawPolygon(s.getPosX(), s.getPosY(), 3);
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillPolygon(s.getPosX(), s.getPosY(), 3);
                }
            } else if (s.getShape() == BasicTools.ARC) {
                g2.drawArc(s.getRectangle().x, s.getRectangle().y, s.getRectangle().width, s.getRectangle().height,
                        s.getStartAngle(), s.getDrawAngle());
                if (!s.transparent) {
                    g2.fillArc(s.getRectangle().x, s.getRectangle().y, s.getRectangle().width, s.getRectangle().height,
                            s.getStartAngle(), s.getDrawAngle());
                }
            } else if (s.getShape() == BasicTools.TEXT) {
                g2.setFont(s.getFont());
                g2.drawString(s.getMessage(), s.getX1(), s.getY1());
            }
        }
        if (preview.size() > 0) {
            Shape s = preview.peek();
            g2.setColor(s.getColor());
            g2.setStroke(s.getStroke());
            if (s.getShape() == BasicTools.LINE) {
                g2.drawLine(s.getX1(), s.getY1(), s.getX2(), s.getY2());
            } else if (s.getShape() == BasicTools.RECTANGLE) {
                g2.drawRect(s.getX1(), s.getY1(), s.getX2(), s.getY2());
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillRect(s.getX1(), s.getY1(), s.getX2(), s.getY2());
                }
            } else if (s.getShape() == BasicTools.ELLIPTICAL) {
                g2.drawOval(s.getX1(), s.getY1(), s.getX2(), s.getY2());
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillOval(s.getX1(), s.getY1(), s.getX2(), s.getY2());
                }
            } else if (s.getShape() == BasicTools.PENTAGON) {
                g2.drawPolygon(s.getPosX(), s.getPosY(), 5);
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillPolygon(s.getPosX(), s.getPosY(), 5);
                }
            } else if (s.getShape() == BasicTools.HEXAGON) {
                g2.drawPolygon(s.getPosX(), s.getPosY(), 6);
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillPolygon(s.getPosX(), s.getPosY(), 6);
                }
            } else if (s.getShape() == BasicTools.TRIANGLE) {
                g2.drawPolygon(s.getPosX(), s.getPosY(), 3);
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillPolygon(s.getPosX(), s.getPosY(), 3);
                }
            } else if (s.getShape() == BasicTools.ARC) {
                var rec = s.getRectangle();
                g2.drawArc(rec.x, rec.y, rec.width, rec.height, s.getStartAngle(), s.getDrawAngle());
                if (!s.transparent) {
                    g2.fillArc(rec.x, rec.y, rec.width, rec.height,
                            s.getStartAngle(), s.getDrawAngle());
                }
            }
        }
    }

    public void clear() {
        Dimension dimension = Main.mainWindow.getCanvas().getSize();
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, dimension.width, dimension.height);
        shapes.removeAllElements();
        removed.removeAllElements();
        Main.mainWindow.getCanvas().repaint();
        graphics2D.setColor(currentColor);
    }

    public void setTool(BasicTools tool) {
        this.actTool = tool;
    }

    public void setInkPanel(int w, int h) {
        // -----** 存疑 **-----
        // 这里必须用argb吗？
        canvas = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        graphics2D = canvas.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.setSize(w - 3, h - 3);
        this.setPreferredSize(new Dimension(w - 3, h - 3));
        clear();
    }

    public void setImage(BufferedImage image) {
        graphics2D.dispose();
        this.setInkPanel(image.getWidth(), image.getHeight());
        canvas = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        graphics2D = canvas.createGraphics();
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
