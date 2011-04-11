import java.awt.*;
import java.awt.image.*;
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
	static int maxX = 0, maxY = 0;
	RPGSprite[][] layer3;	// the object/event/npc tiles
	int a=32; int b=32;
	Chipset terrainChipset;
	
	
	public Map(BaseLoader bsLoader, BaseIO bsIO){
		super(0, 0, TILE_WIDTH, TILE_HEIGHT);
		
		layer1 = new int[38][25];
		layer2 = new int[40][25];
		layer3 = new RPGSprite[40][25];		
		fogofwar = new int[40][25];
		
		
		//  KARTGENELOLTOR
		int t;
		Random rand = new Random();
		//String[] lowerTile = FileUtil.fileRead(bsIO.getStream("map00.lwr"));
		//String[] upperTile = FileUtil.fileRead(bsIO.getStream("map00.upr"));
		for (int j=0;j < layer1[0].length;j++) {
			//StringTokenizer lowerToken = new StringTokenizer(lowerTile[j]);
			//StringTokenizer upperToken = new StringTokenizer(upperTile[j]);
			for (int i=0;i < layer1.length;i++) {
				//layer1[i][j] = Integer.parseInt(lowerToken.nextToken());
				//layer2[i][j] = Integer.parseInt(upperToken.nextToken());
				
				t = rand.nextInt(7);
				layer1[i][j] = t;
				if (t == 1 || t == 2)
					layer2[i][j] = 0;
				else
					layer2[i][j] = -1;
				fogofwar[i][j] = 1;
			}
		}

		// set the actual map size based on the read file
		maxX = 38; 
		maxY = 25;
		setSize(maxX, maxY);
		

		terrainAddonChipset = new Chipset(bsLoader.getImages("Test1.png", 2, 1));
		fogOfWarChipset = new Chipset(bsLoader.getImage("FoW.png"));
		terrainChipset = new Chipset(bsLoader.getImages("TerrainSpriteSheet.png", 7, 1));
		
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
		// sparade kodsnuttar
		//g.drawImage(image, x, y, null);
		//g.drawImage(chipsetEs.image2, x, y, null);
	}

	public boolean isOccupied(int tileX, int tileY) {
	if (layer2[tileX][tileY] != -1 || layer2[tileX][tileY] == 1 || layer3[tileX][tileY] != null || layer1[tileX][tileY] == 144 || layer2[tileX][tileY] == 144)
	   return true;
		// out of bounds
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
		    return (layer2[(int) tileX][(int) tileY] != -1 ||
					layer3[(int) tileX][(int) tileY] != null);
		} catch (Exception e) {
			// out of bounds
			return true;
		} }

	public static void reveal(int tileX, int tileY, int sightRange){
		int count = 1;
		if (tileX < 0) tileX = 0; 
		if (tileY < 0) tileY = 0;
		if (tileX >= 0 && tileY >=0)
			fogofwar[tileX][tileY] = 0;
		if (tileX%2 == 0){			
			if (tileX < maxX) fogofwar[tileX+1][tileY]= 0; 
			if (tileY < maxY) fogofwar[tileX][tileY+1] = 0;
			if (tileX > 0) fogofwar[tileX-1][tileY] = 0;
			if (tileX > 0 && tileY > 0) fogofwar[tileX-1][tileY-1] = 0;
			if (tileX < maxX && tileY > 0) fogofwar[tileX+1][tileY-1] = 0;
			if (tileY > 0) fogofwar[tileX][tileY-1] = 0;
			if(count < sightRange){
				revealRedux(tileX+1, tileY, sightRange, count);
				revealRedux(tileX, tileY+1, sightRange, count);
				revealRedux(tileX-1, tileY, sightRange, count);
				revealRedux(tileX-1, tileY-1, sightRange, count);
				revealRedux(tileX+1, tileY-1, sightRange, count);
				revealRedux(tileX, tileY-1, sightRange, count);
			
			}
		}
		else{
			if (tileX > 0) fogofwar[tileX-1][tileY] = 0;
			if (tileX > 0 && tileY < maxY) fogofwar[tileX-1][tileY+1] = 0;
			if (tileY > 0) fogofwar[tileX][tileY-1] = 0;
			if (tileX < maxX) fogofwar[tileX+1][tileY] = 0;
			if (tileX < maxX && tileY < maxY) fogofwar[tileX+1][tileY+1] = 0;
			if (tileY < maxY) fogofwar[tileX][tileY+1] = 0;
			if(count < sightRange){
				revealRedux(tileX-1, tileY, sightRange, count);
				revealRedux(tileX-1, tileY+1, sightRange, count);
				revealRedux(tileX, tileY-1, sightRange, count);
				revealRedux(tileX+1, tileY, sightRange, count);
				revealRedux(tileX+1, tileY+1, sightRange, count);
				revealRedux(tileX, tileY+1, sightRange, count);
				
			}
		}
	}
	
private static void revealRedux(int tileX, int tileY, int sightRange, int count){
	count++;
	if (tileX < 0) tileX = 0; 
	if (tileY < 0) tileY = 0;
	if (tileX >= 0 && tileY >=0)
		fogofwar[tileX][tileY] = 0;
	if (tileX%2 == 0){			
		if (tileX < maxX) fogofwar[tileX+1][tileY] = 0; 
		if (tileY < maxY) fogofwar[tileX][tileY+1] = 0;
		if (tileX > 0) fogofwar[tileX-1][tileY] = 0;
		if (tileX > 0 && tileY > 0) fogofwar[tileX-1][tileY-1] = 0;
		if (tileX < maxX && tileY > 0) fogofwar[tileX+1][tileY-1] = 0;
		if (tileY > 0) fogofwar[tileX][tileY-1] = 0;
		if(count < sightRange){
			revealRedux(tileX+1, tileY, sightRange, count);
			revealRedux(tileX, tileY+1, sightRange, count);
			revealRedux(tileX-1, tileY, sightRange, count);
			revealRedux(tileX-1, tileY-1, sightRange, count);
			revealRedux(tileX+1, tileY-1, sightRange, count);
			revealRedux(tileX, tileY-1, sightRange, count);
		
		}
	}
	else{
		if (tileX > 0) fogofwar[tileX-1][tileY] = 0;
		if (tileX > 0 && tileY < maxY) fogofwar[tileX-1][tileY+1] = 0;
		if (tileY > 0) fogofwar[tileX][tileY-1] = 0;
		if (tileX < maxX) fogofwar[tileX+1][tileY] = 0;
		if (tileX < maxX && tileY < maxY) fogofwar[tileX+1][tileY+1] = 0;
		if (tileY < maxY) fogofwar[tileX][tileY+1] = 0;
		if(count < sightRange){
			revealRedux(tileX-1, tileY, sightRange, count);
			revealRedux(tileX-1, tileY+1, sightRange, count);
			revealRedux(tileX, tileY-1, sightRange, count);
			revealRedux(tileX+1, tileY, sightRange, count);
			revealRedux(tileX+1, tileY+1, sightRange, count);
			revealRedux(tileX, tileY+1, sightRange, count);
			
		}
	}
}}	
