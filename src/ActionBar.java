
import java.awt.Color;
import java.awt.Graphics2D;

import com.golden.gamedev.Game;
import com.golden.gamedev.GameEngine;
import com.golden.gamedev.GameObject;
import com.golden.gamedev.engine.BaseInput;
import com.golden.gamedev.engine.BaseLoader;
import com.golden.gamedev.gui.TButton;
import com.golden.gamedev.gui.TPanel;
import com.golden.gamedev.gui.toolkit.FrameWork;


public class ActionBar extends GameObject{
	private FrameWork actionFrame = null;
	private TPanel actionBar = null;
	private final int ACTION_BAR_HEIGHT = 85;
	
	public ActionBar(GameEngine parent) {
		super(parent);
	}
	
	
	public void initResources(){
		actionFrame = new FrameWork(bsInput, 640, 480);
		actionBar = new TPanel(0, 480 - ACTION_BAR_HEIGHT, 640, ACTION_BAR_HEIGHT);
		actionBar.setExternalUI(getImages("ActionBarImage.png", 2, 1), false);
		
		TButton btn = new TButton("AT", 5, 5, 35, 35) {
			public void doAction() {
				System.out.println("Hello!");
			}
		};
		
		btn.setExternalUI(getImages("Button.png", 2, 1, "0100", 1), false);
		btn.setToolTipText(
				"a floating panel contains title bar and content pane\n\n" +
				"content pane is simply a panel to hold components\n" +
				"and the title bar is for moving the panel\n" +
				"that's why i called this floating panel :)\n\n" +
				"a floating panel can be iconized or closed by\n" +
				"clicking the right action button in its title bar");
		actionBar.add(btn);
		actionBar.add(new TButton("DE", 5, 45, 35, 35));
		actionBar.add(new TButton("BU", 45, 5, 35, 35));
		actionFrame.add(actionBar);
	}
	
	public void update(long elapsedTime) {
		actionFrame.update();
	}
	
	public void render(Graphics2D g) {
		actionFrame.render(g);
	}
	
	public FrameWork getFrameWork(){
		return actionFrame;
	}
	
}

