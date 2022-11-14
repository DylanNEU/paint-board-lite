package paint.board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Stack;

import static paint.board.ArcStatus.*;
import static paint.board.BasicTools.*;

public class CanvasPanelListener extends JPanel implements MouseListener, MouseMotionListener {

    public static boolean isInCanvas = false;

    /**
     * x1, y1为鼠标按下时的位置
     * x2, y2为鼠标移动过程中的实时位置
     */
    private int x1, x2, y1, y2;
    private boolean dragged = false;
    private Color currentColor;
    private Color lastColor;
    private int grouped;
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
    private ArcStatus drawStatus = NOT_DRAWING;
    private ArcStatus direction = NO_DIRECTION;
    private Dimension center, startPoint;
    private int radius;
    private Vec3 benchmark;
    private Rectangle rectangle;

    /**
     * 当前被选择的形状
     */
    private Shape selectedShape;

    public CanvasPanelListener() {
        setSize(500, 500);
        Dimension d = new Dimension(500, 500);
        setMinimumSize(d);
        setMaximumSize(d);
        setBorder(BorderFactory.createLineBorder(Color.lightGray));
        setBackground(Color.white);

        addMouseListener(this);
        addMouseMotionListener(this);
        requestFocus();
        actTool = BasicTools.PENCIL;
        //System.out.println("create");
        currentColor = Color.BLACK;
        lastColor = Color.WHITE;
//        todo: text dialog
//        textDialog = new TextDialog(StartUp.mainWindow);

        this.shapes = new Stack<>();
        this.removed = new Stack<>();
        this.grouped = 1;
        this.preview = new Stack<>();
        this.isTransparent = true;
    }

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
        actTool = PENCIL;
        currentColor = Color.BLACK;
        lastColor = Color.WHITE;
//        todo: complete text dialog.
//        textDialog = new TextDialog(Main.mainWindow);
        this.shapes = new Stack<>();
        this.removed = new Stack<>();
        this.preview = new Stack<>();
        this.grouped = 1;
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
        Main.mainWindow.setCanvasSizeLabel(w, h);
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

            if (s.getShape() == LINE) {
                g2.drawLine(s.getX1(), s.getY1(), s.getX2(), s.getY2());
            } else if (s.getShape() == RECTANGLE) {
                g2.drawRect(s.getX1(), s.getY1(), s.getX2(), s.getY2());
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillRect(s.getX1(), s.getY1(), s.getX2(), s.getY2());
                }
            } else if (s.getShape() == ELLIPTICAL) {
                g2.drawOval(s.getX1(), s.getY1(), s.getX2(), s.getY2());
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillOval(s.getX1(), s.getY1(), s.getX2(), s.getY2());
                }
            } else if (s.getShape() == PENTAGON) {
                g2.drawPolygon(s.getPosX(), s.getPosY(), 5);
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillPolygon(s.getPosX(), s.getPosY(), 5);
                }
            } else if (s.getShape() == HEXAGON) {
                g2.drawPolygon(s.getPosX(), s.getPosY(), 6);
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillPolygon(s.getPosX(), s.getPosY(), 6);
                }
            } else if (s.getShape() == TRIANGLE) {
                g2.drawPolygon(s.getPosX(), s.getPosY(), 3);
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillPolygon(s.getPosX(), s.getPosY(), 3);
                }
            } else if (s.getShape() == ARC) {
                g2.drawArc(s.getRectangle().x, s.getRectangle().y, s.getRectangle().width, s.getRectangle().height,
                        s.getStartAngle(), s.getDrawAngle());
                if (!s.transparent) {
                    g2.fillArc(s.getRectangle().x, s.getRectangle().y, s.getRectangle().width, s.getRectangle().height,
                            s.getStartAngle(), s.getDrawAngle());
                }
            } else if (s.getShape() == TEXT) {
                g2.setFont(s.getFont());
                g2.drawString(s.getMessage(), s.getX1(), s.getY1());
            }
        }
        if (preview.size() > 0) {
            Shape s = preview.peek();
            g2.setColor(s.getColor());
            g2.setStroke(s.getStroke());
            if (s.getShape() == LINE) {
                g2.drawLine(s.getX1(), s.getY1(), s.getX2(), s.getY2());
            } else if (s.getShape() == RECTANGLE) {
                g2.drawRect(s.getX1(), s.getY1(), s.getX2(), s.getY2());
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillRect(s.getX1(), s.getY1(), s.getX2(), s.getY2());
                }
            } else if (s.getShape() == ELLIPTICAL) {
                g2.drawOval(s.getX1(), s.getY1(), s.getX2(), s.getY2());
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillOval(s.getX1(), s.getY1(), s.getX2(), s.getY2());
                }
            } else if (s.getShape() == PENTAGON) {
                g2.drawPolygon(s.getPosX(), s.getPosY(), 5);
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillPolygon(s.getPosX(), s.getPosY(), 5);
                }
            } else if (s.getShape() == HEXAGON) {
                g2.drawPolygon(s.getPosX(), s.getPosY(), 6);
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillPolygon(s.getPosX(), s.getPosY(), 6);
                }
            } else if (s.getShape() == TRIANGLE) {
                g2.drawPolygon(s.getPosX(), s.getPosY(), 3);
                if (!s.transparent) {
                    g2.setColor(s.getFillColor());
                    g2.fillPolygon(s.getPosX(), s.getPosY(), 3);
                }
            } else if (s.getShape() == ARC) {
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

    public void setColor(Color color) {
        lastColor = currentColor;
        currentColor = color;
        graphics2D.setColor(color);
    }

    public void setThickness(float f) {
        stroke = new BasicStroke(f);
        graphics2D.setStroke(stroke);
    }

    public void setTransparent(Boolean b) {
        this.isTransparent = b;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public int getDistance(Dimension d1, Dimension d2) {
        return (int) Math.sqrt((d1.height - d2.height) * (d1.height - d2.height) + (d1.width - d2.width) * (d1.width - d2.width));
    }

    // todo: shape fill

    public void undo() {
        if (shapes.size() == 0) {
            return;
        }

        Shape last = shapes.pop();
        removed.push(last);

        if (last.group == 0) {
            repaint();
        } else {
            while (!shapes.isEmpty() && shapes.peek().group == last.group) {
                removed.push(shapes.pop());
                repaint();
            }
        }
    }

    public void redo() {
        if (removed.size() > 0 && removed.peek().group == 0) {
            shapes.push(removed.pop());
            repaint();
        } else if (removed.size() > 0 && removed.peek().group != 0) {
            Shape last = removed.pop();
            shapes.push(last);
            while (!removed.isEmpty() && removed.peek().group == last.group) {
                shapes.push(removed.pop());
                repaint();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (actTool == ARC) {
//            System.out.println(drawStatus);
            if (drawStatus == NOT_DRAWING) {
                center = new Dimension(e.getX(), e.getY());
                drawStatus = DEFINED_CENTER;
            } else if (drawStatus == DEFINED_CENTER) {
                startPoint = new Dimension(e.getX(), e.getY());
                radius = getDistance(startPoint, center);
                benchmark = new Vec3(startPoint.width - center.width, -startPoint.height + center.height, 0);
                drawStatus = DEFINED_R;
                rectangle = new Rectangle(center.width - radius, center.height - radius,
                        2 * radius, 2 * radius);
            } else if (drawStatus == DEFINED_R) {
                if (preview.size() != 0) {
                    shapes.push(preview.pop());
                    preview.clear();
                }
                drawStatus = NOT_DRAWING;
                direction = NO_DIRECTION;
            }
        } else if (actTool == STRAW) {
            int x = e.getX(), y = e.getY();
            x += Main.mainWindow.getLocation().x;
            y += Main.mainWindow.getLocation().y;
            x += Main.mainWindow.getStretcher().getLocation().x;
            y += Main.mainWindow.getStretcher().getLocation().y;
            x += getLocation().x;
            y += getLocation().y;
            // todo: why?
            x += 8;
            y += 31;
            // ** ---------------- **
            try {
                Robot robot = new Robot();
                Color color = robot.getPixelColor(x, y);
                Main.mainWindow.setLastColor(getCurrentColor());
                setColor(color);
                Main.mainWindow.setCurColor(getCurrentColor());
            } catch (AWTException ex) {
                throw new RuntimeException(ex);
            }
        } else if (actTool == DRAG) {
            for (var shape : shapes) {
                if (shape.onPressed(x1, y1) == 1) {
                    selectedShape = shape;
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x1 = e.getX();
        y1 = e.getY();
        for (var shape : shapes) {
            System.out.print(shape);
            if (shape.onPressed(x1, y1) == 1) {
                selectedShape = shape;
                System.out.print("<- this is selected shape");
                break;
            }
            System.out.println();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (preview.size() != 0 && actTool != ARC) {
            shapes.push(preview.pop());
            preview.clear();
        } else if (actTool == TEXT) {
            // todo: there needs a text dialog.


            // -------- *** ---------
        } else if (actTool == BUCKET) {
            // todo: there needs a bucket tool.


            // -------- *** ---------
        } else if (actTool == DRAG) {
            int cx = x2 - x1;
            int cy = y2 - y1;
            if (selectedShape != null) {
                selectedShape.moving(cx, cy);
                System.out.println(selectedShape + "-> finished select");
                selectedShape.setIsChosen(0);
                selectedShape = null;
            }
        }
        if (dragged) {
            grouped++;
            dragged = false;
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        isInCanvas = true;

        Cursor cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        setCursor(cursor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        isInCanvas = false;
        Main.mainWindow.setMousePositionLabel(0, 0);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Main.mainWindow.setMousePositionLabel(e.getX(), e.getY());
        Color c1 = currentColor, c2 = lastColor;
        if (SwingUtilities.isRightMouseButton(e)) {
            c1 = c2;
            c2 = currentColor;
        }
        x2 = e.getX();
        y2 = e.getY();
        this.dragged = true;

        if (actTool == ERASER) {
            shapes.push(new Shape(x1, y1, x2, y2, Color.WHITE, stroke, LINE, grouped));
            Main.mainWindow.getCanvas().repaint();
            x1 = x2;
            y1 = y2;
        } else if (actTool == PENCIL) {
            shapes.push(new Shape(x1, y1, x2, y2, c1, stroke, LINE, grouped));
            Main.mainWindow.getCanvas().repaint();
            x1 = x2;
            y1 = y2;
        } else if (actTool == LINE) {
            preview.push(new Shape(x1, y1, x2, y2, c1, stroke, LINE, c2, isTransparent));
            Main.mainWindow.getCanvas().repaint();
        } else if (actTool == RECTANGLE) {
            pushPreview(c1, c2, RECTANGLE);
        } else if (actTool == ELLIPTICAL) {
            pushPreview(c1, c2, ELLIPTICAL);
        } else if (actTool == PENTAGON) {
            pushPreview(c1, c2, PENTAGON);
        } else if (actTool == HEXAGON) {
            pushPreview(c1, c2, HEXAGON);
        } else if (actTool == TRIANGLE) {
            pushPreview(c1, c2, TRIANGLE);
        } else if (actTool == DRAG) {
            // pushPreview(c1, c2, selectedShape.getShape());
            //  pushPreview(selectedShape.getColor(), selectedShape.getFilledColor(), selectedShape.getShape());
        }
    }

    private void pushPreview(Color c1, Color c2, BasicTools rectangle) {
        if (actTool == DRAG) {
//            preview.push(new Shape(x1, y1, x2, y2, c1, stroke, rectangle, c2, isTransparent));
        } else {
            if (x1 < x2 && y1 < y2) {
                preview.push(new Shape(x1, y1, x2 - x1, y2 - y1, c1, stroke, rectangle, c2, isTransparent));
            } else if (x2 < x1 && y1 < y2) {
                preview.push(new Shape(x2, y1, x1 - x2, y2 - y1, c1, stroke, rectangle, c2, isTransparent));
            } else if (x1 < x2 && y2 < y1) {
                preview.push(new Shape(x1, y2, x2 - x1, y1 - y2, c1, stroke, rectangle, c2, isTransparent));
            } else if (x2 < x1 && y2 < y1) {
                preview.push(new Shape(x2, y2, x1 - x2, y1 - y2, c1, stroke, rectangle, c2, isTransparent));
            }
            Main.mainWindow.getCanvas().repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Main.mainWindow.setMousePositionLabel(e.getX(), e.getY());
        if (actTool == ARC) {
            Color c1 = currentColor, c2 = lastColor;
            x2 = e.getX();
            y2 = e.getY();
            if (drawStatus == NOT_DRAWING) {
                return;
            } else if (drawStatus == DEFINED_CENTER) {
                preview.push(new Shape(x1, y1, x2, y2, c1, stroke, LINE, c1, isTransparent));
                Main.mainWindow.getCanvas().repaint();
            } else if (drawStatus == DEFINED_R) {
                //System.out.println(direction);
                if (direction == NO_DIRECTION) {
                    Vec3 temp = new Vec3(x2 - center.width, -y2 + center.height, 0);
                    direction = benchmark.calcDirection(temp);
                } else if (direction == LEFT) {
                    //System.out.println(benchmark.print());
                    int startAngle = benchmark.normalization().calcAngle(new Vec3(1, 0, 0));
                    Vec3 temp = new Vec3(x2 - center.width, -y2 + center.height, 0);
                    int drawAngle = temp.normalization().calcAngle(benchmark.normalization());
                    //System.out.println(startAngle + " " + drawAngle);
                    //System.out.println(temp.calcDirection(benchmark));
                    preview.push(new Shape(rectangle, c1, stroke, ARC, c1, isTransparent, startAngle, drawAngle));
                } else if (direction == RIGHT) {
                    int startAngle = benchmark.normalization().calcAngle(new Vec3(1, 0, 0));
                    Vec3 temp = new Vec3(x2 - center.width, -y2 + center.height, 0);
                    int drawAngle = -(360 - temp.normalization().calcAngle(benchmark.normalization()));
                    preview.push(new Shape(rectangle, c1, stroke, ARC, c1, isTransparent, startAngle, drawAngle));
                }
            }
            Main.mainWindow.getCanvas().repaint();
        }
    }

    public void getShapes() {
        System.out.println(shapes);
    }
}
