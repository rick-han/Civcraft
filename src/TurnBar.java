
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


public class TurnBar{
	private static FrameWork actionFrame = null;
	private static TPanel actionBar = null;
	private static TTextField textField = null;
	private TTextField textField2 = null, textField3 = null;
	private final static int ACTION_BAR_HEIGHT = 85;
	private static GameEngine parent = null;
	static ArrayList<RPGSprite> list = new ArrayList<RPGSprite>();
	static PlayField2 playfield;
	static RPGSprite[][] layer3;
	static boolean selmova=false, even=false;
	static RPGSprite spr;
	static RPGGame own;
	static RPGSprite sprC;
	static String line1 = "Its your turn!";
	String line2 = null, line3 = null;
	BaseLoader bsLoader;
	Chipset csFow;
	static int oldturn=0;
	static Map map;
	static Graphics2D g;
	Proxy p;
	static boolean phal=false,diplo=false, siege=false, wagon=false, infa=false, treb=false, carav=false, gall=false, trir=false, cann=false, cava=false, cata=false, legi=false, pike=false, knig=false, crus=false, arch=false, muske=false;
	public TurnBar(GameEngine parent) {
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
	
	public static void initResources(){
		
		actionFrame = new FrameWork(parent.bsInput, parent.getWidth(), parent.getHeight());
		actionBar = new TPanel(870, parent.getHeight() - ACTION_BAR_HEIGHT, parent.getWidth(), ACTION_BAR_HEIGHT);
		actionBar.setExternalUI(parent.getImages("TurnBarImage.png", 2, 1), false);
		TButton btn = new TButton("New Turn", 45, 5, 70, 30) {
			public void doAction() {
				own.newTurn();
				RPGGame.funnen=false;	
			}
		};	
		
		
		textField = new TTextField(line1, 33, 40, 95, 18);
		
		actionBar.add(btn);
		actionBar.add(textField);
		
		actionFrame.add(actionBar);
		
			
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
		else if(RPGGame.turn - oldturn == 10 && crus){
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

