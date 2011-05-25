import java.awt.Graphics2D;
import java.awt.List;
import java.util.ArrayList;

import javax.swing.text.html.HTMLDocument.Iterator;

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
		Civcraft par;
		public MyPackLyss(Civcraft par){
			this.par=par;
			this.civa=Civcraft.cc;
			this.parent=Civcraft.mm;
			this.rpg=Civcraft.rg;
			this.lobby=Civcraft.lobb;
		}
		
		public void newTurn(Result received){
			
			TurnBar.line1=("Its your turn!");
			
			RPGGame.received=received;
			Map.received=received;
			
			if(RPGGame.turn==1){
				RPGGame.gameState=7;
				RPGGame.waiting=true;
			}
			RPGGame.waiting=false;
			
								
		}

		public void lobbyUpdated(Result recieved){
			par.lobb.hostItemsLabel.setText("");
			int amount = recieved.getNumberPlayers();
			for(int i=0; i<amount; i++){
				par.lobb.hostItemsLabel.setText(par.lobb.hostItemsLabel.getText() + "\n" + recieved.getPlayerName(i) + " " + "\"" + recieved.getPlayerCiv(i)  + "\"");
			}
		
		}
		
		@SuppressWarnings("unchecked")
		public void gameStarted(Result received){
			
			RPGGame.received=received;
			Map.received=received;
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
			
			Civcraft.lobb.finish();
						
		}
		
		public void chatMessageReceived(Result res){
		}

        public void gameClosed(){
            
        }

		@Override
		public void casualtyReport(Result res) {
			
				RPGGame.bombX=res.getBombX();
				RPGGame.bombY=res.getBombY();
				RPGGame.hpL=res.getHealthLost();
				RPGGame.attacked=true;
			
			// TODO Auto-generated method stub
			
		}

		
		

		
		
		
	}