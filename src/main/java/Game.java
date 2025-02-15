import org.ice1000.jimgui.JImGui;
import org.ice1000.jimgui.util.JniLoader;

import java.util.ArrayList;

import static com.sun.java.accessibility.util.AWTEventMonitor.*;

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
    public Camera camera;
    public Screen screen;
    private boolean running;
    private boolean fire0 = false, fire1 = false, fire2 = false;
    private boolean idle = true;

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

    public static void main(String[] args) {
        System.setProperty("os.name", "Windows 10");
        JniLoader.load();

        try (JImGui imGui = new JImGui()) {
            Game game = new Game(imGui);
            game.run(imGui);
        }
    }

    public void render(JImGui imGui) {
        imGui.text("Game Rendering...");

        if (idle) imGui.image(Texture.handNormal.getImage(), 64, 64);
        if (fire0) imGui.image(Texture.handFire.getImage(), 64, 64);
        if (fire1) imGui.image(Texture.hand1BeforeFire.getImage(), 64, 64);
        if (fire2) imGui.image(Texture.hand2BeforeFire.getImage(), 64, 64);
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
            render(imGui);
            imGui.render();
        }
    }
}
