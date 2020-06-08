package pkg.engine.gfx;

import java.awt.Color;

import pkg.engine.utils.Vector;

public class Screen {

	private static final Color	LIGHT_PURPLE_COLOR	= new Color(46, 3, 31);
	private static final Color	DARK_PURPLE_COLOR	= new Color(29, 21, 49);
	private static final Color	BLUE_COLOR			= new Color(113, 158, 255);
	private static final Color	GREEN_COLOR			= new Color(21, 74, 66);
	private static final Color	ORANGE_COLOR		= new Color(255, 199, 103);

	public static final int		ORANGE				= getCol(ORANGE_COLOR.getRed(), ORANGE_COLOR.getGreen(), ORANGE_COLOR.getBlue());
	public static final int		GREEN				= getCol(GREEN_COLOR.getRed(), GREEN_COLOR.getGreen(), GREEN_COLOR.getBlue());
	public static final int		BLUE				= getCol(BLUE_COLOR.getRed(), BLUE_COLOR.getGreen(), BLUE_COLOR.getBlue());
	public static final int		DARK_PURPLE			= getCol(DARK_PURPLE_COLOR.getRed(), DARK_PURPLE_COLOR.getGreen(), DARK_PURPLE_COLOR.getBlue());
	public static final int		LIGHT_PURPLE		= getCol(LIGHT_PURPLE_COLOR.getRed(), LIGHT_PURPLE_COLOR.getGreen(), LIGHT_PURPLE_COLOR.getBlue());

	public static final int		TRANSPARENCY_CONST	= getCol(255, 0, 255);

	public double				xOffs, yOffs;

	public static int blend(int col0, int col1) {
		return blend(col0, col1, 0.5f);
	}

	public static int blend(int col0, int col1, float step) {
		int r0 = (col0 >> 16) & 0xff;
		int g0 = (col0 >> 8) & 0xff;
		int b0 = col0 & 0xff;

		int r1 = (col1 >> 16) & 0xff;
		int g1 = (col1 >> 8) & 0xff;
		int b1 = col1 & 0xff;

		float tr = interpolate(r0, r1, step);
		float tg = interpolate(g0, g1, step);
		float tb = interpolate(b0, b1, step);

		return (int) tr << 16 | (int) tg << 8 | (int) tb;
	}

	public static float interpolate(float v0, float v1, float step) {
		return (1 - step) * v0 + step * v1;
	}

	public static int getCol(int r, int g, int b) {
		r = (int) clamp(r, 0, 255);
		g = (int) clamp(g, 0, 255);
		b = (int) clamp(b, 0, 255);
		return 255 << 24 | r << 16 | g << 8 | b;
	}

	public static final float clamp(float num, float r0, float r1) {
		if (num < r0) num = r0;
		if (num > r1) num = r1;
		return num;
	}

	public final int	w, h;
	public int[]		pixels;

	public Screen(int w, int h) {
		this.w = w;
		this.h = h;
		pixels = new int[w * h];
	}

	public void setOffs(double xOffs, double yOffs) {
		this.xOffs = xOffs;
		this.yOffs = yOffs;
	}

	public void clear() {
		fill(0);
	}

	public void fill(int col) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = col;
		}
	}

	public void drawSprite(Sprite sprite, Vector pos) {
		drawSprite(sprite, pos, false, 0, 1, 1, 1);
	}

	public void drawSprite(Sprite sprite, int x, int y) {
		drawSprite(sprite, x, y, 0, 1, 1);
	}

	public void drawSprite(Sprite sprite, Vector pos, float scale, float alpha) {
		drawSprite(sprite, pos.getIntX(), pos.getIntY(), false, 0, scale, scale, alpha);
	}

	public void drawSprite(Sprite sprite, int x, int y, float scale, float alpha) {
		drawSprite(sprite, x, y, false, 0, scale, scale, alpha);
	}

	public void drawSprite(Sprite sprite, Vector pos, int dir, float scale, float alpha) {
		drawSprite(sprite, pos.getIntX(), pos.getIntY(), true, dir, scale, scale, alpha);
	}

	public void drawSprite(Sprite sprite, int x, int y, int dir, float scale, float alpha) {
		drawSprite(sprite, x, y, true, dir, scale, scale, alpha);
	}

	public void drawSprite(Sprite sprite, Vector pos, boolean flip, int dir, float xScale, float yScale, float alpha) {
		drawSprite(sprite, pos.getIntX(), pos.getIntY(), flip, dir, xScale, yScale, alpha);
	}

	public void drawSprite(Sprite sprite, int x, int y, boolean flip, int dir, float xScale, float yScale, float alpha) {
		drawSprite(sprite, x, y, flip, dir, 0, xScale, yScale, alpha);
	}

	public void drawSprite(Sprite sprite, Vector pos, boolean flip, int dir, double rotation, float xScale, float yScale, float alpha) {
		drawSprite(sprite, pos.getIntX(), pos.getIntY(), flip, dir, rotation, xScale, yScale, alpha);
	}

	public void drawSprite(Sprite sprite, int x, int y, boolean flip, int dir, double rotation, float xScale, float yScale, float alpha) {
		int col = 0, xPixel = 0, yPixel = 0;

		boolean rotating = Math.abs(rotation) > 0 && rotation % 360 != 0;
		rotation = Math.toRadians(rotation);

		final double sin = Math.sin(rotation);
		final double cos = Math.cos(rotation);

		boolean mirrorX = (dir & 0x01) > 0;
		boolean mirrorY = (dir & 0x02) > 0;

		int w = (int) Math.round(sprite.w * xScale);
		int h = (int) Math.round(sprite.h * yScale);

		int centerX = w / 2;
		int centerY = h / 2;

		for (int j = 0; j < h; j++) {
			int yDraw = j;
			int rotY = j - centerY;

			for (int i = 0; i < w; i++) {
				int xDraw = i;
				int rotX = i - centerX;

				double xr = (rotX * cos + rotY * sin) + centerX;
				double yr = (rotY * cos - rotX * sin) + centerY;

				if (mirrorX) xDraw = (int) ((sprite.w - 1) - (i / xScale));
				else xDraw = (int) (i / xScale);
				if (mirrorY) yDraw = (int) ((sprite.h - 1) - (j / yScale));
				else yDraw = (int) (j / yScale);

				if (flip) col = sprite.pixels[xDraw + yDraw * sprite.w];
				else col = sprite.pixels[(int) (i / xScale) + (int) (j / yScale) * sprite.w];

				xPixel = (int) Math.round(x + xr);
				yPixel = (int) Math.round(y + yr);

				if (rotating) {
					drawPixel(xPixel, yPixel, col, 1);
					drawPixel(xPixel + 1, yPixel, col, 1);
				} else {
					drawPixel(xPixel, yPixel, col, alpha);
				}
			}
		}
	}

	public void drawGradient(Vector pos, int w, int h, int dir, int col, int col1) {
		drawGradient(pos, w, h, dir, col, col1, 1);
	}

	public void drawGradient(Vector pos, int w, int h, int dir, int col, int col1, float alpha) {
		drawGradient(pos.getIntX(), pos.getIntY(), w, h, dir, col, col1, alpha);
	}

	public void drawGradient(int x, int y, int w, int h, int dir, int col, int col1) {
		drawGradient(x, y, w, h, dir, col, col1, 1f);
	}

	public void drawGradient(int x, int y, int w, int h, int dir, int col, int col1, float alpha) {
		int src = 0;

		if (dir == 0) {
			for (int yy = 0; yy < h; yy++) {
				src = blend(col, col1, (float) (yy + 1) / h);
				for (int xx = 0; xx < w; xx++) {
					drawPixel(xx + x, yy + y, src, alpha);
				}
			}
		}
		if (dir == 1) {
			for (int xx = 0; xx < w; xx++) {
				src = blend(col, col1, (float) (xx + 1) / w);
				for (int yy = 0; yy < h; yy++) {
					drawPixel(xx + x, yy + y, src, alpha);
				}
			}
		}
	}

	public void drawRect(Vector pos, int w, int h, int col) {
		drawRect(pos, w, h, col, 1);
	}

	public void drawRect(Vector pos, int w, int h, int col, float alpha) {
		drawRect(pos.getIntX(), pos.getIntY(), w, h, col, alpha);
	}

	public void drawRect(int x, int y, int w, int h, int col) {
		drawRect(x, y, w, h, col, 1);
	}

	public void drawRect(int x, int y, int w, int h, int col, float alpha) {
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				drawPixel(x + i, y + j, col, alpha);
			}
		}
	}

	public void drawPixel(Vector pos, int col) {
		drawPixel(pos, col, 1);
	}

	public void drawPixel(Vector pos, int col, float alpha) {
		drawPixel(pos.getIntX(), pos.getIntY(), col, alpha);
	}

	public void drawPixel(int x, int y, int col) {
		drawPixel(x, y, col, 1);
	}

	public void drawPixel(int x, int y, int col, float alpha) {
		alpha = clamp(alpha, 0, 1);

		if (x < 0 || y < 0 || x >= w || y >= h || col == TRANSPARENCY_CONST) return;
		else {
			x += xOffs;
			y += yOffs; 

			int originalCol = pixels[x + y * w];
			pixels[x + y * w] = blend(originalCol, col, alpha);
		}
	}

	public void drawRotatedPixel(int x, int y, int col, float alpha) {
		alpha = clamp(alpha, 0, 1);

		if (x < 0 || y < 0 || x >= w || y >= h || col == TRANSPARENCY_CONST) return;
		else {
			x += xOffs;
			y += yOffs;

			int originalCol = pixels[x + y * w];
			pixels[x + y * w] = blend(originalCol, col, alpha);
		}
	}
}
