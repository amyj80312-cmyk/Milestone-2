package game.engine;

import java.util.ArrayList;
import java.util.Collections;

import game.engine.cards.Card;
import game.engine.cells.*;
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
	
	private void setCardsByRarity(){
		ArrayList<Card> expanded = new ArrayList<Card>();
		for(int i =0;i<cards.size();i++){
			Card c = cards.get(i);
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
	private void updateMonsterPositions(Monster player, Monster opponent){
		for(int i =0;i<boardCells.length;i++){
			for (int j=0;j<boardCells[i].length;j++){
				boardCells[i][j].setMonster(null);
			}
			getCell(player.getPosition()).setMonster(player);
			getCell(opponent.getPosition()).setMonster(opponent);
		}
	}

	

}
