package lab11_csp.util;

public class Utils {
    public static <T> void print(T object, Color color) {
        System.out.println(color(object, color));
    }

    public static <T> String color(T object, Color color) {
        return color + object.toString() + Color.RESET;
    }

    public static void sleep(int nanos) {
        try {
            Thread.sleep(nanos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
