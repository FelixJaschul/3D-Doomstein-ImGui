import org.ice1000.jimgui.JImGui;
import org.ice1000.jimgui.JImTextureID;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// This class contains image utilities
public class Texture {

    public static JImGui imGui;
    public final static Texture wood, bluestone, stone, floor, ceiling, handNormal, handFire, hand2BeforeFire, hand1BeforeFire;

    static {
        try {
            wood = new Texture("D:/Felix/Dokumente/.NvimProjects/Doomstein/res/room/wood.jpg", 64, imGui);
            bluestone = new Texture("D:/Felix/Dokumente/.NvimProjects/Doomstein/res/room/bluestone.jpg", 64, imGui);
            stone = new Texture("D:/Felix/Dokumente/.NvimProjects/Doomstein/res/room/greystone.jpg", 64, imGui);
            floor = new Texture("D:/Felix/Dokumente/.NvimProjects/Doomstein/res/room/floor.jpg", 64, imGui);
            ceiling = new Texture("D:/Felix/Dokumente/.NvimProjects/Doomstein/res/room/ceiling.jpg", 64, imGui);
            handNormal = new Texture("D:/Felix/Dokumente/.NvimProjects/Doomstein/res/hand/handNormal.png", 64, imGui);
            handFire = new Texture("D:/Felix/Dokumente/.NvimProjects/Doomstein/res/hand/handFire.png", 64, imGui);
            hand1BeforeFire = new Texture("D:/Felix/Dokumente/.NvimProjects/Doomstein/res/hand/hand1BeforeFire.png", 64, imGui);
            hand2BeforeFire = new Texture("D:/Felix/Dokumente/.NvimProjects/Doomstein/res/hand/hand2BeforeFire.png", 64, imGui);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final int size;
    private final String location;
    public int[] pixelsArray;
    public BufferedImage image;
    public JImTextureID textureID;

    public Texture(String location, int size, JImGui imGui) throws IOException {
        this.location = location;
        this.size = size;
        pixelsArray = new int[this.size * this.size];
        load(imGui);
    }

    private void load(JImGui imGui) {
        try {
            image = ImageIO.read(new File(location));
            int width = image.getWidth();
            int height = image.getHeight();
            image.getRGB(0, 0, width, height, pixelsArray, 0, width);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public JImTextureID getImage() {
        return textureID;
    }

}