package game.engine.cells;

import game.engine.monsters.Monster;

public class ConveyorBelt extends TransportCell {

	public ConveyorBelt(String name, int effect) {
		super(name, effect);
	}
	
	public void transport(Monster monster)
	{
		monster.move(getEffect());
	}
	//doesnt need to override it can just take from super
}
