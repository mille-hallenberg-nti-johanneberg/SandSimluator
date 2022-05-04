package cell.rules;

public interface CellRule {
	//Each Cell follows rules based on what CellType it is
	
	boolean isStatic(); 				//If the cell is static, then the updater will simply ignore moving.
	int gravity();						//Gravity 
	int spreadFactor();					//For liquids, how far the liquid can move horizontally.
	float moveChance();					//How big the chance for moving each frame is.
	
	public boolean moveDiagonally();	//Can the cell move diagonally? (Behavior that solids/powders use)
	public boolean moveHorizontally();	//Can the cell move horizontally? (Behavior that liquids use)
}