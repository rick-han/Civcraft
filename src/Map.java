import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Random;


import com.golden.gamedev.util.*;
import com.golden.gamedev.engine.*;


public class Map extends AbstractTileBackground2 {

	public static final int TILE_WIDTH = 32, TILE_HEIGHT = 32;

	Chipset		chipsetE;
	Chipset		terrainAddonChipset;
	Chipset 	chipsetEs;
	Chipset 	fogOfWarChipset;
	Chipset[] 	chipset;	
	int[][] layer1;			// the lower tiles
	int[][]	layer2;			// the fringe tiles
	static int[][]	fogofwar;
	
	RPGSprite[][] layer3;	// the object/event/npc tiles
	int a=32, b=32;
	Chipset terrainChipset;
	ArrayList<RPGSprite> list2;
	static int numT = MyPackLyss.numT;
	static boolean multiplayer;
	static ArrayList k;// = new ArrayList<String>();
	static int sizeX = 56, sizeY = 27, maxX=sizeX-1, maxY=sizeY-1;
	//static int sizeX = 56, sizeY = 27, maxX=sizeX-1, maxY=sizeY-1;
	public Map(BaseLoader bsLoader, BaseIO bsIO, int width, int height){
		super(0, 0, width, height);
	}
	
	
	@SuppressWarnings("unchecked")
	public Map(BaseLoader bsLoader, BaseIO bsIO){
		super(0, 0, TILE_WIDTH, TILE_HEIGHT);
		
		
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
						layer2[i][j] = 2;
					else if (layer1[i][j] == 9)
						layer2[i][j] = 3;
					else
						layer2[i][j] = -1;
					fogofwar[i][j] = 0;
				}
			}
		}
		
		// set the actual map size based on the read file
		setSize(sizeX, sizeY);
		
		fogOfWarChipset = new Chipset(bsLoader.getImage("FoW.png"));
		terrainChipset = new Chipset(bsLoader.getImages("TerrainSpriteSheet.png", 12, 1));
		terrainAddonChipset = new Chipset(bsLoader.getImages("TerrainAddonSpriteSheet.png", 6, 1));
		
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

	public void renderTile(Graphics2D g,
						   int tileX, int tileY,
						   int x, int y) {
				
		// render Fog of War
		//g.drawImage(fogOfWarChipset.image2, x, y, null);
		if(multiplayer==true){
			int tilenum = layer1[tileX][tileY];
			int fognum = fogofwar[tileX][tileY];
			fognum = 0;
			// render layer 1
		
			if (fognum < 1){		
			
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
		}
		if(multiplayer==false){
			
			g.drawImage(fogOfWarChipset.image2, x, y, null);

			int tilenum = layer1[tileX][tileY];
			int fognum = fogofwar[tileX][tileY];
			// render layer 1
			if (fognum < 1){
				g.drawImage(terrainChipset.image[tilenum], x, y, null);
				// render layer 2
				int tilenum2 = layer2[tileX][tileY];
				if (tilenum2 != -1)
					g.drawImage(terrainAddonChipset.image[tilenum2], x, y-6, null);		
			}		

		}
		
	}

	public boolean isOccupied(int tileX, int tileY, ArrayList<RPGSprite> lista, RPGSprite unit) {
		list2=lista;
		for(int h = 0; h<list2.size();h++){
			int xs = list2.get(h).getXX();
			int ys = list2.get(h).getYY();		    			
			if (layer3[tileX][tileY] != null)
				if(layer3[tileX][tileY].friend==true && xs == tileX && ys == tileY && list2.get(h).getTyp()=="City")				
					return false;						
		}
		if (layer1[tileX][tileY] == 0 || layer1[tileX][tileY] == 1 || layer1[tileX][tileY] == 11)			
			return true;
		if(layer3[tileX][tileY] != null){
			if(layer3[tileX][tileY].friend==true && layer3[tileX][tileY].getTyp()!="City")		
				return true;					
			if(layer3[tileX][tileY].getTyp()=="City" && layer3[tileX][tileY].friend==unit.friend)
				return false;
		}
		return false;
		
	}
	public boolean boatisOccupied(int tileX, int tileY, ArrayList<RPGSprite> lista, RPGSprite unit) {
		list2=lista;
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
