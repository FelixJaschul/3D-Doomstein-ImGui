import org.ice1000.jimgui.JImGui;
import org.ice1000.jimgui.util.JniLoader;

public class Test {
    public static void main(String... args){
        JniLoader.load();
        try (JImGui imGui = new JImGui()) {
            // load fonts, global initializations, etc.
            while (!imGui.windowShouldClose()) {
                // some drawing-unrelated initializations
                // mostly do nothing here
                imGui.initNewFrame();
                // draw your widgets here, like this
                imGui.text("Hello, World!");
                imGui.render();
                // mostly do nothing here
            }
        }
    }
}