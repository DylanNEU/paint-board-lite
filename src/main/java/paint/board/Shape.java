package paint.board;
import java.awt.*;


public class Shape {

    // 是否透明
    public boolean transparent;

    public int group = 0;

    int x1, x2;
    int y1, y2;

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

    public Shape(int x1, int y1, int fontSize, Font font, Color color, BasicStroke stroke, BasicTools shape, String message) {
        this.x1 = x1;
        this.y1 = y1;
        this.y2 = 0;
        this.font = font;
        this.x2 = fontSize;
        this.color = color;
        this.stroke = stroke;
        this.shape = shape;
        this.group = 0;
        this.message = message;
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
    }

    /**
     * setPentagonPosition: 从最上方顶点开始，按照逆时针方向依次计算五个顶点坐标，并存储到pos数组中
     */
    private void setPentagonPosition() {
        this.posX = new int[]{
                x1 + x2 / 2,  // 最上方的顶点
                x1, // 最左顶点
                x1 + (int) (x2 / 4.3), // 左下底边顶点
                x1 + (int) (x2 * 3.3 / 4.3), // 右下底边顶点
                x1 + x2  // 最右顶点
        };
        this.posY = new int[]{
                y1, // 最上的顶点
                y1 + (int) (((Math.sqrt(3) - 0.9) / 2) * y2), // 最左边顶点
                y1 + y2, // 最下底边的两个顶点
                y1 + y2,
                y1 + (int) (((Math.sqrt(3) - 0.9) / 2) * y2)  // 最右边顶点
        };
    }

    /**
     * setHexagonPosition: 从左上方顶点开始，按照逆时针方向依次计算六个顶点坐标，并存储到pos数组中
     */
    private void setHexagonPosition() {
        this.posX = new int[]{
                x1 + x2 / 4,
                x1,
                x1 + x2 / 4,
                x1 + x2 * 3 / 4,
                x1 + x2,
                x1 + x2 * 3 / 4
        };
        this.posY = new int[]{
                y1,
                y1 + y2 / 2,
                y1 + y2,
                y1 + y2,
                y1 + y2 / 2,
                y1
        };
    }

    /**
     * setTrianglePosition: 从上方顶点开始，按照逆时针方向依次计算三个顶点坐标，并存储到pos数组中
     */
    private void setTrianglePosition() {
        this.posX = new int[]{
                x1 + x2 / 2,
                x1,
                x1 + x2
        };
        this.posY = new int[]{
                y1,
                y1 + y2,
                y1 + y2
        };
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

    public int[] getPosX() {
        return posX;
    }

    public int[] getPosY() {
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
