import java.util.*;

class Result{

	// H�r �r medlemsvariabler som ska kunna returneras.
	private boolean ok = true, locked;
	private String failMsg, hostName, chatMessage, chatName, fromWhom;
	private List sessions, players, map, updatedTiles;
	private int requestFail, requestOk, attackerLeft, defenderLeft;

	// H�r �r medlemsvariabler som bara �r hj�lpvariabler till vissa metoder.
	private List<Unit> cityUnits = new ArrayList<Unit>();
	private Tile temp;

	public Result(){
	}

	// --------------------------------------------
	// H�r under �r setters/till�ggsfunktioner!
	//
	// F�rst s� kommer booleans.

	public void addOk(boolean ok){
		this.ok = ok;
	}

	public void addLocked(boolean locked){
		this.locked = locked;
	}

	// Sen s� kommer str�ngar.

	public void addFailMsg(String msg){
		failMsg = msg;
	}

	public void addHostName(String name){
		hostName = name;
	}

	public void addChatMessage(String message){
		chatMessage = message;
	}

	public void addFromWhom(String name){
		fromWhom = name;
	}

	// H�r nedan �r det f�r listor.

	public void addSessions(List sessions){
		this.sessions = sessions;
	}

	public void addMap(List map){
		this.map = map;
	}

	// Och sist heltal.

	public void addRequestFail(int requestFail){
		this.requestFail = requestFail;
	}

	public void addRequestOk(int requestOK){
		this.requestOk = requestOk;
	}

	public void addAttackerLeft(int attLeft){
		attackerLeft = attLeft;
	}

	public void addDefenderLeft(int defLeft){
		defenderLeft = defLeft;
	}

	// Sets f�r komplexa strukturer.

	// F�r listan med spelare f�rst.

	public void setupPlayers(){
		players = new ArrayList<Player>();
	}

	public void addPlayer(String name, String civ){
		players.add(new Player(name, civ));
	}

	// Sen till den aningen st�rre f�r listan med uppdaterade tiles.

	public void setupTiles(){
		updatedTiles = new ArrayList<Tile>();
	}

	public void setUpdatedTile(int x, int y){
		temp = new Tile(new Position(x, y));
	}

	public void setUnit(Unit unit){
		temp.addUnit(unit);
	}

	public void addCityUnit(Unit unit){
		cityUnits.add(unit);
	}

	public void setCity(String owner, String name){
		temp.addCity(new City(owner, name, cityUnits));
	}

	public void setImprovement(String name){
		temp.addImprovement(name);
	}

	public void addUpdatedTile(){
		updatedTiles.add(temp);
	}

	// H�r slutar setters/till�ggsfunktioner.
	// --------------------------------------------
	// H�r under �r getters!
	//
	// F�rst booleans.

	public boolean getOk(){
		return ok;
	}

	public boolean getLocked(){
		return locked;
	}

	// Sen Str�ngar.

	public String getFailMsg(){
		return failMsg;
	}

	public String getHostName(){
		return hostName;
	}

	public String getChatMessage(){
		return chatName;
	}

	public String getChatFromWhom(){
		return fromWhom;
	}

	// Under finns getters f�r listor.

	public List getSessions(){
		return sessions;
	}

	public List getMap(){
		return map;
	}

	// H�r kommer getters f�r heltal.

	public int getRequestFail(){
		return requestFail;
	}

	public int getRequestOk(){
		return requestOk;
	}

	public int getAttackerLeft(){
		return attackerLeft;
	}

	public int getDefenderLeft(){
		return defenderLeft;
	}

	// H�r under kommer det att finnas getters f�r dom mer komplexa strukturerna.
	//
	// B�rjar med get f�r spelare.

	public int getNumberPlayers(){
		return players.size();
	}

	public String getPlayerName(int n){
		return ((Player)players.get(n)).getName();
	}

	public String getPlayerCiv(int n){
		return ((Player)players.get(n)).getCiv();
	}

	// Och sen f�r den gigantiska Tile-klassen som dessutom d� �r i en lista.

	public int getNumberTiles(){
		return updatedTiles.size();
	}

	public int getTileX(int n){
		return ((Tile)updatedTiles.get(n)).getPosition().getX();
	}

	public int getTileY(int n){
		return ((Tile)updatedTiles.get(n)).getPosition().getY();
	}

	public boolean existUnit(int n){
		if(((Tile)updatedTiles.get(n)).getUnit() != null){
			return true;
		}
		else{
			return false;
		}
	}

	public String getUnitOwner(int n){
		return ((Tile)updatedTiles.get(n)).getUnit().getOwner();
	}

	public String getUnitType(int n){
		return ((Tile)updatedTiles.get(n)).getUnit().getType();
	}

	public int getUnitManPower(int n){
		return ((Tile)updatedTiles.get(n)).getUnit().getManPower();
	}

    public List<Unit> getUnitUnits(int n){
        return ((Tile)updatedTiles.get(n)).getUnit().getUnits();
    }

	public boolean existCity(int n){
		if(((Tile)updatedTiles.get(n)).getCity() != null){
			return true;
		}
		else{
			return false;
		}
	}

	public String getCityOwner(int n){
		return ((Tile)updatedTiles.get(n)).getCity().getOwner();
	}

	public String getCityName(int n){
		return ((Tile)updatedTiles.get(n)).getCity().getName();
	}

	public int getAmountCityUnits(int n){
		return ((Tile)updatedTiles.get(n)).getCity().getAmountUnits();
	}

	public String getCityUnitOwner(int n, int k){
		return ((Tile)updatedTiles.get(n)).getCity().getUnit(k).getOwner();
	}

	public String getCityUnitType(int n, int k){
		return ((Tile)updatedTiles.get(n)).getCity().getUnit(k).getType();
	}

	public int getCityUnitManPower(int n, int k){
		return ((Tile)updatedTiles.get(n)).getCity().getUnit(k).getManPower();
	}

	public String getImprovement(int n){
		return ((Tile)updatedTiles.get(n)).getImprovement();
	}

	// H�r slutar getters.
	// --------------------------------------------
	// H�r under �r nestlade klasser f�r ytterligare containers!

	private class City{
		private String owner, name;
		private List<Unit> units;

		public City(String owner, String name, List<Unit> units){
			this.owner = owner;
			this.name = name;
			this.units = units;
		}

		public String getOwner(){
			return owner;
		}

		public String getName(){
			return name;
		}

		public int getAmountUnits(){
			return units.size();
		}

		public Unit getUnit(int n){
			return units.get(n);
		}

		public String toString(){
			return "City name: " + name + "\nCity owner: " + owner + "\nUnits in city: " + units + "\n";
		}
	}

	private class Position{
		private int x, y;

		public Position(int x, int y){
			this.x = x;
			this.y = y;
		}

		public int getX(){
			return x;
		}

		public int getY(){
			return y;
		}

		public String toString(){
			return x + "\t" + y + "\n";
		}
	}

	private class Tile{
		private Position placement;
		private Unit unit = null;
		private City city = null;
		private String improvement = "";

		public Tile(Position placement){
			this.placement = placement;
		}

		public void addUnit(Unit unit){
			this.unit = unit;
		}

		public void addCity(City city){
			this.city = city;
		}

		public void addImprovement(String improvement){
			this.improvement = improvement;
		}

		public Position getPosition(){
			return placement;
		}

		public Unit getUnit(){
			return unit;
		}

		public City getCity(){
			return city;
		}

		public String getImprovement(){
			return improvement;
		}

		public String toString(){
			return "Tile at position: " + placement + "\nUnit on tile: " + unit + "\nCity on tile: " + city + "\nImprovement on tile: " + improvement + "\n";
		}
	}

	private class Player{
		private String name, civilization;

		public Player(String name, String civilization){
			this.name = name;
			this.civilization = civilization;
		}

		public String getName(){
			return name;
		}

		public String getCiv(){
			return civilization;
		}

		public String toString(){
			return name + "\t" + civilization + "\n";
		}
	}

	// T�nkte l�gga en toString h�r nedan, om man vill se det mesta kommer den bli cp-l�ng.

	public String toString(){
		return "Request accepted: " + ok + "\n" + updatedTiles;
	}
}
