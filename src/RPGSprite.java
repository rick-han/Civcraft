

// JFC
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

// GTGE
import com.golden.gamedev.object.sprite.AdvanceSprite;


public class RPGSprite extends AdvanceSprite{

	// sprite constant direction
	public static final int LEFT 	= 0;
	public static final int RIGHT 	= 1;
	public static final int UP 		= 2;
	public static final int DOWN 	= 3;
	boolean even=true;
	// sprite constant status
	public static final int STANDING = 0,
							MOVING = 1;

	
	public static final int[][] movingAnimation =
		new int[][] { { 10, 11, 10, 9 }, // left animation
			  		  { 4, 5, 4, 3 },    // right animation
					  { 1, 2, 1, 0 },	 // up animation
					  { 7, 8, 7, 6 } };  // down animation

	int tileX;
	int tileY;

	RPGGame		owner;
	Map			map;
	double		speed;
	
	boolean klickad=false;
	boolean mov=true;
	int atk;
	int def;
	int hp;
	String typ;
	int hit;
	int move;
	int sightRange=2;
	int moveThisTurn=0;
	int range;
	public RPGSprite(){
	}
   
	public RPGSprite(RPGGame owner, BufferedImage[] images, int tileX, int tileY,
					 int moveSpeed, int direction, int atk, int def, int hp, int hit, int range, int move, int sightRange, String typ) {
		super(images,(tileX*32)-8,(tileY*32)-32);

		this.owner = owner;
		this.map = owner.map;
		this.tileX = tileX;
		this.tileY = tileY;
		this.hp=hp;
		this.atk=atk;
		this.def=def;
		this.typ=typ;
		this.hit=hit;
		this.range=range;
		this.sightRange=sightRange;
		this.move=move;
		map.layer3[tileX][tileY] = this;	// mark sprite position

		// init status, standing facing direction
		setAnimation(STANDING, direction);

		// the animation speed related with movement speed
		getAnimationTimer().setDelay(550/moveSpeed);

		speed = 0.04*moveSpeed;
	}
	public void setImg(BufferedImage[] images){
		
		setImages(images);
		
	}
	public int getRange(){
		return range;
	}
	public int getMove(){
		return move;
	}
	public int getMoveLeft(){
		return move-moveThisTurn;
	}
	public int getHit(){
		return hit;
	}
	boolean getMov(){
		return mov;
	}
	public String getTyp(){
		return typ;
	}
	public void setMov(){
		mov=false;
	}
	public void movement(){
		if (move == moveThisTurn)
			mov=false;
		else
			moveThisTurn+=1; //ska kolla efter terrainaddons som påverkar movement
		Map.reveal(tileX,tileY, sightRange);
	}
	public int getHP(){
		return hp;
	}
	public int getATK(){
		return atk;
	}
	public int getDEF(){
		return def;
	}
	public int getXX(){
		return tileX;
	}
	public int getYY(){
		return tileY;
	}
	public void setHP(int hp){
		this.hp=hp;
	}
	public void setX(int x){
		tileX=x;
	}
	public void setY(int y){
		tileY=y;
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
    
	// Denna move metod som används
	boolean test(int horiz, int vert){
		if (horiz % 2 == 0){
			even=true;
		}else even=false;
		map.layer3[tileX][tileY] = null;
		int tilX = tileX; int tilY = tileY;
	  
	// set new location
		if(horiz > tileX && vert > tileY){
			setDirection(1);
			if (even){
				tileX+=1; 
				tileY+=1;		
				if (map.isOccupied(tileX, tileY) == true){
					tileX-=1;
					tileY-=1;
					return false;				
				}
		 
				map.layer3[tileX][tileY] = null;
		 
				map.layer3[tileX][tileY] = this;
				setStatus(MOVING);
				setFrame(getFrame() + 1);
				return true;
				}
		}
	  
		else if(horiz < tileX && vert < tileY){
			setDirection(0);
			if (!even){
				tileX-=1;
				tileY-=1;
				if (map.isOccupied(tileX, tileY) == true){
					tileX+=1;
					tileY+=1;
					return false;
				}
				map.layer3[tileX][tileY] = null;
		 
				map.layer3[tileX][tileY] = this;
				setStatus(MOVING);
				setFrame(getFrame() + 1);
				return true;
			}
		}	
		else if(horiz < tileX && vert > tileY){
			setDirection(0);
			if(even){
				tileX-=1;
				tileY+=1;
				if (map.isOccupied(tileX, tileY) == true){
					tileX+=1;
					tileY-=1;
					return false;
				}
				map.layer3[tileX][tileY] = null;
			  
				map.layer3[tileX][tileY] = this;
				setStatus(MOVING);
				setFrame(getFrame() + 1);
				return true;
			}
		}
		else if(horiz > tileX && vert < tileY){
			setDirection(1);
			if (!even){
				tileX+=1;
				tileY-=1;
			
				if (map.isOccupied(tileX, tileY) == true){
					tileX-=1;
					tileY+=1;
					return false;
				}
				map.layer3[tileX][tileY] = null;
				map.layer3[tileX][tileY] = this;
				setStatus(MOVING);
				setFrame(getFrame() + 1);
				return true;
			}
		}	
		if(horiz > tileX){
			setDirection(1);
			tileX+=1;
			if (map.isOccupied(tileX, tileY) == true){
				tileX-=1;
				return false;
			}
			map.layer3[tileX][tileY] = null;
			
			map.layer3[tileX][tileY] = this;
			setStatus(MOVING);
			setFrame(getFrame() + 1);
			return true;
		}
		
		else if(horiz < tileX){
			setDirection(0);
			tileX-=1;
			if (map.isOccupied(tileX, tileY) == true){
				tileX+=1;
				return false;
			}
			map.layer3[tileX][tileY] = null;
			
			map.layer3[tileX][tileY] = this;
			setStatus(MOVING);
			setFrame(getFrame() + 1);
			return true;
		}
		else if(vert > tileY){
			setDirection(3);
			tileY+=1;
			if (map.isOccupied(tileX, tileY) == true){
				tileY-=1;
				return false;
			}
			map.layer3[tileX][tileY] = null;
			
			map.layer3[tileX][tileY] = this;
			setStatus(MOVING);
			setFrame(getFrame() + 1);
			return true;
		}
		else if(vert < tileY){
			setDirection(2);
			tileY-=1;
			if (map.isOccupied(tileX, tileY) == true){
				tileY+=1;
				return false;
			}
			map.layer3[tileX][tileY] = null;
			
			map.layer3[tileX][tileY] = this;
			setStatus(MOVING);
			setFrame(getFrame() + 1);
			return true;
		}
		//if (map.isOccupied(tileX, tileY) == true){
			//return false;
		//}
		map.layer3[tileX][tileY] = null;
	
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
	
	boolean dirSet(int i) {
		setDirection(i);
		return true;
	}
	
}


	

	
		
	

	

