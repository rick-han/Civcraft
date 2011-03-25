	

// JFC
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.StringTokenizer;

// GTGE
import com.golden.gamedev.GameObject;
import com.golden.gamedev.GameEngine;
import com.golden.gamedev.engine.BaseInput;
import com.golden.gamedev.object.Sprite;
import com.golden.gamedev.object.PlayField;
import com.golden.gamedev.object.background.abstraction.AbstractTileBackground;
import com.golden.gamedev.util.FileUtil;
import com.golden.gamedev.*;


public class RPGGame extends GameObject {

 /***************************** GAME STATE **********************************/
	
	public static final int PLAYING = 0, TALKING = 1;
	int gameState = PLAYING;
    BaseInput     bsInput;
    Point tileAt;
	PlayField		playfield;
	Map				map;
	RPGSprite		hero;
    int xs=0, ys=0;
	RPGDialog		dialog;
	boolean gubbeklickad=false		;
	NPC				talkToNPC;			// the NPC we talk to
	int				talkToNPCDirection;	// old NPC direction before
										// we talk to him/her


 /****************************************************************************/
 /******************************* CONSTRUCTOR ********************************/
 /****************************************************************************/
	
	public RPGGame(GameEngine parent) {
		super(parent);
	}
	

	


	public void initResources() {
		map = new Map(bsLoader, bsIO);
		playfield = new PlayField(map);
		playfield.setComparator(new Comparator() {
			public int compare(Object o1, Object o2) {
				// sort based on y-order
				return (int) (((Sprite) o1).getY()-((Sprite) o2).getY());
			}
		} );

		hero = new RPGSprite(this, getImages("Chara1.png",3,4), 10, 10, 3, RPGSprite.DOWN);

		playfield.add(hero);

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

				playfield.add(npc);
			}
		}

		dialog = new RPGDialog(fontManager.getFont(getImage("BitmapFont.png")),
							   getImage("DialogBox.png", false),
							   getImage("DialogArrow.png"));
	}
	
	public void update(long elapsedTime) {
		playfield.update(elapsedTime);
        
		int xx=0, yy=0;
		switch (gameState) {
			
			case PLAYING:
				if (hero.getStatus() == RPGSprite.STANDING) {
				 
					if (click()){
						int x = getMouseX();
						int y = getMouseY();					
						tileAt = map.getTileAt(x, y);
						xs = hero.tileX;
						ys = hero.tileY;
						if (gubbeklickad==true)
							hero.test(tileAt.x,tileAt.y);
						    gubbeklickad=false;
						if (tileAt.x == xs && tileAt.y == ys){
						    gubbeklickad=true;
						    hero.dirSet(3);
							
						}
																		
					}							
					if (keyDown(KeyEvent.VK_LEFT)) {					
						hero.walkTo(RPGSprite.LEFT, -1, 0);

					} else if (keyDown(KeyEvent.VK_RIGHT)) {
						hero.walkTo(RPGSprite.RIGHT, 1, 0);

					} else if (keyDown(KeyEvent.VK_UP)) {
						hero.walkTo(RPGSprite.UP, 0, -1);

					} else if (keyDown(KeyEvent.VK_DOWN)) {
						hero.walkTo(RPGSprite.DOWN, 0, 1);
					}


					// action key
					if (keyPressed(KeyEvent.VK_Z)) {
						int targetX = hero.tileX,
							targetY = hero.tileY;
						switch (hero.getDirection()) {
							case RPGSprite.LEFT:  targetX = hero.tileX - 1; break;
							case RPGSprite.RIGHT: targetX = hero.tileX + 1; break;
							case RPGSprite.UP:    targetY = hero.tileY - 1; break;
							case RPGSprite.DOWN:  targetY = hero.tileY + 1; break;
						}

						talkToNPC = (NPC) map.getLayer3(targetX, targetY);

						if (talkToNPC != null && talkToNPC.dialog != null) {
							dialog.setDialog(talkToNPC.dialog,
								(hero.getScreenY()+hero.getHeight() < 320));

							// make NPC and hero, face to face!
							// we store the old NPC direction first
							talkToNPCDirection = talkToNPC.getDirection();
							switch (hero.getDirection()) {
								case RPGSprite.LEFT:  talkToNPC.setDirection(RPGSprite.RIGHT); break;
								case RPGSprite.RIGHT: talkToNPC.setDirection(RPGSprite.LEFT); break;
								case RPGSprite.UP:    talkToNPC.setDirection(RPGSprite.DOWN); break;
								case RPGSprite.DOWN:  talkToNPC.setDirection(RPGSprite.UP); break;
							}

							gameState = TALKING;
						}
					}


					// quit key
					if (keyPressed(KeyEvent.VK_ESCAPE)) {
						parent.nextGameID = Civcraft.TITLE;
						finish();
					}
				}
			break;

			// talking to npc, end when Z or X or ESC is pressed
			case TALKING:
				if (dialog.endDialog) {
					if (keyPressed(KeyEvent.VK_Z) ||
						keyPressed(KeyEvent.VK_X) ||
						keyPressed(KeyEvent.VK_ESCAPE)) {
						// back to old direction
						talkToNPC.setDirection(talkToNPCDirection);
						gameState = PLAYING;
					}
				}

				dialog.update(elapsedTime);
			break;
		}

		map.setToCenter(hero);
	}

	public void render(Graphics2D g) {
		playfield.render(g);

		if (gameState == TALKING) {
			dialog.render(g);
		}
	}


}