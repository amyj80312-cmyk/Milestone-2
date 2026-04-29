package game.engine.monsters;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Role;

public class Schemer extends Monster {
	
	public Schemer(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}
	 private int stealEnergyFrom(Monster target){
		 int Stolen=0;
		 if(target.getEnergy() < Constants.SCHEMER_STEAL){
			 Stolen = target.getEnergy();
		 }
		 else {
			 Stolen = Constants.SCHEMER_STEAL;
		 }
		 target.setEnergy(target.getEnergy() - Stolen);
		 return Stolen;
	 }
	@Override
	public void executePowerupEffect(Monster opponentMonster){
		int Total = 0;
		Total += stealEnergyFrom(opponentMonster);
		for (int i =0; i<Board.getStationedMonsters().size();i++){
		    Monster m = Board.getStationedMonsters().get(i);
		    Total += stealEnergyFrom(m);
		}
		setEnergy(getEnergy()+Total);
		}
	public void setEnergy(int energy){
		int energychange = energy - this.getEnergy();
		super.setEnergy(this.getEnergy()+energychange + Constants.SCHEMER_STEAL);
	}

		
	}
