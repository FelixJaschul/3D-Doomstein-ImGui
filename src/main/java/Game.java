import org.ice1000.jimgui.JImGui;
import org.ice1000.jimgui.JImVec4;
import org.ice1000.jimgui.util.JniLoader;

import java.util.ArrayList;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;
import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseMotionListener;

// This class has the main game loop and map data
public class Game {

    // TODO Create Level-Editor
    public static int[][] map = {
            {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 2, 2, 2},
            {2, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 2},
            {2, 2, 2, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 2, 2, 2},
            {2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}
    };

    public int mapWidth = 16;
    public int mapHeight = 16;
    public int n = 2; // Skalierungsfaktor
    public ArrayList<Texture> textures;
    public Texture texture;
    public Camera camera;
    public Screen screen;
    private boolean running;

    public Game(JImGui imGui) {
        int width = 480 * n;
        int height = 640 * n;

        textures = new ArrayList<>();
        camera = new Camera(4.5, 4.5, 1, 0, 0, -.66, width, height, imGui);
        screen = new Screen(map, mapWidth, mapHeight, textures, height, width);

        textures.add(Texture.wood);
        textures.add(Texture.brick);
        textures.add(Texture.bluestone);
        textures.add(Texture.stone);
        textures.add(Texture.floor);
        textures.add(Texture.ceiling);

        addKeyListener(camera);
        addMouseMotionListener(camera);
    }

    public void run(JImGui imGui) {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;

        int[] pixels = new int[screen.width * screen.height];

        while (!imGui.windowShouldClose()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                pixels = screen.update(camera, pixels);
                camera.update(map);
                delta--;
            }

            imGui.initNewFrame();

            imGui.setWindowTitle("Doomstein");
            imGui.setBackground(JImVec4.fromHSV(0.55f, 0f, 1.0f));
            imGui.text("Game Rendering...");
            // imGui.image(Texture.wood.getImage(), 64, 64);

            imGui.render();
        }
    }

    public static void main(String[] args) {
        System.setProperty("os.name", "Windows 10");
        JniLoader.load();

        try (JImGui imGui = new JImGui()) {
            Game game = new Game(imGui);
            game.run(imGui);
        }
    }
}
