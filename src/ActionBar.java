
import java.awt.Graphics2D;

import com.golden.gamedev.engine.BaseInput;
import com.golden.gamedev.gui.TButton;
import com.golden.gamedev.gui.TPanel;
import com.golden.gamedev.gui.toolkit.FrameWork;


public class ActionBar {
	private FrameWork actionFrame = null;
	private TPanel actionBar = null;
	private final int ACTION_BAR_HEIGHT = 85;
	private BaseInput bs = null;
	private int width, height;
	
	public ActionBar(BaseInput bs, int width, int height){
		this.bs = bs;
		this.width = width;
		this.height = height;
	}
	
	public void initActionBar(){
		actionFrame = new FrameWork(bs, width, height);
		actionBar = new TPanel(0, height - ACTION_BAR_HEIGHT, width, ACTION_BAR_HEIGHT){
			public void render(Graphics2D g) {
				super.render(g);
				//g.drawImage(getImage("ActionBar.png"), 0, 480 - ACTION_BAR_HEIGHT, null , null);
			}
		};
		//actionBar.setExternalUI(getImages("ActionBar.png", 1, 1), true);
		TButton btn = new TButton("AT", 5, 5, 35, 35);
		actionBar.add(btn);
		actionBar.add(new TButton("DE", 5, 45, 35, 35));
		actionBar.add(new TButton("BU", 45, 5, 35, 35));
		actionFrame.add(actionBar);
	}
	
	public FrameWork getFrameWork(){
		return actionFrame;
	}
	
}

