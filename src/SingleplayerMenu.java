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

public class SingleplayerMenu extends GameObject{
	
	BufferedImage background;
	private FrameWork frame;
	TTextField nickText;
	TTextField heightText;
	TTextField widthText;
	Result returned;
	Civcraft parent;
	public SingleplayerMenu(GameEngine parent) {
		super(parent);
		this.parent = (Civcraft)parent;
	}	

	public void initResources() {
		frame = new FrameWork(bsInput, getWidth(), getHeight());
		background = getImage("Title.png", false);
		TLabel msgLabel = new TLabel("", 150, 20, 200, 40);
		
		TLabel nickLabel = new TLabel("Nickname:", 150, 50, 200, 40);
		nickText = new TTextField("abc", 150, 80, 150, 25);
		
		TLabel heightLabel = new TLabel("Height: ", 150, 110, 200, 40);
		heightText = new TTextField("50", 150, 140, 150, 25);
		
		TLabel widthLabel = new TLabel("Width:", 150, 170, 200, 40);
		widthText = new TTextField("30", 150, 200, 150, 25);
		
		TButton startButton = new TButton("Start Game", 150, 270, 90, 30){
			public void doAction() {
				
				try{
					parent.nextGameID = Civcraft.GAME_MODE;
					RPGGame.nick = nickText.getText();
					RPGGame.spX = Integer.parseInt(widthText.getText());
					RPGGame.spY = Integer.parseInt(heightText.getText());
					finish();
				}
				catch(NumberFormatException e){
					System.out.println("Här ska det stå nått snyggt");
				}
					
			}
		};
		/*
		TButton hostButton = new TButton("Connect&Host&Start", 220, 270, 120, 30){
			public void doAction() {
				RPGGame.nick = nickText.getText();
				parent.setProxy(new Proxy(hostText.getText(), Integer.valueOf(portText.getText()), new MyPackLyss()));
				try {
					returned = parent.getProxy().connect(nickText.getText());
				} catch (FailedException e) {
					e.printStackTrace();
				}
				if (returned.getOk()){
						
						try {
							parent.getProxy().host();
							parent.getProxy().startGame();
						} catch (FailedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}else{
					
				}
			}
		};
		*/
		GameFont externalFont = fontManager.getFont(new Font("Arial", Font.BOLD, 16));
		nickLabel.UIResource().put("Text Font", externalFont);
		nickLabel.UIResource().put("Text Color", Color.RED);
		nickText.UIResource().put("Text Font", externalFont);
		heightLabel.UIResource().put("Text Font", externalFont);
		heightLabel.UIResource().put("Text Color", Color.RED);
		heightText.UIResource().put("Text Font", externalFont);
		widthLabel.UIResource().put("Text Font", externalFont);
		widthLabel.UIResource().put("Text Color", Color.RED);
		widthText.UIResource().put("Text Font", externalFont);
		msgLabel.UIResource().put("Text Font", externalFont);
		msgLabel.UIResource().put("Text Color", Color.RED);
		frame.add(nickLabel);
		frame.add(nickText);
		frame.add(widthLabel);
		frame.add(widthText);
		frame.add(heightLabel);
		frame.add(heightText);
		frame.add(startButton);
		//frame.add(hostButton);
	}

	public void update(long elapsedTime) {
		frame.update();
	}
	public void render(Graphics2D g) {
		g.drawImage(background, 0, 0, null);
		frame.render(g);
	}

}
