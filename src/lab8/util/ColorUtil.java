package lab8.util;

public class ColorUtil {
    public static <T> void print(T object, Color color) {
        System.out.println(color(object, color));
    }

    public static <T> String color(T object, Color color) {
        return color + object.toString() + Color.RESET;
    }
}
