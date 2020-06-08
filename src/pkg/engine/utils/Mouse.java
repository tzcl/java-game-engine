package pkg.engine.utils;

import java.awt.Canvas;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

	public Vector		pos;
	public int			wheel;
	public boolean[]	buttons	= new boolean[4];
	public boolean		btn_left, btn_right, btn_wheel;
	public int			pixel_size;

	public Mouse(Canvas canvas, int pixel_size) {
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseWheelListener(this);

		pos = new Vector();
		wheel = 0;
		this.pixel_size = pixel_size;
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = false;
		}
		btn_left = btn_right = btn_wheel = false;
	}

	public void tick() {
		btn_left = buttons[MouseEvent.BUTTON1];
		btn_wheel = buttons[MouseEvent.BUTTON2];
		btn_right = buttons[MouseEvent.BUTTON3];
	}

	public Vector getPixelPos() {
		return new Vector(pos.x / pixel_size, pos.y / pixel_size);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		wheel += e.getWheelRotation();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		pos.set(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		pos.set(e.getX(), e.getY());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		if (button >= 0 && button < buttons.length) buttons[button] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int button = e.getButton();
		if (button >= 0 && button < buttons.length) buttons[button] = false;
	}
}
