
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
	private GameEngine parent = null;
	static ArrayList<RPGSprite> list = new ArrayList<RPGSprite>();
	static PlayField2 playfield;
	static RPGSprite[][] layer3;
	static boolean selmova=false;
	static RPGSprite spr;
	static RPGGame own;
	String line1 = null, line2 = null, line3 = null;
	BaseLoader bsLoader;
	Chipset csFow;
	static Map map;
	static Graphics2D g;
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
		
		if (spr.getTyp() == "Settler"){
			actionFrame = new FrameWork(parent.bsInput, parent.getWidth(), parent.getHeight());
			actionBar = new TPanel(0, parent.getHeight() - ACTION_BAR_HEIGHT, parent.getWidth(), ACTION_BAR_HEIGHT);
			actionBar.setExternalUI(parent.getImages("ActionBarImage.png", 2, 1), false);
			
			TButton btn = new TButton("Move", 5, 5, 35, 35) {
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
			TButton btn2 = new TButton("Disband", 5, 45, 70, 35){
				public void doAction(){
					RPGGame.gameState=0;
					RPGGame.funnen=false;
					map.layer3[spr.getXX()][spr.getYY()] = null;		
					playfield.remove(spr);
					list.remove(spr);
									
				}
			};
			TButton btn3 = new TButton("City", 45, 5, 50, 35){
				public void doAction(){
					RPGGame.gameState=0;
					map.layer3[spr.getXX()][spr.getYY()] = null;
					list.add(new RPGSprite(own,parent.getImages("citytile.png",3,4), spr.getXX(),spr.getYY(), 3, RPGSprite.DOWN, 1, 1, 10, 1, 1, 1, 2, "City",1000, true));											
					playfield.remove(spr);
					playfield.add(list.get(list.size()-1));
					list.get(list.size()-1).setPOP(1000);					
					list.remove(spr);						
					list.add(new RPGSprite(own,parent.getImages("phalan.png",3,4), spr.getXX(),spr.getYY()+1, 3, RPGSprite.LEFT, 2, 5, 100, 1, 1, 1, 2, "Phalanx",1000, true));
					playfield.add(list.get(list.size()-1));
					RPGGame.funnen=false;
								
				}
			};
			TButton btn4 = new TButton("Mine", 80, 45, 50, 35){
				public void doAction(){
					RPGGame.gameState=0;				
					list.add(new RPGSprite(own,parent.getImages("minesheet.png",3,4), spr.getXX(),spr.getYY(), 3, RPGSprite.DOWN, 1, 1, 10, 1, 1, 1, 2, "Mine",0,0,0,0,true));											
					playfield.add(list.get(list.size()-1));					
					RPGGame.funnen=false;
								
				}
			};
			
			line1 = ("Manpower: " + spr.getHP() + " Attack: " + spr.getATK()+" Defence: "+spr.getDEF());
			line2 = ("Move: "+spr.getMoveLeft()+" Food: "+spr.getFood()+" Range: "+spr.getRange());
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
			
			TButton btn = new TButton("Move", 5, 5, 35, 35) {
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
			TButton btn2 = new TButton("Disband", 5, 45, 70, 35){
				public void doAction(){
					RPGGame.gameState=0;
					RPGGame.funnen=false;
					map.layer3[spr.getXX()][spr.getYY()]=null;
					playfield.remove(spr);
					list.remove(spr);
									
				}
			};
			TButton btn3 = new TButton("Fortify", 45, 5, 70, 35) {
				public void doAction() {
					RPGGame.gameState=0;
					spr.setFort();
					RPGGame.funnen=false;
				     
				
				}
			};
			
			line1 = ("Manpower: " + spr.getHP() + " Attack: " + spr.getATK()+" Defence: "+spr.getDEF());
			line2 = ("Move: "+spr.getMoveLeft()+" Food: "+spr.getFood()+" Range: "+spr.getRange());
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
		
			TButton btn = new TButton("Move", 5, 5, 35, 35) {
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
			TButton btn2 = new TButton("Disband", 5, 45, 70, 35){
				public void doAction(){
					RPGGame.gameState=0;
					RPGGame.funnen=false;
					map.layer3[spr.getXX()][spr.getYY()]=null;
					playfield.remove(spr);
					list.remove(spr);
									
				}
			};
			TButton btn3 = new TButton("Fortify", 45, 5, 70, 35) {
				public void doAction() {
					RPGGame.gameState=0;
					spr.setFort();
					RPGGame.funnen=false;
				
				}
			};
			
			line1 = ("Manpower: " + spr.getHP() + " Attack: " + spr.getATK()+" Defence: "+spr.getDEF());
			line2 = ("Move: "+spr.getMoveLeft()+" Food: "+spr.getFood()+" Range: "+spr.getRange());
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
			actionFrame = new FrameWork(parent.bsInput, parent.getWidth(), parent.getHeight());
			actionBar = new TPanel(0, parent.getHeight() - ACTION_BAR_HEIGHT, parent.getWidth(), ACTION_BAR_HEIGHT);
			actionBar.setExternalUI(parent.getImages("ActionBarImage.png", 2, 1), false);
		
			TButton btn = new TButton("Build?", 5, 5, 45, 35) {
				public void doAction() {
					RPGGame.gameState=0;							
				}
			};
			
			TButton btn3 = new TButton("Destroy city", 60, 5, 100, 35) {
				public void doAction() {
					RPGGame.gameState=0;			      
					RPGGame.funnen=false;	
					map.layer3[spr.getXX()][spr.getYY()]=null;
					playfield.remove(spr);
					list.remove(spr);
				}
			};
			
			line1 = ("Population: " + spr.getPOP());			
			textField = new TTextField(line1, 333, 5, 300, 18);
			
			//btn.setExternalUI(parent.getImages("Button.png", 2, 1, "0100", 1), false);
			btn.setToolTipText("Use this action to build something");		
			btn3.setToolTipText("Use this action to destroy your city");
			actionBar.add(btn);
			actionBar.add(btn3);					
			actionBar.add(textField);
			actionFrame.add(actionBar);
							
		}			
		else if (spr.getTyp()!="Settler" && spr.getTyp() != "City"){
			actionFrame = new FrameWork(parent.bsInput, parent.getWidth(), parent.getHeight());
			actionBar = new TPanel(0, parent.getHeight() - ACTION_BAR_HEIGHT, parent.getWidth(), ACTION_BAR_HEIGHT);
			actionBar.setExternalUI(parent.getImages("ActionBarImage.png", 2, 1), false);
		
			TButton btn = new TButton("Move", 5, 5, 35, 35) {
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
			TButton btn2 = new TButton("Disband", 5, 45, 70, 35){
				public void doAction(){
					RPGGame.gameState=0;
					RPGGame.funnen=false;
					map.layer3[spr.getXX()][spr.getYY()]=null;
					playfield.remove(spr);
					list.remove(spr);
									
				}
			};
			TButton btn3 = new TButton("Fortify", 45, 5, 70, 35) {
				public void doAction() {
					RPGGame.gameState=0;
					spr.setFort();
					RPGGame.funnen=false;
				     
				
				}
			};
			
			line1 = ("Manpower: " + spr.getHP() + " Attack: " + spr.getATK()+" Defence: "+spr.getDEF());
			line2 = ("Move: "+spr.getMoveLeft()+" Food: "+spr.getFood()+" Range: "+spr.getRange());
			
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

