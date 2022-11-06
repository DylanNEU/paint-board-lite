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
    private Color fillColor;

    // 渲染器
    private BasicStroke stroke;
    // 备注
    private String message;

    // 多边形各顶点坐标（顺时针）
    private int[] pointsX;
    private int[] pointsY;

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

}
