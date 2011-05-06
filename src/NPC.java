

// JFC
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

// GTGE
import com.golden.gamedev.GameEngine;
import com.golden.gamedev.object.Timer;
import com.golden.gamedev.util.Utility;


public class NPC extends RPGSprite {

	Timer 			moveTimer;
	LogicUpdater	logic;
	String[]		dialog;
    static String typ="npc";

	public NPC(RPGGame owner, BufferedImage[] images, int tileX, int tileY,
			 int moveSpeed, int direction, int atk, int def, int hp, int hit, int range, int move, int sightRange, String typ, LogicUpdater logic) {
		super(owner,images,tileX,tileY,moveSpeed,direction, atk, def, hp, hit, range, move, 2, typ,0,false);

		this.logic = logic;
	}

	protected void updateLogic(long elapsedTime) {
		
			if(turn>oldturn){
				logic.updateLogic(this, elapsedTime);
			}
		}	
}

interface LogicUpdater {

	public void updateLogic(RPGSprite spr, long elapsedTime);

}

class RandomMovement implements LogicUpdater {

	private static ArrayList<RPGSprite> list;
	static Map map;
	boolean even=false;
	RPGSprite fiende;
	static PlayField2 playfield;
	int wave1, wave2, battlescore;
	Random randa = new Random();
	String soldatTyp;
	WindowHandler windowHandler;
	String dialogNP[] = new String[3];
	static GameEngine parent;
	public static void getList(ArrayList<RPGSprite> lista, Map mapp, PlayField2 pf, GameEngine parenta){
		list=lista;
		map = mapp;
		playfield=pf;
		parent=parenta;
	}
	
	public void updateLogic(RPGSprite spr, long elapsedTime) {
		boolean moved = false;
		int i = 0;
		while (moved==false) {	
							
				if (spr.getXX() % 2 == 0){
					even=true;
				}else even=false;
				if(map.layer3[spr.getXX()+1][spr.getYY()]!=null && map.layer3[spr.getXX()+1][spr.getYY()].getTyp()!="Barbarian"){
					battle(spr, spr.getXX()+1, spr.getYY());
					moved=true;
					break;
				}
				if(map.layer3[spr.getXX()][spr.getYY()+1]!=null && map.layer3[spr.getXX()][spr.getYY()+1].getTyp()!="Barbarian"){
					battle(spr, spr.getXX(), spr.getYY()+1);
					moved=true;
					break;
				}	
				if(even){
					if(map.layer3[spr.getXX()+1][spr.getYY()+1]!=null && map.layer3[spr.getXX()+1][spr.getYY()+1].getTyp()!="Barbarian"){
						battle(spr, spr.getXX()+1, spr.getYY()+1);
						moved=true;
						break;
					}
				}
				if(!even){
					if(spr.getXX()-1 > 0 && spr.getYY()-1 > 0)
						if(map.layer3[spr.getXX()-1][spr.getYY()-1]!=null && map.layer3[spr.getXX()-1][spr.getYY()-1].getTyp()!="Barbarian"){
							battle(spr, spr.getXX()-1, spr.getYY()-1);
							moved=true;
							break;
						}
				}
				if(spr.getXX()-1 > 0)
					if(map.layer3[spr.getXX()-1][spr.getYY()]!=null && map.layer3[spr.getXX()-1][spr.getYY()].getTyp()!="Barbarian"){
						battle(spr, spr.getXX()-1, spr.getYY());
						moved=true;
						break;
					}
				if(spr.getYY()-1 > 0)
					if(map.layer3[spr.getXX()][spr.getYY()-1]!=null && map.layer3[spr.getXX()][spr.getYY()-1].getTyp()!="Barbarian"){
						battle(spr, spr.getXX(), spr.getYY()-1);
						moved=true;
						break;
					}
				if(!even){
					if(spr.getYY()-1 > 0)
						if(map.layer3[spr.getXX()+1][spr.getYY()-1]!=null && map.layer3[spr.getXX()+1][spr.getYY()-1].getTyp()!="Barbarian"){
							battle(spr, spr.getXX()+1, spr.getYY()-1);
							moved=true;
							break;
						}
					}
				if(even){
					if(spr.getXX()-1 > 0)
						if(map.layer3[spr.getXX()-1][spr.getYY()+1]!=null && map.layer3[spr.getXX()-1][spr.getYY()+1].getTyp()!="Barbarian"){
							battle(spr, spr.getXX()-1, spr.getYY()+1);
							moved=true;
							break;
						}
					}
				for(int h = 0; h<list.size();h++){
					if (list.get(h).getTyp()=="City"){
						moved = spr.test(list.get(h).tileX, list.get(h).tileY, spr, list);
						moved=true;
						break;
					}
			}
				break;
			
		}
		spr.oldturn=spr.turn;
	}
	public void battle(RPGSprite spr, int x, int y){
		fiende = (RPGSprite) map.getLayer3(x, y);						
		wave1=randa.nextInt(12);
		wave2=randa.nextInt(12);
		int waves = wave1+wave2;
	   
		battlescore=3;
		if (fiende.isFortified()){								  
			fiende.setDEF(fiende.getDEF()*1.5);
		}else fiende.setDEF(fiende.origdef);
	
		while(waves > 0){	
								   
			double DUDP=(fiende.getDEF()*fiende.getHP()/100);								   				   
			double AUDP=(spr.getATK()*spr.getHP())/100;								   								  
			double ATKR1 = randa.nextDouble() * AUDP;
			double ATKR2 = randa.nextDouble() * AUDP;
			double DEFR1 = randa.nextDouble() * DUDP;
			double DEFR2 = randa.nextDouble() * DUDP;
		   
			double DAU = ATKR1 + ATKR2;
			double DDU = DEFR1 + DEFR2;
		   
			fiende.setHP((int) (fiende.getHP()-DAU));
			spr.setHP((int) (spr.getHP()-DDU));
			   
			if (fiende.getHP()<=0 || DDU<=0){	
				battlescore=1;
				playfield.remove(fiende);									   
				map.layer3[fiende.getXX()][fiende.getYY()] = null;	
				break;					   
			}
			else if (spr.getHP()<=0 || DAU<=0){	
				battlescore=2;
				playfield.remove(spr);
				soldatTyp = spr.getTyp();
				map.layer3[spr.getXX()][spr.getYY()] = null;
				list.remove(spr);
				break;						   
			}
		}
		   
		if(battlescore==2){						
			dialogNP[0]="Your "+fiende.getTyp()+" was attacked\n";
			dialogNP[1]="by a barbarian and lost some MP\n";
			dialogNP[2]=fiende.getHP()+" soldiers are left";						   
			RPGGame.windowHandler.setLabel(dialogNP[0] + dialogNP[1] + dialogNP[2]);
			RPGGame.windowHandler.setVisible(true);   
			RPGGame.gameState=6;			
		}
		if(battlescore==1){						
			dialogNP[0]="Your "+fiende.getTyp()+" was attacked\n";
			dialogNP[1]="by a barbarian and died, lol";
			dialogNP[2]="";			
			list.remove(fiende);
			RPGGame.windowHandler.setLabel(dialogNP[0] + dialogNP[1] + dialogNP[2]);
			RPGGame.windowHandler.setVisible(true);   
			RPGGame.gameState=6;			
		}
		
		
	}

}



