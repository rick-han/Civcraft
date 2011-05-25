
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
	static String line1 = "Not your turn!";
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
	public static void send(RPGGame owna){
		
		own=owna;
		
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
	
	public void render(Graphics2D ga) {
		actionFrame.render(ga);
		g=ga;
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

