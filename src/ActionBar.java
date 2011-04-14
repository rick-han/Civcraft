
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.golden.gamedev.Game;
import com.golden.gamedev.GameEngine;
import com.golden.gamedev.GameObject;
import com.golden.gamedev.engine.BaseInput;
import com.golden.gamedev.engine.BaseLoader;
import com.golden.gamedev.gui.TButton;
import com.golden.gamedev.gui.TPanel;
import com.golden.gamedev.gui.toolkit.FrameWork;
import com.golden.gamedev.object.Sprite;


public class ActionBar{
	private FrameWork actionFrame = null;
	private TPanel actionBar = null;
	private final int ACTION_BAR_HEIGHT = 85;
	private GameEngine parent = null;
	static ArrayList<RPGSprite> list = new ArrayList<RPGSprite>();
	static PlayField2 playfield;
	static RPGSprite[][] layer3;
	static boolean selmova=false;
	static RPGSprite spr;
	static RPGGame own;
	Map map;
	public ActionBar(GameEngine parent) {
		this.parent = parent;
	}
	public static void send(RPGSprite h,PlayField2 playfieldd, RPGSprite[][] layer, ArrayList<RPGSprite> listd, RPGGame owna){
		spr = h;
		playfield=playfieldd;
		list=listd;
		layer3=layer;
		own=owna;
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
				      spr.selmov=true;				      
				
				}
			};
			TButton btn2 = new TButton("Disband", 5, 45, 70, 35){
				public void doAction(){
					RPGGame.gameState=0;
					RPGGame.funnen=false;
					layer3[spr.getXX()][spr.getYY()]=null;
					playfield.remove(spr);
					list.remove(spr);
									
				}
			};
			TButton btn3 = new TButton("City", 45, 5, 50, 35){
				public void doAction(){
					RPGGame.gameState=0;
					list.add(new RPGSprite(own,parent.getImages("CharaC.png",3,4), spr.getXX(),spr.getYY(), 3, RPGSprite.DOWN, 1, 1, 10, 1, 1, 1, 2, "City"));						
					layer3[spr.getXX()][spr.getYY()] = null;
					playfield.remove(spr);
					playfield.add(list.get(list.size()-1));
					list.get(list.size()-1).setMov();
					list.remove(spr);						
					list.add(new RPGSprite(own,parent.getImages("Chara1.png",3,4), spr.getXX(),spr.getYY(), 3, RPGSprite.LEFT, 1, 2, 100, 1, 1, 1, 2, "Pikeman"));
					playfield.add(list.get(list.size()-1));
					RPGGame.funnen=false;
								
				}
			};
			btn.setToolTipText("Use this action to move selected unit");
			btn2.setToolTipText("Use this action to disband selected unit");
			btn3.setToolTipText("Use this action to build a city");
			actionBar.add(btn);
			actionBar.add(btn2);
			actionBar.add(btn3);
			actionFrame.add(actionBar);
		}
		if (spr.getTyp() == "Infantry"){
			actionFrame = new FrameWork(parent.bsInput, parent.getWidth(), parent.getHeight());
			actionBar = new TPanel(0, parent.getHeight() - ACTION_BAR_HEIGHT, parent.getWidth(), ACTION_BAR_HEIGHT);
			actionBar.setExternalUI(parent.getImages("ActionBarImage.png", 2, 1), false);
		
			TButton btn = new TButton("Move", 5, 5, 35, 35) {
				public void doAction() {
					RPGGame.gameState=0;
				      spr.selmov=true;
				     
				
				}
			};
			TButton btn2 = new TButton("Disband", 5, 45, 70, 35){
				public void doAction(){
					RPGGame.gameState=0;
					RPGGame.funnen=false;
					layer3[spr.getXX()][spr.getYY()]=null;
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
			//btn.setExternalUI(parent.getImages("Button.png", 2, 1, "0100", 1), false);
			btn.setToolTipText("Use this action to move selected unit");
			btn2.setToolTipText("Use this action to disband selected unit");
			btn3.setToolTipText("Use this action to fortify selected unit");
			actionBar.add(btn);
			actionBar.add(btn3);
			actionBar.add(btn2);
			actionFrame.add(actionBar);
		}
		if (spr.getTyp() == "Pikeman"){
			actionFrame = new FrameWork(parent.bsInput, parent.getWidth(), parent.getHeight());
			actionBar = new TPanel(0, parent.getHeight() - ACTION_BAR_HEIGHT, parent.getWidth(), ACTION_BAR_HEIGHT);
			actionBar.setExternalUI(parent.getImages("ActionBarImage.png", 2, 1), false);
		
			TButton btn = new TButton("Move", 5, 5, 35, 35) {
				public void doAction() {
					RPGGame.gameState=0;
				      spr.selmov=true;				
				}
			};
			TButton btn2 = new TButton("Disband", 5, 45, 70, 35){
				public void doAction(){
					RPGGame.gameState=0;
					RPGGame.funnen=false;
					layer3[spr.getXX()][spr.getYY()]=null;
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
			//btn.setExternalUI(parent.getImages("Button.png", 2, 1, "0100", 1), false);
			btn.setToolTipText("Use this action to move selected unit");
			btn2.setToolTipText("Use this action to disband selected unit");
			btn3.setToolTipText("Use this action to fortify selected unit");
			actionBar.add(btn);
			actionBar.add(btn3);
			actionBar.add(btn2);
			actionFrame.add(actionBar);
		}
			
	}		
	public void update(long elapsedTime) {
		actionFrame.update();
	}
	
	public void render(Graphics2D g) {
		actionFrame.render(g);
	}
	
}

