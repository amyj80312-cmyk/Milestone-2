package game.engine.cells;

import java.util.ArrayList;

import game.engine.Board;
import game.engine.Role;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class DoorCell extends Cell implements CanisterModifier {
    private Role role;
    private int energy;
    private boolean activated;

    public DoorCell(String name, Role role, int energy) {
        super(name);
        this.role = role;
        this.energy = energy;
        this.activated = false;
    }

    public Role getRole() {
    	return role;
    	}
    public int getEnergy() { 
    	return energy; 
    	}
    public boolean isActivated() { 
    	return activated;
    	}
    public void setActivated(boolean isActivated) {
    	this.activated = isActivated;
    	}

  
    public void modifyCanisterEnergy(Monster monster, int canisterValue) {
    	 if(monster.getRole() == this.role) {
    		 monster.alterEnergy(canisterValue);
    	    }
    	 else {
    		 monster.alterEnergy(-canisterValue);  
    	    }
    	}
    

    public void onLand(Monster landingMonster, Monster opponentMonster) {
        super.onLand(landingMonster, opponentMonster);
        if(activated)
        	return;
        
        boolean wasShielded = landingMonster.isShielded();
        boolean isNegativeChange = landingMonster.getRole() != this.role; 
        modifyCanisterEnergy(landingMonster, energy);
        if(!(wasShielded && isNegativeChange)) {
            for(Monster m : Board.getStationedMonsters()) {
                if(m.getRole() == landingMonster.getRole())
                    modifyCanisterEnergy(m, energy);
            }
            setActivated(true);
        }
    }
        
}

