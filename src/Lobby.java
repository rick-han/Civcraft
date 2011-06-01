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
	TTextField newCivText;
	TTextField heightText;
	TTextField widthText;
	TLabel listItemsLabel;
	TButton startButton;
	TButton hostButton;
	TButton joinButton;
	TButton newCivButton;
	TLabel hostItemsLabel;
	TButton listButton;
	TButton leaveButton;
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
		
		listItemsLabel = new TLabel("", 40, 50, 255, 305);
		listGames();
		toJoinText = new TTextField("", 40, 410, 150 ,30);
		frame.add(toJoinText);
		hostItemsLabel = new TLabel("", 315, 50, 300, 235);
		newCivText = new TTextField("", 315, 380, 110 ,30);
		frame.add(newCivText);
		listButton = new TButton("List", 40, 370, 80, 30) {
			public void doAction() {
				listGames();
			}
		};
		joinButton = new TButton("Join", 210, 410, 80, 30) {
			public void doAction() {
				joinGame();
			}
		};
		hostButton = new TButton("Host", 315, 300, 90, 30){
			public void doAction() {
				hostGame();
			}
		};
		leaveButton = new TButton("Leave Game", 425, 300, 140, 30){
			public void doAction(){
				leaveGame();
			}
		};
		heightText = new TTextField("26", 455, 340, 60,30);
		widthText = new TTextField("26", 535, 340, 60 ,30);
		startButton = new TButton("Start Game", 315, 340, 120, 30){
			public void doAction() {
				startGame();
			}
		};
		newCivButton = new TButton("Change Civilisation", 445, 380, 180, 30) {
			public void doAction() {
				changeCivilisation(newCivText.getText());
			}
		};
		startButton.setEnabled(false);
		leaveButton.setEnabled(false);
		newCivButton.setEnabled(false);
		frame.add(heightText);
		frame.add(widthText);
		frame.add(startButton);
		frame.add(hostButton);
		frame.add(joinButton);
		frame.add(listButton);
		frame.add(leaveButton);
		frame.add(newCivButton);
		listItemsLabel.UIResource().put("Background Color", Color.orange);
		listItemsLabel.UIResource().put("Text Font", externalFont);
		listItemsLabel.UIResource().put("Text Color", Color.RED);
		hostItemsLabel.UIResource().put("Background Color", Color.orange);
		hostItemsLabel.UIResource().put("Text Font", externalFont);
		hostItemsLabel.UIResource().put("Text Color", Color.RED);
		toJoinText.UIResource().put("Text Font", externalFont);
		msgLabel.UIResource().put("Text Font", externalFont);
		msgLabel.UIResource().put("Text Color", Color.RED);
		frame.add(msgLabel);
		frame.add(listItemsLabel);
		frame.add(hostItemsLabel);
	}
	public void changeCivilisation(String civ){
		try{
			p.changeCiv(civ);
		}
		catch(FailedException fe){
			System.out.println(fe);
		}
	}
	public void leaveGame(){
		hostItemsLabel.setText("");
		try{
			p.leaveGame();
		}catch(FailedException fe){
			System.out.println(fe);
		}
		listButton.setEnabled(true);
		leaveButton.setEnabled(false);
		hostButton.setEnabled(true);
		joinButton.setEnabled(true);
		startButton.setEnabled(false);
		newCivButton.setEnabled(false);
		listGames();
	}
	public void listGames(){
		try {
			returned = p.listGames();
		} catch (FailedException e) {
			e.printStackTrace();
		}
		Iterator iter = returned.getSessions().iterator();
		listItemsLabel.setText("");
		while (iter.hasNext()){
			listItemsLabel.setText(listItemsLabel.getText() + "\n" + (String)iter.next());
		}
	}
	public void startGame(){
		try {
			p.startGame(Integer.valueOf(widthText.getText()), Integer.valueOf(heightText.getText()));
		} catch (FailedException e) {
			e.printStackTrace();
		}
	}
	public void hostGame(){	
		try {
			returned = p.host();
			//p.host();
		} catch (FailedException e) {
			e.printStackTrace();
		}
		startButton.setEnabled(true);
		listButton.setEnabled(false);
		leaveButton.setEnabled(true);
		joinButton.setEnabled(false);
		hostButton.setEnabled(false);
		newCivButton.setEnabled(true);
		
	}
	public void joinGame(){
		listButton.setEnabled(false);
		leaveButton.setEnabled(true);
		hostButton.setEnabled(false);
		joinButton.setEnabled(false);
		newCivButton.setEnabled(true);
		try {
			p.joinGame(toJoinText.getText());
		} catch (FailedException e) {
			hostItemsLabel.setText("Game does not exist!");
			listButton.setEnabled(true);
			leaveButton.setEnabled(false);
			hostButton.setEnabled(true);
			joinButton.setEnabled(true);
			startButton.setEnabled(false);
			newCivButton.setEnabled(false);
			listGames();
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


