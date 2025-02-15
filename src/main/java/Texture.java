import org.ice1000.jimgui.*;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.*;
import javax.imageio.ImageIO;

// This class contains image utilities
public class Texture {

    public static JImGui imGui;
    public static Texture wood, brick, bluestone, stone, floor, ceiling, handNormal, handFire, hand2BeforeFire, hand1BeforeFire;

    static {
        try {
            wood = new Texture("res/room/wood.jpg", 64, imGui);
            bluestone = new Texture("res/room/bluestone.jpg", 64, imGui);
            stone = new Texture("res/room/greystone.jpg", 64, imGui);
            floor = new Texture("res/room/floor.jpg", 64, imGui);
            ceiling = new Texture("res/room/ceiling.jpg", 64, imGui);
            handNormal = new Texture("res/hand/handNormal.png", 64, imGui);
            handFire = new Texture("res/hand/handFire.png", 64, imGui);
            hand2BeforeFire = new Texture("res/hand/hand2BeforeFire.png", 64, imGui);
            hand1BeforeFire = new Texture("res/hand/hand1BeforeFire.png", 64, imGui);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public final int SIZE;
    public int[] pixels;
    public BufferedImage image;
    public JImTextureID textureID;
    private String loc;


    // Konstruktor
    public Texture(String location, int size, JImGui imGui) throws IOException {
        loc = location;
        SIZE = size;
        pixels = new int[SIZE * SIZE]; // Initialisiere das Pixel-Array
        load(imGui);  // Lade das Bild und erstelle die Textur
    }

    private void load(JImGui imGui) {
        try {
            image = ImageIO.read(new File(loc));
            ByteBuffer buffer = loadTexture(image);

            //textureID = imGui.createTexture(buffer, image.getWidth(), image.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ByteBuffer loadTexture(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Create a buffer to store the image data in RGBA format
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * width * height);

        // Get the image's pixel data and store it in the buffer
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                byte r = (byte) ((pixel >> 16) & 0xFF);
                byte g = (byte) ((pixel >> 8) & 0xFF);
                byte b = (byte) (pixel & 0xFF);
                byte a = (byte) ((pixel >> 24) & 0xFF);

                buffer.put(r);
                buffer.put(g);
                buffer.put(b);
                buffer.put(a);
            }
        }

        // Flip the buffer before using it
        buffer.flip();

        return buffer;
    }

    // Bild zurückgeben
    public @NotNull JImTextureID getImage() {
        return textureID;
    }

}
