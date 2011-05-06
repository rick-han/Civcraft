	

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;


// GTGE
import com.golden.gamedev.GameObject;
import com.golden.gamedev.GameEngine;
import com.golden.gamedev.object.Sprite;

public class RPGGame extends GameObject {
	
	public static final int PLAYING = 0, TALKING = 1, CHOOSING=3, BATTLEINFO=4, BATTLE=5, TILEI=6;
	static int gameState = PLAYING;
	static boolean zoom = false;
	LogicUpdater randomMovement = new RandomMovement();
	LogicUpdater logic = randomMovement;
    Point tileAt, tileScroll;
	PlayField2		playfield;
	RPGSprite		hero,hero2;
	Map map;
    int xs=0, ys=0, xd=0,yd=0, xScroll=0, yScroll=0;
    int turn=1;
	RPGDialog		dialog;
	boolean gubbeklickad=false		;
	static boolean funnen=false;
	RPGSprite fiende;			// the NPC we talk to
	int	fiendeDirection, wave1, wave2;	// old NPC direction before
	int h = 0;								// we talk to him/her
	ArrayList<RPGSprite> list = new ArrayList<RPGSprite>();
	int chance=0;
	boolean battle=false;
	int targetX,targetY;
	ActionBar actionBar;
	static WindowHandler windowHandler;
	GameEngine parent;
	boolean clicka=false;
	int battlescore=3;
	Random rand = new Random();
	Random rand2 = new Random();
	Random rand3 = new Random();
	Random rand4 = new Random();
	String soldatTyp;
	int gal=0;
	public RPGGame(GameEngine parent) {
		super(parent);
		this.parent = parent;
	}	
	
	public void initResources() {
		actionBar = new ActionBar(parent);
		windowHandler = new WindowHandler(parent);
		windowHandler.initResources();
		map = new Map(bsLoader, bsIO);
		playfield = new PlayField2(map);
		
		
		playfield.setComparator(new Comparator() {
			public int compare(Object o1, Object o2) {
				// sort based on y-order
				return (int) (((Sprite) o1).getY()-((Sprite) o2).getY());
			}
		} );

		list.add(new RPGSprite(this, getImages("Chara2.png",3,4), 0,0, 3, RPGSprite.DOWN, 0, 2, 10, 1, 1, 1, 2, "Settler",100,true));
		list.add(new RPGSprite(this, getImages("InfantrySheet.png",3,4), 1, 1, 3, RPGSprite.RIGHT, 3,3, 100, 1, 1, 1, 2, "Infantry",1000,true));
		list.add(new RPGSprite(this, getImages("LegionSheet.png",3,4), 2, 1, 3, RPGSprite.RIGHT, 6,4, 100, 1, 1, 1, 2, "Legion",1000,true));
		list.add(new RPGSprite(this, getImages("PikemanSheet.png",3,4), 3, 1, 3, RPGSprite.RIGHT, 2,3, 100, 1, 1, 1, 2, "Pikeman",1000,true));
		list.add(new RPGSprite(this, getImages("CavalrySheet.png",3,4), 4, 1, 3, RPGSprite.RIGHT, 6,4, 100, 1, 1, 2, 2, "Cavalry",1000,true));
		list.add(new RPGSprite(this, getImages("KnightSheet.png",3,4), 5, 1, 3, RPGSprite.RIGHT, 12,8, 100, 1, 1, 2, 2, "Knight",1000,true));
		list.add(new RPGSprite(this, getImages("CrusaderSheet.png",3,4), 6, 1, 3, RPGSprite.RIGHT, 18,12, 100, 1, 1, 2, 2, "Crusader",1000,false));
		//list.add(new RPGSprite(this, getImages("TriremeSheet.png",3,4), 7, 1, 3, RPGSprite.RIGHT, 4,3, 50, 1, 1, 3, 2, "Trireme",1000));
		//list.add(new RPGSprite(this, getImages("GalleySheet.png",3,4), 8, 1, 3, RPGSprite.RIGHT, 30,25, 250, 1, 2, 4, 2, "Galley",1000));
		//list.add(new RPGSprite(this, getImages("CaravelSheet.png",3,4), 1, 2, 3, RPGSprite.RIGHT, 50,40, 100, 1, 3, 6, 2, "Caravel",1000));
		list.add(new RPGSprite(this, getImages("ArcherSheet.png",3,4), 1, 3, 3, RPGSprite.RIGHT, 4,2, 100, 1, 2, 1, 2, "Archer",1000,true));
		list.add(new RPGSprite(this, getImages("MusketeerSheet.png",3,4), 1, 4, 3, RPGSprite.RIGHT, 8,6, 100, 1, 2, 1, 2, "Musketeer",1000,true));
		list.add(new RPGSprite(this, getImages("CatapultSheet.png",3,4), 1, 5, 3, RPGSprite.RIGHT, 12,3, 100, 1, 2, 1, 2, "Catapult",1000, 50,0,0,true));
		list.add(new RPGSprite(this, getImages("TrebuchetSheet.png",3,4), 1, 6, 3, RPGSprite.RIGHT, 20,2, 100, 1, 3, 1, 2, "Trebuchet",1000,75,0,0,true));
		list.add(new RPGSprite(this, getImages("CannonSheet.png",3,4), 1, 7, 3, RPGSprite.RIGHT, 30,3, 100, 1, 4, 1, 2, "Cannon",1000,0,100,100,true));
		fiende = list.get(1);
		playfield.add(list.get(0));
		playfield.add(list.get(1));
		playfield.add(list.get(2));
		playfield.add(list.get(3));
		playfield.add(list.get(4));
		playfield.add(list.get(5));
		playfield.add(list.get(6));
		playfield.add(list.get(7));
		
		playfield.add(list.get(8));
		playfield.add(list.get(9));
		playfield.add(list.get(10));
		playfield.add(list.get(11));
		
		//for (int j=0;j < map.layer1[0].length;j++) {
			//for (int i=0;i < map.layer1.length;i++) {
				//if (map.layer1[i][j] == 1 || map.layer1[i][j] == 0 && gal==0){
					//list.add(new RPGSprite(this, getImages("GalleySheet.png",3,4), i, j, 3, RPGSprite.RIGHT, 30,25, 250, 1, 2, 4, 2, "Galley",10000,400,0,0));
					//playfield.add(list.get(list.size()-1));
				//	map.layer3[i][j] = list.get(list.size()-1);
					//gal=1;
					//break;
				//}
				//break;
			//}
		//}
		//for (int j=0;j < map.layer1[0].length;j++) {
			//for (int i=0;i < map.layer1.length;i++) {
				//if (map.layer1[i][j] == 1 || map.layer1[i][j] == 0 && gal==1 && map.layer3[i][j] == null){
					//list.add(new RPGSprite(this, getImages("CaravelSheet.png",3,4), i, j, 3, RPGSprite.RIGHT, 50,40, 100, 1, 3, 6, 2, "Caravel",10000,0,200,200));
					//playfield.add(list.get(list.size()-1));
					//map.layer3[i][j] = list.get(list.size()-1);
					//gal=2;
					//break;
			//	}
				//break;
			//}
		//}
		//for (int j=0;j < map.layer1[0].length;j++) {
		//	for (int i=0;i < map.layer1.length;i++) {
			//	if (map.layer1[i][j] == 1 || map.layer1[i][j] == 0 && gal==2 && map.layer3[i][j] == null){
				//	list.add(new RPGSprite(this, getImages("TriremeSheet.png",3,4), i, j, 3, RPGSprite.RIGHT, 4,3, 50, 1, 1, 3, 2, "Trireme",1000));
					//playfield.add(list.get(list.size()-1));
					//map.layer3[i][j] = list.get(list.size()-1);
					//gal=0;
					//break;	
				//}
			//break;
			//}
		//}
		ActionBar.send(list.get(0), playfield, map.layer3, list, this, map);
		map.setToCenter(list.get(0));
		actionBar.initResources();
		for (int i=0; i < list.size(); i++ )
			Map.reveal(list.get(i).tileX,list.get(i).tileY,list.get(i).sightRange);
		
		
		dialog = new RPGDialog(fontManager.getFont(getImage("BitmapFont.png")),
							   getImage("Box.png", false));
							
	}
		
	public void update(long elapsedTime) {
		playfield.update(elapsedTime);
		String dialogNP[] = new String[3];
		String dialogNP2[] = new String[4];
		Database data = new Database();
		switch (gameState) {
			
			case PLAYING:
				
				if (rightClick()){
					
					tileAt = map.getTileAt(getMouseX(), getMouseY());
					int terrainCode = map.layer1[tileAt.x][tileAt.y];
					dialogNP2[0] = "";
					dialogNP2[1] = "";
					dialogNP2[2] = "";
					dialogNP2[3] = "";
					dialogNP2[0] += "WHEAT: " + data.getWheat(terrainCode) + " ";
					dialogNP2[0] += "FISH: " + data.getFish(terrainCode) + " ";
					dialogNP2[0] += "GAME: " + data.getGame(terrainCode) + " ";
					dialogNP2[0] += "HAY: " + data.getHay(terrainCode) + " ";
					dialogNP2[0] += "STONE: " + data.getStone(terrainCode) + "\n";
					dialogNP2[1] += "ORE: " + data.getOre(terrainCode) + " ";
					dialogNP2[1] += "LUMBER: " + data.getLumber(terrainCode) + " ";
					dialogNP2[1] += "GOLD: " + data.getGold(terrainCode) + " ";
					dialogNP2[1] += "COAL: " + data.getCoal(terrainCode) + " \n";
					dialogNP2[2] += "SULFUR: " + data.getSulfur(terrainCode) + " ";
					dialogNP2[2] += "HAPPINESS: " + data.getHappiness(terrainCode) + " ";
					dialogNP2[2] += "SCIENCE: " + data.getScience(terrainCode) + " \n";
					dialogNP2[3] += "ATTACKBONUS: " + data.getAttackBonus(terrainCode) + " ";
					dialogNP2[3] += "DEFENCEBONUS: " + data.getDefenceBonus(terrainCode);
								
					windowHandler.setLabel(dialogNP2[0] + dialogNP2[1] + dialogNP2[2] + dialogNP2[3]);
					windowHandler.setVisible(true);
					gameState = TILEI;
					

					break;
				}
				
				if(battle){					
					   battle=!battle;
					   if(list.get(h).getTyp()!="Archer" && list.get(h).getTyp()!="Musketeer" && list.get(h).getTyp()!="Trireme"){
						   wave1=rand.nextInt(12);
						   wave2=rand.nextInt(12);
					   }
					   else if(list.get(h).getTyp()=="Archer" || list.get(h).getTyp()=="Musketeer" || list.get(h).getTyp()=="Trireme"){
						   wave1=rand.nextInt(3);
						   wave2=rand.nextInt(3);
					   }		   
					   int waves = wave1+wave2;
					   
					   battlescore=3;
					   if (fiende.isFortified()){								  
						   fiende.setDEF(fiende.getDEF()*1.5);
					   }else fiende.setDEF(fiende.origdef);
					   					  
					   while(waves > 0){	
						 
						   if (list.get(h).getTyp() != "Trireme" && list.get(h).getTyp() != "Musketeer" && list.get(h).getTyp() != "Archer" && list.get(h).getTyp() != "Catapult" && list.get(h).getTyp() != "Trebuchet" && list.get(h).getTyp() != "Cannon" && list.get(h).getTyp() != "Caravel" && list.get(h).getTyp() != "Galley"){
							   
							   double DUDP=(fiende.getDEF()*fiende.getHP()/100);								   				   
							   double AUDP=(list.get(h).getATK()*list.get(h).getHP())/100;								   								  
							   double ATKR1 = rand.nextDouble() * AUDP;
							   double ATKR2 = rand2.nextDouble() * AUDP;
							   double DEFR1 = rand3.nextDouble() * DUDP;
							   double DEFR2 = rand4.nextDouble() * DUDP;
						   
							   double DAU = ATKR1 + ATKR2;
							   double DDU = DEFR1 + DEFR2;
						   
							   fiende.setHP((int) (fiende.getHP()-DAU));
							   list.get(h).setHP((int) (list.get(h).getHP()-DDU));
							   list.get(h).setMov();
							   if (fiende.getHP()<=0 || DDU<=0){	
								   battlescore=1;
								   playfield.remove(fiende);									   
								   map.layer3[targetX][targetY] = null;	
								   
								   break;					   
							   }
							   else if (list.get(h).getHP()<=0 || DAU<=0){	
								   battlescore=2;
								   playfield.remove(list.get(h));
								   soldatTyp = list.get(h).getTyp();
								   map.layer3[list.get(h).getXX()][list.get(h).getYY()] = null;	
								   
								   break;						   
							   }
							   
						   }
					   
						   else if(list.get(h).getTyp() == "Catapult" || list.get(h).getTyp() == "Trebuchet" || list.get(h).getTyp() == "Cannon" || list.get(h).getTyp() == "Caravel" || list.get(h).getTyp() == "Galley"){
							   if(list.get(h).getTyp() == "Catapult" || list.get(h).getTyp() == "Trebuchet" || list.get(h).getTyp() == "Galley"){
								   list.get(h).setStone(list.get(h).getStone()-1);		   
							   }
							   if(list.get(h).getTyp() == "Caravel" || list.get(h).getTyp() == "Cannon"){
								   list.get(h).setIron(list.get(h).getIron()-1);
								   list.get(h).setGunP(list.get(h).getGunP()-1);
							   }
							   if(list.get(h).getTyp() == "Caravel" || list.get(h).getTyp() == "Cannon" && list.get(h).getIron() > 0 && list.get(h).getGunP() > 0){
								   double AUDP=(list.get(h).getATK()*list.get(h).getHP())/100;
								   double DAU = rand.nextDouble() * AUDP;
								   fiende.setHP((int) (fiende.getHP()-DAU));
								   list.get(h).setMov();
							   }
							   if(list.get(h).getTyp() == "Catapult" || list.get(h).getTyp() == "Trebuchet" || list.get(h).getTyp() == "Galley" && list.get(h).getStone() > 0){
								   double AUDP=(list.get(h).getATK()*list.get(h).getHP())/100;
								   double DAU = rand.nextDouble() * AUDP;
								   fiende.setHP((int) (fiende.getHP()-DAU));
								   list.get(h).setMov();				   
							   }
							   if (fiende.getHP()<=0){	
								   battlescore=4;
								   playfield.remove(fiende);									   
								   map.layer3[targetX][targetY] = null;	
								  
								   break;					   
							   }
						   }
						   else if(list.get(h).getTyp() == "Archer" || list.get(h).getTyp() == "Musketeer" || list.get(h).getTyp() == "Trireme"){
								
							   double AUDP=(list.get(h).getATK()*list.get(h).getHP())/100;
							   double DAU = rand.nextDouble() * AUDP;
							   double DAU2= rand2.nextDouble() * AUDP;
							   fiende.setHP((int) (fiende.getHP()-(DAU+DAU2)));
							   list.get(h).setMov();
								   
							   if (fiende.getHP()<=0){	
								   battlescore=4;
								   playfield.remove(fiende);									   
								   map.layer3[targetX][targetY] = null;	
								   list.remove(fiende);
								   break;					   
							   }
						   
						   
						   }waves--;
					   }
					   if(battlescore==1 && list.get(h).getTyp() != "Catapult" && list.get(h).getTyp() != "Trebuchet" && list.get(h).getTyp() != "Cannon"){
						   dialogNP[0]=list.get(h).getTyp()+" won this battle\n";
						   dialogNP[1]="but lost some soldiers\n";
						   dialogNP[2]=list.get(h).getHP()+" soldiers are left";						   
						   windowHandler.setLabel(dialogNP[0] + dialogNP[1] + dialogNP[2]);
						   windowHandler.setVisible(true);						   				  						   
						   list.get(h).test(tileAt.x,tileAt.y, list.get(h), list);
						   list.remove(fiende);						   
						   gameState=TILEI;
						   break;	
					   }
					   else if(battlescore==2 && list.get(h).getTyp() != "Catapult" && list.get(h).getTyp() != "Trebuchet" && list.get(h).getTyp() != "Cannon"){
						   dialogNP[0]=soldatTyp+" lost this battle\n";
						   dialogNP[1]="";
						   dialogNP[2]="";
						   windowHandler.setLabel(dialogNP[0] + dialogNP[1] + dialogNP[2]);
						   windowHandler.setVisible(true);						   				  							   
						   list.remove(list.get(h));
						   gameState=TILEI;
						   break;	 						   
					   }
					   else if(battlescore==3 && list.get(h).getTyp() != "Catapult" && list.get(h).getTyp() != "Trebuchet" && list.get(h).getTyp() != "Cannon"){
						   dialogNP[0]="The battle is over.\n";
						   dialogNP[1]="You have "+list.get(h).getHP()+"\n";
						   dialogNP[2]="soldiers left";
						   windowHandler.setLabel(dialogNP[0] + dialogNP[1] + dialogNP[2]);
						   windowHandler.setVisible(true);						   				  	
						   gameState=TILEI;
						   break;	 						   
					   }
					   else if(battlescore==4 && list.get(h).getTyp() == "Catapult" || list.get(h).getTyp() == "Trebuchet" || list.get(h).getTyp() == "Cannon" && fiende.getHP()<=0){
						   dialogNP[0]="Your "+list.get(h).getTyp()+"\n";
						   dialogNP[1]="performed a bombardment\n";
						   dialogNP[2]="on an enemy unit";
						   list.remove(fiende);
						   windowHandler.setLabel(dialogNP[0] + dialogNP[1] + dialogNP[2]);
						   windowHandler.setVisible(true);						   				  	
						   gameState=TILEI;
						   break;	 				 						   
					   }
					   else if(battlescore==4 && list.get(h).getTyp() == "Catapult" || list.get(h).getTyp() == "Trebuchet" || list.get(h).getTyp() == "Cannon"){
						   dialogNP[0]="Your "+list.get(h).getTyp()+"\n";
						   dialogNP[1]="performed a bombardment\n";
						   dialogNP[2]="on an enemy unit";
						   windowHandler.setLabel(dialogNP[0] + dialogNP[1] + dialogNP[2]);
						   windowHandler.setVisible(true);
						   gameState=TILEI;							   
						   break;	 				 						   
					   }	
				}
				if (click()){
					int x = getMouseX();
					int y = getMouseY();
					tileAt = map.getTileAt(x, y);
										
					if (funnen && list.get(h).selmov){
						funnen=false;		
						fiende=null;
						if (tileAt.x == list.get(h).tileX +1 || tileAt.x > list.get(h).tileX +1 && tileAt.x <= list.get(h).tileX+list.get(h).getRange() && tileAt.y == list.get(h).tileY +1 || tileAt.y > list.get(h).tileY +1 && tileAt.y <= list.get(h).tileY+list.get(h).getRange() || tileAt.x == list.get(h).tileX -1 || tileAt.x < list.get(h).tileX -1 && tileAt.x >= list.get(h).tileX-list.get(h).getRange() && tileAt.y == list.get(h).tileY -1 || tileAt.y < list.get(h).tileY -1 && tileAt.y >= list.get(h).tileY-list.get(h).getRange() || tileAt.y == list.get(h).tileY -1 || tileAt.y < list.get(h).tileY -1 && tileAt.y >= list.get(h).tileY-list.get(h).getRange() && tileAt.x == list.get(h).tileX +1 || tileAt.x > list.get(h).tileX +1 && tileAt.x <= list.get(h).tileX+list.get(h).getRange() || tileAt.y == list.get(h).tileY +1 || tileAt.y > list.get(h).tileY +1 && tileAt.y <= list.get(h).tileY+list.get(h).getRange() && tileAt.x == list.get(h).tileX -1 || tileAt.x < list.get(h).tileX -1 && tileAt.x >= list.get(h).tileX-list.get(h).getRange() || tileAt.y == list.get(h).tileY +1 || tileAt.y > list.get(h).tileY +1 && tileAt.y <= list.get(h).tileY+list.get(h).getRange() && tileAt.x == list.get(h).tileX +0 || tileAt.y == list.get(h).tileY -1 || tileAt.y < list.get(h).tileY -1 && tileAt.y >= list.get(h).tileY-list.get(h).getRange() && tileAt.x == list.get(h).tileX +0 || tileAt.y == list.get(h).tileY +0 && tileAt.x == list.get(h).tileX -1 || tileAt.x < list.get(h).tileX -1 && tileAt.x >= list.get(h).tileX-list.get(h).getRange() || tileAt.y == list.get(h).tileY +0 && tileAt.x == list.get(h).tileX +1 || tileAt.x > list.get(h).tileX +1 && tileAt.x <= list.get(h).tileX+list.get(h).getRange()){
							 targetX = tileAt.x; targetY = tileAt.y;
							 fiende = (RPGSprite) map.getLayer3(targetX, targetY);
						}
						list.get(h).selmov=false;
						list.get(h).fortified=false;
																							
						if (fiende!=null && fiende!=list.get(h) && list.get(h).getTyp() != "Cannon" && list.get(h).getTyp() != "Trebuchet" && list.get(h).getTyp() != "Catapult" && fiende!=list.get(h) && fiende.getTyp() != "City" && fiende.friend!=list.get(h).friend){
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
												
							dialogNP[0]=list.get(h).getTyp()+" with "+list.get(h).getHP()+" MP\n";
							dialogNP[1]="has a "+chance+"% chance\n";
							dialogNP[2]="against "+fiende.getTyp()+" with "+fiende.getHP()+ " MP";							
							windowHandler.setLabel(dialogNP[0] + dialogNP[1] + dialogNP[2]);
							windowHandler.setVisible(true);
							gameState=TILEI;	
						}
						if (fiende != null && fiende!=list.get(h) && battle==false && fiende.friend!=list.get(h).friend) {							
						    	battle=true;								
								break;
																	
								}else 												
									list.get(h).test(tileAt.x,tileAt.y, list.get(h), list);
									list.get(h).movement();															
									break;
							}
					if (!funnen){
						for(h = 0; h<list.size();h++){
							xs = list.get(h).getXX();
							ys = list.get(h).getYY();						
							
							if (tileAt.x == xs && tileAt.y == ys && list.get(h).getTyp()!="Mine"){									
								map.setToCenter(list.get(h)); 								
								funnen=true;
								if(list.get(h).getTyp()=="City"){
									funnen=false;
								}
								list.get(h).dirSet(3);
								ActionBar.send(list.get(h), playfield, map.layer3, list, this, map);
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
				if (keyPressed(KeyEvent.VK_RIGHT)){
					if (xScroll < (Map.sizeX*32)-1024){
						xScroll+=64;
						map.setToCenter(xScroll,yScroll, 1024, 640);
					}
					break;
				}
				if (keyPressed(KeyEvent.VK_LEFT)){
					if (xScroll >= 64){
						xScroll-=64;
						map.setToCenter(xScroll,yScroll, 1024, 640);
					}
					break;
				}
				if (keyPressed(KeyEvent.VK_UP)){
					if (yScroll >= 64){
						yScroll-=64;
						map.setToCenter(xScroll,yScroll, 1024, 640);
					}
					break;
				}
				if (keyPressed(KeyEvent.VK_DOWN)){
					if (yScroll < (Map.sizeY*32)-640){
						yScroll+=64;
						map.setToCenter(xScroll,yScroll, 1024, 640);
					}
					break;
				}
				if (keyPressed(KeyEvent.VK_O)){
					zoom = !zoom;
				}
				break;
		
			case CHOOSING:
				
			case TILEI:
				
					if (keyPressed(KeyEvent.VK_Z) ||
						rightClick() ||
						keyPressed(KeyEvent.VK_ESCAPE)) {
						
						//fiende.setDirection(fiendeDirection);
						windowHandler.setVisible(false);
						gameState = PLAYING;
					
				}

				//dialog.update(elapsedTime);
			break;			
			
			case TALKING:
				if (dialog.endDialog) {
					if (keyPressed(KeyEvent.VK_Z) ||
						rightClick() ||
						keyPressed(KeyEvent.VK_ESCAPE)) {
						
						//fiende.setDirection(fiendeDirection);
						gameState = PLAYING;
					}
				}

				dialog.update(elapsedTime);
			break;
		}
		actionBar.update(elapsedTime);
		windowHandler.update(elapsedTime);
	}

	public void render(Graphics2D g) {
		playfield.render(g);
		if (gameState == TILEI){
			windowHandler.render(g);
		}
		if (gameState == TALKING) {
			dialog.render(g);
			windowHandler.render(g);
		}
		actionBar.render(g);
		
	}
	
	public void sendList(){		
		RandomMovement.getList(list, map, playfield, parent);
	}
	
	public void newTurn(){
		for (int i=0; i < list.size(); i++){
			if(list.get(i).getTyp()=="Barbarian"){
				list.get(i).setOldTurn(turn);
			}
				
		}
		turn++;	
		for (int i=0; i < list.size(); i++){
			if(list.get(i).getTyp()=="Barbarian"){
				list.get(i).setTurn(turn);
			}
				
		}
		spawnBarb();
		sendList();	
		
		for (int i=0; i < list.size(); i++){
			list.get(i).mov = true;
			list.get(i).moveThisTurn = 0;
			if (list.get(i).getTyp() != "City" && list.get(i).getTyp() != "Barbarian"){
				list.get(i).setFood(list.get(i).getFood()-1);							
			}
			if (list.get(i).getTyp() != "City" && list.get(i).getFood() <=0 && list.get(i).getTyp() != "Barbarian" && list.get(i).getTyp() != "City" && list.get(i).getTyp() != "Mine"){
				map.layer3[list.get(i).getXX()][list.get(i).getYY()]=null;
				playfield.remove(list.get(i));
				list.remove(list.get(i));
			}
		}
	}
	public void spawnBarb(){
		int x = rand.nextInt(Map.maxX-1);
		int y = rand.nextInt(Map.maxY-1);
		if (rand.nextInt(10)==0 && Map.fogofwar[x][y] >= 0){
			list.add(new NPC(this, getImages("Chara1.png",3,4), x, y, 3, RPGSprite.DOWN, 2,2, 100, 1, 1, 1, 2, "Barbarian",logic));
			//list.add(new RPGSprite(this, getImages("Chara1.png",3,4), x, y, 3, RPGSprite.DOWN, 2,2, 100, 1, 1, 1, 2, "Barbarian",1000, false));
			playfield.add(list.get(list.size()-1));
			//Map.reveal(x,y,2);
		}
	}

}