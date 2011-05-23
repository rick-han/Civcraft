import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Random;


import com.golden.gamedev.GameEngine;
import com.golden.gamedev.util.*;
import com.golden.gamedev.engine.*;


public class Map extends AbstractTileBackground2 {

	public static final int TILE_WIDTH = 32, TILE_HEIGHT = 32;

	Chipset		chipsetE;
	Chipset 	terrainChipset, terrainDarkChipset;
	Chipset		terrainAddonChipset, terrainAddonDarkChipset;
	Chipset 	chipsetEs;
	Chipset 	fogOfWarChipset;
	Chipset[] 	chipset;	
	int[][] layer1;			// the lower tiles
	int[][]	layer2;			// the fringe tiles
	static int[][]	fogofwar;
	static PlayField2 playfield;
	RPGSprite[][] layer3;	// the object/event/npc tiles
	int a=32, b=32;
	int lwrlmt = 40, uprlmt = 80; //när kartar återgår till oexplored, lwrlmt är grått.
	static RPGGame own;
	static ArrayList<RPGSprite> list2;
	static int numT = MyPackLyss.numT;
	static boolean multiplayer=false;
	GameEngine parent=null;
	String nick;
	public static Result received;
	static ArrayList k;// = new ArrayList<String>();
	static int sizeX = 56, sizeY = 27, maxX=sizeX-1, maxY=sizeY-1;
	//static int sizeX = 56, sizeY = 27, maxX=sizeX-1, maxY=sizeY-1;
	public Map(BaseLoader bsLoader, BaseIO bsIO, int width, int height){
		super(0, 0, width, height);
	}
	
	
	@SuppressWarnings("unchecked")
	public Map(BaseLoader bsLoader, BaseIO bsIO, GameEngine parent, String nick){
		super(0, 0, TILE_WIDTH, TILE_HEIGHT);
		this.parent = parent;
		this.nick=nick;
		if(multiplayer==true){
			int yy = ((ArrayList) k.get(0)).size();
			int xx = k.size();
		    
			//anropar mapGenerator();
			maxY=yy-1;
			maxX=xx-1;
			sizeX=xx;
			sizeY=yy;
		}
		layer1 = new int[sizeX][sizeY];
		layer2 = new int[sizeX][sizeY];
		
		layer3 = new RPGSprite[sizeX][sizeY];		
		fogofwar = new int[sizeX][sizeY];
		mapGenerator(4);
		
		//  KARTGENELOLTOR
		//String[] lowerTile = FileUtil.fileRead(bsIO.getStream("map00.lwr"));
		//String[] upperTile = FileUtil.fileRead(bsIO.getStream("map00.upr"));
		//StringTokenizer lowerToken = new StringTokenizer(lowerTile[j]);
		//StringTokenizer upperToken = new StringTokenizer(upperTile[j]);
		//layer1[i][j] = Integer.parseInt(lowerToken.nextToken());
		//layer2[i][j] = Integer.parseInt(upperToken.nextToken());
		if(multiplayer==false){
			for (int j=0;j < layer1[0].length;j++) {
				for (int i=0;i < layer1.length;i++) {
					if (layer1[i][j] == 11)
						layer2[i][j] = 0;
					else if (layer1[i][j] == 8)
						layer2[i][j] = 1;
					else if (layer1[i][j] == 9)
						layer2[i][j] = 3;
					else if (layer1[i][j] == 2)
						layer2[i][j] = 5;
					else if (layer1[i][j] == 4)
						layer2[i][j] = 4;
					else
						layer2[i][j] = -1;
					fogofwar[i][j] = 0;//uprlmt;
				}
			}
		}
		if(multiplayer==true){
			for (int j=0;j < ((ArrayList) k.get(0)).size();j++) {
				for (int i=0;i < k.size();i++) {
					fogofwar[i][j] = uprlmt;
				}
			}
		}
		// set the actual map size based on the read file
		setSize(sizeX, sizeY);
		
		fogOfWarChipset = new Chipset(bsLoader.getImage("FoW.png"));
		terrainChipset = new Chipset(bsLoader.getImages("TerrainSpriteSheet.png", 12, 1));
		terrainDarkChipset = new Chipset(bsLoader.getImages("TerrainDarkSpriteSheet.png", 12, 1));
		terrainAddonChipset = new Chipset(bsLoader.getImages("TerrainAddonSpriteSheet.png", 6, 1));
		terrainAddonDarkChipset = new Chipset(bsLoader.getImages("TerrainAddonDarkSpriteSheet.png", 6, 1));
		
		/*
		chipset = new Chipset[16];
		BufferedImage[] image = bsLoader.getImages("ChipSet1.png", 4, 4, false);
		int[] chipnum = new int[] { 0,1,4,5,8,9,11,12,2,3,6,7,10,11,14,15 };
		for (int i=0;i < chipset.length;i++) {
			int num = chipnum[i];
			BufferedImage[] chips = ImageUtil.splitImages(image[num], 3, 4);
			chipset[i] = new Chipset(chips);
		}
		*/
		
		
	}
    public Map(BaseLoader bsLoader, BaseIO bsIO, GameEngine parent, String nick, int mapHeight, int mapWidth){
		super(0, 0, TILE_WIDTH, TILE_HEIGHT);
		this.parent = parent;
		this.nick=nick;
		maxX=mapHeight-1;
		maxY=mapWidth-1;
		sizeX=mapHeight;
		sizeY=mapWidth;
		layer1 = new int[sizeX][sizeY];
		layer2 = new int[sizeX][sizeY];
		layer3 = new RPGSprite[sizeX][sizeY];		
		fogofwar = new int[sizeX][sizeY];
		mapGenerator(1);
		if(multiplayer==false){
			for (int j=0;j < layer1[0].length;j++) {
				for (int i=0;i < layer1.length;i++) {
					if (layer1[i][j] == 11)
						layer2[i][j] = 0;
					else if (layer1[i][j] == 8)
						layer2[i][j] = 1;
					else if (layer1[i][j] == 9)
						layer2[i][j] = 3;
				else if (layer1[i][j] == 2)
					layer2[i][j] = 5;
				else if (layer1[i][j] == 4)
					layer2[i][j] = 4;
				else
					layer2[i][j] = -1;
				fogofwar[i][j] = 0;//uprlmt;
			}
		}
	}
	// set the actual map size based on the read file
	setSize(sizeX, sizeY);
	fogOfWarChipset = new Chipset(bsLoader.getImage("FoW.png"));
	terrainChipset = new Chipset(bsLoader.getImages("TerrainSpriteSheet.png", 12, 1));
	terrainDarkChipset = new Chipset(bsLoader.getImages("TerrainDarkSpriteSheet.png", 12, 1));
	terrainAddonChipset = new Chipset(bsLoader.getImages("TerrainAddonSpriteSheet.png", 6, 1));
	terrainAddonDarkChipset = new Chipset(bsLoader.getImages("TerrainAddonDarkSpriteSheet.png", 6, 1));
	}
	public void renderTile(Graphics2D g,
						   int tileX, int tileY,
						   int x, int y) {
				
		// render Fog of War
		//g.drawImage(fogOfWarChipset.image2, x, y, null);
		if(list2!=null){
			for(int h=0;h<list2.size();h++){
				RPGSprite unit = list2.get(h);
				if(unit.getTyp()=="Barbarian" && fogofwar[unit.getXX()][unit.getYY()] > uprlmt && unit.added==false){
					playfield.add(unit);
					unit.added=true;
				}
			}
			
		}
		
		if(multiplayer==true){
			int tilenum = layer1[tileX][tileY];
			int fognum = fogofwar[tileX][tileY];
			
			// render layer 1
			g.drawImage(fogOfWarChipset.image2, x, y, null);
			if (fognum < lwrlmt){		
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Desert")){			
					g.drawImage(terrainChipset.image[2], x, y, null);
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Rain Forrest")){
					g.drawImage(terrainChipset.image[4], x, y, null);
					g.drawImage(terrainAddonChipset.image[5], x, y, null);	
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Hills")){
					g.drawImage(terrainChipset.image[4], x, y, null);
					g.drawImage(terrainAddonChipset.image[1], x, y, null);
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Tundra")){
					g.drawImage(terrainChipset.image[6], x, y, null);				
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Broadleaf Forrest")){
					g.drawImage(terrainChipset.image[2], x, y, null);
					g.drawImage(terrainAddonChipset.image[4], x, y, null);
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Marsh")){
					g.drawImage(terrainChipset.image[4], x, y, null);
					
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Sea")){
					g.drawImage(terrainChipset.image[1], x, y, null);
				
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Ocean")){
					g.drawImage(terrainChipset.image[0], x, y, null);
					
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Mountains")){
					g.drawImage(terrainChipset.image[3], x, y, null);
					g.drawImage(terrainAddonChipset.image[0], x, y, null);
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Plains")){
					g.drawImage(terrainChipset.image[5], x, y, null);
					
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Conifer Forrest")){
					g.drawImage(terrainChipset.image[3], x, y, null);
					g.drawImage(terrainAddonChipset.image[3], x, y, null);
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Grassland")){
					g.drawImage(terrainChipset.image[3], x, y, null);
					
				}
				// render layer 2
				int tilenum2 = layer2[tileX][tileY];
				//if (tilenum2 != -1)
				//	g.drawImage(terrainAddonChipset.image[tilenum2], x, y-6, null);
			}
			else if (fognum >= lwrlmt && fognum < uprlmt){	
				
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Desert")){			
					g.drawImage(terrainDarkChipset.image[2], x, y, null);
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Rain Forrest")){
					g.drawImage(terrainDarkChipset.image[4], x, y, null);
					g.drawImage(terrainAddonDarkChipset.image[5], x, y, null);	
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Hills")){
					g.drawImage(terrainDarkChipset.image[4], x, y, null);
					g.drawImage(terrainAddonDarkChipset.image[1], x, y, null);
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Tundra")){
					g.drawImage(terrainDarkChipset.image[6], x, y, null);				
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Broadleaf Forrest")){
					g.drawImage(terrainDarkChipset.image[2], x, y, null);
					g.drawImage(terrainAddonDarkChipset.image[4], x, y, null);
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Marsh")){
					g.drawImage(terrainDarkChipset.image[4], x, y, null);
					
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Sea")){
					g.drawImage(terrainDarkChipset.image[1], x, y, null);	
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Ocean")){
					g.drawImage(terrainDarkChipset.image[0], x, y, null);
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Mountains")){
					g.drawImage(terrainDarkChipset.image[3], x, y, null);
					g.drawImage(terrainAddonDarkChipset.image[0], x, y, null);
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Plains")){
					g.drawImage(terrainDarkChipset.image[5], x, y, null);
					
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Conifer Forrest")){
					g.drawImage(terrainDarkChipset.image[3], x, y, null);
					g.drawImage(terrainAddonDarkChipset.image[3], x, y, null);
				}
				if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Grassland")){
					g.drawImage(terrainDarkChipset.image[3], x, y, null);
					
				}
				// render layer 2
				int tilenum2 = layer2[tileX][tileY];
				//if (tilenum2 != -1)
				//	g.drawImage(terrainAddonChipset.image[tilenum2], x, y-6, null);
			}
		}
		if(multiplayer==false){
			g.drawImage(fogOfWarChipset.image2, x, y, null);

			int tilenum = layer1[tileX][tileY];
			int fognum = fogofwar[tileX][tileY];
			// render layer 1
			if (fognum < lwrlmt){
				g.drawImage(terrainChipset.image[tilenum], x, y, null);
				// render layer 2
				int tilenum2 = layer2[tileX][tileY];
				if (tilenum2 != -1)
					g.drawImage(terrainAddonChipset.image[tilenum2], x, y-6, null);		
			}
			else if (fognum >= lwrlmt && fognum < uprlmt){
				g.drawImage(terrainDarkChipset.image[tilenum], x, y, null);
				// render layer 2
				int tilenum2 = layer2[tileX][tileY];
				if (tilenum2 != -1)
					g.drawImage(terrainAddonDarkChipset.image[tilenum2], x, y-6, null);
			}
		}
		
	}
	public static void send(PlayField2 playfieldd, ArrayList<RPGSprite> listd, RPGGame owna){
		playfield=playfieldd;
		list2=listd;
		own = owna;
	}
	public boolean isOccupied(int tileX, int tileY, ArrayList<RPGSprite> lista, RPGSprite unit) {
		list2=lista;
		if (multiplayer==false){
			for(int h = 0; h<list2.size();h++){
				int xs = list2.get(h).getXX();
				int ys = list2.get(h).getYY();		    			
				if (layer3[tileX][tileY] != null)
					if(layer3[tileX][tileY].friend==true && xs == tileX && ys == tileY && list2.get(h).getTyp()=="City")
						return false;
			}
			if (layer1[tileX][tileY] == 0 || layer1[tileX][tileY] == 1 || layer1[tileX][tileY] == 11){
				if(layer3[tileX][tileY] != null){
					for (int i = 0; i < list2.size(); i++){
						if (layer3[tileX][tileY].friend==true && layer3[tileX][tileY].checkCapacity()){
							for (int j = 0; j < layer3[tileX][tileY].capacity.length; j++){
								
								if (layer3[tileX][tileY].capacity[i]==null){
									layer3[tileX][tileY].capacity[i]=unit;
									layer3[tileX][tileY]=null;
									RPGGame.bordat=true;
									return false;
								}
							}
						}
					}
				}
				return true;
			}
			if(layer3[tileX][tileY] != null){
				if(layer3[tileX][tileY].getTyp()=="City" && layer3[tileX][tileY].friend==unit.friend)
					return false;
				if(layer3[tileX][tileY].getTyp()=="SiegeTower" && layer3[tileX][tileY].friend==unit.friend)
					return false;
				if(layer3[tileX][tileY].friend==true && layer3[tileX][tileY].getTyp()!="City" || layer3[tileX][tileY].getTyp()!="SiegeTower")		
					return true;
			}
			
		}
		if (multiplayer==true){
			if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Sea"))
				return true;
			if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Ocean"))
				return true;
			if(((String) (((ArrayList) k.get(tileX))).get(tileY)).equalsIgnoreCase("Mountain"))
				return true;
		}
		return false;
	}
	public boolean boatisOccupied(int tileX, int tileY, ArrayList<RPGSprite> lista, RPGSprite unit) {
		list2=lista;
		if(multiplayer==false){
			for(int h = 0; h<list2.size();h++){
				int xs = list2.get(h).getXX();
				int ys = list2.get(h).getYY();		    			
				if (layer3[tileX][tileY] != null)
					if(layer3[tileX][tileY].friend==true && xs == tileX && ys == tileY && list2.get(h).getTyp()=="City")				
						return false;
			}
			if(layer1[tileX][tileY] != 1 || layer3[tileX][tileY] != null || layer1[tileX][tileY] !=1)
				return true;
			if(layer3[tileX][tileY] != null){
				if(layer3[tileX][tileY].friend==true && layer3[tileX][tileY].getTyp()!="City")		
					return true;					
				if(layer3[tileX][tileY].getTyp()=="City" && layer3[tileX][tileY].friend==unit.friend)
					return false;
			}			
			
		}
		if (multiplayer==true){
			if(((String) (((ArrayList) k.get(tileX))).get(tileY))!=("Sea") || ((String) (((ArrayList) k.get(tileX))).get(tileY))!=("Ocean"))
				return true;
		}
		return false;
	} 

	public RPGSprite getLayer3(int tileX, int tileY) {
	try {
	    return layer3[tileX][tileY];
	} catch (Exception e) {
		// out of bounds
		return null;
	} }


	// chipset is only a pack of images
	class Chipset {
		BufferedImage[] image;
		BufferedImage image2;
		public Chipset(BufferedImage[] image) {
			this.image = image;
		}

		public Chipset(BufferedImage image2) {
			this.image2=image2;
			// TODO Auto-generated constructor stub
		}

	}

	public boolean isOccupied(double tileX, double tileY) {
		try {
			return (layer1[(int) tileX][(int) tileY] == 0 ||
					layer1[(int) tileX][(int) tileY] == 1 ||
					layer1[(int) tileX][(int) tileY] == 11 ||
					layer3[(int) tileX][(int) tileY] != null);
		} catch (Exception e) {
			// out of bounds
			return true;
		} }


void updateFogOfWar(){
	if(multiplayer==false){
		for (int j=0;j < layer1[0].length;j++) {
			for (int i=0;i < layer1.length;i++) {
				if (fogofwar[i][j] <= uprlmt)
					fogofwar[i][j]++;
			}
		}
	}
	if(multiplayer==true){
		for (int j=0;j < ((ArrayList) k.get(0)).size();j++) {
			for (int i=0;i < k.size();i++) {
				if (fogofwar[i][j] <= uprlmt)
					fogofwar[i][j]++;
			}
		}
	}
}

public static void reveal(int tileX, int tileY, int sightRange){
	int current = 1;
	if (tileX < 0) tileX = 0; 
	if (tileY < 0) tileY = 0;
	if (tileX >= 0 && tileY >= 0 && tileX <= maxX && tileY <= maxY)
		fogofwar[tileX][tileY] = 0;
	if (tileX%2 == 0 && tileX >= 0 && tileY >= 0 && tileX <= maxX && tileY <= maxY){			
		if (tileX < maxX) fogofwar[tileX+1][tileY]= 0; 
		if (tileY < maxY) fogofwar[tileX][tileY+1] = 0;
		if (tileX > 0) fogofwar[tileX-1][tileY] = 0;
		if (tileX > 0 && tileY > 0) fogofwar[tileX-1][tileY-1] = 0;
		if (tileX < maxX && tileY > 0) fogofwar[tileX+1][tileY-1] = 0;
		if (tileY > 0) fogofwar[tileX][tileY-1] = 0;
		if(current < sightRange){
			revealRedux(tileX+1, tileY, sightRange, current);
			revealRedux(tileX, tileY+1, sightRange, current);
			revealRedux(tileX-1, tileY, sightRange, current);
			revealRedux(tileX-1, tileY-1, sightRange, current);
			revealRedux(tileX+1, tileY-1, sightRange, current);
			revealRedux(tileX, tileY-1, sightRange, current);
		
		}
	}
	else if(tileX >= 0 && tileY >= 0 && tileX <= maxX && tileY <= maxY){
		if (tileX > 0) fogofwar[tileX-1][tileY] = 0;
		if (tileX > 0 && tileY < maxY) fogofwar[tileX-1][tileY+1] = 0;
		if (tileY > 0) fogofwar[tileX][tileY-1] = 0;
		if (tileX < maxX) fogofwar[tileX+1][tileY] = 0;
		if (tileX < maxX && tileY < maxY) fogofwar[tileX+1][tileY+1] = 0;
		if (tileY < maxY) fogofwar[tileX][tileY+1] = 0;
		if(current < sightRange){
			revealRedux(tileX-1, tileY, sightRange, current);
			revealRedux(tileX-1, tileY+1, sightRange, current);
			revealRedux(tileX, tileY-1, sightRange, current);
			revealRedux(tileX+1, tileY, sightRange, current);
			revealRedux(tileX+1, tileY+1, sightRange, current);
			revealRedux(tileX, tileY+1, sightRange, current);		
		}
	}
}	
private static void revealRedux(int tileX, int tileY, int sightRange, int current){
	current++;
	if (tileX < 0) tileX = 0; 
	if (tileY < 0) tileY = 0;
	if (tileX >= 0 && tileY >= 0 && tileX <= maxX && tileY <= maxY)
		fogofwar[tileX][tileY] = 0;
	if (tileX%2 == 0  && tileX >= 0 && tileY >= 0 && tileX <= maxX && tileY <= maxY){			
		if (tileX < maxX) fogofwar[tileX+1][tileY]= 0; 
		if (tileY < maxY) fogofwar[tileX][tileY+1] = 0;
		if (tileX > 0) fogofwar[tileX-1][tileY] = 0;
		if (tileX > 0 && tileY > 0) fogofwar[tileX-1][tileY-1] = 0;
		if (tileX < maxX && tileY > 0) fogofwar[tileX+1][tileY-1] = 0;
		if (tileY > 0) fogofwar[tileX][tileY-1] = 0;
		if(current < sightRange){
			revealRedux(tileX+1, tileY, sightRange, current);
			revealRedux(tileX, tileY+1, sightRange, current);
			revealRedux(tileX-1, tileY, sightRange, current);
			revealRedux(tileX-1, tileY-1, sightRange, current);
			revealRedux(tileX+1, tileY-1, sightRange, current);
			revealRedux(tileX, tileY-1, sightRange, current);
		
		}
	}
	else if(tileX >= 0 && tileY >= 0 && tileX <= maxX && tileY <= maxY){
		if (tileX > 0) fogofwar[tileX-1][tileY] = 0;
		if (tileX > 0 && tileY < maxY) fogofwar[tileX-1][tileY+1] = 0;
		if (tileY > 0) fogofwar[tileX][tileY-1] = 0;
		if (tileX < maxX) fogofwar[tileX+1][tileY] = 0;
		if (tileX < maxX && tileY < maxY) fogofwar[tileX+1][tileY+1] = 0;
		if (tileY < maxY) fogofwar[tileX][tileY+1] = 0;
		if(current < sightRange){
			revealRedux(tileX-1, tileY, sightRange, current);
			revealRedux(tileX-1, tileY+1, sightRange, current);
			revealRedux(tileX, tileY-1, sightRange, current);
			revealRedux(tileX+1, tileY, sightRange, current);
			revealRedux(tileX+1, tileY+1, sightRange, current);
			revealRedux(tileX, tileY+1, sightRange, current);		
		}
	}
}

public boolean visible(int x, int y){
	if (fogofwar[x][y] < lwrlmt)
		return true;
	else if (fogofwar[x][y] < uprlmt && (layer3[x][y].typ.equalsIgnoreCase("city") || layer3[x][y].typ.equalsIgnoreCase("mine"))) //lägg till fler undantag kanske med en metod
		return true;
	else
		return false;
}

private void mapGenerator(int bas){
	Random rand = new Random();
	int r = 0, a = 0 , b = 0, c = 0;
	for (int j=0;j < layer1[0].length;j++) {
		for (int i=0;i < layer1.length;i++) {
			r = tilePicker();
			//layer1[i][j] = bas;
			if (i == 0 && j == 0)
				layer1[i][j] = bas;
			else if (j == 0){
				a = layer1[i-1][j];
				if (rand.nextInt(2) > 0)
					layer1[i][j] = a;
				else
					layer1[i][j] = r;
			}
			else if (i == 0){
				a = layer1[i][j-1];
				if (rand.nextInt(2) > 0)
					layer1[i][j] = a;
				else
					layer1[i][j] = r;
				}
			else {
				a = layer1[i-1][j];
				b = layer1[i][j-1];
				c = layer1[i-1][j-1];
				if (a == b && a == c){
					if (rand.nextInt(2) > 0)
						layer1[i][j] = a;
					else
						layer1[i][j] = r;
				}
				else if (r != a && r != b && r != c)
					switch	(rand.nextInt(3)) {
						case 0: layer1[i][j] = a; break;
						case 1: layer1[i][j] = b; break;
						case 2: layer1[i][j] = c; break;
					}
				else if (a == c && a != b)
					layer1[i][j] = r;
				else
					switch	(rand.nextInt(5)) {
						case 0: layer1[i][j] = a; break;
						case 1: layer1[i][j] = b; break;
						case 2: layer1[i][j] = c; break;
						default: layer1[i][j] = r; break;
				}	
			}
		}
	}
	
	r = rand.nextInt(4)+2;
	for (int i = 0; i < r; i++){
		spawnCircle(rand.nextInt(maxX),rand.nextInt(maxY), rand.nextInt(4)+1,1);
		spawnRange(rand.nextInt(maxX),rand.nextInt(maxY),0);
	}
	r = rand.nextInt(4)+2;;
	for (int i = 0; i < r; i++){
		r = rand.nextInt(3);
		switch (r){
			case 0:{ 
				spawnCircle(rand.nextInt(maxX),rand.nextInt(maxY), rand.nextInt(4)+1,tilePicker());
				break;
				}
			case 1:{
				spawnRange(rand.nextInt(maxX),rand.nextInt(maxY),tilePicker());
				break;
			}
		}
	}
	for (int j=0;j < layer1[0].length;j++) {
		for (int i=0;i < layer1.length;i++) {
			if (j < 2 || j > maxY-2)
				layer1[i][j] = 6;
		}
	}
}

private int tilePicker(){
	Random rand = new Random();
	int r = rand.nextInt(11);
	if (r > 5)
		r++;
	//har spexxar man troligheten för var och en lolol
	return r;
}
private void spawnCircle(int tileX, int tileY, int goal, int bas){
	int radie=1;
	if (tileX < 0) tileX = 0; 
	if (tileY < 0) tileY = 0;
	if (tileX >= 0 && tileY >=0 && tileX <= maxX && tileY <= maxY)
		layer1[tileX][tileY] = bas;
	if (tileX%2 == 0 && tileX >= 0 && tileY >= 0 && tileX <= maxX && tileY <= maxY){			
		if (tileX < maxX) layer1[tileX+1][tileY] = bas; 
		if (tileY < maxY) layer1[tileX][tileY+1] = bas;
		if (tileX > 0) layer1[tileX-1][tileY] = bas;
		if (tileX > 0 && tileY > 0) layer1[tileX-1][tileY-1] = bas;
		if (tileX < maxX && tileY > 0) layer1[tileX+1][tileY-1] = bas;
		if (tileY > 0) layer1[tileX][tileY-1] = bas;
		if(radie < goal){
			spawnCircleRedux(tileX+1, tileY, bas, goal, radie);
			spawnCircleRedux(tileX, tileY+1, bas, goal, radie);
			spawnCircleRedux(tileX-1, tileY, bas, goal, radie);
			spawnCircleRedux(tileX-1, tileY-1, bas, goal, radie);
			spawnCircleRedux(tileX+1, tileY-1, bas, goal, radie);
			spawnCircleRedux(tileX, tileY-1, bas, goal, radie);
		
		}
	}
	else if(tileX >= 0 && tileY >= 0 && tileX <= maxX && tileY <= maxY){
		if (tileX > 0) layer1[tileX-1][tileY] = bas;
		if (tileX > 0 && tileY < maxY) layer1[tileX-1][tileY+1] = bas;
		if (tileY > 0) layer1[tileX][tileY-1] = bas;
		if (tileX < maxX) layer1[tileX+1][tileY] = bas;
		if (tileX < maxX && tileY < maxY) layer1[tileX+1][tileY+1] = bas;
		if (tileY < maxY) layer1[tileX][tileY+1] = bas;
		if(radie < goal){
			spawnCircleRedux(tileX-1, tileY, bas, goal, radie);
			spawnCircleRedux(tileX-1, tileY+1, bas, goal, radie);
			spawnCircleRedux(tileX, tileY-1, bas, goal, radie);
			spawnCircleRedux(tileX+1, tileY, bas, goal, radie);
			spawnCircleRedux(tileX+1, tileY+1, bas, goal, radie);
			spawnCircleRedux(tileX, tileY+1, bas, goal, radie);	
		}
	}
}
private void spawnCircleRedux(int tileX, int tileY, int bas, int goal, int radie){
	radie++;
	if (tileX < 0) tileX = 0; 
	if (tileY < 0) tileY = 0;
	if (tileX >= 0 && tileY >=0 && tileX <= maxX && tileY <= maxY)
		layer1[tileX][tileY] = bas;
	if (tileX%2 == 0 && tileX >= 0 && tileY >= 0 && tileX <= maxX && tileY <= maxY){			
		if (tileX < maxX) layer1[tileX+1][tileY] = bas; 
		if (tileY < maxY) layer1[tileX][tileY+1] = bas;
		if (tileX > 0) layer1[tileX-1][tileY] = bas;
		if (tileX > 0 && tileY > 0) layer1[tileX-1][tileY-1] = bas;
		if (tileX < maxX && tileY > 0) layer1[tileX+1][tileY-1] = bas;
		if (tileY > 0) layer1[tileX][tileY-1] = bas;
		if(radie < goal){
			spawnCircleRedux(tileX+1, tileY, bas, goal, radie);
			spawnCircleRedux(tileX, tileY+1, bas, goal, radie);
			spawnCircleRedux(tileX-1, tileY, bas, goal, radie);
			spawnCircleRedux(tileX-1, tileY-1, bas, goal, radie);
			spawnCircleRedux(tileX+1, tileY-1, bas, goal, radie);
			spawnCircleRedux(tileX, tileY-1, bas, goal, radie);
		
		}
	}
	else if(tileX >= 0 && tileY >= 0 && tileX <= maxX && tileY <= maxY){
		if (tileX > 0) layer1[tileX-1][tileY] = bas;
		if (tileX > 0 && tileY < maxY) layer1[tileX-1][tileY+1] = bas;
		if (tileY > 0) layer1[tileX][tileY-1] = bas;
		if (tileX < maxX) layer1[tileX+1][tileY] = bas;
		if (tileX < maxX && tileY < maxY) layer1[tileX+1][tileY+1] = bas;
		if (tileY < maxY) layer1[tileX][tileY+1] = bas;
		if(radie < goal){
			spawnCircleRedux(tileX-1, tileY, bas, goal, radie);
			spawnCircleRedux(tileX-1, tileY+1, bas, goal, radie);
			spawnCircleRedux(tileX, tileY-1, bas, goal, radie);
			spawnCircleRedux(tileX+1, tileY, bas, goal, radie);
			spawnCircleRedux(tileX+1, tileY+1, bas, goal, radie);
			spawnCircleRedux(tileX, tileY+1, bas, goal, radie);	
		}
	}
}
private void spawnRange(int tileX, int tileY, int bas){
	Random rand = new Random();
	int size = rand.nextInt(10)+5;
	int currSize=1;
	int direction = rand.nextInt(4);
	if (tileX < 0) tileX = 0; 
	if (tileY < 0) tileY = 0;
	if (tileY > maxY || tileX > maxX)
		return;
	switch (direction){
		case 0:{//väster
			if (tileX > 0 && tileX < maxX){
				layer1[tileX][tileY] = bas;
				spawnRangeRedux(tileX-1, tileY, bas, direction, size, currSize, rand);
			}
			break;
		}
		case 1:{//öster
			if (tileX > 0 && tileX < maxX){
				layer1[tileX][tileY] = bas;
				spawnRangeRedux(tileX+1, tileY, bas, direction, size, currSize, rand);
			}
			break;
		}
		case 2:{//söder
			if (tileY > 0 && tileY < maxY){
				layer1[tileX][tileY] = bas;
				spawnRangeRedux(tileX, tileY+1, bas, direction, size, currSize, rand);
			}
			break;
		}
		case 3:{//norr
			if (tileY > 0 && tileY < maxY){
				layer1[tileX][tileY] = bas;
				spawnRangeRedux(tileX, tileY-1, bas, direction, size, currSize, rand);
			}
			break;
		}
	}
}
private void spawnRangeRedux(int tileX, int tileY, int bas, int direction, int size, int currSize, Random rand){
	currSize++;
	if (tileX < 0) tileX = 0; 
	if (tileY < 0) tileY = 0;
	if (tileY > maxY || tileX > maxX)
		return;
	//if (currSize % 2 == 1)
		direction = rand.nextInt(3);
	if (currSize <= size){
		switch (direction){
			case 0:{//väster
				if (tileX > 0 && tileX < maxX){
					layer1[tileX][tileY] = bas;
					spawnRangeRedux(tileX-1, tileY, bas, direction, size, currSize, rand);
				}
				break;
			}
			case 1:{//öster
				if (tileX > 0 && tileX < maxX){
					layer1[tileX][tileY] = bas;
					spawnRangeRedux(tileX+1, tileY, bas, direction, size, currSize, rand);
				}	
				break;
			}
			case 2:{//söder
				if (tileY > 0 && tileY < maxY){
					layer1[tileX][tileY] = bas;
					spawnRangeRedux(tileX, tileY+1, bas, direction, size, currSize, rand);
				}
				break;
			}
			case 3:{//norr
				if (tileY > 0 && tileY < maxY){
					layer1[tileX][tileY] = bas;
					spawnRangeRedux(tileX, tileY-1, bas, direction, size, currSize, rand);
				}	
				break;
			}
		}
	}
}
}
