package game.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import game.engine.dataloader.DataLoader;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.*;

public class Game {
	private Board board;
	private ArrayList<Monster> allMonsters; 
	private Monster player;
	private Monster opponent;
	private Monster current;
	
	public Game(Role playerRole) throws IOException {
        this.board = new Board(DataLoader.readCards());
        this.allMonsters = DataLoader.readMonsters();
        ArrayList<Monster> unique = new ArrayList<>();
        for(Monster m : allMonsters) {
            boolean found = false;
            for(Monster u : unique) {
                if(u.getName().equals(m.getName())) {
                    found = true;
                    break;
                }
            }
            if(!found)
                unique.add(m);
        }
        this.allMonsters=unique;
        this.player = selectRandomMonsterByRole(playerRole);
        this.opponent = selectRandomMonsterByRole(playerRole == Role.SCARER ? Role.LAUGHER : Role.SCARER);
        this.current = player;
        allMonsters.remove(player);
        allMonsters.remove(opponent);
        ArrayList<Monster> stationed = new ArrayList<>(allMonsters);
        stationed.remove(player);
        stationed.remove(opponent);
        board.setStationedMonsters(stationed);

        board.initializeBoard(DataLoader.readCells());
	}
	
	public Board getBoard() {
		return board;
	}
	
	public ArrayList<Monster> getAllMonsters() {
		return allMonsters; 
	}
	
	public Monster getPlayer() {
		return player;
	}
	
	public Monster getOpponent() {
		return opponent;
	}
	
	public Monster getCurrent() {
		return current;
	}
	
	public void setCurrent(Monster current) {
		this.current = current;
	}
	
	private Monster selectRandomMonsterByRole(Role role) {
		Collections.shuffle(allMonsters);
	    return allMonsters.stream()
	    		.filter(m -> m.getRole() == role)
	    		.findFirst()
	    		.orElse(null);
	}
	
	private Monster getCurrentOpponent()
	{
		if(current == player)
			return opponent;
		return player;
	}
	
	private int rollDice()
	{
		return (int)(Math.random()*6)+1;
	}
	
	public void usePowerup() throws OutOfEnergyException
	{
		if(current.getEnergy()<Constants.POWERUP_COST)
			throw new OutOfEnergyException();
		current.alterEnergy(-Constants.POWERUP_COST);
		current.executePowerupEffect(getCurrentOpponent());
	}
	private void switchTurn()
	{
		current = getCurrentOpponent();
	}
	
	public void playTurn() throws InvalidMoveException
	{
		if(current.isFrozen())
			current.setFrozen(false);
		else
		{
			int roll = rollDice();
			board.moveMonster(current,roll,getCurrentOpponent());
		}
		switchTurn();
	}
	
	private boolean checkWinCondition(Monster monster)
	{
		if(monster.getPosition()==Constants.WINNING_POSITION&&monster.getEnergy()>=Constants.WINNING_ENERGY)
			return true;
		return false;
	}
	
	public Monster getWinner()
	{
		if (checkWinCondition(player))
			return player;
		else if(checkWinCondition(opponent))
			return opponent;
		else return null;
	}
	
}