
public class Database {
	private final int[][] rawTerrainData = {{0,6,0,0,0,0,0,2,0,0,2,2,50,0},
							{0,4,0,0,0,0,0,1,0,0,1,1,30,20},
							{5,0,1,6,1,0,1,2,1,0,2,2,25,0},
							{6,0,1,8,0,0,1,2,1,0,2,2,25,0},
							{2,0,0,2,0,2,0,1,0,0,0,0,0,25},
							{1,0,0,0,0,1,0,1,0,0,0,1,10,0},
							{1,0,1,1,0,0,0,1,0,0,0,1,15,0},
							{1,0,4,1,0,0,6,1,0,0,0,0,15,50},
							{1,0,5,1,0,0,10,2,0,0,1,0,25,75},
							{1,0,6,1,0,0,8,2,0,0,1,0,25,75},
							{1,0,1,1,6,8,1,0,4,1,1,1,25,100},
							{0,0,0,0,7,6,0,0,3,6,0,0,20,200}};
	
	private int[][] terrainData;
	
	public Database(){
		terrainData = rawTerrainData;
	}
	
	public Database(int[] terrainTypeCodes){
		/*
		 *Konstruktorn tar en lista med nummer som motsvarar terrŠngtypen i fšljande ordning
		 *Sea,Ocean,Plains,Grassland,Marsh,Desert,Tundra,Rain Forrest,Conifer Forrest,Broadleaf Forrest,Hills,Mountains
		 */
		for (int i = 0; i < terrainTypeCodes.length; i++){
			terrainData[terrainTypeCodes[i]] = rawTerrainData[i];
		}
	}
	public int getWheat(int terrainTypeCode){
		return terrainData[terrainTypeCode][0];
	}
	public int getFish(int terrainTypeCode){
		return terrainData[terrainTypeCode][1];
	}
	public int getGame(int terrainTypeCode){
		return terrainData[terrainTypeCode][2];
	}
	public int getHay(int terrainTypeCode){
		return terrainData[terrainTypeCode][3];
	}
	public int getStone(int terrainTypeCode){
		return terrainData[terrainTypeCode][4];
	}
	public int getOre(int terrainTypeCode){
		return terrainData[terrainTypeCode][5];
	}
	public int getLumber(int terrainTypeCode){
		return terrainData[terrainTypeCode][6];
	}
	public int getGold(int terrainTypeCode){
		return terrainData[terrainTypeCode][7];
	}
	public int getCoal(int terrainTypeCode){
		return terrainData[terrainTypeCode][8];
	}
	public int getSulfur(int terrainTypeCode){
		return terrainData[terrainTypeCode][9];
	}
	public int getHappiness(int terrainTypeCode){
		return terrainData[terrainTypeCode][10];
	}
	public int getScience(int terrainTypeCode){
		return terrainData[terrainTypeCode][11];
	}
	public int getAttackBonus(int terrainTypeCode){
		return terrainData[terrainTypeCode][12];
	}
	public int getDefenceBonus(int terrainTypeCode){
		return terrainData[terrainTypeCode][13];
	}
	
}