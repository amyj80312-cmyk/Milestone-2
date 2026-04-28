package game.engine.cells;

import game.engine.Role;
import game.engine.monsters.*;

public class MonsterCell extends Cell {
	private Monster cellMonster;

	public MonsterCell(String name, Monster cellMonster) {
		super(name);
		this.cellMonster = cellMonster;
	}

	public Monster getCellMonster() {
		return cellMonster;
	}
	
	public void onLand(Monster landingMonster,Monster opponentMonster)
	{
		super.onLand(landingMonster, opponentMonster);
		Role landingRole = landingMonster.getRole();
		Role cellRole = cellMonster.getRole();
		if(landingRole == cellRole)
		{
			landingMonster.executePowerupEffect(opponentMonster);
		}
		else
		{
			int cellMonsterEnergy = cellMonster.getEnergy();
			int landingMonsterEnergy = landingMonster.getEnergy();
			if(landingMonster.getEnergy()>cellMonster.getEnergy())
			{
				cellMonster.setEnergy(landingMonsterEnergy);
				landingMonster.alterEnergy(cellMonsterEnergy-landingMonsterEnergy);
			}
		}
	}

}
