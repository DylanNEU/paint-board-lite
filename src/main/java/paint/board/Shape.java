package paint.board;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;


public class Shape {

    public final int bound = 5;
    // 是否透明
    public boolean transparent;
    public int group = 0;
    public int x1, x2;
    public int y1, y2;
    public int isChosen;
    // 线条颜色
    private Color color;
    // 填充颜色
    private Color filledColor;

    // 渲染器
    private BasicStroke stroke;
    // 备注
    private String message;

    // 多边形各顶点坐标（顺时针）
    private int[] posX;
    private int[] posY;

    // 圆弧用
    private Rectangle rectangle;
    private int startAngle;
    private int drawAngle;

    private BasicTools shape;
    private Font font;

    public Shape(int x1, int y1, Font font, Color color, BasicStroke stroke, BasicTools shape, String message) {

        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        Rectangle rec = font.getStringBounds("a", frc).getBounds();
        int height = (int) rec.getHeight();
        int width = (int) rec.getWidth();
        this.x1 = x1;
        this.y1 = y1;
        this.y2 = y1 - height;
        this.font = font;
        this.x2 = x1 + width * message.length();
        this.color = color;
        this.stroke = stroke;
        this.shape = shape;
        this.group = 0;
        this.message = message;
        this.isChosen = 0;
    }

    public Shape(int x1, int y1, int x2, int y2, Color color, BasicStroke stroke, BasicTools shape, int group) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.color = color;
        this.stroke = stroke;
        this.shape = shape;
        this.group = group;
        this.isChosen = 0;
    }

    public Shape(int x1, int y1, int x2, int y2, Color color, BasicStroke stroke, BasicTools shape, Color fill, boolean isTransparent) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.color = color;
        this.stroke = stroke;
        this.shape = shape;
        this.group = 0;
        this.filledColor = fill;
        this.transparent = isTransparent;
        if (shape == BasicTools.PENTAGON) {
            setPentagonPosition();
        }
        if (shape == BasicTools.HEXAGON) {
            setHexagonPosition();
        }
        if (shape == BasicTools.TRIANGLE) {
            setTrianglePosition();
        }
        this.isChosen = 0;
    }

    public Shape(Rectangle rectangle, Color c1, BasicStroke stroke, BasicTools arc, Color fill, boolean isTransparent, int startAngle, int drawAngle) {
        this.rectangle = rectangle;
        this.color = c1;
        this.stroke = stroke;
        this.shape = arc;
        this.filledColor = fill;
        this.transparent = isTransparent;
        this.startAngle = startAngle;
        this.drawAngle = drawAngle;
        this.isChosen = 0;
    }

    public Shape(Shape shape) {
        this.transparent = shape.transparent;
        this.group = shape.group;
        this.x1 = shape.x1;
        this.x2 = shape.x2;
        this.y1 = shape.y1;
        this.y2 = shape.y2;
        this.isChosen = shape.isChosen;
        this.color = shape.color;
        this.filledColor = shape.filledColor;
        this.stroke = shape.stroke;
        this.message = shape.message;
        this.posX = shape.posX;
        this.posY = shape.posY;
        this.rectangle = shape.rectangle;
        this.startAngle = shape.startAngle;
        this.drawAngle = shape.drawAngle;
        this.shape = shape.shape;
        this.font = shape.font;
    }


    public int onPressed(int x, int y) {
        if (x1 < x2 && y1 < y2) {
            if (x > x1 - bound && x < x2 + bound && y > y1 - bound && y < y2 + bound) isChosen = 1;
        } else if (x1 > x2 && y1 > y2) {
            if (x > x2 - bound && x < x1 + bound && y > y2 - bound && y < y1 + bound) isChosen = 1;
        } else if (x1 > x2 && y1 < y2) {
            if (x > x2 - bound && x < x1 + bound && y > y1 - bound && y < y2 + bound) isChosen = 1;
        } else if (x1 < x2 && y1 > y2) {
            if (x > x1 - bound && x < x2 + bound && y > y2 - bound && y < y1 + bound) isChosen = 1;
        } else {
            isChosen = 0;
        }
        System.out.println("x = " + x + ", y = " + y);
        System.out.println("{x1 = " + x1 + ", y1 = " + y1 + "}, {x2= " + x2 + ", y2 = " + y2 + "}");
        return isChosen;
    }

    public void setIsChosen(int isChosen) {
        this.isChosen = isChosen;
    }

    public void moving(int a, int b) {
        if (isChosen == 1) {
            this.setX1(x1 + a);
            this.setX2(x2 + a);
            this.setY1(y1 + b);
            this.setY2(y2 + b);
        } else {
            this.setX1(x1);
            this.setX2(x2);
            this.setY1(y1);
            this.setY2(y2);
        }
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    /**
     * setPentagonPosition: 从最上方顶点开始，按照逆时针方向依次计算五个顶点坐标，并存储到pos数组中
     */
    private void setPentagonPosition() {
        int w = this.getWidth();
        int h = this.getHeight();
        this.posX = new int[]{
                x1 + w / 2,  // 最上方的顶点
                x1, // 最左顶点
                x1 + (int) (w / 4.3), // 左下底边顶点
                x1 + (int) (w * 3.3 / 4.3), // 右下底边顶点
                x1 + w  // 最右顶点
        };
        this.posY = new int[]{
                y1, // 最上的顶点
                y1 + (int) (((Math.sqrt(3) - 0.9) / 2) * h), // 最左边顶点
                y1 + h, // 最下底边的两个顶点
                y1 + h,
                y1 + (int) (((Math.sqrt(3) - 0.9) / 2) * h)  // 最右边顶点
        };
    }

    /**
     * setHexagonPosition: 从左上方顶点开始，按照逆时针方向依次计算六个顶点坐标，并存储到pos数组中
     */
    private void setHexagonPosition() {
        int w = this.getWidth();
        int h = this.getHeight();
        this.posX = new int[]{
                x1 + w / 4,
                x1,
                x1 + w / 4,
                x1 + w * 3 / 4,
                x1 + w,
                x1 + w * 3 / 4
        };
        this.posY = new int[]{
                y1,
                y1 + h / 2,
                y1 + h,
                y1 + h,
                y1 + h / 2,
                y1
        };
    }

    /**
     * setTrianglePosition: 从上方顶点开始，按照逆时针方向依次计算三个顶点坐标，并存储到pos数组中
     */
    private void setTrianglePosition() {
        int w = this.getWidth();
        int h = this.getHeight();
        this.posX = new int[]{
                x1 + w / 2,
                x1,
                x1 + w
        };
        this.posY = new int[]{
                y1,
                y1 + h,
                y1 + h
        };
    }

    /**
     * setCompassPositoin: 从上方顶点开始，按照逆时针方向依次计算八个顶点坐标，并存储到pos数组中。
     *
     * @apiNote 四角星（指南针形）
     */
    private void setCompassPosition() {
        int w = this.getWidth();
        int h = this.getHeight();
        this.posX = new int[]{
                x1 + w / 2,
                x1 + 2 * w / 5,
                x1,
                x1 + 2 * w / 5,
                x1 + w / 2,
                x1 + 3 * w / 5,
                x1 + w,
                x1 + 3 * w / 5
        };
        this.posY = new int[]{
                y1,
                y1 + 2 * h / 5,
                y1 + h / 2,
                y1 + 3 * h / 5,
                y1 + h,
                y1 + 3 * h / 5,
                y1 + h / 2,
                y1 + 2 * h / 5,
        };
    }

    /**
     * setPentagramPosition(): 从上方顶点开始，按照逆时针方向依次计算十个顶点坐标，并存储到pos数组中
     *
     * @apiNote 五角星
     */
    private void setPentagramPosition() {
        int w = this.getWidth();
        int h = this.getHeight();
        this.posX = new int[]{
                x1 + w / 2,
                x1 + 5 * w / 12,
                x1,
                x1 + 17 * w / 48,
                x1 + w / 4,
                x1 + w / 2,
                x1 + 3 * w / 4,
                x1 + 31 * w / 48,
                x1 + w,
                x1 + 7 * w / 12
        };
        this.posY = new int[]{
                y1,
                y1 + h / 3,
                y1 + h / 3,
                y1 + 2 * h / 3,
                y1 + h,
                y1 + 7 * h / 9,
                y1 + h,
                y1 + 2 * h / 3,
                y1 + h / 3,
                y1 + h / 3,
        };
    }


    public int getWidth() {
        return Math.abs(x1 - x2);
    }

//    public void draw(Graphics2D g, Color c) {
//        switch (this.shape) {
//            case TRIANGLE -> {
//                g.drawPolygon(this.getPosX(), this.getPosY(), 3);
//                if (!this.transparent) {
//                    g.setColor(this.getFillColor());
//                    g.fillPolygon(this.getPosX(), this.getPosY(), 3);
//                }
//            }
//        }
//    }

    public int getHeight() {
        return Math.abs(y1 - y2);
    }

    public Color getColor() {
        return color;
    }

    public Color getFilledColor() {
        return filledColor;
    }

    public BasicStroke getStroke() {
        return stroke;
    }

    public String getMessage() {
        return message;
    }

    private void updatePosition() {
        switch (this.shape) {
            case TRIANGLE -> this.setTrianglePosition();
            case HEXAGON -> this.setHexagonPosition();
            case PENTAGON -> this.setPentagonPosition();
            case PENTAGRAM -> this.setPentagramPosition();
            case COMPASS -> this.setCompassPosition();
        }
    }

    public int[] getPosX() {
        updatePosition();
        return posX;
    }

    public int[] getPosY() {
        updatePosition();
        return posY;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public int getStartAngle() {
        return startAngle;
    }

    public int getDrawAngle() {
        return drawAngle;
    }

    public BasicTools getShape() {
        return shape;
    }

    public Font getFont() {
        return font;
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }

    public Color getFillColor() {
        return filledColor;
    }
}
