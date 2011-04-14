	

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;


// GTGE
import com.golden.gamedev.GameObject;
import com.golden.gamedev.GameEngine;
import com.golden.gamedev.object.Sprite;

public class RPGGame extends GameObject {
	
	public static final int PLAYING = 0, TALKING = 1, CHOOSING=3;
	static int gameState = PLAYING;
    Point tileAt;
	PlayField2		playfield;
	RPGSprite		hero,hero2;
	Map map;
    int xs=0, ys=0, xd=0,yd=0;
    int turn=1;
	RPGDialog		dialog;
	boolean gubbeklickad=false		;
	static boolean funnen=false;
	RPGSprite fiende;			// the NPC we talk to
	int	fiendeDirection;	// old NPC direction before
	int h = 0;								// we talk to him/her
	ArrayList<RPGSprite> list = new ArrayList<RPGSprite>();
	int chance=0;
	boolean clicked=false;
	int targetX,targetY;
	ActionBar actionBar;
	GameEngine parent;
	Random rand = new Random();
	Random rand2 = new Random();
	
	public RPGGame(GameEngine parent) {
		super(parent);
		this.parent = parent;
	}	
	
	public void initResources() {
		actionBar = new ActionBar(parent);
		
		map = new Map(bsLoader, bsIO);
		playfield = new PlayField2(map);
		
		
		playfield.setComparator(new Comparator() {
			public int compare(Object o1, Object o2) {
				// sort based on y-order
				return (int) (((Sprite) o1).getY()-((Sprite) o2).getY());
			}
		} );

		
		list.add(new RPGSprite(this, getImages("Chara2.png",3,4), 0,0, 3, RPGSprite.DOWN, 1, 1, 10, 1, 1, 1, 2, "Settler"));
		list.add(new RPGSprite(this, getImages("Chara1.png",3,4), 1, 1, 3, RPGSprite.RIGHT, 2,2, 100, 1, 1, 1, 2, "Infantry"));
		
		playfield.add(list.get(0));
		playfield.add(list.get(1));
		ActionBar.send(list.get(0), playfield, map.layer3, list, this);
		map.setToCenter(list.get(0));
		actionBar.initResources();
		for (int i=0; i < list.size(); i++ )
			Map.reveal(list.get(i).tileX,list.get(i).tileY,list.get(i).sightRange);
		//Hela NPC delen används ej.
		/*
		String[] event = FileUtil.fileRead(bsIO.getStream("map00.evt"));
		LogicUpdater stayStill = new StayStill();
		LogicUpdater randomMovement = new Randommovement();
		LogicUpdater cycleUpDown = new CycleUpDown();
		LogicUpdater cycleLeftRight = new CycleLeftRight();
		for (int i=0;i < event.length;i++) {
			if (event[i].startsWith("#") == false) {
				StringTokenizer token = new StringTokenizer(event[i], ",");
				String type 	= token.nextToken();
				String image 	= token.nextToken();
				int posx 		= Integer.parseInt(token.nextToken());
				int posy 		= Integer.parseInt(token.nextToken());
				int direction 	= Integer.parseInt(token.nextToken());
				int speed 		= Integer.parseInt(token.nextToken());
				int frequence 	= Integer.parseInt(token.nextToken());

				String logicUpdater = token.nextToken();
				LogicUpdater logic = stayStill;
				if (logicUpdater.equals("random")) {
					logic = randomMovement;
				} else if (logicUpdater.equals("updown")) {
					logic = cycleUpDown;
				} else if (logicUpdater.equals("leftright")) {
					logic = cycleLeftRight;
				}

				String[] dialogNPC = null;
				if (token.hasMoreTokens()) {
					StringTokenizer dialogToken = new StringTokenizer(token.nextToken(),"+");
					dialogNPC = new String[dialogToken.countTokens()];
					for (int j=0;j < dialogNPC.length;j++) {
						dialogNPC[j] = dialogToken.nextToken().toUpperCase();
					}
				}


				BufferedImage[] npcImage = null;
				if (image.equals("none") == false) {
					npcImage = getImages(image,3,4);
				}

				RPGSprite npc = new NPC(this,
										npcImage, posx, posy,
										speed, direction,
										frequence, logic,
										dialogNPC);
				if (type.equals("stepping")) {
					npc.setAnimate(true);
					npc.setLoopAnim(true);
				}

				//list.add(npc);
				//playfield.add(npc);
			}
			
		}
		//Här slutar NPC delen ^^
		*/
		
		dialog = new RPGDialog(fontManager.getFont(getImage("BitmapFont.png")),
							   getImage("Box.png", false));
							
	}
		
	public void update(long elapsedTime) {
		playfield.update(elapsedTime);
		
		switch (gameState) {
			
			case PLAYING:
								
				if (click()){
					int x = getMouseX();
					int y = getMouseY();					
					tileAt = map.getTileAt(x, y);
					
					if (funnen && list.get(h).selmov){
						funnen=false;		
						
						if (tileAt.x == list.get(h).tileX +1 && tileAt.y == list.get(h).tileY +1 || tileAt.x == list.get(h).tileX -1 && tileAt.y == list.get(h).tileY -1 || tileAt.y == list.get(h).tileY -1 && tileAt.x == list.get(h).tileX +1 || tileAt.y == list.get(h).tileY +1 && tileAt.x == list.get(h).tileX -1 || tileAt.y == list.get(h).tileY +1 && tileAt.x == list.get(h).tileX +0 || tileAt.y == list.get(h).tileY -1 && tileAt.x == list.get(h).tileX +0 || tileAt.y == list.get(h).tileY +0 && tileAt.x == list.get(h).tileX -1 || tileAt.y == list.get(h).tileY +0 && tileAt.x == list.get(h).tileX +1){
							 targetX = tileAt.x; targetY = tileAt.y;
							 fiende = (RPGSprite) map.getLayer3(targetX, targetY);
						}
						list.get(h).selmov=false;
						list.get(h).fortified=false;
						String dialogNP[] = new String[3];						
						//Battle metoden:
						if(clicked && fiende!=null && fiende!=list.get(h)){				
							   clicked=!clicked;
							   int wave=rand.nextInt(100);
							  
							   if (fiende.isFortified()){								  
								   fiende.setDEF(fiende.getDEF()*1.5);
							   }else fiende.setDEF(fiende.origdef);
							   
							  
							   while(wave > 0){						   
								   int fiDEF=rand.nextInt((int) (fiende.getDEF()*fiende.getHit()));
								   list.get(h).setHP(list.get(h).getHP()-fiDEF);					   
								   int minATK=rand2.nextInt(list.get(h).getATK()*list.get(h).getHit());								   
								   fiende.setHP(fiende.getHP()-minATK);
								   
								   list.get(h).setMov();
								   if (fiende.getHP()<=0){						   
									   playfield.remove(fiende);
									   list.get(h).test(tileAt.x,tileAt.y);									   
									   map.layer3[targetX][targetY] = null;
									   list.remove(fiende);
									   break;
								   
								   }
								   else if (list.get(h).getHP()<=0){						   
									   playfield.remove(list.get(h));
									   map.layer3[list.get(h).getXX()][list.get(h).getYY()] = null;
									   list.remove(list.get(h));
									   break;						   
								   }
								   wave--;
							   }
							   
							   break;
						}
						if (fiende!=null && fiende!=list.get(h)){
							if (fiende.getDEF() == list.get(h).getATK())
								chance=50;
							else if (fiende.getDEF() > list.get(h).getATK()){
								if (fiende.getDEF() - list.get(h).getATK() == 1)
									chance=40;
								if (fiende.getDEF() - list.get(h).getATK() == 2)
									chance=30;
								if (fiende.getDEF() - list.get(h).getATK() > 2)
									chance=20;
								else if (fiende.getDEF() - list.get(h).getATK() > 3)
									chance=10;
							}
												
							dialogNP[0]=list.get(h).getTyp().toUpperCase()+" WITH "+list.get(h).getHP()+" MP";
							dialogNP[1]="HAS A "+chance+"% CHANCE";
							dialogNP[2]="AGAINST "+fiende.getTyp().toUpperCase()+" WITH "+fiende.getHP()+ " MP";
						}
						if (fiende != null && dialogNP != null && fiende!=list.get(h) && clicked==false) {
							dialog.setDialog(dialogNP,
								(list.get(h).getScreenY()+list.get(h).getHeight() < 320));
							     clicked=true;
								 gameState=TALKING;
								 break;
						}else 												
							list.get(h).test(tileAt.x,tileAt.y);
						    list.get(h).movement();															
						    break;
					}
					if (!funnen){
						for(h = 0; h<list.size();h++){
							xs = list.get(h).getXX();
							ys = list.get(h).getYY();
							if (tileAt.x == xs && tileAt.y == ys && list.get(h).getMov() && list.get(h).getTyp()!="City"){
						  		map.setToCenter(list.get(h));
						  									
								funnen=true;
								list.get(h).dirSet(3);
								ActionBar.send(list.get(h), playfield, map.layer3, list, this);
								actionBar.initResources();
								gameState=CHOOSING;
								break;
							}
							
						}
					}
				}
											
				// quit key
				if (keyPressed(KeyEvent.VK_ESCAPE)) {
					parent.nextGameID = Civcraft.TITLE;
					finish();
				}
				
				if (keyPressed(KeyEvent.VK_ENTER)) {
					newTurn();
					break;
				}
				break;

			case CHOOSING:
														
				
			case TALKING:
				if (dialog.endDialog) {
					if (keyPressed(KeyEvent.VK_Z) ||
						click() ||
						keyPressed(KeyEvent.VK_ESCAPE)) {
						
						//fiende.setDirection(fiendeDirection);
						gameState = PLAYING;
					}
				}

				dialog.update(elapsedTime);
			break;
		}
		actionBar.update(elapsedTime);
	}

	public void render(Graphics2D g) {
		playfield.render(g);

		if (gameState == TALKING) {
			dialog.render(g);
		}
		actionBar.render(g);
	}
	
	public void newTurn(){
		turn++;	
		spawnBarb();
		for (int i=0; i < list.size(); i++){
			list.get(i).mov = true;
			list.get(i).moveThisTurn = 0;
		}
	}
	public void spawnBarb(){
		int x = rand.nextInt(Map.maxX-1);
		int y = rand.nextInt(Map.maxY-1);
		if (rand.nextInt(10)==0 && Map.fogofwar[x][y] > 0){
			list.add(new RPGSprite(this, getImages("Chara1.png",3,4), x, y, 3, RPGSprite.DOWN, 2,2, 100, 1, 1, 1, 2, "Barbarian"));
			playfield.add(list.get(list.size()-1));
			//Map.reveal(x,y,2);
		}
	}

}