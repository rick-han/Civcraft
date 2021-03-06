

// JFC
import java.awt.*;


// GTGE
import com.golden.gamedev.*;
import com.golden.gamedev.funbox.*;


public class Civcraft extends GameEngine {
	
	static MultiplayerMenu mm = null;
	static Civcraft cc = null;
	static RPGGame rg = null;
	static SingleplayerMenu sm = null;
	static Lobby lobb = null;
	static Instruction instr = null;
	public static final int	TITLE = 0, GAME_MODE = 1, MULTIPLAYER_MENU = 2, LOBBY = 3, INSTRUCTIONS=4, SINGLEPLAYER_MENU=5;
	private Proxy p = null;

	public void setProxy(Proxy proxy){
		p = proxy;
	}
	
	public Proxy getProxy(){
		return p;
	}
	
	public void initResources() {
		nextGameID = TITLE;
		showCursor();
		
		

	}

	public GameObject getGame(int GameID) {
		switch (GameID) {
			case TITLE : return new Title(this);
			case GAME_MODE :
				rg = new RPGGame(this);
				return rg; 
			case MULTIPLAYER_MENU : 
				mm = new MultiplayerMenu(this);
				return mm;
			case LOBBY : 
				lobb = new Lobby(this);
				return lobb;
			case INSTRUCTIONS : 
				instr = new Instruction(this);
				return instr;
			case SINGLEPLAYER_MENU : 
				sm = new SingleplayerMenu(this);
				return sm;	
		}

		return null;
	}

	public static void main(String[] args) {
//		if (args != null && args.length > 0 && args[0].equalsIgnoreCase("Java2D")) {
//			GameSettings settings = new GameSettings() {
//				protected JPanel initSettings() {
//					JPanel pane = super.initSettings();
//					pane.remove(sound);	// the game is not using sound
//					return pane;
//				}
//
//				public void start() {
//					GameLoader game = new GameLoader();
//					game.setup(new DemoRPG(), new Dimension(640, 480),
//							   fullscreen.isSelected(),
//							   bufferstrategy.isSelected());
//					game.start();
//				}
//			};
//
//		} else {
//			// this is for opengl mode
//			// require GTGE add-ons library
//			// LWJGL library, and JOGL library
//			new DemoRPGSettings();
//		}


		GameLoader game = new GameLoader();
		cc = new Civcraft();
		game.setup(cc, new Dimension(1024, 720), false);
		game.start();
	}

	{ distribute = true; }

	protected void notifyError(Throwable e) {
		new ErrorNotificationDialog(e, bsGraphics,
								    "DemoRPG v0.1", "pauwui@yahoo.com");
	}

}

//class DemoRPGSettings extends GameSettings {
//
//
//	JComboBox 	combo;
//	JCheckBox	vsync;
//
//
//	protected JPanel initSettings() {
//		combo = new JComboBox(new Object[] { "Java2D", "OpenGL LWJGL", "OpenGL JOGL" } );
//		combo.addActionListener(this);
//
//		vsync = new JCheckBox("VSync", true);
//		vsync.setEnabled(false);
//		vsync.setToolTipText("Enable or disable vertical monitor synchronization");
//
//		JPanel pane = super.initSettings();
//		pane.setLayout(new GridLayout(0, 1));
//		pane.removeAll();
//
//		pane.add(combo);
//		pane.add(fullscreen);
//		pane.add(bufferstrategy);
//		pane.add(vsync);
//
//		return pane;
//	}
//
//	public void actionPerformed(ActionEvent e) {
//		if (e.getSource() == combo) {
//			if (combo.getSelectedIndex() == 0) { // Java2D
//				bufferstrategy.setEnabled(true);
//				vsync.setEnabled(false);
//
//			} else { // JOGL or LWJGL
//				bufferstrategy.setEnabled(false);
//				vsync.setEnabled(true);
//			}
//
//		} else {
//			super.actionPerformed(e);
//		}
//	}
//
//
//	public void start() {
//		// the real game loader
//		OpenGLGameLoader game = new OpenGLGameLoader();
//
//		DemoRPG rpg = new DemoRPG();
//		Dimension dimension = new Dimension(640, 480);
//		boolean fs = fullscreen.isSelected();
//		boolean bs = bufferstrategy.isSelected();
//		boolean vs = vsync.isSelected();
//
//		switch (combo.getSelectedIndex()) {
//			// Java2D
//			case 0: game.setup(rpg, dimension, fs, bs); break;
//
//			// LWJGL
//			case 1: game.setupLWJGL(rpg, dimension, fs, vs); break;
//
//			// JOGL
//			case 2: game.setupJOGL(rpg, dimension, fs, vs); break;
//		}
//
//		game.start();
//	}
//
//}