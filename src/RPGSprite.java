

// JFC
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

// GTGE
import com.golden.gamedev.object.sprite.AdvanceSprite;


public class RPGSprite extends AdvanceSprite {


	// sprite constant direction
	public static final int LEFT 	= 0;
	public static final int RIGHT 	= 1;
	public static final int UP 		= 2;
	public static final int DOWN 	= 3;

	// sprite constant status
	public static final int STANDING = 0,
							MOVING = 1;


	public static final int[][] movingAnimation =
		new int[][] { { 10, 11, 10, 9 }, // left animation
			  		  { 4, 5, 4, 3 },    // right animation
					  { 1, 2, 1, 0 },	 // up animation
					  { 7, 8, 7, 6 } };  // down animation

	 int			tileX;
	int tileY;

	RPGGame		owner;
	Map			map;
	double		speed;

   public int getXX(){
	   return tileX;
   }
	public RPGSprite(RPGGame owner, BufferedImage[] images, int tileX, int tileY,
					 int moveSpeed, int direction) {
		super(images,(tileX*32)-8,(tileY*32)-32);

		this.owner = owner;
		this.map = owner.map;
		this.tileX = tileX;
		this.tileY = tileY;
		map.layer3[tileX][tileY] = this;	// mark sprite position

		// init status, standing facing direction
		setAnimation(STANDING, direction);

		// the animation speed related with movement speed
		getAnimationTimer().setDelay(550/moveSpeed);

		speed = 0.04*moveSpeed;
	}

	
	public void update(long elapsedTime) {
		super.update(elapsedTime);

		if (getStatus() == MOVING) {
			if (moveTo(elapsedTime, (tileX*32)-8, (tileY*32)-32, speed)) {
				setStatus(STANDING);

				// next frame
				setFrame(getFrame() + 1);
			}
		}

		if (getStatus() == STANDING) {
			// update next sprite logic
			updateLogic(elapsedTime);
		}
	}


	// the npc is standing and do nothing, time to think what to do next
	protected void updateLogic(long elapsedTime) { }
    
	boolean test(int dir, int horiz, int vert){
		System.out.print(tileX+" <tx "+tileY+" <ty");
		if (horiz > tileX && vert > tileY){
			horiz=1; vert=1;dir=1;}
		if (horiz < tileX && vert < tileY){
			horiz=-1; vert=-1;dir=1;}
		if (horiz > tileX){
			horiz=1; dir=1;
		}
		if (horiz < tileX){
			horiz=-1; dir=0;}
		if (vert > tileY){
			vert=1;
		}else{vert=-1;}
	
		setDirection(dir);

	//if (map.isOccupied(tileX+horiz, tileY+vert) == true) {
		// tile is not empty!
		// can't go to this direction
		//return false;
	//}

	// unoccupy old location
		map.layer3[tileX][tileY] = null;

	// set new location
		tileX += horiz;
		tileY += vert;
	// occupy new location
		map.layer3[tileX][tileY] = this;


		setStatus(MOVING);


	// next frame
		setFrame(getFrame() + 1);

		return true;
	}

	// sprite is walking to tileX+horiz, tileY+vert
	boolean walkTo(int dir, int horiz, int vert) {
		
		
		
		setDirection(dir);

		//if (map.isOccupied(tileX+horiz, tileY+vert) == true) {
			// tile is not empty!
			// can't go to this direction
			//return false;
		//}

		// unoccupy old location
		map.layer3[tileX][tileY] = null;

		// set new location
		tileX += horiz;
		tileY += vert;
		// occupy new location
		map.layer3[tileX][tileY] = this;


		setStatus(MOVING);


		// next frame
		setFrame(getFrame() + 1);

		return true;
	}

	protected void animationChanged(int oldStat, int oldDir,
									int status, int direction) {
		setAnimationFrame(movingAnimation[direction]);
	}

	public void render(Graphics2D g) {
		// sometime the sprite/npc/event has no image
		if (getImages() != null) {
			super.render(g);
		}
	}

}