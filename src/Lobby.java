import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import com.golden.gamedev.GameObject;
import com.golden.gamedev.GameEngine;
import com.golden.gamedev.gui.TButton;
import com.golden.gamedev.gui.TLabel;
import com.golden.gamedev.gui.TTextField;
import com.golden.gamedev.gui.toolkit.FrameWork;
import com.golden.gamedev.object.GameFont;


public class Lobby extends GameObject{
	BufferedImage background;
	private FrameWork frame;
	Result returned;
	GameEngine parent;
	Proxy p;
	String tmp;
	TTextField toJoinText;
	TLabel listItemsLabel;
	TLabel hostItemsLabel;
	TButton startButton;
	public Lobby(GameEngine parent) {
		super(parent);
		this.parent = parent;
	}

	public void initResources() {
		p = ((Civcraft)parent).getProxy();
		frame = new FrameWork(bsInput, getWidth(), getHeight());
		background = getImage("Title.png", false);
		GameFont externalFont = fontManager.getFont(new Font("Arial", Font.BOLD, 16));
		TLabel msgLabel = new TLabel("Lobby", 150, 10, 200, 25);
		try {
			returned = p.listGames();
		} catch (FailedException e) {
			e.printStackTrace();
		}
		Iterator iter = returned.getSessions().iterator();
		listItemsLabel = new TLabel("", 70, 30, 170, 220);
		
		while (iter.hasNext()){
			listItemsLabel.setText(listItemsLabel.getText() + (String)iter.next());
		}
		
		toJoinText = new TTextField("", 70, 410, 70 ,30);
		frame.add(toJoinText);
		
		TButton joinButton =new TButton("Join", 140, 410, 60, 30) {
			public void doAction() {
				joinGame();
			}
		};
		hostItemsLabel = new TLabel("", 260, 30, 100, 150);
		TButton hostButton = new TButton("Host", 260, 190, 70, 30){
			public void doAction() {
				hostGame();
			}
		};
		TButton startButton = new TButton("Start Game", 350, 190, 70, 30){
			public void doAction() {
				startGame();
			}
		};
		
		frame.add(startButton);
		frame.add(hostButton);
		frame.add(joinButton);
		hostItemsLabel.UIResource().put("Text Font", externalFont);
		hostItemsLabel.UIResource().put("Text Color", Color.RED);
		listItemsLabel.UIResource().put("Text Font", externalFont);
		listItemsLabel.UIResource().put("Text Color", Color.RED);
		toJoinText.UIResource().put("Text Font", externalFont);
		msgLabel.UIResource().put("Text Font", externalFont);
		msgLabel.UIResource().put("Text Color", Color.RED);
		frame.add(hostItemsLabel);
		frame.add(msgLabel);
		frame.add(listItemsLabel);
	}
	public void startGame(){
		try {
			p.startGame();
		} catch (FailedException e) {
			e.printStackTrace();
		}
	}
	public void hostGame(){	
		try {
			//returned = p.host();
			p.host();
		} catch (FailedException e) {
			e.printStackTrace();
		}
		
	}
	public void joinGame(){
		try {
			p.joinGame(toJoinText.getText());
		} catch (FailedException e) {
			e.printStackTrace();
		}
	}
	public void update(long elapsedTime) {
		frame.update();
	}
	public void render(Graphics2D g) {
		g.drawImage(background, 0, 0, null);
		frame.render(g);
	}
}


