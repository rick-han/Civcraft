public class Construction {
	int tileX, tileY;
	int turnDone=0;
	String building;
	RPGSprite sprite=null;
 
	Construction(RPGSprite sprite, int x, int y, int done, String str){
		this.sprite=sprite;
		tileX=x;
		tileY=y;
		turnDone=done;
		building=str;
	}
	public String toString(){
		return "(" + tileX + ","+ tileY + ")" + building + ": " + (turnDone-RPGGame.turn)+" turns left";
	}
}
