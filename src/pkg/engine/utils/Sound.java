package pkg.engine.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Sound {

	private FileInputStream		fis;
	private BufferedInputStream	bis;
	private Thread				thread;

	public Player				player;
	public String				path;
	public long					pauseTime;
	public long					length;
	public boolean				closed;
	public int					count	= 1;

	public Sound(String path) {
		this.path = path;
	}

	public synchronized void stop() {
		if (player != null) {
			player.close();
			player = null;

			pauseTime = 0;
			length = 0;
			count = 1;
		}
		if (thread != null) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		closed = true;
	}

	public synchronized void pause() {
		if (player != null) {
			try {
				pauseTime = fis.available();
				player.close();
				player = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (thread != null) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void resume() {
		if (player == null && !closed) {
			try {
				fis = new FileInputStream(path);
				fis.skip(length - pauseTime);
				bis = new BufferedInputStream(fis);

				player = new Player(bis);

			} catch (Exception e) {
				e.printStackTrace();
			}

			thread = new Thread() {
				public void run() {
					try {
						player.play();
					} catch (JavaLayerException e) {
						e.printStackTrace();
					}
				}
			};
			thread.start();
		}
	}

	public synchronized void play() {
		if (player == null) {
			try {
				fis = new FileInputStream(path);
				bis = new BufferedInputStream(fis);
				length = fis.available();

				player = new Player(bis);
			} catch (Exception e) {
				e.printStackTrace();
			}

			thread = new Thread() {
				public void run() {
					try {
						player.play();
					} catch (JavaLayerException e) {
						e.printStackTrace();
					}
				}
			};
			thread.start();

			closed = false;
		}
	}

	private synchronized void _stop() {
		stop();
	}

	public synchronized void loop() {
		loop(-1);
	}

	public synchronized void loop(int num) {
		try {
			fis = new FileInputStream(path);
			bis = new BufferedInputStream(fis);
			length = fis.available();

			player = new Player(bis);
		} catch (Exception e) {
			e.printStackTrace();
		}

		thread = new Thread() {
			public void run() {
				try {
					player.play();

					if (num > 0) {
						if (count >= num) _stop();
						else if (player.isComplete() && count < num) {
							loop(num);
							count++;
						}
					} else {
						if (player.isComplete()) loop(-1);
					}
				} catch (JavaLayerException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();

		closed = false;
	}
}
