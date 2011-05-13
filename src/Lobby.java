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
		//System.out.println(returned.getSessions());
		Iterator iter = returned.getSessions().iterator();
		int counter = 1;
		
		while (iter.hasNext()){
			counter++;
			tmp = (String)iter.next();
			frame.add(new TLabel(tmp, 150, 30 * counter, 200, 30));
			frame.get().UIResource().put("Text Font", externalFont);
			frame.get().UIResource().put("Text Color", Color.RED);
			frame.add(new TButton("J", 350, 30 * counter, 25, 25) {
				public void doAction() {
					try {
						p.joinGame(Lobby.this.tmp);
						
					} catch (FailedException e) {
						e.printStackTrace();
					}
					
				}
			});
			
		}
		
		msgLabel.UIResource().put("Text Font", externalFont);
		msgLabel.UIResource().put("Text Color", Color.RED);
		
		frame.add(msgLabel);
		
		
	}

	public void update(long elapsedTime) {
		frame.update();
	}
	public void render(Graphics2D g) {
		g.drawImage(background, 0, 0, null);
		frame.render(g);
	}
}

