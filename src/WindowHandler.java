import java.awt.Color;
import java.awt.Graphics2D;

import com.golden.gamedev.GameEngine;
import com.golden.gamedev.gui.TFloatPanel;
import com.golden.gamedev.gui.TLabel;
import com.golden.gamedev.gui.toolkit.FrameWork;
import com.golden.gamedev.gui.toolkit.TContainer;
import com.golden.gamedev.gui.toolkit.UIConstants;


public class WindowHandler {
	private GameEngine parent = null;
	private FrameWork frame = null;
	private TFloatPanel floatPane = null;
	private TLabel label = null;
	public WindowHandler(GameEngine parent) {
		this.parent  = parent;
	}
	public void initResources(){
	frame = new FrameWork(parent.bsInput, parent.getWidth(), parent.getHeight());
	floatPane = new TFloatPanel("Floating Panel", true, true, 60, 60, 290, 230);
	label = new TLabel("",10,10,290,150);
	TContainer contentPane = floatPane.getContentPane();
	contentPane.add(label);
	floatPane.setVisible(false);
	frame.add(floatPane);
	}
	public void update(long elapsedTime) {
		frame.update();
	}
	
	public void render(Graphics2D g) {
		frame.render(g);
	}
	public void setLabel(String text){
		label.setText(text);
	}
	public void setVisible(Boolean isVisible){
		floatPane.setVisible(isVisible);
	}
}
