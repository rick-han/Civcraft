

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
		super(owner,images,tileX,tileY,moveSpeed,direction, frequence, frequence, frequence, frequence, frequence, frequence, 2, typ,0);

		moveTimer = new Timer((8-frequence)*1500);
		if (moveTimer.getDelay() == 0) {
			// it's not good to set the timer delay to 0!
			moveTimer.setDelay(10);
		}

		this.logic = logic;
		this.dialog = dialog;
	}

	public NPC(RPGGame owner, BufferedImage[] images, int tileX, int tileY,
			 int moveSpeed, int direction, int atk, int def, int hp, int hit, int range, int move, int sightRange, String typ, LogicUpdater logic) {
		super(owner,images,tileX,tileY,moveSpeed,direction, atk, def, hp, hit, range, move, 2, typ,0);
		
		moveTimer = new Timer((8-3)*1500);
		if (moveTimer.getDelay() == 0) {
			// it's not good to set the timer delay to 0!
			moveTimer.setDelay(10);
		}

		this.logic = logic;
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

interface LogicUpdater {

	public void updateLogic(RPGSprite spr, long elapsedTime);

}

class RandomMovement implements LogicUpdater {

	public void updateLogic(RPGSprite spr, long elapsedTime) {
		// random direction
		boolean moved = false;
		int i = 0;
		while (!moved) {
			switch (Utility.getRandom(0, 5)) {
				case 0: 	moved = spr.test(spr.tileX, spr.tileY, spr); break;
				case 1: 	moved = spr.test(spr.tileX, spr.tileY, spr); break;
				case 2: 	moved = spr.test(spr.tileX, spr.tileY, spr); break;
				case 3: 	moved = spr.test(spr.tileX, spr.tileY, spr); break;
				case 4: 	moved = spr.test(spr.tileX, spr.tileY, spr); break;
				case 5: 	moved = spr.test(spr.tileX, spr.tileY, spr); break;
				//case 0: 	moved = spr.test(spr.tileX-1, spr.tileY, spr); break;
				//case 1: 	moved = spr.test(spr.tileX+1, spr.tileY, spr); break;
				//case 2: 	moved = spr.test(spr.tileX+1, spr.tileY-1, spr); break;
				//case 3: 	moved = spr.test(spr.tileX+1, spr.tileY+1, spr); break;
				//case 4: 	moved = spr.test(spr.tileX, spr.tileY-1, spr); break;
				//case 5: 	moved = spr.test(spr.tileX-1, spr.tileY-1, spr); break;
			}

			if (i++ > 10) {
				// try for 10 times
				// in case the npc is stuck and can't move
				break;
			}
		}
	}

}



