	

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
	static int spX=0, spY=0;//storleksvariabler för síngleplaýerkartan
	static boolean bordat=false, zoom = false;
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
    boolean fajtats=false;
    static boolean attacked=false;
    static int bombX=1000,bombY=1000,hpL=1000;
	RPGDialog		dialog;
	boolean gubbeklickad=false		;
	static boolean funnen=false;
	Result returned;
	RPGSprite fiende;			// the NPC we talk to
	int	fiendeDirection, wave1, wave2;	// old NPC direction before
	int h = 0, a=0,F=0;								// we talk to him/her
	ArrayList<RPGSprite> list;
	ArrayList<RPGSprite> listny;
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
	public static Result receivedS;
	static String nick;
	boolean istad=false;
	boolean faild=false;
	static RPGSprite enhet = null;
	static ArrayList<Construction> byggList = new ArrayList<Construction>();
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
		TurnBar.send(this);
		TurnBar.initResources();
		if (multiplayer)
			map = new Map(bsLoader, bsIO, parent, nick);
		else if (!multiplayer)
			map = new Map(bsLoader, bsIO, parent, nick, spX, spY);
		playfield = new PlayField2(map);
		p = ((Civcraft)parent).getProxy();
		list = new ArrayList<RPGSprite>();
		listny = new ArrayList<RPGSprite>();
		Map.send(playfield, list, this);
		playfield.setComparator(new Comparator() {
			public int compare(Object o1, Object o2) {
				// sort based on y-order
				return (int) (((Sprite) o1).getY()-((Sprite) o2).getY());
			}
		});
		
		if(multiplayer==false){
			if (map.sizeX==1 && map.sizeY == 1){
				list.add(new RPGSprite(this, getImages("Chara2.png",3,4), 0, 0, 3, RPGSprite.DOWN, 0, 2, 10, 1, 1, 1, 2, "Settler",100,true));
				list.add(new RPGSprite(this, getImages("Chara2.png",3,4), 0, 0, 3, RPGSprite.DOWN, 0, 2, 10, 1, 1, 1, 2, "Settler",100,true));
			}
			else{
				int rX = rand.nextInt(map.maxX+1); 
				int rY = rand.nextInt(map.maxY+1);
				int rXX = 0, rYY = 0;
				while (map.layer1[rX][rY]==0 || map.layer1[rX][rY]==1){
					rX = rand.nextInt(map.maxX+1); 
					rY = rand.nextInt(map.maxY+1);
				}
				list.add(new RPGSprite(this, getImages("Chara2.png",3,4), rX, rY, 3, RPGSprite.DOWN, 0, 2, 10, 1, 1, 1, 2, "Settler",100,true));
				rXX = rand.nextInt(7)-3;
				rYY = rand.nextInt(7)-3;
				while (rX+rXX>map.maxX)
					rXX-=2;
				while (rY+rYY>map.maxY)
					rYY-=2;
				while (rX+rXX<0)
					rXX+=2;
				while (rY+rYY<0)
					rYY+=2;
				while (map.layer1[rX+rXX][rY+rYY]==0 || map.layer1[rX+rXX][rY+rYY]==1){
					rXX = rand.nextInt(7)-3;
					rYY = rand.nextInt(7)-3;
					while (rX+rXX>map.maxX)
						rXX-=2;
					while (rY+rYY>map.maxY)
						rYY-=2;
					while (rX+rXX<0)
						rXX+=2;
					while (rY+rYY<0)
						rYY+=2;
				}
				list.add(new RPGSprite(this, getImages("Chara2.png",3,4), rX+rXX, rY+rYY, 3, RPGSprite.DOWN, 0, 2, 10, 1, 1, 1, 2, "Settler",100,true));

				playfield.add(list.get(0));
				playfield.add(list.get(1));
				TurnBar.send(this);
				ActionBar.send(list.get(0), playfield, map.layer3, list, this, map);
				map.setToCenter(list.get(0));
				actionBar.initResources();
				TurnBar.initResources();
				for (int i=0; i < list.size(); i++)
					Map.reveal(list.get(i).tileX,list.get(i).tileY,list.get(i).sightRange);
			}
		}
		dialog = new RPGDialog(fontManager.getFont(getImage("BitmapFont.png")),
							   getImage("Box.png", false));
					
		
		if(multiplayer==true){
			
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
						TurnBar.send(this);
						TurnBar.initResources();
					}
					else if(theType.equalsIgnoreCase("Settler") && theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("Chara2.png",3,4), xe,ye, 3, RPGSprite.UP, 0, 2, 10, 1, 1, 1, 2, "Settler",100,true));
						playfield.add(list.get(list.size()-1));
						ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
						map.setToCenter(list.get(list.size()-1));
						actionBar.initResources();
						TurnBar.send(this);
						TurnBar.initResources();
					}
					else if(theType.equalsIgnoreCase("Galley") && theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("GalleySheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 30,25, 250, 1, 2, 4, 2, "Galley",1000, true, 5));
						playfield.add(list.get(list.size()-1));
						ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
						map.setToCenter(list.get(list.size()-1));
						actionBar.initResources();
						TurnBar.send(this);
						TurnBar.initResources();
					}
					else if(theType.equalsIgnoreCase("Crusader") && theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("CrusaderSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 18,12, 100, 1, 1, 2, 2, "Crusader",1000,true));
						playfield.add(list.get(list.size()-1));
						ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
						map.setToCenter(list.get(list.size()-1));
						actionBar.initResources();
						TurnBar.send(this);
						TurnBar.initResources();
					}
					else if(theType.equalsIgnoreCase("Trireme") && theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("TriremeSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 4,3, 50, 1, 1, 3, 2, "Trireme",1000, true, 2));
						playfield.add(list.get(list.size()-1));
						ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
						map.setToCenter(list.get(list.size()-1));
						actionBar.initResources();
						TurnBar.send(this);
						TurnBar.initResources();
					}
					else if(theType.equalsIgnoreCase("Caravel") && theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("CaravelSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 50,40, 100, 1, 3, 6, 2, "Caravel",1000, true, 3));
						playfield.add(list.get(list.size()-1));
						ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
						map.setToCenter(list.get(list.size()-1));
						actionBar.initResources();
						TurnBar.send(this);
						TurnBar.initResources();
					}
					else if(theType.equalsIgnoreCase("Cannon") && theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("CannonSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 30,3, 100, 1, 4, 1, 2, "Cannon",1000,0,100,100,true));
						playfield.add(list.get(list.size()-1));
						ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
						map.setToCenter(list.get(list.size()-1));
						actionBar.initResources();
						TurnBar.send(this);
						TurnBar.initResources();
					}
					else if(theType.equalsIgnoreCase("Knight") && !theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("KnightSheet.png",3,4), xe,ye, 3, RPGSprite.DOWN, 12,8, 100, 1, 1, 2, 2, "Knight",1000,false));
					}
					else if(theType.equalsIgnoreCase("Settler") && !theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("Chara2.png",3,4), xe,ye, 3, RPGSprite.DOWN, 0, 2, 10, 1, 1, 1, 2, "Settler",100,false));
						if(Map.fogofwar[xe][ye] < map.lwrlmt)
							playfield.add(list.get(list.size()-1));
					}
					else if(theType.equalsIgnoreCase("Crusader") && !theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("CrusaderSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 18,12, 100, 1, 1, 2, 2, "Crusader",1000,false));
					}
					else if(theType.equalsIgnoreCase("Galley") && !theOwner.equalsIgnoreCase(nick)){			
						list.add(new RPGSprite(this, getImages("GalleySheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 30,25, 250, 1, 2, 4, 2, "Galley",1000, false, 5));
					}
					else if(theType.equalsIgnoreCase("Trireme") && !theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("TriremeSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 4,3, 50, 1, 1, 3, 2, "Trireme",1000, false, 2));
					}
					else if(theType.equalsIgnoreCase("Caravel") && !theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("CaravelSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 50,40, 100, 1, 3, 6, 2, "Caravel",1000, false, 3));
					}
					else if(theType.equalsIgnoreCase("Cannon") && !theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("CannonSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 30,3, 100, 1, 4, 1, 2, "Cannon",1000,0,100,100,false));
					}
					else if(theType.equalsIgnoreCase("Catapult") && !theOwner.equalsIgnoreCase(nick)){			
						list.add(new RPGSprite(this, getImages("CatapultSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 12,3, 100, 1, 2, 1, 2, "Catapult",1000, 50,0,0,false));
					}
					else if(theType.equalsIgnoreCase("Trebuchet") && !theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("TrebuchetSheet.png",3,4),xe,ye, 3, RPGSprite.RIGHT, 20,2, 100, 1, 3, 1, 2, "Trebuchet",1000,75,0,0,false));
					}
					else if(theType.equalsIgnoreCase("Archer") && !theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, parent.getImages("ArcherSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 4,2, 100, 1, 2, 1, 2, "Archer",1000,false));
					}
					else if(theType.equalsIgnoreCase("Infantry") && !theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("InfantrySheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 3,3, 100, 1, 1, 1, 2, "Infantry",1000,false));
					}
					else if(theType.equalsIgnoreCase("Legion") && !theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("LegionSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 6,4, 100, 1, 1, 1, 2, "Legion",1000,false));
					}
					else if(theType.equalsIgnoreCase("Pikeman") && !theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this, getImages("PikemanSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 2,3, 100, 1, 1, 1, 2, "Pikeman",1000,false));
					}
					else if(theType.equalsIgnoreCase("Phalanx") && !theOwner.equalsIgnoreCase(nick)){				
						list.add(new RPGSprite(this,getImages("PhalanxSheet.png",3,4), xe,ye, 3, RPGSprite.LEFT, 2, 5, 100, 1, 1, 3, 2, "Phalanx",1000, false));
					}
					else if(theType.equalsIgnoreCase("Musketeer") && !theOwner.equalsIgnoreCase(nick)){			
						list.add(new RPGSprite(this, getImages("MusketeerSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 8,6, 100, 1, 2, 1, 2, "Musketeer",1000,false));
					}
					
				}
				if(received.existCity(i)){
					theOwner = received.getCityOwner(i);
					if(theOwner==(nick)){
						list.add(new RPGSprite(this,getImages("citytile.png",3,4), xe,ye, 3, RPGSprite.DOWN, 1, 1, 10, 1, 1, 1, 2, "City",1000, true));														
						playfield.add(list.get(list.size()-1));
						ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
						map.setToCenter(list.get(list.size()-1));
						actionBar.initResources();
						TurnBar.send(this);
						TurnBar.initResources();
						
					}
					if(!theOwner.equalsIgnoreCase(nick)){
						list.add(new RPGSprite(this,getImages("citytile.png",3,4), xe,ye, 3, RPGSprite.DOWN, 1, 1, 10, 1, 1, 1, 2, "City",1000, false));														
						if(Map.fogofwar[xe][ye] < map.lwrlmt)
							playfield.add(list.get(list.size()-1));
						
					}
				}
				for (int r=0; r < list.size(); r++ ){
					if(list.get(r).friend==true)
						Map.reveal(list.get(r).tileX,list.get(r).tileY,list.get(r).sightRange);
				}
				
				Map.send(playfield, list, this);
				
			}
		}
	}
    public void spMap(String nick, int x, int y){
		map = new Map(bsLoader, bsIO, parent, nick, x, y);
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
						   	fajtats=true;
						    faild=false;
						   	battlescore=3;
							try {
								returned = p.combatRequest(list.get(h).getXX(), list.get(h).getYY(), fiende.getXX(), fiende.getYY());
							} catch (FailedException e) {
								faild=true;
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(returned!=null && faild==false){
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
						   for(j=0; j<list.size(); j++){
							   int ys = list.get(h).getYY();
							   int xs = list.get(h).getXX();
							   if(xs == list.get(j).getXX() && ys == list.get(j).getYY() && list.get(j).getTyp()=="SiegeTower"){
								   list.get(h).setATK(list.get(h).getATK()*2);						   
								   break;
							   }else list.get(h).setATK(list.get(h).origatk);
						   
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
								// fiende = (RPGSprite) map.getLayer3(targetX, targetY);
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
										istad=false;
										int g = list.get(h).getXX();
										int gt = list.get(h).getYY();
										test.add(list.get(h).getXX());
										test.add(list.get(h).getYY());
										list.get(h).test(tileAt.x,tileAt.y, list.get(h), list);
										
										test.add(list.get(h).getXX());
										test.add(list.get(h).getYY());
									
										for(int o=0;o<list.size();o++){
											
											if(g == list.get(o).getXX() && gt == list.get(o).getYY() && list.get(o).getTyp()=="City"){
												istad=true;
												
												try {
													
													p.moveOutUnit(g,gt, list.get(h).getTyp(), list.get(h).getHP(), list.get(h).getXX(), list.get(h).getYY());
													list.get(h).sparad=false;
													test.clear();
													
													
												} catch (FailedException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}
										}
										if(istad==false){
											
											for(int o=0;o<list.size();o++){
												
												if(list.get(o).getXX() == list.get(h).getXX() && list.get(o).getYY() == list.get(h).getYY() && list.get(o).getTyp()=="City"){
													
													list.get(h).sparad=true;
												}
											}
											try {
												
												p.moveUnit(test);
												test.clear();
												ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);

											
											} catch (FailedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
												
											}
										}
										
											
										if (!bordat)
											list.get(h).movement();	
										
										if (bordat){
											playfield.remove(list.get(h));
											
											list.remove(list.get(h));
											bordat=false;
										}
										
											
										a=0;
										break;
										
									}
							list.get(h).test(tileAt.x,tileAt.y, list.get(h), list);
							if (!bordat)
								list.get(h).movement();	
							a=0;
							if (bordat){
								playfield.remove(list.get(h));
								
								list.remove(h);
								bordat=false;
							}
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
						if(multiplayer==true){
							if(h<list.size()){
								if(list.get(h)!=null){
									gameState=CHOOSING;
									funnen=true;
								}
							}
						}
						if(multiplayer==false){
							if(h<list.size()){
								if(list.get(h)!=null){
									gameState=CHOOSING;
									funnen=true;
								}
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
				if(attacked==true){
					
					dialogNP[0]="You were attacked on tile "+bombX+","+bombY+"\n";
					dialogNP[1]="and lost "+hpL+" MP\n";
					dialogNP[2]="";						   
					windowHandler.setLabel(dialogNP[0] + dialogNP[1] + dialogNP[2]);
					windowHandler.setVisible(true);						   				  						   				   				   
					gameState=TILEI;
				}
				if(waiting==false){		
					TurnBar.send(this);
					TurnBar.initResources();
				  if(turn>1){	
					if(delt==true){
						for(int f = 0; f<list.size();f++){
							if(list.get(f).sparad==false)
								playfield.remove(list.get(f));
							
							
						}
						delt=false;
						
					}
					listny.clear();
					for(int f=0;f<list.size();f++){
						if(list.get(f).sparad==true){
							list.get(f).mov = true;
							list.get(f).moveThisTurn = 0;					
							list.get(f).setMove(2);
							listny.add(list.get(f));
						}
					}
					list.clear();
					if(listny!=null){
						for(int f=0;f<listny.size();f++){
							
							list.add(listny.get(f));
						
						}
					}
					Map.send(playfield, list, this);
					int numberStartingPositions = received.getNumberTiles();
					String theType, theOwner;
					int hpLeft;
					//ActionBar.addSpr();
					spawnUnit();
					//spawnBarb();
					for(int i=0; i<numberStartingPositions; i++){
						xe = received.getTileX(i);
						ye = received.getTileY(i);
							
							if(received.existUnit(i)){	
								xe = received.getTileX(i);
								ye = received.getTileY(i);
								hpLeft = received.getUnitManPower(i);							
								theType = received.getUnitType(i);	
								theOwner = received.getUnitOwner(i);
								
								if(theType.equalsIgnoreCase("Knight") && theOwner.equalsIgnoreCase(nick)){	
														
									list.add(new RPGSprite(this, getImages("KnightSheet.png",3,4), xe,ye, 3, RPGSprite.UP, 12,8, hpLeft, 1, 1, 2, 2, "Knight",1000,true));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(theType.equalsIgnoreCase("Galley") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("GalleySheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 30,25, 250, 1, 2, 4, 2, "Galley",1000, true, 5));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(theType.equalsIgnoreCase("Cavalry") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("CavalrySheet.png",3,4),xe,ye, 3, RPGSprite.RIGHT, 6,4, 100, 1, 1, 2, 2, "Cavalry",1000,true));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(theType.equalsIgnoreCase("Crusader") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("CrusaderSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 18,12, 100, 1, 1, 2, 2, "Crusader",1000,true));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(theType.equalsIgnoreCase("Trireme") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("TriremeSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 4,3, 50, 1, 1, 3, 2, "Trireme",1000, true, 2));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(theType.equalsIgnoreCase("Caravel") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("CaravelSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 50,40, 100, 1, 3, 6, 2, "Caravel",1000, true, 3));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(theType.equalsIgnoreCase("Cannon") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("CannonSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 30,3, 100, 1, 4, 1, 2, "Cannon",1000,0,100,100,true));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(theType.equalsIgnoreCase("Infantry") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("InfantrySheet.png",3,4), xe,ye, 3, RPGSprite.DOWN, 3,3, hpLeft, 1, 1, 1, 2, "Infantry",1000,true));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(theType.equalsIgnoreCase("Catapult") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("CatapultSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 12,3, 100, 1, 2, 1, 2, "Catapult",1000, 50,0,0,true));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(theType.equalsIgnoreCase("Trebuchet") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("TrebuchetSheet.png",3,4),xe,ye, 3, RPGSprite.RIGHT, 20,2, hpLeft, 1, 3, 1, 2, "Trebuchet",1000,75,0,0,true));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(theType.equalsIgnoreCase("Archer") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("ArcherSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 4,2, hpLeft, 1, 2, 1, 2, "Archer",1000,true));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(theType.equalsIgnoreCase("Legion") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("LegionSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 6,4, hpLeft, 1, 1, 1, 2, "Legion",1000,true));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(theType.equalsIgnoreCase("Pikeman") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("PikemanSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 2,3, 100, 1, 1, 1, 2, "Pikeman",1000,true));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(theType.equalsIgnoreCase("Phalanx") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this,getImages("PhalanxSheet.png",3,4), xe,ye, 3, RPGSprite.LEFT, 2, 5, 100, 1, 1, 3, 2, "Phalanx",1000, true));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(theType.equalsIgnoreCase("Musketeer") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("MusketeerSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 8,6, 100, 1, 2, 1, 2, "Musketeer",1000,true));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(theType.equalsIgnoreCase("Siege Tower") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("SiegeTowerSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 1,0, 100, 1, 2, 1, 2, "SiegeTower",50,true));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(theType.equalsIgnoreCase("Wagon Train") && theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("WagonTrainSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 1,0, 100, 1, 1, 2, 2, "WagonTrain",100,true));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								
								if(theType.equalsIgnoreCase("Knight") && !theOwner.equalsIgnoreCase(nick)){	
										
									list.add(new RPGSprite(this, getImages("KnightSheet.png",3,4), xe,ye, 3, RPGSprite.DOWN, 12,8, hpLeft, 1, 1, 2, 2, "Knight",1000,false));				
									TurnBar.send(this);
									TurnBar.initResources();
									
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
																
								}
								if(theType.equalsIgnoreCase("Infantry") && !theOwner.equalsIgnoreCase(nick)){	
									
									list.add(new RPGSprite(this, getImages("InfantrySheet.png",3,4), xe,ye, 3, RPGSprite.DOWN, 3,3, hpLeft, 1, 1, 1, 2, "Infantry",1000,false));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
																
								}
								if(theType.equalsIgnoreCase("Crusader") && !theOwner.equalsIgnoreCase(nick)){	
									
									list.add(new RPGSprite(this, getImages("CrusaderSheet.png",3,4), xe,ye, 3, RPGSprite.DOWN, 18,12, hpLeft, 1, 1, 2, 2, "Crusader",1000,false));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
																
								}
							
								if(theType.equalsIgnoreCase("Settler") && theOwner.equalsIgnoreCase(nick)){	
									
									list.add(new RPGSprite(this, getImages("Chara2.png",3,4), xe,ye, 3, RPGSprite.UP, 0, 2, hpLeft, 1, 1, 1, 2, "Settler",100,true));
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
									
								}
								
								if(theType.equalsIgnoreCase("Settler") && !theOwner.equalsIgnoreCase(nick)){
									
									list.add(new RPGSprite(this, getImages("Chara2.png",3,4), xe,ye, 3, RPGSprite.DOWN, 0, 2, 10, 1, 1, 1, 2, "Settler",100,false));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt){
										playfield.add(list.get(list.size()-1));
										
									}
									Map.send(playfield, list, this);
								}
								if(theType.equalsIgnoreCase("Crusader") && !theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("CrusaderSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 18,12, 100, 1, 1, 2, 2, "Crusader",1000,false));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
								}
								if(theType.equalsIgnoreCase("Cavalry") && !theOwner.equalsIgnoreCase(nick)){			
									list.add(new RPGSprite(this, getImages("CavalrySheet.png",3,4),xe,ye, 3, RPGSprite.RIGHT, 6,4, 100, 1, 1, 2, 2, "Cavalry",1000,false));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
								}
								if(theType.equalsIgnoreCase("Siege Tower") && !theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("SiegeTowerSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 1,0, 100, 1, 2, 1, 2, "SiegeTower",50,false));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
								}
								if(theType.equalsIgnoreCase("Wagon Train") && !theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("WagonTrainSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 1,0, 100, 1, 1, 2, 2, "WagonTrain",100,false));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
								}
								if(theType.equalsIgnoreCase("Galley") && !theOwner.equalsIgnoreCase(nick)){			
									list.add(new RPGSprite(this, getImages("GalleySheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 30,25, 250, 1, 2, 4, 2, "Galley",1000, false, 5));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] == 0)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
								}
								if(theType.equalsIgnoreCase("Trireme") && !theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("TriremeSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 4,3, 50, 1, 1, 3, 2, "Trireme",1000, false, 2));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
								}
								if(theType.equalsIgnoreCase("Caravel") && !theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("CaravelSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 50,40, 100, 1, 3, 6, 2, "Caravel",1000, false, 3));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
								}
								if(theType.equalsIgnoreCase("Cannon") && !theOwner.equalsIgnoreCase(nick)){		
									list.add(new RPGSprite(this, getImages("CannonSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 30,3, 100, 1, 4, 1, 2, "Cannon",1000,0,100,100,false));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
								}
								if(theType.equalsIgnoreCase("Catapult") && !theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("CatapultSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 12,3, 100, 1, 2, 1, 2, "Catapult",1000, 50,0,0,false));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
								}
								if(theType.equalsIgnoreCase("Trebuchet") && !theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("TrebuchetSheet.png",3,4),xe,ye, 3, RPGSprite.RIGHT, 20,2, 100, 1, 3, 1, 2, "Trebuchet",1000,75,0,0,false));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
								}
								if(theType.equalsIgnoreCase("Archer") && !theOwner.equalsIgnoreCase(nick)){			
									list.add(new RPGSprite(this, parent.getImages("ArcherSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 4,2, 100, 1, 2, 1, 2, "Archer",1000,false));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
								}
								if(theType.equalsIgnoreCase("Infantry") && !theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("InfantrySheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 3,3, 100, 1, 1, 1, 2, "Infantry",1000,false));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
								}
								if(theType.equalsIgnoreCase("Legion") && !theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("LegionSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 6,4, 100, 1, 1, 1, 2, "Legion",1000,false));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
								}
								if(theType.equalsIgnoreCase("Pikeman") && !theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this, getImages("PikemanSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 2,3, 100, 1, 1, 1, 2, "Pikeman",1000,false));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
								}
								if(theType.equalsIgnoreCase("Phalanx") && !theOwner.equalsIgnoreCase(nick)){				
									list.add(new RPGSprite(this,getImages("PhalanxSheet.png",3,4), xe,ye, 3, RPGSprite.LEFT, 2, 5, 100, 1, 1, 3, 2, "Phalanx",1000, false));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
								}
								if(theType.equalsIgnoreCase("Musketeer") && !theOwner.equalsIgnoreCase(nick)){			
									list.add(new RPGSprite(this, getImages("MusketeerSheet.png",3,4), xe,ye, 3, RPGSprite.RIGHT, 8,6, 100, 1, 2, 1, 2, "Musketeer",1000,false));
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									Map.send(playfield, list, this);
								}
							}
							if(received.existCity(i)){
								theOwner = received.getCityOwner(i);
								
								if(theOwner.equalsIgnoreCase(nick)){
									list.add(new RPGSprite(this,getImages("citytile.png",3,4), xe,ye, 3, RPGSprite.DOWN, 1, 1, 10, 1, 1, 1, 2, "City",1000, true));														
									playfield.add(list.get(list.size()-1));
									ActionBar.send(list.get(list.size()-1), playfield, map.layer3, list, this, map);
									map.setToCenter(list.get(list.size()-1));
									actionBar.initResources();
									TurnBar.send(this);
									TurnBar.initResources();
									
								}
								if(!theOwner.equalsIgnoreCase(nick)){
									list.add(new RPGSprite(this,getImages("citytile.png",3,4), xe,ye, 3, RPGSprite.DOWN, 1, 1, 10, 1, 1, 1, 2, "City",1000, false));														
									TurnBar.send(this);
									TurnBar.initResources();
									if(Map.fogofwar[xe][ye] < map.lwrlmt)
										playfield.add(list.get(list.size()-1));
									
								}
							}}}
						gameState = PLAYING;
					
						//break;
				}	
				break;
				
			case TILEI:
				if(multiplayer==false){
					if (keyPressed(KeyEvent.VK_Z) ||
						rightClick() ||
						keyPressed(KeyEvent.VK_ESCAPE)) {
						
						//fiende.setDirection(fiendeDirection);
						windowHandler.setVisible(false);
						gameState = PLAYING;
					
					}
				}
				if(multiplayer==true){
					if (keyPressed(KeyEvent.VK_Z) ||
							rightClick() ||
							keyPressed(KeyEvent.VK_ESCAPE)) {
							
							
							if(attacked==true){
								attacked=false;
								gameState=WAIT;
								
							}
							else if(fajtats==true){
								fajtats=false;
								gameState=PLAYING;
								
							}
							else if(fajtats==false && attacked==false){
								
								gameState=PLAYING;
								
							}
							//fiende.setDirection(fiendeDirection);
							windowHandler.setVisible(false);
							
											
					}
					
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
		if(multiplayer==true){
			TurnBar.line1=("Not your turn!");
			TurnBar.initResources();
		}
		if(multiplayer==false){
			TurnBar.line1=("Its your turn!");
			TurnBar.initResources();
			//ActionBar.addSpr();
			spawnUnit();
		}
		
		Map.send(playfield, list, this);
		if(multiplayer==true){
			try {
				
				p.endTurn();			
			} catch (FailedException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
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
			if (rand.nextInt(5)==0 && Map.fogofwar[x][y] < map.lwrlmt){
				if(map.layer1[x][y] == 1 || map.layer1[x][y] == 0)
					if (rand.nextInt(2) == 0)
						list.add(new NPC(this, getImages("GalleySheet.png",3,4), x,y, 3, RPGSprite.RIGHT, 30,25, 250, 1, 2, 4, 2, "BarbarianB",logic));
					else
						list.add(new NPC(this, getImages("TriremeSheet.png",3,4), x,y, 3, RPGSprite.RIGHT, 4,3, 50, 1, 1, 3, 2, "BarbarianB",logic));
				else{
					int rnd = rand.nextInt(9);
					if (rnd == 0)
						list.add(new NPC(this, getImages("ArcherSheet.png",3,4), x,y, 3, RPGSprite.RIGHT, 4,2, 100, 1, 2, 1, 2, "Barbarian",logic));
					else if (rnd == 1)
						list.add(new NPC(this, getImages("MusketeerSheet.png",3,4), x,y, 3, RPGSprite.RIGHT, 8,6, 100, 1, 2, 1, 2, "Barbarian",logic));
					else if (rnd == 2)
						list.add(new NPC(this, getImages("PhalanxSheet.png",3,4), x,y, 3, RPGSprite.LEFT, 2, 5, 100, 1, 1, 1, 2, "Barbarian",logic));
					else if (rnd == 3)
						list.add(new NPC(this, getImages("LegionSheet.png",3,4), x,y, 3, RPGSprite.RIGHT, 6,4, 100, 1, 1, 1, 2, "Barbarian",logic));
					else if (rnd == 4)
						list.add(new NPC(this, getImages("InfantrySheet.png",3,4), x,y, 3, RPGSprite.RIGHT, 3,3, 100, 1, 1, 1, 2, "Barbarian",logic));
					else if (rnd == 5)
						list.add(new NPC(this, getImages("PikemanSheet.png",3,4), x,y, 3, RPGSprite.RIGHT, 2,3, 100, 1, 1, 1, 2, "Barbarian",logic));
					else if (rnd == 6)
						list.add(new NPC(this, getImages("CavalrySheet.png",3,4),x,y, 3, RPGSprite.RIGHT, 6,4, 100, 1, 1, 2, 2, "Barbarian",logic));
					else if (rnd == 7)
						list.add(new NPC(this, getImages("KnightSheet.png",3,4), x,y, 3, RPGSprite.RIGHT, 12,8, 100, 1, 1, 2, 2, "Barbarian",logic));
					else if (rnd == 8)	
						list.add(new NPC(this, getImages("CrusaderSheet.png",3,4), x,y, 3, RPGSprite.RIGHT, 18,12, 100, 1, 1, 2, 2, "Barbarian",logic));
				}
				Map.send(playfield, list, this);
				
			}
		}
	}
	void spawnUnit(){
		for (int i = 0; i < byggList.size(); i++){
			if (byggList.get(i).turnDone==turn){
				createUnit(byggList.get(i).tileX, byggList.get(i).tileY, byggList.get(i).building);
				byggList.get(i).sprite.idle=true;
				byggList.remove(i);
			}
		}
	}
	private void createUnit(int x,int y, String str){
		if (str.equalsIgnoreCase("Archer")){
			list.add(new RPGSprite(this, parent.getImages("ArcherSheet.png",3,4), x, y, 3, RPGSprite.RIGHT, 4,2, 100, 1, 2, 1, 2, "Archer",1000,true));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Archer",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("Phalanx")){
			list.add(new RPGSprite(this,parent.getImages("PhalanxSheet.png",3,4), x,y, 3, RPGSprite.LEFT, 2, 5, 100, 1, 1, 1, 2, "Phalanx",1000, true));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Phalanx",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
				}
			}
					
		}
		else if(str.equalsIgnoreCase("Diplomat")){
			list.add(new RPGSprite(this,parent.getImages("PhalanxSheet.png",3,4), x,y, 3, RPGSprite.LEFT, 1, 0, 25, 1, 1, 3, 2, "Diplomat",25, true));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Diplomat",25);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("Infantry")){
			list.add(new RPGSprite(this, parent.getImages("InfantrySheet.png",3,4), x, y, 3, RPGSprite.RIGHT, 3,3, 100, 1, 1, 1, 2, "Infantry",1000,true));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Infantry",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("WagonTrain")){
			list.add(new RPGSprite(this, parent.getImages("WagonTrainSheet.png",3,4), x, y, 3, RPGSprite.RIGHT, 1,0, 100, 1, 1, 2, 2, "WagonTrain",100,true));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Wagon Train",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("Settler")){	 		
			list.add(new RPGSprite(this, parent.getImages("Chara2.png",3,4), x, y, 3, RPGSprite.RIGHT, 0, 2, 10, 1, 1, 1, 2, "Settler",100,true)); 		
			playfield.add(list.get(list.size()-1));		
			list.get(list.size()-1).sparad=true; 		
			if(RPGGame.multiplayer==true){ 		
				try {
					p.madeUnit(x, y, nick,"Settler",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("Catapult")){
			list.add(new RPGSprite(this, parent.getImages("CatapultSheet.png",3,4), x, y, 3, RPGSprite.RIGHT, 12,3, 100, 1, 2, 1, 2, "Catapult",1000, 50,0,0,true));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Catapult",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("Legion")){
			list.add(new RPGSprite(this, parent.getImages("LegionSheet.png",3,4), x, y, 3, RPGSprite.RIGHT, 6,4, 100, 1, 1, 1, 2, "Legion",1000,true));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Legion",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("Pikeman")){
			list.add(new RPGSprite(this, parent.getImages("PikemanSheet.png",3,4), x, y, 3, RPGSprite.RIGHT, 2,3, 100, 1, 1, 1, 2, "Pikeman",1000,true));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Pikeman",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("Musketeer")){
			list.add(new RPGSprite(this, parent.getImages("MusketeerSheet.png",3,4), x, y, 3, RPGSprite.RIGHT, 8,6, 100, 1, 2, 1, 2, "Musketeer",1000,true));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Musketeer",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("SiegeTower")){
			list.add(new RPGSprite(this, parent.getImages("SiegeTowerSheet.png",3,4), x, y, 3, RPGSprite.RIGHT, 1,0, 100, 1, 2, 1, 2, "SiegeTower",50,true));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Siege Tower",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("Trebuchet")){
			list.add(new RPGSprite(this, parent.getImages("TrebuchetSheet.png",3,4),x, y, 3, RPGSprite.RIGHT, 20,2, 100, 1, 3, 1, 2, "Trebuchet",1000,75,0,0,true));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Trebuchet",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("Cavalry")){
			list.add(new RPGSprite(this, parent.getImages("CavalrySheet.png",3,4),x, y, 3, RPGSprite.RIGHT, 6,4, 100, 1, 1, 2, 2, "Cavalry",1000,true));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Cavalry",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("Knight")){
			list.add(new RPGSprite(this, parent.getImages("KnightSheet.png",3,4), x, y, 3, RPGSprite.RIGHT, 12,8, 100, 1, 1, 2, 2, "Knight",1000,true));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Knight",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("Crusader")){
			list.add(new RPGSprite(this, parent.getImages("CrusaderSheet.png",3,4), x, y, 3, RPGSprite.RIGHT, 18,12, 100, 1, 1, 2, 2, "Crusader",1000,true));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Crusader",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("Trireme")){
			list.add(new RPGSprite(this, parent.getImages("TriremeSheet.png",3,4), x, y, 3, RPGSprite.RIGHT, 4,3, 50, 1, 1, 3, 2, "Trireme",1000, true, 2));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Trireme",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("Cannon")){
			list.add(new RPGSprite(this, parent.getImages("CannonSheet.png",3,4), x, y, 3, RPGSprite.RIGHT, 30,3, 100, 1, 4, 1, 2, "Cannon",1000,0,100,100,true));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Cannon",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("Galley")){
			list.add(new RPGSprite(this, parent.getImages("GalleySheet.png",3,4), x, y, 3, RPGSprite.RIGHT, 30,25, 250, 1, 2, 4, 2, "Galley",1000, true, 5));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Galley",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(str.equalsIgnoreCase("Caravel")){
			list.add(new RPGSprite(this, parent.getImages("CaravelSheet.png",3,4), x, y, 3, RPGSprite.RIGHT, 50,40, 100, 1, 3, 6, 2, "Caravel",1000, true, 3));
			playfield.add(list.get(list.size()-1));
			list.get(list.size()-1).sparad=true;
			if(RPGGame.multiplayer==true){
				try {
					p.madeUnit(x, y, nick,"Caravel",100);
				} catch (FailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}