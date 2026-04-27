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
	
	public void modifyCanisterEnergy(Monster monster, int canisterValue)
	{
		monster.alterEnergy(canisterValue);
	}
	public void onLand(Monster landingMonster, Monster opponentMonster)
	{
		super.onLand(landingMonster, opponentMonster);
		if(isActivated())
			return;
		setActivated(true);
		ArrayList <Monster> stationaryMonsters = Board.getStationedMonsters();
		int size = stationaryMonsters.size();
		if(landingMonster.getRole()==role)
		{
			modifyCanisterEnergy(landingMonster,energy);
			for(int i=0;i<size;i++)
			{
				if(stationaryMonsters.get(i).getRole()==role)
				{
					modifyCanisterEnergy(stationaryMonsters.get(i),energy);
				}
			}
		}
		else
		{
			modifyCanisterEnergy(landingMonster,-energy);
			for(int i=0;i<size;i++)
			{
				if(stationaryMonsters.get(i).getRole()==landingMonster.getRole())
				{
					modifyCanisterEnergy(stationaryMonsters.get(i),-energy);
				}
			}

		}
	}

}
