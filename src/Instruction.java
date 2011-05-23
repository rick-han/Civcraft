import java.util.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import com.golden.gamedev.GameObject;
import com.golden.gamedev.GameEngine;
import com.golden.gamedev.gui.TButton;
import com.golden.gamedev.gui.TLabel;
import com.golden.gamedev.gui.TTextField;
import com.golden.gamedev.gui.toolkit.FrameWork;
import com.golden.gamedev.object.GameFont;


public class Instruction extends GameObject{
	BufferedImage background;
	private FrameWork frame;
	Result returned;
	GameEngine parent;
	Proxy p;
	String tmp;
	
	GameFont font;
	public Instruction(GameEngine parent) {
		super(parent);
		this.parent = parent;
	}

	public void initResources() {
		
		font = fontManager.getFont(getImage("BitmapFont.png"));
		background = getImage("Title.png", false);
		
		
	}
	
	public void update(long elapsedTime) {
		if (keyPressed(KeyEvent.VK_ESCAPE)) {
			parent.nextGameID = Civcraft.TITLE;
			finish();
		}

	}
	public void render(Graphics2D g) {
		g.drawImage(background, 0, 0, null);
		font.drawString(g, "INSTRUCTIONS:", 40, 50);
		font.drawString(g, "AFTER SELECTING A UNIT/CITY, PRESS 'C' TO CONFIRM", 40, 70);
		font.drawString(g, "AND THEN PRESS AN ACTION", 40, 90);
		font.drawString(g, "IF THERE IS SEVERAL UNITS ON A TILE,", 40, 110);
		font.drawString(g, "LIKE IN A CITY", 40, 130);
		font.drawString(g, "PRESS 'S' TO SWITCH BETWEEN THE UNITS", 40, 150);
		font.drawString(g, "AND CONFIRM WITH 'C' WHEN DONE", 40, 170);
		font.drawString(g, "RETURN TO MENU WITH 'ESC'", 40, 190);
		
	}
}


