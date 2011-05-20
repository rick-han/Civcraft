	

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;


// GTGE
import com.golden.gamedev.GameObject;
import com.golden.gamedev.GameEngine;
import com.golden.gamedev.object.Sprite;

public class RPGGame extends GameObject {
	
	public static final int PLAYING = 0, TALKING = 1, CHOOSING=3, BATTLEINFO=4, BATTLE=5, TILEI=6, WAIT=7;
	static int gameState = PLAYING;
	static boolean zoom = false;
	LogicUpdater randomMovement = new RandomMovement();
	LogicUpdater logic = randomMovement;
    Point tileAt, tileScroll;
	PlayField2		playfield;
	RPGSprite		hero,hero2;
	static boolean delt=false;
	Map map;
    int xs=0, ys=0, xd=0,yd=0, xScroll=0, yScroll=0;
    static boolean multiplayer=false;
    static int turn=1;
	RPGDialog		dialog;
	boolean gubbeklickad=false		;
	static boolean funnen=false;
	Result returned;
	RPGSprite fiende;			// the NPC we talk to
	int	fiendeDirection, wave1, wave2;	// old NPC direction before
	int h = 0, a=0,F=0;								// we talk to him/her
	ArrayList<RPGSprite> list;
	int t=0;
	int chance=0;
	boolean battle=false;
	int targetX,targetY;
	ActionBar actionBar;
	static WindowHandler windowHandler;
	GameEngine parent;
	boolean clicka=false;
	int battlescore=3;
	static boolean waiting=false;
	Random rand = new Random();
	Random rand2 = new Random();
	Random rand3 = new Random();
	Random rand4 = new Random();
	String soldatTyp;
	static ArrayList<String> k;
	ArrayList<Integer> test = new ArrayList<Integer>();
	int j = 0;
	boolean found=false, init=false;
	int gal=0;
	Proxy p;
	RPGGame civ;
	int xe;
	int ye;
	TurnBar turnBar;
	static GameEngine parent2;
	public static Result received;
	static String nick;
	public RPGGame(GameEngine parent) {
		super(parent);
		this.parent = parent;
		this.parent2 = parent;	
	}
	public RPGGame(){
		super(parent2);
	}
	
	public void initResources() {	
		turnBar = new TurnBar(parent);
		
		actionBar = new ActionBar(parent);
		windowHandler = new WindowHandler(parent);
		windowHandler.initResources();	
		map = new Map(bsLoader, bsIO, parent, nick);
		playfield = new PlayField2(map);
		p = ((Civcraft)parent).getProxy();
		list = new ArrayList<RPGSprite>();
		Map.send(playfield, list, this);
		playfield.setComparator(new Comparator() {
			public int compare(Object o1, Object o2) {
				// sort based on y-order
				return (int) (((Sprite) o1).getY()-((Sprite) o2).getY());
			}
		});
		
		if(multiplayer==false){
			list.add(new RPGSprite(this, getImages("Chara2.png",3,4), 0,0, 3, RPGSprite.DOWN, 0, 2, 10, 1, 1, 1, 2, "Settler",100,true));
			list.add(new RPGSprite(this, getImages("Chara2.png",3,4), 0,4, 3, RPGSprite.DOWN, 0, 2, 10, 1, 1, 1, 2, "Settler",100,true));
		
			playfield.add(list.get(0));
			playfield.add(list.get(1));
			TurnBar.send(list.get(0), playfield, map.layer3, list, this, map);
			ActionBar.send(list.get(0), playfield, map.layer3, list, this, map);
			map.setToCenter(list.get(0));
			actionBar.initResources();
			TurnBar.initResources();
			for (int i=0; i < list.size(); i++ )
				Map.reveal(list.get(i).tileX,list.get(i).tileY,list.get(i).sightRange);
		
		}
		dialog = new RPGDialog(fontManager.getFont(getImage("BitmapFont.png")),
							   getImage("Box.png", false));
					
		
		if(multiplayer==true){
			TurnBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
			TurnBar.initResources();
			int numberStartingPositions = received.getNumberTiles();
			String theType, theOwner;
			for(int i=0; i<numberStartingPositions; i++){
				xe = received.getTileX(i);
				ye = received.getTileY(i);
				
				if(received.existUnit(i)){				
					theType = received.getUnitType(i);	
					theOwner = received.getUnitOwner(i);
					if(theType.equalsIgnoreCase("Knight") && theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("KnightSheet.png",3,4), xe,ye, 3, RPGSprite.UP, 12,8, 100, 1, 1, 2, 2, "Knight",1000,true));
						playfield.add(list.get(list.size()-1));
						ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
						map.setToCenter(list.get(list.size()-1));
						actionBar.initResources();
					}
					else if(theType.equalsIgnoreCase("Settler") && theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("Chara2.png",3,4), xe,ye, 3, RPGSprite.UP, 0, 2, 10, 1, 1, 1, 2, "Settler",100,true));
						playfield.add(list.get(list.size()-1));
						ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
						map.setToCenter(list.get(list.size()-1));
						actionBar.initResources();
					}
					else if(theType.equalsIgnoreCase("Knight") && theOwner!=(nick)){				
						list.add(new RPGSprite(this, getImages("KnightSheet.png",3,4), xe,ye, 3, RPGSprite.DOWN, 12,8, 100, 1, 1, 2, 2, "Knight",1000,false));
					}
					else if(theType.equalsIgnoreCase("Settler") && theOwner!=(nick)){				
						list.add(new RPGSprite(this, getImages("Chara2.png",3,4), xe,ye, 3, RPGSprite.DOWN, 0, 2, 10, 1, 1, 1, 2, "Settler",100,false));
					}
					
				}
				if(received.existCity(i)){	
					list.add(new RPGSprite(this,getImages("citytile.png",3,4), xe,ye, 3, RPGSprite.DOWN, 1, 1, 10, 1, 1, 1, 2, "City",1000, true));														
					playfield.add(list.get(list.size()-1));
				}
				for (int r=0; r < list.size(); r++ ){
					if(list.get(r).friend==true)
						Map.reveal(list.get(r).tileX,list.get(r).tileY,list.get(r).sightRange);
				}
				Map.send(playfield, list, this);
			}
		}
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
					   if(multiplayer==true){
						   	battlescore=3;
							try {
								returned = p.combatRequest(list.get(h).getXX(), list.get(h).getYY(), fiende.getXX(), fiende.getYY());
							} catch (FailedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							if(returned.getAttackerLeft()<=0){
								battlescore=1;
								playfield.remove(list.get(h));
								
							}
							else if(returned.getDefenderLeft()<=0){
								battlescore=2;
								playfield.remove(fiende);
								
							}
							else if(returned.getAttackerLeft()>0 && returned.getDefenderLeft()>0){
								battlescore=4;						
								list.get(h).setHP(returned.getAttackerLeft());
								fiende.setHP(returned.getDefenderLeft());
							}
							
							if(battlescore==1){
								   dialogNP[0]="You lost this battle\n";
								   dialogNP[1]="but atleast you tried..\n";
								   dialogNP[2]="";						   
								   windowHandler.setLabel(dialogNP[0] + dialogNP[1] + dialogNP[2]);
								   windowHandler.setVisible(true);						   				  						   				   				   
								   gameState=TILEI;
								   break;	
							}
							if(battlescore==2){
								   dialogNP[0]="You won this battle!!\n";
								   dialogNP[1]="congratulations sir!\n";
								   dialogNP[2]="";						   
								   windowHandler.setLabel(dialogNP[0] + dialogNP[1] + dialogNP[2]);
								   windowHandler.setVisible(true);						   				  						   				   				   
								   gameState=TILEI;
								   break;	
							}
							if(battlescore==4){
								   dialogNP[0]="This battle ended with no winner!\n";
								   dialogNP[1]="you lost brave men on the field\n";
								   dialogNP[2]=returned.getAttackerLeft()+" of your men are left";						   
								   windowHandler.setLabel(dialogNP[0] + dialogNP[1] + dialogNP[2]);
								   windowHandler.setVisible(true);						   				  						   				   				   
								   gameState=TILEI;
								   break;	
							}
							break;
						} 
					   if(multiplayer==false){
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
					   
						   for(j=0; j<list.size(); j++){
							   int ys = fiende.getYY();
							   int xs = fiende.getXX();
							   if(xs == list.get(j).getXX() && ys == list.get(j).getYY() && list.get(j).getTyp()=="City"){
								   fiende.setDEF(fiende.getDEF()*2);						   
								   break;
							   }
						   
						   }
						   for(j=0; j<list.size(); j++){
							   int ys = fiende.getYY();
							   int xs = fiende.getXX();
							   if(xs == list.get(j).getXX() && ys == list.get(j).getYY() && list.get(j).getTyp()=="SiegeTower"){
								   fiende.setATK(fiende.getATK()*2);						   
								   break;
							   }else fiende.setATK(fiende.origatk);
						   
					 	  }
					   					  
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
							   
								   if (fiende.getHP()<=0 || DDU<=0){	
									   battlescore=1;
									   playfield.remove(fiende);	
									   list.get(h).movement();
									   list.get(h).setFood(list.get(h).getFood()+fiende.getFood());
									   map.layer3[targetX][targetY] = null;	
								   
									   break;					   
								   }
								   else if (list.get(h).getHP()<=0 || DAU<=0){	
									   battlescore=2;
									   playfield.remove(list.get(h));
									   soldatTyp = list.get(h).getTyp();
									   fiende.setFood(fiende.getFood()+list.get(h).getFood());
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
								  
								   }
								   if(list.get(h).getTyp() == "Catapult" || list.get(h).getTyp() == "Trebuchet" || list.get(h).getTyp() == "Galley" && list.get(h).getStone() > 0){
									   double AUDP=(list.get(h).getATK()*list.get(h).getHP())/100;
									   double DAU = rand.nextDouble() * AUDP;
									   fiende.setHP((int) (fiende.getHP()-DAU));
								   				   
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
							   list.get(h).movement();
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
							   list.get(h).movement();
							   windowHandler.setLabel(dialogNP[0] + dialogNP[1] + dialogNP[2]);
							   windowHandler.setVisible(true);						   				  	
							   gameState=TILEI;
							   break;	 				 						   
						   }
						   else if(battlescore==4 && list.get(h).getTyp() == "Catapult" || list.get(h).getTyp() == "Trebuchet" || list.get(h).getTyp() == "Cannon"){
							   dialogNP[0]="Your "+list.get(h).getTyp()+"\n";
							   dialogNP[1]="performed a bombardment\n";
							   dialogNP[2]="on an enemy unit";
							   list.get(h).movement();
							   windowHandler.setLabel(dialogNP[0] + dialogNP[1] + dialogNP[2]);
							   windowHandler.setVisible(true);
							   gameState=TILEI;							   
							   break;	 				 						   
						   }	
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
							 if(multiplayer==false)
								 fiende = (RPGSprite) map.getLayer3(targetX, targetY);
							 else if(multiplayer==true){
								 for(int g=0;g<list.size();g++){
									 if(list.get(g).getXX()==targetX && list.get(g).getYY()==targetY){
										 fiende = list.get(g);
									 }
								 }
							 }
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
									if(multiplayer==true){
										test.add(list.get(h).getXX());
										test.add(list.get(h).getYY());
										list.get(h).test(tileAt.x,tileAt.y, list.get(h), list);
										test.add(list.get(h).getXX());
										test.add(list.get(h).getYY());
										try {
											p.moveUnit(test);
											test.clear();
										} catch (FailedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										list.get(h).movement();	
										a=0;
										break;
									}
							list.get(h).test(tileAt.x,tileAt.y, list.get(h), list);
							list.get(h).movement();	
							a=0;
							break;
							}
					if (!funnen){
						h=0;
						
						for(h = 0; h<list.size();h++){
							xs = list.get(h).getXX();
							ys = list.get(h).getYY();						
						    
							if (tileAt.x == xs && tileAt.y == ys && list.get(h).getTyp()!="Mine" && list.get(h).friend==true){		
							
								map.setToCenter(list.get(h)); 
								
								if(list.get(h).getTyp()=="City"){
									funnen=false;
								}
								list.get(h).dirSet(3);
								ActionBar.send(list.get(h), playfield, map.layer3, list, this, map);
								actionBar.initResources();
								
								
								break;
							}
										
							
						}
						
							
							
					}
				}
				
				if (keyPressed(KeyEvent.VK_S)) {					
					if(F>0)
						a++;
					F++;
					
					while(a<=list.size()-1){						
						
						xs = list.get(a).getXX();
						ys = list.get(a).getYY();
						if (tileAt.x == xs && tileAt.y == ys && list.get(a).getTyp()!="Mine"){
							list.get(a).dirSet(3);						
							ActionBar.send(list.get(a), playfield, map.layer3, list, this, map);
							actionBar.initResources();						
							break;
						}
						if(a>list.size()-1)
							a=0;
						a++;					
					}
				}		
				if (keyPressed(KeyEvent.VK_C)) {
					if(a>list.size()-1)
						a=list.size()-1;
					if(list.get(a)!=null || list.get(h)!=null){		
						if(F>0){						
							h=a;							
						}
						a=0;		
						F=0;
						gameState=CHOOSING;
						funnen=true;
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
					
					if (yScroll <= (Map.sizeY*32)-640){						
						yScroll+=64;
						map.setToCenter(xScroll,yScroll, 1024, 720);
					}
					break;
				}
				if (keyPressed(KeyEvent.VK_O)){
					zoom = !zoom;
				}
				break;
				
					
			case WAIT:					
				if(waiting==false){		
					if(delt==true){
						for(int f = 0; f<list.size();f++){
							
							playfield.remove(list.get(f));
							
							delt=false;
						}
						
					}
					list.clear();
					int numberStartingPositions = received.getNumberTiles();
					String theType, theOwner;
					int hpLeft;
						
					for(int i=0; i<numberStartingPositions; i++){
						xe = received.getTileX(i);
						ye = received.getTileY(i);
							
							if(received.existUnit(i)){	
								
								hpLeft = received.getUnitManPower(i);							
								theType = received.getUnitType(i);	
								theOwner = received.getUnitOwner(i);
								
								if(theType.equalsIgnoreCase("Knight") && theOwner.equalsIgnoreCase(nick)){	
														
									list.add(new RPGSprite(this, getImages("KnightSheet.png",3,4), xe,ye, 3, RPGSprite.UP, 12,8, hpLeft, 1, 1, 2, 2, "Knight",1000,true));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
										
										
								
								}
								if(theType.equalsIgnoreCase("Knight") && !theOwner.equalsIgnoreCase(nick)){	
										
									list.add(new RPGSprite(this, getImages("KnightSheet.png",3,4), xe,ye, 3, RPGSprite.DOWN, 12,8, hpLeft, 1, 1, 2, 2, "Knight",1000,false));				
									if(map.fogofwar[xe][ye] == 0)
										playfield.add(list.get(list.size()-1));
									
																
								}
								if(theType.equalsIgnoreCase("Infantry") && !theOwner.equalsIgnoreCase(nick)){	
									
									list.add(new RPGSprite(this, getImages("InfantrySheet.png",3,4), xe,ye, 3, RPGSprite.DOWN, 3,3, hpLeft, 1, 1, 1, 2, "Infantry",1000,false));
									if(map.fogofwar[xe][ye] == 0)
										playfield.add(list.get(list.size()-1));
									
																
								}
								if(theType.equalsIgnoreCase("Crusader") && !theOwner.equalsIgnoreCase(nick)){	
									
									list.add(new RPGSprite(this, getImages("CrusaderSheet.png",3,4), xe,ye, 3, RPGSprite.DOWN, 18,12, hpLeft, 1, 1, 2, 2, "Crusader",1000,false));
									if(map.fogofwar[xe][ye] == 0)
										playfield.add(list.get(list.size()-1));
									
																
								}
							
								if(theType.equalsIgnoreCase("Settler") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("Chara2.png",3,4), xe,ye, 3, RPGSprite.DOWN, 0, 2, hpLeft, 1, 1, 1, 2, "Settler",100,false));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
								}
						
							}
							if(received.existCity(i)){	
								list.add(new RPGSprite(this,getImages("citytile.png",3,4), xe,ye, 3, RPGSprite.DOWN, 1, 1, 10, 1, 1, 1, 2, "City",1000, false));														
								playfield.add(list.get(list.size()-1));
								break;
							}
							}
						gameState = PLAYING;
					
						//break;
				}	
				break;
				
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
			
			case CHOOSING:
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
		
		
		
		if(list.size() > 0){
			actionBar.update(elapsedTime);
			turnBar.update(elapsedTime);
		}
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
		if(list.size() > 0){
			actionBar.render(g);
			turnBar.render(g);
		}
	}
	
	public void sendList(){		
		RandomMovement.getList(list, map, playfield, parent);
	}
	
	public void newTurn(){
		Map.send(playfield, list, this);
		if(multiplayer==true){
		try {
				p.endTurn();			
			} catch (FailedException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ActionBar.addSpr();
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
		if(multiplayer==false)
			spawnBarb();
		sendList();	
		if(multiplayer==false){
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
		if(multiplayer==true){		
			delt=true;
			waiting=true;
			gameState=WAIT;					
		}
		map.updateFogOfWar();
		for (int i = 0; i < list.size(); i++){
			if (list.get(i).friend){
				Map.reveal(list.get(i).tileX, list.get(i).tileY, list.get(i).move);
			}
		} 
	}
	public void spawnBarb(){
		if(turn>25){
			int x = rand.nextInt(Map.maxX-1);
			int y = rand.nextInt(Map.maxY-1);
			if (rand.nextInt(20)== 0){
				list.add(new NPC(this, getImages("Chara1.png",3,4), x, y, 3, RPGSprite.DOWN, 2,5, 100, 1, 1, 1, 2, "Barbarian",logic));
				Map.send(playfield, list, this);
				
			}
		}
	}

}