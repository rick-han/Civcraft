

// JFC
import java.awt.image.BufferedImage;

// GTGE
import com.golden.gamedev.object.Timer;
import com.golden.gamedev.util.Utility;


public class NPC extends RPGSprite {

	Timer 			moveTimer;
	LogicUpdater	logic;
	String[]		dialog;
    static String typ="npc";

	public NPC(RPGGame owner, BufferedImage[] images, int tileX, int tileY,
			   int moveSpeed, int direction,
			   int frequence, LogicUpdater logic,
			   String[] dialog) {
		super(owner,images,tileX,tileY,moveSpeed,direction, frequence, frequence, frequence, frequence, frequence, frequence, 2, typ);

		moveTimer = new Timer((8-frequence)*1500);
		if (moveTimer.getDelay() == 0) {
			// it's not good to set the timer delay to 0!
			moveTimer.setDelay(10);
		}

		this.logic = logic;
		this.dialog = dialog;
	}


	protected void updateLogic(long elapsedTime) {
		if (owner.gameState == RPGGame.PLAYING) {
			// if playing then
			// update the npc based on its logic updater
			if (moveTimer.action(elapsedTime)) {
				logic.updateLogic(this, elapsedTime);
			}
		}
	}

}


 /****************************************************************************/
 /***************************** NPC MOVEMENT AI ******************************/
 /****************************************************************************/

/**
 * NPC Movement.
 */
interface LogicUpdater {

	public void updateLogic(RPGSprite spr, long elapsedTime);

}


class StayStill implements LogicUpdater {

	public void updateLogic(RPGSprite spr, long elapsedTime) {
		// stay still npc, just do nothing
	}

}


class RandomMovement implements LogicUpdater {

	public void updateLogic(RPGSprite spr, long elapsedTime) {
		// random direction
		boolean moved = false;
		int i = 0;
		while (!moved) {
			switch (Utility.getRandom(0, 3)) {
				case RPGSprite.LEFT: 	moved = spr.test(-1, 0); break;
				case RPGSprite.RIGHT: 	moved = spr.test(1, 0); break;
				case RPGSprite.UP: 		moved = spr.test(1, -1); break;
				case RPGSprite.DOWN: 	moved = spr.test(1, 1); break;
			}

			if (i++ > 10) {
				// try for 10 times
				// in case the npc is stuck and can't move
				break;
			}
		}
	}

}


class CycleUpDown implements LogicUpdater {

	public void updateLogic(RPGSprite spr, long elapsedTime) {
		// going up, and down
		boolean moved = false;

		if (spr.getDirection() == RPGSprite.UP) {
			// if the npc facing up, then moving up until the movement blocked
			moved = spr.test(0, 0);
			if (!moved) {
				// can't move up, time to move down
				spr.test(0, -0);
			}

		} else {
			moved = spr.test(0, 0);
			if (!moved) {
				spr.test(0, -0);
			}
		}
	}

}


class CycleLeftRight implements LogicUpdater {

	public void updateLogic(RPGSprite spr, long elapsedTime) {
		// going left, and right
		boolean moved = false;

		if (spr.getDirection() == RPGSprite.LEFT) {
			moved = spr.test(-1, 0);
			if (!moved) {
				spr.test(1, 0);
			}

		} else {
			moved = spr.test(1, 0);
			if (!moved) {
				spr.test(-1, 0);
			}
		}
	}

}