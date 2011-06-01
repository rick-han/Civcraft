
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
	static Map map;
	static Graphics2D g;
	static Proxy p;
	Result ret=null;
	Construction constr = null;
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
					playfield.remove(spr);
					if(RPGGame.multiplayer==true){
						try {						
							p.disbandUnit(spr.getXX(), spr.getYY());						
							list.remove(spr);
							
						} catch (FailedException e) {
							// TODO Auto-generated catch block
						e.printStackTrace();
						}
					}
					map.layer3[spr.getXX()][spr.getYY()] = null;		
					
									
				}
			};
			TButton btn3 = new TButton("City", 45, 5, 50, 30){
				public void doAction(){
					RPGGame.gameState=0;
					map.layer3[spr.getXX()][spr.getYY()] = null;
					list.add(new RPGSprite(own,parent.getImages("citytile.png",3,4), spr.getXX(),spr.getYY(), 3, RPGSprite.DOWN, 1, 1, 10, 1, 1, 1, 2, "City",1000, true, true));
					playfield.add(list.get(list.size()-1));
					list.get(list.size()-1).setPOP(1000);	
					map.layer3[spr.getXX()][spr.getYY()] = list.get(list.size()-1);
				
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
			//actionBar.add(btn4);
		
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
					playfield.remove(spr);
					if(RPGGame.multiplayer==true){
						try {
							
							p.disbandUnit(spr.getXX(), spr.getYY());						
							list.remove(spr);
							
						} catch (FailedException e) {
							// TODO Auto-generated catch block
						e.printStackTrace();
						}
					}
					map.layer3[spr.getXX()][spr.getYY()] = null;	
									
				}
			};
			TButton btn3 = new TButton("Fortify", 45, 5, 70, 30) {
				public void doAction() {
					RPGGame.gameState=0;
					spr.setFort();
					spr.movement();
					if (RPGGame.multiplayer==true){
						 try {
							if (spr.fortified)
								p.fortify(spr.getXX(), spr.getYY());
							else
								p.unFortify(spr.getXX(), spr.getYY());
						} catch (FailedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
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
					playfield.remove(spr);
					if(RPGGame.multiplayer==true){
						try {
							
							p.disbandUnit(spr.getXX(), spr.getYY());						
							list.remove(spr);
							
						} catch (FailedException e) {
							// TODO Auto-generated catch block
						e.printStackTrace();
						}
					}
					map.layer3[spr.getXX()][spr.getYY()] = null;	
									
				}
			};
			TButton btn3 = new TButton("Fortify", 45, 5, 70, 30) {
				public void doAction() {
					RPGGame.gameState=0;
					spr.setFort();
					spr.movement();
					if (RPGGame.multiplayer==true){
						 try {
							if (spr.fortified)
								p.fortify(spr.getXX(), spr.getYY());
							else
								p.unFortify(spr.getXX(), spr.getYY());
						} catch (FailedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
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
		
			TButton btn = new TButton("Destroy", 5, 5, 60, 30) {
				public void doAction() {
					RPGGame.gameState=0;
					RPGGame.funnen=false;	
					map.layer3[spr.getXX()][spr.getYY()]=null;
					playfield.remove(spr);
					list.remove(spr);
				}
			};
			TButton btn2 = new TButton("Crusader", 5, 45, 60, 30) {
				public void doAction() {
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.idle){
						constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn + 12 ,"Crusader");
						RPGGame.byggList.add(constr);
						spr.idle=false;
					}
				}
			};
			
			TButton btn3 = new TButton("Diplomat", 70, 5, 60, 30) {
				public void doAction() {
					
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.idle){
						constr = new Construction(spr, spr.getXX(),spr.getYY(), RPGGame.turn + 2 ,"Diplomat");
						RPGGame.byggList.add(constr);
						spr.idle=false;
					};
				}
			};
			TButton btn4 = new TButton("Infantry", 70, 45, 60, 30) {
				public void doAction() {
					
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.idle){
						constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn + 3 ,"Infantry");
						RPGGame.byggList.add(constr);
						spr.idle=false;
					}
				}
			};
			TButton btn5 = new TButton("Archer", 135, 45, 60, 30) {
				public void doAction() {
					
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.idle){
						constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn + 2 ,"Archer");
						RPGGame.byggList.add(constr);
						spr.idle=false;
					}
				}
			};
			TButton btn6 = new TButton("Musketeer", 200, 45, 60, 30) {
				public void doAction() {
					
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.idle){
						constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn + 5 ,"Musketeer");
						RPGGame.byggList.add(constr);
						spr.idle=false;
					}
				}
			};
			TButton btn7 = new TButton("Legion", 135, 5, 60, 30) {
				public void doAction() {
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.idle){
						constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn + 4 ,"Legion");
						RPGGame.byggList.add(constr);
						spr.idle=false;
					}
				}
			};
			TButton btn8 = new TButton("Pikeman", 200, 5, 60, 30) {
				public void doAction() {
					
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.idle){
						constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn + 4 , "Pikeman");
						RPGGame.byggList.add(constr);
						spr.idle=false;
					}
				}
			};
			TButton btn9 = new TButton("Cavalry", 265, 45, 60, 30) {
				public void doAction() {
					
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.idle){
						constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn + 8 , "Cavalry");
						RPGGame.byggList.add(constr);
						spr.idle=false;
					}
				}
			};
			TButton btn10 = new TButton("Knight", 330, 45, 60, 30) {
				public void doAction() {
					
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.idle){
						constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn+10,"Knight");
						RPGGame.byggList.add(constr);
						spr.idle=false;
					}
				}
			};
			TButton btn11 = new TButton("Catapult", 395, 45, 60, 30) {
				public void doAction() {
					
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.idle){
						constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn+4,"Cata");
						RPGGame.byggList.add(constr);
						spr.idle=false;
					}
				}
			};
			TButton btn12 = new TButton("Trebuchet", 460, 45, 60, 30) {
				public void doAction() {
					
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.idle){
						constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn+7,"Trebuchet");
						RPGGame.byggList.add(constr);
						spr.idle=false;
					}
				}
			};
			TButton btn13 = new TButton("Cannon", 525, 45, 60, 30) {
				public void doAction() {
					RPGGame.funnen=false;
					RPGGame.gameState=0;		
					
					if (spr.idle){
						constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn+20,"Cannon");
						RPGGame.byggList.add(constr);
						spr.idle=false;
					}
				}
			};
			TButton btn17 = new TButton("Phalanx", 590, 45, 60, 30) {
				public void doAction() {
					
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.idle){
						constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn+3,"Phalanx");
						RPGGame.byggList.add(constr);
						spr.idle=false;
					}
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
							if (spr.idle){
								constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn+16,"Trireme");
								RPGGame.byggList.add(constr);
								spr.idle=false;
							}
						}
					}
					if(even){
						if (!map.boatisOccupied(spr.getXX()+1, spr.getYY()+1, list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY()+1, list, spr) || !map.boatisOccupied(spr.getXX(), spr.getYY()-1, list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY(), list, spr)|| !map.boatisOccupied(spr.getXX()+1, spr.getYY(), list, spr)|| !map.boatisOccupied(spr.getXX(), spr.getYY()+1, list, spr)){
							if (spr.idle){
								constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn+16,"Trireme");
								RPGGame.byggList.add(constr);
								spr.idle=false;
							}	
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
							if (spr.idle){
								constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn+58,"Galley");
								RPGGame.byggList.add(constr);
								spr.idle=false;
							}
						}
					}
					if(even){
						if (!map.boatisOccupied(spr.getXX()+1, spr.getYY()+1, list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY()+1, list, spr) || !map.boatisOccupied(spr.getXX(), spr.getYY()-1, list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY(), list, spr)|| !map.boatisOccupied(spr.getXX()+1, spr.getYY(), list, spr)|| !map.boatisOccupied(spr.getXX(), spr.getYY()+1, list, spr)){
							if (spr.idle){
								constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn+58,"Galley");
								RPGGame.byggList.add(constr);
								spr.idle=false;
							}	
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
							if (spr.idle){
								constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn+70,"Caravel");
								RPGGame.byggList.add(constr);
								spr.idle=false;
							}
						}
					}
					if(even){
						if (!map.boatisOccupied(spr.getXX()+1, spr.getYY()+1, list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY()+1, list, spr) || !map.boatisOccupied(spr.getXX(), spr.getYY()-1, list, spr) || !map.boatisOccupied(spr.getXX()-1, spr.getYY(), list, spr)|| !map.boatisOccupied(spr.getXX()+1, spr.getYY(), list, spr)|| !map.boatisOccupied(spr.getXX(), spr.getYY()+1, list, spr)){
							if (spr.idle){
								constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn+70,"Caravel");
								RPGGame.byggList.add(constr);
								spr.idle=false;
							}
						}
						
					}
					
				}
			};
			
			TButton btn19 = new TButton("SiegeTower", 460, 5, 70, 30) {
				public void doAction() {
					
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.idle){
						constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn+6,"SiegeTower");
						RPGGame.byggList.add(constr);
						spr.idle=false;
					}	
				}
			};
			TButton btn20 = new TButton("WagonTrain", 535, 5, 70, 30) {
				public void doAction() {
					
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.idle){
						constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn+3,"WagonTrain");
						RPGGame.byggList.add(constr);
						spr.idle=false;
					}
				}
			};
			TButton btn21 = new TButton("Settler", 610, 5, 60, 30) {
				public void doAction() {
					
					RPGGame.funnen=false;
					RPGGame.gameState=0;
					if (spr.idle){
						constr = new Construction(spr,spr.getXX(),spr.getYY(), RPGGame.turn+4,"Settler");
						RPGGame.byggList.add(constr);
						spr.idle=false;
					}
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
			actionBar.add(btn21);
			actionBar.add(btn19);
			actionBar.add(btn20);
			//actionBar.add(textField);
			actionFrame.add(actionBar);
							
		}
		else if (spr.getTyp()=="Galley" || spr.getTyp()=="Caravel" || spr.getTyp()=="Trireme"){
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
					playfield.remove(spr);
					if(RPGGame.multiplayer==true){
						try {
							
							p.disbandUnit(spr.getXX(), spr.getYY());						
							list.remove(spr);
							
						} catch (FailedException e) {
							// TODO Auto-generated catch block
						e.printStackTrace();
						}
					}
					map.layer3[spr.getXX()][spr.getYY()] = null;	
									
				}
			};
			TButton btn3 = new TButton("Fortify", 45, 5, 70, 30) {
				public void doAction() {
					RPGGame.gameState=0;
					spr.setFort();
					spr.movement();
					if (RPGGame.multiplayer==true){
						 try {
							if (spr.fortified)
								p.fortify(spr.getXX(), spr.getYY());
							else
								p.unFortify(spr.getXX(), spr.getYY());
						} catch (FailedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					RPGGame.funnen=false;
				}
			};
			TButton btn4 = new TButton("Unload", 115, 5, 70, 30) {
				public void doAction() {
					RPGGame.gameState=0;
					RPGGame.funnen=false;
					for (int i =0 ; i < spr.capacity.length; i++){
						if(spr.capacity[i]!=null){
							if(RPGGame.multiplayer==false){
								spr.capacity[i].tileX = spr.tileX;
								spr.capacity[i].tileY = spr.tileY;
								spr.capacity[i].render=true;
								list.add(spr.capacity[i]);
								playfield.add(spr.capacity[i]);
								spr.capacity[i]=null;
							}
							if(RPGGame.multiplayer==true){
								try {
									if(!((String) (((ArrayList) Map.k.get(spr.tileX+1))).get(spr.tileY)).equalsIgnoreCase("Sea") 
											&& !((String) (((ArrayList) Map.k.get(spr.tileX+1))).get(spr.tileY)).equalsIgnoreCase("Ocean")){
										ret = p.moveOutUnit(spr.capacity[i].tileX, spr.capacity[i].tileY, spr.capacity[i].getTyp() , spr.capacity[i].getHP(), spr.tileX+1, spr.tileY);
										if(ret.getOk()){
											spr.capacity[i].tileY = spr.tileY;
											spr.capacity[i].tileX = spr.tileX+1;
											spr.capacity[i].render=true;
											list.add(spr.capacity[i]);
											playfield.add(spr.capacity[i]);
											spr.capacity[i]=null;
										}
									}
									else if(!((String) (((ArrayList) Map.k.get(spr.tileX-1))).get(spr.tileY)).equalsIgnoreCase("Sea") 
											&& !((String) (((ArrayList) Map.k.get(spr.tileX-1))).get(spr.tileY)).equalsIgnoreCase("Ocean")){
										ret = p.moveOutUnit(spr.capacity[i].tileX, spr.capacity[i].tileY, spr.capacity[i].getTyp() , spr.capacity[i].getHP(), spr.tileX-1, spr.tileY);
										if(ret.getOk()){
											spr.capacity[i].tileY = spr.tileY;
											spr.capacity[i].tileX = spr.tileX-1;
											spr.capacity[i].render=true;
											list.add(spr.capacity[i]);
											playfield.add(spr.capacity[i]);
											spr.capacity[i]=null;
										}
									}
									else if(!((String) (((ArrayList) Map.k.get(spr.tileX))).get(spr.tileY+1)).equalsIgnoreCase("Sea") 
											&& !((String) (((ArrayList) Map.k.get(spr.tileX))).get(spr.tileY+1)).equalsIgnoreCase("Ocean")){
										ret = p.moveOutUnit(spr.capacity[i].tileX, spr.capacity[i].tileY, spr.capacity[i].getTyp() , spr.capacity[i].getHP(), spr.tileX, spr.tileY+1);
										if(ret.getOk()){
											spr.capacity[i].tileX = spr.tileX;
											spr.capacity[i].tileY = spr.tileY+1;
											spr.capacity[i].render=true;
											list.add(spr.capacity[i]);
											playfield.add(spr.capacity[i]);
											spr.capacity[i]=null;
										}
									}
									else if(!((String) (((ArrayList) Map.k.get(spr.tileX))).get(spr.tileY-1)).equalsIgnoreCase("Sea") 
											&& !((String) (((ArrayList) Map.k.get(spr.tileX))).get(spr.tileY-1)).equalsIgnoreCase("Ocean")){
										ret = p.moveOutUnit(spr.capacity[i].tileX, spr.capacity[i].tileY, spr.capacity[i].getTyp() , spr.capacity[i].getHP(), spr.tileX, spr.tileY-1);
										if(ret.getOk()){
											spr.capacity[i].tileX = spr.tileX;
											spr.capacity[i].tileY = spr.tileY-1;
											spr.capacity[i].render=true;
											list.add(spr.capacity[i]);
											playfield.add(spr.capacity[i]);
											spr.capacity[i]=null;
										}
									}
									} catch (FailedException e) {
									// TODO Auto-generated catch block
								e.printStackTrace();
								}
							}
							
							
						}
					}
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
			actionBar.add(btn4);
			
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
		
		else if (spr.getTyp()!="Settler" && spr.getTyp() != "City" && !(spr.getTyp()=="Galley" || spr.getTyp()=="Caravel" || spr.getTyp()=="Trireme")){
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
					playfield.remove(spr);
					if(RPGGame.multiplayer==true){
						try {
							
							p.disbandUnit(spr.getXX(), spr.getYY());
							list.remove(spr);
							
						} catch (FailedException e) {
							// TODO Auto-generated catch block
						e.printStackTrace();
						}
					}
					map.layer3[spr.getXX()][spr.getYY()] = null;	
									
				}
			};
			TButton btn3 = new TButton("Fortify", 45, 5, 70, 30) {
				public void doAction() {
					RPGGame.gameState=0;
					spr.setFort();
					spr.movement();
					if (RPGGame.multiplayer==true){
						 try {
							if (spr.fortified)
								p.fortify(spr.getXX(), spr.getYY());
							else
								p.unFortify(spr.getXX(), spr.getYY());
						} catch (FailedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
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

	public static void get(Graphics2D g2) {
		g=g2;
		// TODO Auto-generated method stub
		
	}
	
}

