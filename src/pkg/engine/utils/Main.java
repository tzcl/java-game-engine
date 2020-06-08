package pkg.engine.utils;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Main extends Canvas implements Runnable {

	public static int		PIXEL_SIZE;
	public static int		WIDTH;
	public static int		HEIGHT;
	public static Dimension	SIZE;
	public static String	TITLE;
	public static Main		instance;

	public JFrame			frame;
	public boolean			running;
	public Thread			thread;

	public BufferedImage	image;
	public int[]			pixels;
	public GameManager		game;

	public Main(int _PIXEL_SIZE, int _WIDTH, int _HEIGHT, String _TITLE, GameManager _game) {
		PIXEL_SIZE = _PIXEL_SIZE;
		WIDTH = _WIDTH / _PIXEL_SIZE;
		HEIGHT = _HEIGHT / _PIXEL_SIZE;
		TITLE = _TITLE;
		game = _game;
		
		SIZE = new Dimension(WIDTH * PIXEL_SIZE, HEIGHT * PIXEL_SIZE);
		
		this.setSize(SIZE);

		frame = new JFrame(TITLE);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this);
		frame.pack();

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		instance = this;

		requestFocus();
	}

	private void init() {
		int w = getWidth() / PIXEL_SIZE, h = getHeight() / PIXEL_SIZE, z = PIXEL_SIZE * PIXEL_SIZE;
		if (w * PIXEL_SIZE < SIZE.getWidth() + z) w = (int) ((SIZE.getWidth() + z) / PIXEL_SIZE);
		if (h * PIXEL_SIZE < SIZE.getHeight() + z) h = (int) ((SIZE.getHeight() + z) / PIXEL_SIZE);

		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

		frame.setVisible(true);
	}

	public synchronized void start() {
		if (running) return;
		running = true;
		thread = new Thread(this, "_main");
		thread.start();
	}

	public synchronized void stop() {
		if (!running) return;
		running = false;
		if (thread != null) try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		long lastTimer = System.currentTimeMillis();
		double delta = 0, nsPerTick = 1000000000D / 60D;

		int ticks = 0, frames = 0;

		init();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;

			while (delta >= 1) {
				tick();
				ticks++;
				delta--;
				shouldRender = true;
			}

			if (shouldRender) {
				render();
				frames++;
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				System.out.println(ticks + " ticks, " + frames + " frames");
				frame.setTitle(TITLE + " | " + ticks + " ticks, " + frames + " frames");
				ticks = 0;
				frames = 0;
			}
		}
	}

	private void tick() {
		game.tick();
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		game.render(pixels);

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, image.getWidth() * PIXEL_SIZE, image.getHeight() * PIXEL_SIZE, null);
		g.dispose();
		bs.show();
	}
}
