package pkg.engine.gfx;

public class AnimatedSprite {

	public Sprite	sprite;
	public Sprite[]	sprites;
	public int		currentFrame;
	public long		delay;

	private boolean	reverse;
	private long	timer;

	public AnimatedSprite(Sprite[] sprites, long delay) {
		this.sprites = sprites;
		this.delay = delay;
		this.timer = System.currentTimeMillis();
		this.currentFrame = 0;
		this.sprite = sprites[currentFrame];
	}

	public void update() {
		if (System.currentTimeMillis() - timer >= delay) {
			this.timer = System.currentTimeMillis();
			
			if(reverse) currentFrame = currentFrame < 1 ? sprites.length - 1 : currentFrame - 1;		
			else currentFrame = (currentFrame + 1) % sprites.length;
			
			sprite = sprites[currentFrame];
		}
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
}
