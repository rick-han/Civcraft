

// JFC
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

// GTGE
import com.golden.gamedev.GameEngine;
import com.golden.gamedev.gui.TButton;
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
	boolean selmov=false;
	boolean fortified=false;
	boolean friend;
	int atk;
	double def;
	int hp;
	String typ;
	int hit, stone, iron, gunpowder, move, range, food, population, turn, oldturn;
	int sightRange=2;
	int moveThisTurn=0;
	double origdef;
	boolean added=false;
	ArrayList<RPGSprite> list2;
	GameEngine parent;
	int origatk;
	int cap = 0;
	RPGSprite[] capacity = null;
	//Proxy p = ((Civcraft)parent).getProxy();
	
	public RPGSprite(GameEngine parent) {	
		this.parent = parent;
	}
   
	public RPGSprite(RPGGame owner, BufferedImage[] images, int tileX, int tileY,
					 int moveSpeed, int direction, int atk, int def, int hp, int hit, int range, int move, int sightRange, String typ, int food, boolean friend) {
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
		this.origdef=def;
		this.food=food;
		this.friend=friend;
		map.layer3[tileX][tileY] = this;	// mark sprite position
		this.added=false;
		this.origatk=atk;
		// init status, standing facing direction
		setAnimation(STANDING, direction);

		// the animation speed related with movement speed
		getAnimationTimer().setDelay(550/moveSpeed);

		speed = 0.04*moveSpeed;
	}
	public RPGSprite(RPGGame owner, BufferedImage[] images, int tileX, int tileY,
			 int moveSpeed, int direction, int atk, int def, int hp, int hit, int range, int move, int sightRange, String typ, int food, boolean friend, int cap) {
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
		this.origdef=def;
		this.food=food;
		this.friend=friend;
		map.layer3[tileX][tileY] = this;	// mark sprite position
		this.added=false;
		this.origatk=atk;
		this.cap=cap;
		capacity = new RPGSprite[cap];
		for (int i = 0; i < capacity.length; i++){
			capacity[i]=null;
		}
		// init status, standing facing direction
		setAnimation(STANDING, direction);

		// the animation speed related with movement speed
		getAnimationTimer().setDelay(550/moveSpeed);

		speed = 0.04*moveSpeed;
	}
	//konstruktor för bombardment units.
	public RPGSprite(RPGGame owner, BufferedImage[] images, int tileX, int tileY,
			 int moveSpeed, int direction, int atk, int def, int hp, int hit, int range, int move, int sightRange, String typ, int food, int stone, int gunpowder, int iron, boolean friend) {
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
		this.origdef=def;
		this.food=food;
		this.iron=iron;
		this.gunpowder=gunpowder;
		this.stone=stone;
		this.friend=friend;
		map.layer3[tileX][tileY] = this;	// mark sprite position
		this.origatk=atk;
		// init status, standing facing direction
		setAnimation(STANDING, direction);

		// the animation speed related with movement speed
		getAnimationTimer().setDelay(550/moveSpeed);

		speed = 0.04*moveSpeed;
	}
	
	public void setImg(BufferedImage[] images){
		
		setImages(images);
		
	}
	public void setTurn(int tur){
		turn=tur;
	}
	public void setOldTurn(int tura){
		oldturn=tura;
	}
	public void setPOP(int populations){
		population=populations;
	}
	public void setStone(int stone){
		this.stone=stone;
	}
	public void setIron(int iron){
		this.iron=iron;
	}
	public void setATK(int atk){
		this.atk=atk;
	}
	public void setGunP(int gunp){
		this.gunpowder=gunp;
	}
	public int getPOP(){
		return population;
	}
	public boolean isFortified(){
		return fortified;
	}
	public void setFood(int food){
		this.food=food;
	}
	public int getFood(){
		return food;
	}
	public int getStone(){
		return stone;
	}
	public int getIron(){
		return iron;
	}
	public int getGunP(){
		return gunpowder;
	}
	public void setFort(){
		fortified=true;
	}
	public boolean selm(){
		return selmov;
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
		if (move == moveThisTurn){
			mov=false;
			
		}
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
	public double getDEF(){
		return def;
	}
	public int getXX(){
		return tileX;
	}
	public int getYY(){
		return tileY;
	}
	public void setHP(int d){
		this.hp=d;
	}
	public void setX(int x){
		tileX=x;
	}
	public void setY(int y){
		tileY=y;	
	}
	public void setDEF(double d){
		this.def=d;
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
	public boolean checkCapacity(){
		if (capacity==null){
			System.out.print("capacity null");
			return false;
		}
		for(int i = 0; i < capacity.length; i++){
			if (capacity[i]==null)
				return true;
		}
		return false;
	}
	// the npc is standing and do nothing, time to think what to do next
	protected void updateLogic(long elapsedTime) { }
    
	// Denna move metod som används
	boolean test(int horiz, int vert, RPGSprite unit, ArrayList<RPGSprite> lista){
		if (horiz % 2 == 0){
			even=true;
		}else even=false;	
		list2=lista;
		map.layer3[tileX][tileY] = null;
	
		if(horiz > tileX && vert > tileY){
			
			setDirection(1);
			if (even){			
				tileX+=1; 
				tileY+=1;	
				
				
				if (unit.getTyp() == "Galley" || unit.getTyp() == "Caravel" || unit.getTyp() == "Trireme"){
					if (map.boatisOccupied(tileX, tileY, list2, unit) == true){
						tileX-=1;
						tileY-=1;
						map.layer3[tileX][tileY] = unit;
						
						return false;				
					}
				}
				else if (unit.getTyp() != "Galley" && unit.getTyp() != "Caravel" && unit.getTyp() != "Trireme"){
					if (map.isOccupied(tileX, tileY, list2, unit) == true){
						tileX-=1;
						tileY-=1;
						map.layer3[tileX][tileY] = unit;
						
						return false;				
					}
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
				
				if (unit.getTyp() == "Galley" || unit.getTyp() == "Caravel" || unit.getTyp() == "Trireme"){
					if (map.boatisOccupied(tileX, tileY, list2, unit) == true){
						tileX+=1;
						tileY+=1;
						map.layer3[tileX][tileY] = unit;
						
						return false;				
					}
				}
				else if (unit.getTyp() != "Galley" && unit.getTyp() != "Caravel" && unit.getTyp() != "Trireme"){
					if (map.isOccupied(tileX, tileY, list2, unit) == true){
						tileX+=1;
						tileY+=1;
						map.layer3[tileX][tileY] = unit;
						
						return false;				
					}
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
				
				if (unit.getTyp() == "Galley" || unit.getTyp() == "Caravel" || unit.getTyp() == "Trireme"){
					if (map.boatisOccupied(tileX, tileY, list2, unit) == true){
						tileX+=1;
						tileY-=1;
						map.layer3[tileX][tileY] = unit;
						
						return false;				
					}
				}
				else if (unit.getTyp() != "Galley" && unit.getTyp() != "Caravel" && unit.getTyp() != "Trireme"){
					if (map.isOccupied(tileX, tileY, list2, unit) == true){
						tileX+=1;
						tileY-=1;
						map.layer3[tileX][tileY] = unit;
						
						return false;				
					}
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
				if (unit.getTyp() == "Galley" || unit.getTyp() == "Caravel" || unit.getTyp() == "Trireme"){
					if (map.boatisOccupied(tileX, tileY, list2, unit) == true){
						tileX-=1;
						tileY+=1;
						map.layer3[tileX][tileY] = unit;
						return false;				
					}
				}
				else if (unit.getTyp() != "Galley" && unit.getTyp() != "Caravel" && unit.getTyp() != "Trireme"){
					if (map.isOccupied(tileX, tileY, list2, unit) == true){
						tileX-=1;
						tileY+=1;
						map.layer3[tileX][tileY] = unit;
						return false;				
					}
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
			
			if (unit.getTyp() == "Galley" || unit.getTyp() == "Caravel" || unit.getTyp() == "Trireme"){
				if (map.boatisOccupied(tileX, tileY, list2, unit) == true){
					tileX-=1;	
					map.layer3[tileX][tileY] = unit;
					
					return false;				
				}
			}
			else if (unit.getTyp() != "Galley" && unit.getTyp() != "Caravel" && unit.getTyp() != "Trireme"){
				if (map.isOccupied(tileX, tileY, list2, unit) == true){
					tileX-=1;	
					map.layer3[tileX][tileY] = unit;
					
					return false;				
				}
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
			if (unit.getTyp() == "Galley" || unit.getTyp() == "Caravel" || unit.getTyp() == "Trireme"){
				if (map.boatisOccupied(tileX, tileY, list2, unit) == true){
					tileX+=1;
					map.layer3[tileX][tileY] = unit;
					return false;				
				}
			}
			else if (unit.getTyp() != "Galley" || unit.getTyp() != "Caravel" || unit.getTyp() != "Trireme"){
				if (map.isOccupied(tileX, tileY, list2, unit) == true){
					tileX+=1;	
					map.layer3[tileX][tileY] = unit;
					return false;				
				}
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
			if (unit.getTyp() == "Galley" || unit.getTyp() == "Caravel" || unit.getTyp() == "Trireme"){
				if (map.boatisOccupied(tileX, tileY, list2, unit) == true){				
					tileY-=1;
					map.layer3[tileX][tileY] = unit;
					return false;				
				}
			}
			else if (unit.getTyp() != "Galley" || unit.getTyp() != "Caravel" || unit.getTyp() != "Trireme"){
				if (map.isOccupied(tileX, tileY, list2, unit) == true){				
					tileY-=1;
					map.layer3[tileX][tileY] = unit;
					return false;				
				}
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
			if (unit.getTyp() == "Galley" || unit.getTyp() == "Caravel" || unit.getTyp() == "Trireme"){
				if (map.boatisOccupied(tileX, tileY, list2, unit) == true){			
					tileY+=1;
					map.layer3[tileX][tileY] = unit;
					return false;				
				}
			}
			else if (unit.getTyp() != "Galley" || unit.getTyp() != "Caravel" || unit.getTyp() != "Trireme"){
				if (map.isOccupied(tileX, tileY, list2, unit) == true){				
					tileY+=1;
					map.layer3[tileX][tileY] = unit;
					return false;				
				}
			}
			map.layer3[tileX][tileY] = null;
			
			map.layer3[tileX][tileY] = this;
			setStatus(MOVING);
			setFrame(getFrame() + 1);
			return true;
		}	
		map.layer3[tileX][tileY] = null;
		map.layer3[tileX][tileY] = this;     		
		setStatus(MOVING);     		
		setFrame(getFrame() + 1);
		return true;
	}

	protected void animationChanged(int oldStat, int oldDir,
									int status, int direction) {
		setAnimationFrame(movingAnimation[direction]);
	}

	public void render(Graphics2D g) {
		// sometime the sprite/npc/event has no image
		if (getImages() != null && map.visible(tileX,tileY)) {
			super.render(g);
		}
	}
	
	boolean dirSet(int i) {
		setDirection(i);
		return true;
	}
	
}


	

	
		
	

	

