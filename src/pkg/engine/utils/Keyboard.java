package pkg.engine.utils;

import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	public boolean[]	keys	= new boolean[1024];

	public Keyboard(Canvas canvas) {
		canvas.addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key > 0 && key < keys.length) keys[key] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key > 0 && key < keys.length) keys[key] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
