

// JFC
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

// GTGE
import com.golden.gamedev.object.GameFont;
import com.golden.gamedev.object.Timer;


public class RPGDialog {

	GameFont		font;
	BufferedImage	box;
	BufferedImage	arrow;

	String[] 		dialog;
	int				frame;
	int				totalFrame;
	Timer			speed;
	boolean			blink;
	Timer			blinkTimer;

	boolean			endDialog;

	int				y;


	public RPGDialog(GameFont font, BufferedImage box, BufferedImage arrow) {
		this.font = font;
		this.box = box;
		this.arrow = arrow;

		speed = new Timer(20);
		blinkTimer = new Timer(400);
	}


	public void setDialog(String[] dialog, boolean bottom) {
		this.dialog = dialog;

		endDialog = false;
		frame = totalFrame = 0;
		for (int i=0;i < dialog.length;i++) {
			totalFrame += dialog[i].length();
		}

		blink = false;

		y = (bottom == true) ? 320 : 0;
	}


	public void update(long elapsedTime) {
		if (!endDialog) {
			if (speed.action(elapsedTime)) {
				if (++frame >= totalFrame) {
					endDialog = true;
				}
			}

		} else {
			if (blinkTimer.action(elapsedTime)) {
				blink = !blink;
			}
		}
	}


	public void render(Graphics2D g) {
		// render the box
		g.drawImage(box, 0, y, null);

		if (endDialog) {
			for (int i=0;i < dialog.length;i++) {
				font.drawString(g, dialog[i], 20, y+20+(i*20));
			}

			if (!blink) {
				g.drawImage(arrow, 310, y+146, null);
			}

		} else {
			// type dialog letter by letter
			int typing = frame;

			for (int i=0;i < dialog.length;i++) {
				if (typing > dialog[i].length()) {
					typing -= dialog[i].length();
					font.drawString(g, dialog[i], 20, y+20+(i*20));

				} else {
					// last dialog in current rendering
					font.drawString(g, dialog[i].substring(0, typing), 20, y+20+(i*20));
					break;
				}
			}
		}
	}


}