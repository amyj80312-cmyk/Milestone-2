package game.engine;

import java.util.ArrayList;
import java.util.Collections;

import game.engine.cards.Card;
import game.engine.cells.*;
import game.engine.exceptions.InvalidMoveException;
import game.engine.monsters.Monster;

public class Board {
	private Cell[][] boardCells;
	private static ArrayList<Monster> stationedMonsters; 
	private static ArrayList<Card> originalCards;
	public static ArrayList<Card> cards;
	
	public Board(ArrayList<Card> readCards) {
		this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
		stationedMonsters = new ArrayList<Monster>();
		originalCards = readCards;
		cards = new ArrayList<Card>();
		setCardsByRarity();
		reloadCards();
	}
	
	public Cell[][] getBoardCells() {
		return boardCells;
	}
	
	public static ArrayList<Monster> getStationedMonsters() {
		return stationedMonsters;
	}
	
	public static void setStationedMonsters(ArrayList<Monster> stationedMonsters) {
		Board.stationedMonsters = stationedMonsters;
	}

	public static ArrayList<Card> getOriginalCards() {
		return originalCards;
	}
	
	public static ArrayList<Card> getCards() {
		return cards;
	}
	
	public static void setCards(ArrayList<Card> cards) {
		Board.cards = cards;
	}
	private int[] indexToRowCol(int index){
		int row = index/ Constants.BOARD_COLS;
		int col = index % Constants.BOARD_COLS;
		if (row%2==1){
			col = (Constants.BOARD_COLS-1)-col;
			}
		int [] point = new int [] {row,col};
		return point;
		}
	private Cell getCell(int index){
		int [] point = indexToRowCol(index);
		return boardCells[point[0]][point[1]];
	}
	private void setCell(int index, Cell cell) {
	    int[] point = indexToRowCol(index);
	    boardCells[point[0]][point[1]] = cell;
	}
	public void initializeBoard(ArrayList<Cell> specialCells){
		for(int i = 0 ; i< Constants.BOARD_SIZE;i+=2){
				setCell(i,new Cell("Normal Cell"));}
		
		int doorsindex = 1;
		for(int i=0;i<specialCells.size();i++){
				Cell x = specialCells.get(i);
				if (x instanceof DoorCell){
				setCell(doorsindex,x);
				doorsindex+=2;}}
		
		int conveyorIndex = 0;
		int sockIndex = 0;
		for(int i=0;i<specialCells.size();i++){
			Cell x = specialCells.get(i); 
			if (x instanceof ConveyorBelt){
				setCell(Constants.CONVEYOR_CELL_INDICES[conveyorIndex++],x);
			}
			else if(x instanceof ContaminationSock){
				setCell(Constants.SOCK_CELL_INDICES[sockIndex++],x);
				}
			}
		for(int i=0;i<Constants.CARD_CELL_INDICES.length;i++){
			setCell(Constants.CARD_CELL_INDICES[i],new CardCell("Card cell"));
			}
		for(int i=0;i<Constants.MONSTER_CELL_INDICES.length;i++){
			if (i<stationedMonsters.size()&&stationedMonsters.get(i)!= null){
			stationedMonsters.get(i).setPosition(Constants.MONSTER_CELL_INDICES[i]);
			String name = stationedMonsters.get(i).getName();
			setCell(Constants.MONSTER_CELL_INDICES[i],new MonsterCell(name,stationedMonsters.get(i)));}
			else {
				setCell(Constants.MONSTER_CELL_INDICES[i],new MonsterCell("Monster Cell",null));
				}
			}
		}
	
		
	private void setCardsByRarity(){
		ArrayList<Card> expanded = new ArrayList<Card>();
		for(int i =0;i<originalCards.size();i++){
			Card c = originalCards.get(i);
			for(int j =0;j<c.getRarity();j++){
				expanded.add(c);
			}
		}
		 originalCards.clear();
		 originalCards.addAll(expanded);
		}
	public static void reloadCards(){
		ArrayList<Card> deck = new ArrayList<Card>(originalCards);
		Collections.shuffle(deck);
		cards = deck;
	}
	public static Card drawCard(){
		if(cards.isEmpty()){
			reloadCards();
		}
		return cards.remove(0);
	}
	
	public void moveMonster(Monster currentMonster, int roll, Monster opponentMonster)throws InvalidMoveException{
		int oldPosition = currentMonster.getPosition();
		currentMonster.move(roll);
		Cell landedcell = getCell(currentMonster.getPosition());
		landedcell.onLand(currentMonster,opponentMonster);
		if(currentMonster.getPosition()==opponentMonster.getPosition()){
			currentMonster.setPosition(oldPosition);
			updateMonsterPositions(currentMonster,opponentMonster);
			throw new InvalidMoveException();
			}
		if(currentMonster.isConfused()){
			currentMonster.decrementConfusion();
		if(opponentMonster.isConfused()){
			opponentMonster.decrementConfusion();
		}}
		updateMonsterPositions(currentMonster,opponentMonster);
		
	}
	
	
	
	private void updateMonsterPositions(Monster player, Monster opponent){
		for(int i =0;i<boardCells.length;i++){
			for (int j=0;j<boardCells[i].length;j++){
				boardCells[i][j].setMonster(null);
			}
		}
		getCell(player.getPosition()).setMonster(player);
		getCell(opponent.getPosition()).setMonster(opponent);
	}
	

	

}
