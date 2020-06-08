package pkg.engine.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {

	public static Sprite loadSprite(String path) {
		BufferedImage image = loadBufferedImage(path);

		Sprite sprite = new Sprite(image.getWidth(), image.getHeight());
		sprite.pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
		
		return sprite;
	}

	public static BufferedImage loadBufferedImage(String path) {
		try {
			return ImageIO.read(Sprite.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public final int w, h;
	public int[] pixels;
	
	public Sprite(int w, int h) {
		this.w = w;
		this.h = h;
		pixels = new int[w * h];
	}
	
	public Sprite cut(int xOffs, int yOffs, int w, int h) {
        Sprite result = new Sprite(w, h);

        for (int i = 0; i < h; i++) {
            int yy = yOffs + i;
            if (yy < 0 || yy >= this.h) continue;
            for (int j = 0; j < w; j++) {
                int xx = xOffs + j;
                if (xx < 0 || xx >= this.w) continue;
                result.pixels[j + i * w] = pixels[xx + yy * this.w];
            }
        }

        return result;
    }

    public Sprite[] cut1D(int xOffs, int yOffs, int w, int h, int frames) {
        Sprite[] result = new Sprite[frames];

        for (int i = 0; i < frames; i++) {
            result[i] = new Sprite(w, h);

            for (int yy = 0; yy < h; yy++) {
                int yp = yOffs + yy;
                if (yp < 0 || yp >= this.h) continue;
                for (int xx = 0; xx < w; xx++) {
                    int xp = (w * i) + xx + xOffs;

                    result[i].pixels[xx + yy * w] = pixels[xp + yp * this.w];
                }
            }
        }

        return result;
    }

    public Sprite[][] cut2D(int xOffs, int yOffs, int w, int h, int xFrames, int yFrames) {
    	if(xFrames == -1) xFrames = (this.w - xOffs) / w;
    	if(yFrames == -1) yFrames = (this.h - yOffs) / h;
    	
        Sprite[][] result = new Sprite[xFrames][yFrames];

        for (int y = 0; y < yFrames; y++) {
            for (int x = 0; x < xFrames; x++) {
                result[x][y] = new Sprite(w, h);

                for (int yy = 0; yy < h; yy++) {
                    int yp = (h * y) + yy + yOffs;
                    if(yp < 0 || yp >= this.h) continue;
                    for (int xx = 0; xx < w; xx++) {
                        int xp = (w * x) + xx + xOffs;
                        if(xp < 0 || xp >= this.w) continue;

                        result[x][y].pixels[xx + yy * w] = pixels[xp + yp * this.w];
                    }
                }
            }
        }

        return result;
    }
}
