package paint.board;

public class Vec3 {
    public static final double PI = Math.acos(-1);
    private double x, y, z;
    private double modulus;

    public Vec3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        modulus = Math.sqrt(x * x + y * y + z * z);
    }

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        modulus = Math.sqrt(x * x + y * y + z * z);
    }

    public double getModulus() {
        return modulus;
    }

    public ArcStatus calcDirection(Vec3 vector3) {
        double val = x * vector3.y - y * vector3.x;
        if (val < 0) {
            return ArcStatus.RIGHT;
        } else if (val > 0) {
            return ArcStatus.LEFT;
        } else {
            return ArcStatus.NO_DIRECTION;
        }
    }

    public int calcAngle(Vec3 vector3) {
        System.out.println(Math.acos((x * vector3.x + y * vector3.y) / (modulus * vector3.getModulus())));
        int angle = (int) ((180 / PI) * Math.acos((x * vector3.x + y * vector3.y) / (modulus * vector3.getModulus())));

        if (calcDirection(vector3) == ArcStatus.LEFT) {
            System.out.println(angle + " sd");
            angle = 360 - angle;
        }
        return angle;
    }

    public String print() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public Vec3 normalization() {
        return new Vec3(x / modulus, y / modulus, z / modulus);
    }

}

