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

public class MultiplayerMenu extends GameObject{
	
	BufferedImage background;
	private FrameWork frame;
	TTextField hostText;
	TTextField portText;
	TTextField nickText;
	Result returned;
	Civcraft parent;
	public MultiplayerMenu(GameEngine parent) {
		super(parent);
		this.parent = (Civcraft)parent;
	}	

	public void initResources() {
		frame = new FrameWork(bsInput, getWidth(), getHeight());
		background = getImage("Title.png", false);
		TLabel msgLabel = new TLabel("", 150, 20, 200, 40);
		
		TLabel nickLabel = new TLabel("Nickname:", 150, 50, 200, 40);
		nickText = new TTextField("abc", 150, 80, 150, 25);
		
		TLabel hostLabel = new TLabel("Host:", 150, 110, 200, 40);
		hostText = new TTextField("192.168.1.3", 150, 150, 150, 25);
		
		TLabel portLabel = new TLabel("Port:", 150, 180, 200, 40);
		portText = new TTextField("1339", 150, 210, 80, 25);
		
		TButton connectButton = new TButton("Connect", 150, 270, 90, 30){
			public void doAction() {
				RPGGame.nick = nickText.getText();		
				parent.setProxy(new Proxy(hostText.getText(), Integer.valueOf(portText.getText()), new MyPackLyss()));
				try {
					returned = parent.getProxy().connect(nickText.getText());
				} catch (FailedException e) {
					e.printStackTrace();
				}
				if (returned.getOk()){
					parent.nextGameID = Civcraft.LOBBY;
					finish();
				}else{
					
				}
			}
		};
		
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
		GameFont externalFont = fontManager.getFont(new Font("Arial", Font.BOLD, 16));
		hostLabel.UIResource().put("Text Font", externalFont);
		hostLabel.UIResource().put("Text Color", Color.RED);
		hostText.UIResource().put("Text Font", externalFont);
		portLabel.UIResource().put("Text Font", externalFont);
		portLabel.UIResource().put("Text Color", Color.RED);
		portText.UIResource().put("Text Font", externalFont);
		nickLabel.UIResource().put("Text Font", externalFont);
		nickLabel.UIResource().put("Text Color", Color.RED);
		nickText.UIResource().put("Text Font", externalFont);
		msgLabel.UIResource().put("Text Font", externalFont);
		msgLabel.UIResource().put("Text Color", Color.RED);
		frame.add(hostLabel);
		frame.add(hostText);
		frame.add(portLabel);
		frame.add(portText);
		frame.add(nickLabel);
		frame.add(nickText);
		frame.add(connectButton);
		frame.add(hostButton);
		
	}

	public void update(long elapsedTime) {
		frame.update();
	}
	public void render(Graphics2D g) {
		g.drawImage(background, 0, 0, null);
		frame.render(g);
	}

}