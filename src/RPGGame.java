	

// JFC
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

// GTGE
import com.golden.gamedev.GameObject;
import com.golden.gamedev.GameEngine;
import com.golden.gamedev.engine.BaseInput;
import com.golden.gamedev.gui.TButton;
import com.golden.gamedev.object.Background;
import com.golden.gamedev.object.Sprite;
import com.golden.gamedev.object.PlayField;
import com.golden.gamedev.object.SpriteGroup;
import com.golden.gamedev.object.background.abstraction.AbstractTileBackground;
import com.golden.gamedev.util.FileUtil;
import com.golden.gamedev.*;


public class RPGGame extends GameObject {

 /***************************** GAME STATE **********************************/
	
	public static final int PLAYING = 0, TALKING = 1;
	int gameState = PLAYING;
    BaseInput     bsInput;
    Point tileAt;
	PlayField2		playfield;
	RPGSprite		hero,hero2;
	Map map;
    int xs=0, ys=0, xd=0,yd=0;
	RPGDialog		dialog;
	boolean gubbeklickad=false		;
	boolean funnen=false;
	RPGSprite talkToNPC;			// the NPC we talk to
	int	talkToNPCDirection;	// old NPC direction before
	int h = 0;								// we talk to him/her
	ArrayList<RPGSprite> list = new ArrayList<RPGSprite>();
	int chance=0;
	boolean clicked=false;
	int targetX,targetY;
 /****************************************************************************/
 /******************************* CONSTRUCTOR ********************************/
 /****************************************************************************/
	
	public RPGGame(GameEngine parent) {
		super(parent);
	}	

	public void initResources() {
		
		map = new Map(bsLoader, bsIO);
		playfield = new PlayField2(map);
		
		
		playfield.setComparator(new Comparator() {
			public int compare(Object o1, Object o2) {
				// sort based on y-order
				return (int) (((Sprite) o1).getY()-((Sprite) o2).getY());
			}
		} );

		
		list.add(new RPGSprite(this, getImages("Chara1.png",3,4), 0,0, 3, RPGSprite.LEFT, 11, 11, 100, "swordsman"));
		list.add(new RPGSprite(this, getImages("Chara1.png",3,4), 5, 5, 3, RPGSprite.RIGHT, 10,10, 100, "archer"));
		
		playfield.add(list.get(0));
		playfield.add(list.get(1));
		Map.reveal(list.get(0).tileX,list.get(0).tileY,2);
		//Hela NPC delen används ej.
		/*
		String[] event = FileUtil.fileRead(bsIO.getStream("map00.evt"));
		LogicUpdater stayStill = new StayStill();
		LogicUpdater randomMovement = new RandomMovement();
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
				
				if (rightClick()){
					int xd = getMouseX();
					int yd = getMouseY();
					tileAt = map.getTileAt(xd, yd);
					xs = list.get(0).getXX();
					ys = list.get(0).getYY();
					if (tileAt.x == xs && tileAt.y == ys){
						list.get(0).setMov();						
						list.get(0).setImg(getImages("CharaC.png",3,4));
						playfield.add(list.get(1));
						break;						
					}
				}
				if (click()){
					int x = getMouseX();
					int y = getMouseY();
					tileAt = map.getTileAt(x, y);
					
					if (funnen){
						funnen=false;		
						
						if (tileAt.x == list.get(h).tileX +1 || tileAt.x == list.get(h).tileX -1 || tileAt.y == list.get(h).tileY +1 || tileAt.y == list.get(h).tileY -1 ){
							 targetX = tileAt.x; targetY = tileAt.y;
						
						}
						
						String dialogNP[] = new String[3];
						talkToNPC = (RPGSprite) map.getLayer3(targetX, targetY);
						
						if(clicked){				
							   clicked=!clicked;
							   							   							   
							   if (talkToNPC!=list.get(h) && talkToNPC.getHP()==100 && list.get(h).getHP()==100 && talkToNPC.getDEF() < list.get(h).getATK()){						   
								   playfield.remove(talkToNPC);
								   list.get(h).test(tileAt.x,tileAt.y);
								   talkToNPC.setX(1000);
								   talkToNPC.setY(1000);
								   break;
								   
							   }
							   else if (talkToNPC.getHP()==100 && list.get(h).getHP()==100 && list.get(h).getATK() < talkToNPC.getDEF()){						   
								   playfield.remove(list.get(h));
								   list.get(h).setX(1000);
								   list.get(h).setY(1000);
								   break;						   
							   }
							   else if (talkToNPC.getHP()==100 && list.get(h).getHP()==100 && list.get(h).getATK() == talkToNPC.getDEF()){						   
								   
								   list.get(h).setHP(50);
								   talkToNPC.setHP(50);
								   break;						   
							   }
							   else if (talkToNPC.getHP()==50 && list.get(h).getHP()==100 && list.get(h).getATK() == talkToNPC.getDEF()){						   
								   playfield.remove(talkToNPC);
								   talkToNPC.setX(1000);
								   talkToNPC.setY(1000);
								   list.get(h).setHP(50);
								   
								   break;						   
							   }
							   else if (talkToNPC.getHP()==100 && list.get(h).getHP()==50 && list.get(h).getATK() == talkToNPC.getDEF()){						   
								   playfield.remove(list.get(h));
								   list.get(h).setX(1000);
								   list.get(h).setY(1000);
								   talkToNPC.setHP(50);
								   
								   break;						   
							   }
						}
					
						if (talkToNPC!=null && talkToNPC!=list.get(h)){
							if (talkToNPC.getHP()==100 && list.get(h).getHP()==100 && talkToNPC.getDEF() > list.get(h).getATK())
								chance=0;
							else if (talkToNPC.getHP()==100 && list.get(h).getHP()==100 && talkToNPC.getDEF() < list.get(h).getATK())
								chance=100;
							else if (talkToNPC.getHP()==100 && list.get(h).getHP()==100 && talkToNPC.getDEF() == list.get(h).getATK())
								chance=50;
							else if (talkToNPC.getHP()==50 && list.get(h).getHP()==100 && talkToNPC.getDEF() == list.get(h).getATK())
								chance=100;
							else if (talkToNPC.getHP()==100 && list.get(h).getHP()==50 && talkToNPC.getDEF() == list.get(h).getATK())
								chance=0;
							else if (talkToNPC.getHP()==50 && list.get(h).getHP()==100 && talkToNPC.getDEF() == list.get(h).getATK())
								chance=50;
							dialogNP[0]=list.get(h).getTyp().toUpperCase()+" WITH "+list.get(h).getHP()+" HP";
							dialogNP[1]="HAS A "+chance+"% CHANCE";
							dialogNP[2]="AGAINST "+talkToNPC.getTyp().toUpperCase()+" WITH "+talkToNPC.getHP()+ "HP";
						}
						if (talkToNPC != null && dialogNP != null && talkToNPC!=list.get(h)) {
							dialog.setDialog(dialogNP,
								(list.get(h).getScreenY()+list.get(h).getHeight() < 320));
							     clicked=true;
								 gameState=TALKING;
						}else list.get(h).test(tileAt.x,tileAt.y);
						Map.reveal(list.get(h).tileX,list.get(h).tileY,2);
						break;
					}
					if (!funnen){
						for(h = 0; h<list.size();h++){
							xs = list.get(h).getXX();
							ys = list.get(h).getYY();
							if (tileAt.x == xs && tileAt.y == ys && list.get(h).getMov()==true){
								map.setToCenter(list.get(h));
								
								funnen=true;
								list.get(h).dirSet(3);
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
		
				break;

			
			case TALKING:
				if (dialog.endDialog) {
					if (keyPressed(KeyEvent.VK_Z) ||
						keyPressed(KeyEvent.VK_X) ||
						keyPressed(KeyEvent.VK_ESCAPE)) {
						
						//talkToNPC.setDirection(talkToNPCDirection);
						gameState = PLAYING;
					}
				}

				dialog.update(elapsedTime);
			break;
		}
		
	}

	public void render(Graphics2D g) {
		playfield.render(g);

		if (gameState == TALKING) {
			dialog.render(g);
		}
	}


}