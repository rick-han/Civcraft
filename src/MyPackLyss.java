import java.awt.Graphics2D;
import java.awt.List;
import java.util.ArrayList;

import com.golden.gamedev.GameEngine;
import com.golden.gamedev.GameObject;

public class MyPackLyss implements PacketListener{
		static int numT;	
		static ArrayList<String> k;
		static int tileXValue;
		static int tileYValue;
		Civcraft civa;
		RPGGame rpg;
		Lobby lobby;
		static String theType;
		MultiplayerMenu parent;
		
		public MyPackLyss(){
			this.civa=Civcraft.cc;
			this.parent=Civcraft.mm;
			this.rpg=Civcraft.rg;
			this.lobby=Civcraft.lobb;
		}
		
		public void newTurn(Result received){
			
			k = (ArrayList<String>) received.getMap();			
			numT = received.getNumberTiles();
			int numberStartingPositions = received.getNumberTiles();
			
			for(int i=0; i<numberStartingPositions; i++){
			//	tileXValue = received.getTileX(i);
				//tileYValue = received.getTileY(i);
				if(received.existUnit(i)){
					String theOwner = received.getUnitOwner(i);
					theType = received.getUnitType(i);
					int manPowerLeft = received.getUnitManPower(i);
					
					
				} if(received.existCity(i)){
					String theOwner = received.getCityOwner(i);
					String theName = received.getCityName(i);
					ArrayList<String> buildings = (ArrayList<String>) received.getCityBuildings(i);
					int amountCityUnits = received.getAmountCityUnits(i);
					for(int j=0; j<amountCityUnits; j++){
						String cityUnitOwner = received.getCityUnitOwner(i, j);
						String cityUnitType = received.getCityUnitType(i, j);
				int cityUnitManpower = received.getCityUnitManPower(i, j);
					}
				}
				String improvement = received.getImprovement(i);
			}
		}

		public void lobbyUpdated(Result res){
			
		}
		
		@SuppressWarnings("unchecked")
		public void gameStarted(Result received){
			RPGGame.received=received;
			Map.k = (ArrayList<String>) received.getMap();	
			
			numT = received.getNumberTiles();
			int numberStartingPositions = received.getNumberTiles();
	
			for(int i=0; i<numberStartingPositions; i++){
				tileXValue = received.getTileX(i);
				tileYValue = received.getTileY(i);
				if(received.existUnit(i)){
					String theOwner = received.getUnitOwner(i);
					theType = received.getUnitType(i);
					tileXValue = received.getTileX(i);
					tileYValue = received.getTileY(i);
					
					
				} if(received.existCity(i)){
					String theOwner = received.getCityOwner(i);
					String theName = received.getCityName(i);
					ArrayList<String> buildings = (ArrayList<String>) received.getCityBuildings(i);
					int amountCityUnits = received.getAmountCityUnits(i);
					for(int j=0; j<amountCityUnits; j++){
						String cityUnitOwner = received.getCityUnitOwner(i, j);
						String cityUnitType = received.getCityUnitType(i, j);
				int cityUnitManpower = received.getCityUnitManPower(i, j);
					}
				}
				String improvement = received.getImprovement(i);
			}
			civa.nextGameID=Civcraft.GAME_MODE;
			RPGGame.multiplayer=true;
			Map.multiplayer=true;
			parent.finish();
			lobby.finish();
			
		
					
		}
		
		public void chatMessageReceived(Result res){
		}

        public void gameClosed(){
            
        }

		
		

		
		
		
	}