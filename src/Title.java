

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

import com.golden.gamedev.*;
import com.golden.gamedev.gui.TButton;
import com.golden.gamedev.object.*;
import com.golden.gamedev.object.background.*;


public class Title extends GameObject {

	TButton but;
	GameFont		font;

	BufferedImage	title;
	BufferedImage	arrow;

	int				option;

	boolean			blink;
	Timer			blinkTimer = new Timer(400);


	public Title(GameEngine parent) {
		super(parent);
	}


	public void initResources() {
		title = getImage("Title.png", false);
		arrow = getImage("Arrow.png");
		but = new TButton("hej", 10, 10, 40, 40);
		font = fontManager.getFont(getImage("BitmapFont.png"));
	}


	public void update(long elapsedTime) {
		if (blinkTimer.action(elapsedTime)) {
			blink = !blink;
		}

		switch (bsInput.getKeyPressed()) {
			case KeyEvent.VK_ENTER :
				if (option == 0) {
					// start
					parent.nextGameID = Civcraft.GAME_MODE;
					finish();
				} else if (option == 1){
					// multiplayer
					parent.nextGameID = Civcraft.MULTIPLAYER_MENU;
					finish();
				} else if (option == 2) {
					// load

				} else if (option == 3) {
					// end
					finish();
				}
			break;

			case KeyEvent.VK_UP :
				option--;
				if (option < 0) option = 2;
			break;

			case KeyEvent.VK_DOWN :
				option++;
				if (option > 3) option = 0;
			break;

			case KeyEvent.VK_ESCAPE :
				finish();
			break;
		}
	}


	public void render(Graphics2D g) {
		g.drawImage(title, 0, 0, null);
		font.drawString(g, "SINGLEPLAYER", 450, 300);
		font.drawString(g, "MULTIPLAYER", 450, 320);
		font.drawString(g, "LOAD", 450, 340);
		font.drawString(g, "END", 450, 360);

		font.drawString(g, "PRESS \"Z\" FOR ACTION", 10, 455);

		if (!blink) {
			g.drawImage(arrow, 434, 297+(option*20), null);
		}
	}

}