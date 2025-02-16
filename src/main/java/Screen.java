import java.awt.*;
import java.util.ArrayList;

// this class contains most of the actual raycasting
public class Screen {

    public int[][] map;
    public int mapWidth, mapHeight, width, height;

    public ArrayList<Texture> textures;

    public Screen(int[][] map, int mapWidth, int mapHeight, ArrayList<Texture> textures, int width, int heihgt) {
        this.map = map;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.textures = textures;
        this.width = width;
        height = heihgt;
    }

    public int[] update(Camera camera, int[] pixels) {
        // Floor and ceiling
        for (int n = 0; n < pixels.length / 2; n++) {
            // if (pixels[n] != Color.DARK_GRAY.getRGB())
            pixels[n] = Color.BLACK.getRGB();
        }

        for (int i = pixels.length / 2; i < pixels.length; i++) {
            // if (pixels[i] != Color.gray.getRGB())
            pixels[i] = Color.BLACK.getRGB();
        }

        // Walls
        for (int x = 0; x < width; x = x + 1) {
            double cameraX = 2 * x / (double) (width) - 1;

            double raycastDirectionX = camera.xDirection + camera.xPlane * cameraX;
            double raycastDirectionY = camera.yDirection + camera.yPlane * cameraX;

            // Map position
            int mapX = (int) camera.xPosition;
            int mapY = (int) camera.yPosition;

            // length of ray from current position to next x or y-side
            double sideDistanceX;
            double sideDistanceY;

            // Length of ray from one side to next in map
            double deltaDistanceX = Math.sqrt(1 + (raycastDirectionY * raycastDirectionY) / (raycastDirectionX * raycastDirectionX));
            double deltaDistanceY = Math.sqrt(1 + (raycastDirectionX * raycastDirectionX) / (raycastDirectionY * raycastDirectionY));

            double perpWallDistance;

            int stepX, stepY; // Direction to go in x and y

            boolean hit = false; // was a wall hit

            int side = 0; // was the wall vertical or horizontal

            if (raycastDirectionX < 0) { // Figure out the step direction and initial distance to a side
                stepX = -1;
                sideDistanceX = (camera.xPosition - mapX) * deltaDistanceX;
            } else {
                stepX = 1;
                sideDistanceX = (mapX + 1.0 - camera.xPosition) * deltaDistanceX;
            }

            if (raycastDirectionY < 0) {
                stepY = -1;
                sideDistanceY = (camera.yPosition - mapY) * deltaDistanceY;
            } else {
                stepY = 1;
                sideDistanceY = (mapY + 1.0 - camera.yPosition) * deltaDistanceY;
            }

            while (!hit) { // Loop to find where the ray hits a wall
                if (sideDistanceX < sideDistanceY) { // Jump to next square
                    sideDistanceX += deltaDistanceX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistanceY += deltaDistanceY;
                    mapY += stepY;
                    side = 1;
                }

                if (map[mapX][mapY] > 0) hit = true; // Check if ray has hit a wall
            }

            // Calculate distance to the point of impact
            if (side == 0) perpWallDistance = Math.abs((mapX - camera.xPosition + (1 - stepX) / 2) / raycastDirectionX);
            else perpWallDistance = Math.abs((mapY - camera.yPosition + (1 - stepY) / 2) / raycastDirectionY);

            // Now calculate the height of the wall based on the distance from the camera
            int lineHeight;

            if (perpWallDistance > 0) lineHeight = Math.abs((int) (height / perpWallDistance));
            else lineHeight = height;

            // calculate lowest and highest pixel to fill in current stripe
            int drawStart = -lineHeight / 2 + height / 2;

            if (drawStart < 0) drawStart = 0;

            int drawEnd = lineHeight / 2 + height / 2;

            if (drawEnd >= height) drawEnd = height - 1;

            // add a texture
            int textureNumber = map[mapX][mapY] - 1;
            double wallX; // Exact position of where wall was hit

            // If it's a y-axis wall
            if (side == 1) wallX = (camera.xPosition + ((mapY - camera.yPosition + (1 - stepY) / 2) / raycastDirectionY) * raycastDirectionX);
            else wallX = (camera.yPosition + ((mapX - camera.xPosition + (1 - stepX) / 2) / raycastDirectionX) * raycastDirectionY); // X-axis wall

            wallX -= Math.floor(wallX);

            int textureX = (int) (wallX * (textures.get(textureNumber).size)); // x coordinate on the texture

            if (side == 0 && raycastDirectionX > 0) textureX = textures.get(textureNumber).size - textureX - 1;
            if (side == 1 && raycastDirectionY < 0) textureX = textures.get(textureNumber).size - textureX - 1;

            // calculate y coordinate on texture
            for (int y = drawStart; y < drawEnd; y++) {
                int color;
                int texY = (((y * 2 - height + lineHeight) << 6) / lineHeight) / 2;

                if (side == 0) color = textures.get(textureNumber).pixelsArray[textureX + (texY * textures.get(textureNumber).size)];
                else color = (textures.get(textureNumber).pixelsArray[textureX + (texY * textures.get(textureNumber).size)] >> 1) & 8355711; // Make y sides darker

                int red = (color >> 16) & 0xFF;
                int green = (color >> 8) & 0xFF;
                int blue = color & 0xFF;

                int factor = 32; // Higher factor = fewer colors, more pixel-art look
                red = (red / factor) * factor;
                green = (green / factor) * factor;
                blue = (blue / factor) * factor;

                color = (red << 16) | (green << 8) | blue;

                pixels[x + y * (width)] = color;
            }

        }

        return pixels;
    }
}
