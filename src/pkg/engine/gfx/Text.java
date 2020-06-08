package pkg.engine.gfx;

public class Text {

	public static final String	CHARS	= "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " + "0123456789.,!?'\"-+=/\\%()<>:;    ";
	public static Sprite[] FONT_SHEET;

	public static void write(String msg, Screen screen, int x, int y, int col, int bgCol) {
		write(msg, screen, x, y, col, bgCol, false, false, 0, 1, 1, 1);
	}
	
	public static void write(String msg, Screen screen, int x, int y, int col, int bgCol, boolean flip, boolean fliptext, int dir, float xScale, float yScale, float alpha) {
		msg = msg.toUpperCase();
		if(fliptext) msg = new StringBuilder(msg).reverse().toString();
		for (int i = 0; i < msg.length(); i++) {
			int ix = CHARS.indexOf(msg.charAt(i));
			if (ix >= 0) drawSprite(FONT_SHEET[ix], screen, (int)(x + i * FONT_SHEET[ix].w * xScale), y, col, bgCol, flip, dir, xScale, yScale, alpha);
		}
	}
	
	public static void write(String msg, Screen screen, int x, int y, int col) {
		write(msg, screen, x, y, col, false, false, 0, 1, 1, 1);
	}

	public static void write(String msg, Screen screen, int x, int y, int col, boolean flip, boolean fliptext, int dir, float xScale, float yScale, float alpha) {
		msg = msg.toUpperCase();
		if(fliptext) msg = new StringBuilder(msg).reverse().toString();
		for (int i = 0; i < msg.length(); i++) {
			int ix = CHARS.indexOf(msg.charAt(i));
			if (ix >= 0) drawSprite(FONT_SHEET[ix], screen, (int)(x + i * FONT_SHEET[ix].w * xScale), y, col, flip, dir, xScale, yScale, alpha);
		}
	}
	
	public static void setFont(Sprite[] font) {
		FONT_SHEET = font;
	}

	private static void drawSprite(Sprite sprite, Screen screen, int x, int y, int col, int bgCol, boolean flip, int dir, float xScale, float yScale, float alpha) {
		int res = 0;

		int w = (int) Math.round(xScale * sprite.w);
		int h = (int) Math.round(yScale * sprite.h);
		
		boolean mirrorX = (dir & 0x01) > 0;
		boolean mirrorY = (dir & 0x02) > 0;
		
		for (int j = 0; j < h; j++) {
			int yDraw = j;
			int yPixel = j + y;
			for (int i = 0; i < w; i++) {
				int xDraw = i;
				int xPixel = i + x;
				
				if (mirrorX) xDraw = (int) ((sprite.w - 1) - (i / xScale));
				else
					xDraw = (int) (i / xScale);
				if (mirrorY) yDraw = (int) ((sprite.h - 1) - (j / yScale));
				else
					yDraw = (int) (j / yScale);

				if (flip) res = sprite.pixels[xDraw + yDraw * sprite.w];
				else res = sprite.pixels[(int)(i/xScale) + (int)(j/yScale) * sprite.w];
				
				if (res == Screen.TRANSPARENCY_CONST) res = bgCol;
				else res = col;

				screen.drawPixel(xPixel, yPixel, res, alpha);
			}
		}
	}

	private static void drawSprite(Sprite sprite, Screen screen, int x, int y, int col, boolean flip, int dir, float xScale, float yScale, float alpha) {
		int res = 0;

		int w = (int) Math.round(sprite.w * xScale);
		int h = (int) Math.round(sprite.h * yScale);
		
		boolean mirrorX = (dir & 0x01) > 0;
		boolean mirrorY = (dir & 0x02) > 0;
		
		for (int j = 0; j < h; j++) {
			int yDraw = j;
			int yPixel = j + y;
			for (int i = 0; i < w; i++) {
				int xDraw = i;
				int xPixel = i + x;
				
				if (mirrorX) xDraw = (int) ((sprite.w - 1) - (i / xScale));
				else
					xDraw = (int) (i / xScale);
				if (mirrorY) yDraw = (int) ((sprite.h - 1) - (j / yScale));
				else
					yDraw = (int) (j / yScale);

				if (flip) res = sprite.pixels[xDraw + yDraw * sprite.w];
				else res = sprite.pixels[(int) (i / xScale) + (int) (j / yScale) * sprite.w];
				
				if (res == Screen.TRANSPARENCY_CONST) continue;
				else {
					res = col;
					screen.drawPixel(xPixel, yPixel, res, alpha);
				}
			}
		}
	}
}
