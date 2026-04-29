package game.engine.monsters;

import game.engine.Role;

public class Dynamo extends Monster {
	
	public Dynamo(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}
	public void setEnergy(int energy){
		int energychange = energy - this.getEnergy();
		int doubled = energychange*2;
		super.setEnergy(this.getEnergy()+doubled);
	}

	@Override
	public void executePowerupEffect(Monster opponentMonster) {
		opponentMonster.setFrozen(true);
	}
		
	}
