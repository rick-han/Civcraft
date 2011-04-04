import java.awt.*;
import java.awt.image.*;
import java.util.StringTokenizer;

import com.golden.gamedev.object.*;
import com.golden.gamedev.object.background.abstraction.*;
import com.golden.gamedev.util.*;
import com.golden.gamedev.engine.*;


public class Map extends AbstractTileBackground2 {

	public static final int TILE_WIDTH = 32, TILE_HEIGHT = 32;

	Chipset		chipsetE;
	Chipset		chipsetF;
	Chipset chipsetEs;
	Chipset[] 	chipset;	
	int[][] layer1;			// the lower tiles
	int[][]	layer2;			// the fringe tiles
	RPGSprite[][] layer3;	// the object/event/npc tiles
	int a=32; int b=32;
	public Map(BaseLoader bsLoader, BaseIO bsIO){
		super(0, 0, TILE_WIDTH, TILE_HEIGHT);
		//super(0,0, i*a - (a/4 + 1) * i, j*b - b/2);
		layer1 = new int[38][25];
		layer2 = new int[40][25];
		layer3 = new RPGSprite[40][25];		

		String[] lowerTile = FileUtil.fileRead(bsIO.getStream("map00.lwr"));
		String[] upperTile = FileUtil.fileRead(bsIO.getStream("map00.upr"));
		for (int j=0;j < layer1[0].length;j++) {
			StringTokenizer lowerToken = new StringTokenizer(lowerTile[j]);
			StringTokenizer upperToken = new StringTokenizer(upperTile[j]);
			for (int i=0;i < layer1.length;i++) {
				layer1[i][j] = Integer.parseInt(lowerToken.nextToken());
				layer2[i][j] = Integer.parseInt(upperToken.nextToken());
			}
		}

		// set the actual map size based on the read file
		setSize(38, 25);

		chipsetE = new Chipset(bsLoader.getImages("ChipSet2.png", 6, 24, false));
		chipsetF = new Chipset(bsLoader.getImages("ChipSet3.png", 6, 24));



		chipset = new Chipset[16];
		BufferedImage[] image = bsLoader.getImages("ChipSet1.png", 4, 4, false);
		int[] chipnum = new int[] { 0,1,4,5,8,9,11,12,2,3,6,7,10,11,14,15 };
		for (int i=0;i < chipset.length;i++) {
			int num = chipnum[i];
			BufferedImage[] chips = ImageUtil.splitImages(image[num], 3, 4);
			chipset[i] = new Chipset(chips);
		}
	}

	public void renderTile(Graphics2D g,
						   int tileX, int tileY,
						   int x, int y) {
		// render layer 1
		
		int tilenum = layer1[tileX][tileY];
		//BufferedImage image = chipset[tilenum-chipsetE.image.length].image[2];
		
		if (tilenum < chipsetE.image.length) {
			//g.drawImage(chipsetE.image[tilenum], x, y, null);
			//	g.drawImage(chipsetEs.image2, x, y, null);
			g.drawImage(chipsetE.image[tilenum], x, y, null);
			//g.drawImage(image, x, y, null);
		} else if (tilenum >= chipsetE.image.length) {
			BufferedImage image = chipset[tilenum-chipsetE.image.length].image[2];
			g.drawImage(image, x, y, null);
			//g.drawImage(image, x, y, null);
			//g.drawImage(chipsetEs.image2, x, y, null);
		}

		// render layer 2
		int tilenum2 = layer2[tileX][tileY];
		if (tilenum2 != -1) {
			//g.drawImage(image, x, y, null);
			//g.drawImage(chipsetEs.image2, x, y, null);
			g.drawImage(chipsetF.image[tilenum2], x, y, null);
		}

		// layer 3 is rendered by sprite group
	}

	public boolean isOccupied(int tileX, int tileY) {
	try {
	    return (layer2[tileX][tileY] != -1 || layer3[tileX][tileY] != null);
	} catch (Exception e) {
		// out of bounds
		return true;
	} }

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

}
