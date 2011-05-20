
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.golden.gamedev.Game;
import com.golden.gamedev.GameEngine;
import com.golden.gamedev.GameObject;
import com.golden.gamedev.engine.BaseInput;
import com.golden.gamedev.engine.BaseLoader;
import com.golden.gamedev.gui.TButton;
import com.golden.gamedev.gui.TPanel;
import com.golden.gamedev.gui.TTextField;
import com.golden.gamedev.gui.toolkit.FrameWork;
import com.golden.gamedev.object.Sprite;


public class ActionBar{
	private FrameWork actionFrame = null;
	private TPanel actionBar = null;
	private TTextField textField = null, textField2 = null, textField3 = null;
	private final int ACTION_BAR_HEIGHT = 85;
	private static GameEngine parent = null;
	static ArrayList<RPGSprite> list = new ArrayList<RPGSprite>();
	static PlayField2 playfield;
	static RPGSprite[][] layer3;
	static boolean selmova=false, even=false;
	static RPGSprite spr;
	static RPGGame own;
	static RPGSprite sprC;
	String line1 = null, line2 = null, line3 = null;
	BaseLoader bsLoader;
	Chipset csFow;
	static int oldturn=0;
	static Map map;
	static Graphics2D g;
	Proxy p;
	static boolean phal=false,diplo=false, siege=false, wagon=false, infa=false, treb=false, carav=false, gall=false, trir=false, cann=false, cava=false, cata=false, legi=false, pike=false, knig=false, crus=false, arch=false, muske=false;
	public ActionBar(GameEngine parent) {
		this.parent = parent;
	}
	public static void send(RPGSprite h,PlayField2 playfieldd, RPGSprite[][] layer, ArrayList<RPGSprite> listd, RPGGame owna, Map mapp){
		spr = h;
		playfield=playfieldd;
		list=listd;
		layer3=layer;
		own=owna;
		map=mapp;
	}
	
	public void initResources(){
		layer3 = new RPGSprite[40][25];
		p = ((Civcraft)parent).getProxy();
		if (spr.getTyp() == "Settler"){
			actionFrame = new FrameWork(parent.bsInput, parent.getWidth(), parent.getHeight());
			actionBar = new TPanel(0, parent.getHeight() - ACTION_BAR_HEIGHT, parent.getWidth(), ACTION_BAR_HEIGHT);
			actionBar.setExternalUI(parent.getImages("ActionBarImage.png", 2, 1), false);
			
			TButton btn = new TButton("Move", 5, 5, 35, 30) {
				public void doAction() {
					RPGGame.gameState=0;
					if(spr.getMov()){					
						spr.selmov=true;				      
					}
					else if(!spr.getMov()){					
						RPGGame.funnen=false;				      
					}
				}
			};
			TButton btn2 = new TButton("Disband", 5, 45, 70, 30){
				public void doAction(){
					RPGGame.gameState=0;
					RPGGame.funnen=false;
					map.layer3[spr.getXX()][spr.getYY()] = null;		
					playfield.remove(spr);
					list.remove(spr);
									
				}
			};
			TButton btn3 = new TButton("City", 45, 5, 50, 30){
				public void doAction(){
					RPGGame.gameState=0;
					map.layer3[spr.getXX()][spr.getYY()] = null;
					list.add(new RPGSprite(own,parent.getImages("citytile.png",3,4), spr.getXX(),spr.getYY(), 3, RPGSprite.DOWN, 1, 1, 10, 1, 1, 1, 2, "City",1000, true));														
					playfield.add(list.get(list.size()-1));
					list.get(list.size()-1).setPOP(1000);	
					map.layer3[spr.getXX()][spr.getYY()] = list.get(list.size()-1);
					//list.add(new RPGSprite(own,parent.getImages("PhalanxSheet.png",3,4), spr.getXX(),spr.getYY()+1, 3, RPGSprite.LEFT, 2, 5, 100, 1, 1, 1, 2, "Settler",1000, true));
					//playfield.add(list.get(list.size()-1));
					if(RPGGame.multiplayer==true){
						try {
							p.builtCity(spr.getXX(), spr.getYY(), "City");
						} catch (FailedException e) {
							// TODO Auto-generated catch block
						e.printStackTrace();
						}
					}
					playfield.remove(spr);
					list.remove(spr);
					RPGGame.funnen=false;
					
								
				}
			};
			TButton btn4 = new TButton("Mine", 80, 45, 50, 30){
				public void doAction(){
					RPGGame.gameState=0;				
					list.add(new RPGSprite(own,parent.getImages("minesheet.png",3,4), spr.getXX(),spr.getYY(), 3, RPGSprite.DOWN, 1, 1, 10, 1, 1, 1, 2, "Mine",0,0,0,0,true));											
					playfield.add(list.get(list.size()-1));					
					RPGGame.funnen=false;
								
				}
			};
			
			line1 = ("Manpower: " + spr.getHP() + " Attack: " + spr.getATK()+" Defence: "+spr.getDEF());
			line2 = ("Move: "+spr.getMoveLeft()+" Food: "+spr.getFood()+" Type: "+spr.getTyp());
			if (spr.isFortified())
				line2+= " Fortified";
			textField = new TTextField(line1, 333, 5, 300, 18);
			textField2 = new TTextField(line2, 333, 22, 300, 18);
			
			btn.setToolTipText("Use this action to move selected unit");
			btn2.setToolTipText("Use this action to disband selected unit");
			btn3.setToolTipText("Use this action to build a city");
			actionBar.add(btn);
			actionBar.add(btn2);
			actionBar.add(btn3);
			actionBar.add(btn4);
		
			actionBar.add(textField);
			actionBar.add(textField2);
			actionFrame.add(actionBar);
		}
		else if (spr.getTyp() == "Infantry"){
			actionFrame = new FrameWork(parent.bsInput, parent.getWidth(), parent.getHeight());
			actionBar = new TPanel(0, parent.getHeight() - ACTION_BAR_HEIGHT, parent.getWidth(), ACTION_BAR_HEIGHT);
			actionBar.setExternalUI(parent.getImages("ActionBarImage.png", 2, 1), false);
			
			TButton btn = new TButton("Move", 5, 5, 35, 30) {
				public void doAction() {
					RPGGame.gameState=0;
					if(spr.getMov()){					
						spr.selmov=true;				      
					}
					else if(!spr.getMov()){					
						RPGGame.funnen=false;				      
					}
				     
				
				}
			};
			TButton btn2 = new TButton("Disband", 5, 45, 70, 30){
				public void doAction(){
					RPGGame.gameState=0;
					RPGGame.funnen=false;
					map.layer3[spr.getXX()][spr.getYY()]=null;
					playfield.remove(spr);
					list.remove(spr);
									
				}
			};
			TButton btn3 = new TButton("Fortify", 45, 5, 70, 30) {
				public void doAction() {
					RPGGame.gameState=0;
					spr.setFort();
					spr.movement();
					RPGGame.funnen=false;
				     
				
				}
			};
			
			line1 = ("Manpower: " + spr.getHP() + " Attack: " + spr.getATK()+" Defence: "+spr.getDEF());
			line2 = ("Move: "+spr.getMoveLeft()+" Food: "+spr.getFood()+" Type: "+spr.getTyp());
			if (spr.isFortified())
				line2+= " Fortified";
			textField = new TTextField(line1, 333, 5, 300, 18);
			textField2 = new TTextField(line2, 333, 22, 300, 18);
			
			//btn.setExternalUI(parent.getImages("Button.png", 2, 1, "0100", 1), false);
			btn.setToolTipText("Use this action to move selected unit");
			btn2.setToolTipText("Use this action to disband selected unit");
			btn3.setToolTipText("Use this action to fortify selected unit");
			actionBar.add(btn);
			actionBar.add(btn3);
			actionBar.add(btn2);
			
			actionBar.add(textField);
			actionBar.add(textField2);
			actionFrame.add(actionBar);
		}
		else if (spr.getTyp() == "Cannon"){
			actionFrame = new FrameWork(parent.bsInput, parent.getWidth(), parent.getHeight());
			actionBar = new TPanel(0, parent.getHeight() - ACTION_BAR_HEIGHT, parent.getWidth(), ACTION_BAR_HEIGHT);
			actionBar.setExternalUI(parent.getImages("ActionBarImage.png", 2, 1), false);
		
			TButton btn = new TButton("Move", 5, 5, 35, 30) {
				public void doAction() {
					RPGGame.gameState=0;
					if(spr.getMov()){					
						spr.selmov=true;				      
					}
					else if(!spr.getMov()){					
						RPGGame.funnen=false;				      
					}
				}
			};
			TButton btn2 = new TButton("Disband", 5, 45, 70, 30){
				public void doAction(){
					RPGGame.gameState=0;
					RPGGame.funnen=false;
					map.layer3[spr.getXX()][spr.getYY()]=null;
					playfield.remove(spr);
					list.remove(spr);
									
				}
			};
			TButton btn3 = new TButton("Fortify", 45, 5, 70, 30) {
				public void doAction() {
					RPGGame.gameState=0;
					spr.setFort();
					spr.movement();
					RPGGame.funnen=false;
				
				}
			};
			
			line1 = ("Manpower: " + spr.getHP() + " Attack: " + spr.getATK()+" Defence: "+spr.getDEF());
			line2 = ("Move: "+spr.getMoveLeft()+" Food: "+spr.getFood()+" Type: "+spr.getTyp());
			line3 = ("Iron: "+spr.getIron()+" Gunpowder: "+spr.getGunP());
			if (spr.isFortified())
				line2+= " Fortified";
			textField = new TTextField(line1, 333, 5, 300, 18);
			textField2 = new TTextField(line2, 333, 22, 300, 18);
			textField3 = new TTextField(line3, 333, 39, 300, 18);
			//btn.setExternalUI(parent.getImages("Button.png", 2, 1, "0100", 1), false);
			btn.setToolTipText("Use this action to move selected unit");
			btn2.setToolTipText("Use this action to disband selected unit");
			btn3.setToolTipText("Use this action to fortify selected unit");
			actionBar.add(btn);
			actionBar.add(btn3);
			actionBar.add(btn2);
			
			actionBar.add(textField);
			actionBar.add(textField2);
			actionBar.add(textField3);
			actionFrame.add(actionBar);
			
		}else if (spr.getTyp()=="City"){		
			sprC=spr;
			actionFrame = new FrameWork(parent.bsInput, parent.getWidth(), parent.getHeight());
			actionBar = new TPanel(0, parent.getHeight() - ACTION_BAR_HEIGHT, parent.getWidth(), ACTION_BAR_HEIGHT);
			actionBar.setExternalUI(parent.getImages("ActionBarImage.png", 2, 1), false);
		
			TButton btn = new TButton("Diplomat", 5, 5, 60, 30) {
				public void doAction() {
					oldturn = RPGGame.turn;
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					diplo=true;
					
				}
			};
			TButton btn2 = new TButton("Crusader", 5, 45, 60, 30) {
				public void doAction() {
					oldturn = RPGGame.turn;
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					crus=true;
					//list.add(new RPGSprite(own, parent.getImages("CrusaderSheet.png",3,4), spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 18,12, 100, 1, 1, 2, 2, "Crusader",1000,true));
					//playfield.add(list.get(list.size()-1));
					
				}
			};
			
			TButton btn3 = new TButton("Destroy", 70, 5, 60, 30) {
				public void doAction() {
					RPGGame.gameState=0;			      
					RPGGame.funnen=false;	
					map.layer3[spr.getXX()][spr.getYY()]=null;
					playfield.remove(spr);
					list.remove(spr);
				}
			};
			TButton btn4 = new TButton("Infantry", 70, 45, 60, 30) {
				public void doAction() {
					oldturn = RPGGame.turn;
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					infa=true;
					//list.add(new RPGSprite(own, parent.getImages("InfantrySheet.png",3,4), spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 3,3, 100, 1, 1, 1, 2, "Infantry",1000,true));
					//playfield.add(list.get(list.size()-1));
					
				}
			};
			TButton btn5 = new TButton("Archer", 135, 45, 60, 30) {
				public void doAction() {
					oldturn = RPGGame.turn;
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					arch=true;
					//addSpr();
					//list.add(new RPGSprite(own, parent.getImages("ArcherSheet.png",3,4), spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 4,2, 100, 1, 2, 1, 2, "Archer",1000,true));
					//playfield.add(list.get(list.size()-1));
					
				}
			};
			TButton btn6 = new TButton("Musketeer", 200, 45, 60, 30) {
				public void doAction() {
					oldturn = RPGGame.turn;
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					muske=true;
					//list.add(new RPGSprite(own, parent.getImages("MusketeerSheet.png",3,4), spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 8,6, 100, 1, 2, 1, 2, "Musketeer",1000,true));
					//playfield.add(list.get(list.size()-1));
					
				}
			};
			TButton btn7 = new TButton("Legion", 135, 5, 60, 30) {
				public void doAction() {
					oldturn = RPGGame.turn;
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					legi=true;
					//list.add(new RPGSprite(own, parent.getImages("LegionSheet.png",3,4), spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 6,4, 100, 1, 1, 1, 2, "Legion",1000,true));
					//playfield.add(list.get(list.size()-1));
					
				}
			};
			TButton btn8 = new TButton("Pikeman", 200, 5, 60, 30) {
				public void doAction() {
					oldturn = RPGGame.turn;
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					pike=true;
					//list.add(new RPGSprite(own, parent.getImages("PikemanSheet.png",3,4), spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 2,3, 100, 1, 1, 1, 2, "Pikeman",1000,true));
					//playfield.add(list.get(list.size()-1));
					
				}
			};
			TButton btn9 = new TButton("Cavalry", 265, 45, 60, 30) {
				public void doAction() {
					oldturn = RPGGame.turn;
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					cava=true;
					//list.add(new RPGSprite(own, parent.getImages("CavalrySheet.png",3,4),spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 6,4, 100, 1, 1, 2, 2, "Cavalry",1000,true));
					//playfield.add(list.get(list.size()-1));
					
				}
			};
			TButton btn10 = new TButton("Knight", 330, 45, 60, 30) {
				public void doAction() {
					oldturn = RPGGame.turn;
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					knig=true;
					//list.add(new RPGSprite(own, parent.getImages("KnightSheet.png",3,4), spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 12,8, 100, 1, 1, 2, 2, "Knight",1000,true));
					//playfield.add(list.get(list.size()-1));
					
				}
			};
			TButton btn11 = new TButton("Catapult", 395, 45, 60, 30) {
				public void doAction() {
					oldturn = RPGGame.turn;
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					cata=true;
					//list.add(new RPGSprite(own, parent.getImages("CatapultSheet.png",3,4), spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 12,3, 100, 1, 2, 1, 2, "Catapult",1000, 50,0,0,true));
					//playfield.add(list.get(list.size()-1));
					
				}
			};
			TButton btn12 = new TButton("Trebuchet", 460, 45, 60, 30) {
				public void doAction() {
					oldturn = RPGGame.turn;
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					treb=true;
					//list.add(new RPGSprite(own, parent.getImages("TrebuchetSheet.png",3,4),spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 20,2, 100, 1, 3, 1, 2, "Trebuchet",1000,75,0,0,true));
					//playfield.add(list.get(list.size()-1));
					
				}
			};
			TButton btn13 = new TButton("Cannon", 525, 45, 60, 30) {
				public void doAction() {
					RPGGame.funnen=false;
					RPGGame.gameState=0;		
					oldturn = RPGGame.turn;
					cann=true;
					//list.add(new RPGSprite(own, parent.getImages("CannonSheet.png",3,4), spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 30,3, 100, 1, 4, 1, 2, "Cannon",1000,0,100,100,true));
					//playfield.add(list.get(list.size()-1));
					
				}
			};
			TButton btn17 = new TButton("Phalanx", 590, 45, 60, 30) {
				public void doAction() {
					oldturn = RPGGame.turn;
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					phal=true;
					//list.add(new RPGSprite(own, parent.getImages("CannonSheet.png",3,4), spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 30,3, 100, 1, 4, 1, 2, "Cannon",1000,0,100,100,true));
					//playfield.add(list.get(list.size()-1));
					
				}
			};
			TButton btn14 = new TButton("Trireme", 265, 5, 60, 30) {
				public void doAction() {
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.getXX() % 2 == 0){
						even=true;
					}else even=false;
					if(!even){
						if (!map.boatisOccupied(spr.getXX()+1, spr.getYY(), list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY(), list, spr) || !map.boatisOccupied(spr.getXX(), spr.getYY()+1, list, spr) || !map.boatisOccupied(spr.getXX(), spr.getYY()-1, list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY()-1, list, spr) || !map.boatisOccupied(spr.getXX()+1, spr.getYY()-1, list, spr)){
							oldturn = RPGGame.turn;
							trir=true;
							//list.add(new RPGSprite(own, parent.getImages("TriremeSheet.png",3,4), spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 4,3, 50, 1, 1, 3, 2, "Trireme",1000, true));
							//playfield.add(list.get(list.size()-1));
						}
					}
					if(even){
						if (!map.boatisOccupied(spr.getXX()+1, spr.getYY()+1, list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY()+1, list, spr) || !map.boatisOccupied(spr.getXX(), spr.getYY()-1, list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY(), list, spr)|| !map.boatisOccupied(spr.getXX()+1, spr.getYY(), list, spr)|| !map.boatisOccupied(spr.getXX(), spr.getYY()+1, list, spr)){
							oldturn = RPGGame.turn;
							trir=true;
							//list.add(new RPGSprite(own, parent.getImages("TriremeSheet.png",3,4), spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 4,3, 50, 1, 1, 3, 2, "Trireme",1000, true));
							//playfield.add(list.get(list.size()-1));	
						}
						
					}
					
				}
			};
			TButton btn15 = new TButton("Galley", 330, 5, 60, 30) {
				public void doAction() {
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.getXX() % 2 == 0){
						even=true;
					}else even=false;
					if(!even){
						if (!map.boatisOccupied(spr.getXX()+1, spr.getYY(), list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY(), list, spr) || !map.boatisOccupied(spr.getXX(), spr.getYY()+1, list, spr) || !map.boatisOccupied(spr.getXX(), spr.getYY()-1, list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY()-1, list, spr) || !map.boatisOccupied(spr.getXX()+1, spr.getYY()-1, list, spr)){
							oldturn = RPGGame.turn;
							gall=true;
							//list.add(new RPGSprite(own, parent.getImages("GalleySheet.png",3,4), spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 30,25, 250, 1, 2, 4, 2, "Galley",1000, true));
							//playfield.add(list.get(list.size()-1));
						}
					}
					if(even){
						if (!map.boatisOccupied(spr.getXX()+1, spr.getYY()+1, list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY()+1, list, spr) || !map.boatisOccupied(spr.getXX(), spr.getYY()-1, list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY(), list, spr)|| !map.boatisOccupied(spr.getXX()+1, spr.getYY(), list, spr)|| !map.boatisOccupied(spr.getXX(), spr.getYY()+1, list, spr)){
							oldturn = RPGGame.turn;
							gall=true;
							//list.add(new RPGSprite(own, parent.getImages("GalleySheet.png",3,4), spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 30,25, 250, 1, 2, 4, 2, "Galley",1000, true));
							//playfield.add(list.get(list.size()-1));	
						}
						
					}
					
				}
			};
			TButton btn16 = new TButton("Caravel", 395, 5, 60, 30) {
				public void doAction() {
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.getXX() % 2 == 0){
						even=true;
					}else even=false;
					if(!even){
						if (!map.boatisOccupied(spr.getXX()+1, spr.getYY(), list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY(), list, spr) || !map.boatisOccupied(spr.getXX(), spr.getYY()+1, list, spr) || !map.boatisOccupied(spr.getXX(), spr.getYY()-1, list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY()-1, list, spr) || !map.boatisOccupied(spr.getXX()+1, spr.getYY()-1, list, spr)){
							oldturn = RPGGame.turn;
							carav=true;
							//list.add(new RPGSprite(own, parent.getImages("CaravelSheet.png",3,4), spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 50,40, 100, 1, 3, 6, 2, "Caravel",1000, true));
							//playfield.add(list.get(list.size()-1));
						}
					}
					if(even){
						if (!map.boatisOccupied(spr.getXX()+1, spr.getYY()+1, list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY()+1, list, spr) || !map.boatisOccupied(spr.getXX(), spr.getYY()-1, list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY(), list, spr)|| !map.boatisOccupied(spr.getXX()+1, spr.getYY(), list, spr)|| !map.boatisOccupied(spr.getXX(), spr.getYY()+1, list, spr)){
							oldturn = RPGGame.turn;
							carav=true;
							//list.add(new RPGSprite(own, parent.getImages("CaravelSheet.png",3,4), spr.getXX(), spr.getYY(), 3, RPGSprite.RIGHT, 50,40, 100, 1, 3, 6, 2, "Caravel",1000, true));
							//playfield.add(list.get(list.size()-1));	
						}
						
					}
					
				}
			};
			
			TButton btn19 = new TButton("SiegeTower", 460, 5, 70, 30) {
				public void doAction() {
					oldturn = RPGGame.turn;
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					siege=true;	
				}
			};
			TButton btn20 = new TButton("WagonTrain", 535, 5, 70, 30) {
				public void doAction() {
					oldturn = RPGGame.turn;
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					wagon=true;	
				}
			};
			line1 = ("Population: " + spr.getPOP());			
			textField = new TTextField(line1, 333, 5, 300, 18);
			
			//btn.setExternalUI(parent.getImages("Button.png", 2, 1, "0100", 1), false);		
			btn3.setToolTipText("Use this action to destroy your city");
			actionBar.add(btn);
			actionBar.add(btn2);
			actionBar.add(btn3);
			actionBar.add(btn4);
			actionBar.add(btn5);
			actionBar.add(btn6);
			actionBar.add(btn7);
			actionBar.add(btn8);
			actionBar.add(btn9);
			actionBar.add(btn10);
			actionBar.add(btn11);
			actionBar.add(btn12);
			actionBar.add(btn13);
			actionBar.add(btn14);
			actionBar.add(btn15);
			actionBar.add(btn16);
			actionBar.add(btn17);
			
			actionBar.add(btn19);
			actionBar.add(btn20);
			//actionBar.add(textField);
			actionFrame.add(actionBar);
							
		}			
		else if (spr.getTyp()!="Settler" && spr.getTyp() != "City"){
			actionFrame = new FrameWork(parent.bsInput, parent.getWidth(), parent.getHeight());
			actionBar = new TPanel(0, parent.getHeight() - ACTION_BAR_HEIGHT, parent.getWidth(), ACTION_BAR_HEIGHT);
			actionBar.setExternalUI(parent.getImages("ActionBarImage.png", 2, 1), false);
		
			TButton btn = new TButton("Move", 5, 5, 35, 30) {
				public void doAction() {
					RPGGame.gameState=0;
					if(spr.getMov()){					
						spr.selmov=true;				      
					}
					else if(!spr.getMov()){					
						RPGGame.funnen=false;				      
					}
				     
				
				}
			};
			TButton btn2 = new TButton("Disband", 5, 45, 70, 30){
				public void doAction(){
					RPGGame.gameState=0;
					RPGGame.funnen=false;
					map.layer3[spr.getXX()][spr.getYY()]=null;
					playfield.remove(spr);
					list.remove(spr);
									
				}
			};
			TButton btn3 = new TButton("Fortify", 45, 5, 70, 30) {
				public void doAction() {
					RPGGame.gameState=0;
					spr.setFort();
					spr.movement();
					RPGGame.funnen=false;
				     
				
				}
			};
			
			line1 = ("Manpower: " + spr.getHP() + " Attack: " + spr.getATK()+" Defence: "+spr.getDEF());
			line2 = ("Move: "+spr.getMoveLeft()+" Food: "+spr.getFood()+" Type: "+spr.getTyp());
			//btn.setExternalUI(parent.getImages("Button.png", 2, 1, "0100", 1), false);
			btn.setToolTipText("Use this action to move selected unit");
			btn2.setToolTipText("Use this action to disband selected unit");
			btn3.setToolTipText("Use this action to fortify selected unit");
			actionBar.add(btn);
			actionBar.add(btn3);
			actionBar.add(btn2);
			
			if(spr.getTyp()!="City"){
				if (spr.isFortified())
					line2+= " Fortified";
				textField = new TTextField(line1, 333, 5, 300, 18);
				textField2 = new TTextField(line2, 333, 22, 300, 18);
				actionBar.add(textField);
				actionBar.add(textField2);
			}			
			actionFrame.add(actionBar);
							
		}
	}		
	public void update(long elapsedTime) {
		actionFrame.update();
	}
	
	public void render(Graphics2D g) {
		actionFrame.render(g);
	}
	class Chipset {
		BufferedImage[] image;
		BufferedImage image2;
		public Chipset(BufferedImage[] image) {
			this.image = image;
		}

		public Chipset(BufferedImage image2) {
			this.image2=image2;
			// TODO Auto-generated constructor stub
		}

	}
	public static void addSpr(){
		if(RPGGame.turn - oldturn == 2 && arch){
			list.add(new RPGSprite(own, parent.getImages("ArcherSheet.png",3,4), sprC.getXX(), sprC.getYY(), 3, RPGSprite.RIGHT, 4,2, 100, 1, 2, 1, 2, "Archer",1000,true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;
			arch=false;
		}
		else if(RPGGame.turn - oldturn == 3 && phal){
			list.add(new RPGSprite(own,parent.getImages("PhalanxSheet.png",3,4), sprC.getXX(),sprC.getYY(), 3, RPGSprite.LEFT, 2, 5, 100, 1, 1, 1, 2, "Phalanx",1000, true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;
			phal=false;
		}
		else if(RPGGame.turn - oldturn == 2 && diplo){
			list.add(new RPGSprite(own,parent.getImages("PhalanxSheet.png",3,4), sprC.getXX(),sprC.getYY(), 3, RPGSprite.LEFT, 1, 0, 25, 1, 1, 3, 2, "Diplomat",25, true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;
			diplo=false;
		}
		else if(RPGGame.turn - oldturn == 3 && infa){
			list.add(new RPGSprite(own, parent.getImages("InfantrySheet.png",3,4), sprC.getXX(), sprC.getYY(), 3, RPGSprite.RIGHT, 3,3, 100, 1, 1, 1, 2, "Infantry",1000,true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;
			infa=false;
		}
		else if(RPGGame.turn - oldturn == 3 && wagon){
			list.add(new RPGSprite(own, parent.getImages("InfantrySheet.png",3,4), sprC.getXX(), sprC.getYY(), 3, RPGSprite.RIGHT, 1,0, 100, 1, 1, 2, 2, "WagonTrain",100,true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;
			wagon=false;
		}
		else if(RPGGame.turn - oldturn == 4 && cata){
			list.add(new RPGSprite(own, parent.getImages("CatapultSheet.png",3,4), sprC.getXX(), sprC.getYY(), 3, RPGSprite.RIGHT, 12,3, 100, 1, 2, 1, 2, "Catapult",1000, 50,0,0,true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;
			cata=false;
		}
		else if(RPGGame.turn - oldturn == 4 && legi){
			list.add(new RPGSprite(own, parent.getImages("LegionSheet.png",3,4), sprC.getXX(), sprC.getYY(), 3, RPGSprite.RIGHT, 6,4, 100, 1, 1, 1, 2, "Legion",1000,true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;
			legi=false;
		}
		else if(RPGGame.turn - oldturn == 4 && pike){
			list.add(new RPGSprite(own, parent.getImages("PikemanSheet.png",3,4), sprC.getXX(), sprC.getYY(), 3, RPGSprite.RIGHT, 2,3, 100, 1, 1, 1, 2, "Pikeman",1000,true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;
			pike=false;
		}
		else if(RPGGame.turn - oldturn == 5 && muske){
			list.add(new RPGSprite(own, parent.getImages("MusketeerSheet.png",3,4), sprC.getXX(), sprC.getYY(), 3, RPGSprite.RIGHT, 8,6, 100, 1, 2, 1, 2, "Musketeer",1000,true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;
			muske=false;
		}
		else if(RPGGame.turn - oldturn == 6 && siege){
			list.add(new RPGSprite(own, parent.getImages("MusketeerSheet.png",3,4), sprC.getXX(), sprC.getYY(), 3, RPGSprite.RIGHT, 1,0, 100, 1, 2, 1, 2, "SiegeTower",50,true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;
			siege=false;
		}
		else if(RPGGame.turn - oldturn == 7 && treb){
			list.add(new RPGSprite(own, parent.getImages("TrebuchetSheet.png",3,4),sprC.getXX(), sprC.getYY(), 3, RPGSprite.RIGHT, 20,2, 100, 1, 3, 1, 2, "Trebuchet",1000,75,0,0,true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;	
			treb=false;
		}
		else if(RPGGame.turn - oldturn == 8 && cava){
			list.add(new RPGSprite(own, parent.getImages("CavalrySheet.png",3,4),sprC.getXX(), sprC.getYY(), 3, RPGSprite.RIGHT, 6,4, 100, 1, 1, 2, 2, "Cavalry",1000,true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;
			cava=false;
		}
		else if(RPGGame.turn - oldturn == 10 && knig){
			list.add(new RPGSprite(own, parent.getImages("KnightSheet.png",3,4), sprC.getXX(), sprC.getYY(), 3, RPGSprite.RIGHT, 12,8, 100, 1, 1, 2, 2, "Knight",1000,true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;
			knig=false;
		}
		else if(RPGGame.turn - oldturn == 12 && crus){
			list.add(new RPGSprite(own, parent.getImages("CrusaderSheet.png",3,4), sprC.getXX(), sprC.getYY(), 3, RPGSprite.RIGHT, 18,12, 100, 1, 1, 2, 2, "Crusader",1000,true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;
			crus=false;
		}
		else if(RPGGame.turn - oldturn == 16 && trir){
			list.add(new RPGSprite(own, parent.getImages("TriremeSheet.png",3,4), sprC.getXX(), sprC.getYY(), 3, RPGSprite.RIGHT, 4,3, 50, 1, 1, 3, 2, "Trireme",1000, true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;		
			trir=false;
		}
		else if(RPGGame.turn - oldturn == 20 && cann){
			list.add(new RPGSprite(own, parent.getImages("CannonSheet.png",3,4), sprC.getXX(), sprC.getYY(), 3, RPGSprite.RIGHT, 30,3, 100, 1, 4, 1, 2, "Cannon",1000,0,100,100,true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;	
			cann=false;
		}
		else if(RPGGame.turn - oldturn == 58 && gall){
			list.add(new RPGSprite(own, parent.getImages("GalleySheet.png",3,4), sprC.getXX(), sprC.getYY(), 3, RPGSprite.RIGHT, 30,25, 250, 1, 2, 4, 2, "Galley",1000, true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;
			gall=false;
		}
		else if(RPGGame.turn - oldturn == 70 && carav){
			list.add(new RPGSprite(own, parent.getImages("CaravelSheet.png",3,4), sprC.getXX(), sprC.getYY(), 3, RPGSprite.RIGHT, 50,40, 100, 1, 3, 6, 2, "Caravel",1000, true));
			playfield.add(list.get(list.size()-1));
			oldturn = 0;
			carav=false;
		}
	}
	public static void get(Graphics2D g2) {
		g=g2;
		// TODO Auto-generated method stub
		
	}
	
}

