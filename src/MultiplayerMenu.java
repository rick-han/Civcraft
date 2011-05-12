import inetclient.*;

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
	Proxy p;
	Result returned;
	public MultiplayerMenu(GameEngine parent) {
		super(parent);
	}	

	public void initResources() {
		frame = new FrameWork(bsInput, getWidth(), getHeight());
		background = getImage("Title.png", false);
		TLabel msgLabel = new TLabel("", 150, 20, 200, 40);
		
		TLabel nickLabel = new TLabel("Nickname:", 150, 50, 200, 40);
		nickText = new TTextField("Grupp21", 150, 80, 150, 25);
		
		TLabel hostLabel = new TLabel("Host:", 150, 110, 200, 40);
		hostText = new TTextField("dvk.fishface.se", 150, 150, 150, 25);
		
		TLabel portLabel = new TLabel("Port:", 150, 180, 200, 40);
		portText = new TTextField("1339", 150, 210, 80, 25);
		
		TButton connectButton = new TButton("Connect", 150, 270, 90, 30){
			public void doAction() {
				p = new Proxy(hostText.getText(), Integer.valueOf(portText.getText()), new MyPackLyss());
				try {
					returned = p.connect(nickText.getText());
				} catch (FailedException e) {
					e.printStackTrace();
				}
				if (returned.getOk()){
					try {
						//p.host();
						System.out.println(returned = p.listGames());
						//System.out.println(returned.getSessions());
						//returned = p.host();
						//System.out.println(returned.getHostName());
						
					} catch (FailedException e) {
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
		
	}

	public void update(long elapsedTime) {
		frame.update();
	}
	public void render(Graphics2D g) {
		g.drawImage(background, 0, 0, null);
		frame.render(g);
	}
private class MyPackLyss implements PacketListener{
		
		MyPackLyss(){
		}

		public void newTurn(Result res){
			
		}

		public void lobbyUpdated(Result res){
			
		}

		public void gameStarted(Result res){
			
		}

		public void chatMessageReceived(Result res){
		}

        public void gameClosed(){
            
        }
	}
}
