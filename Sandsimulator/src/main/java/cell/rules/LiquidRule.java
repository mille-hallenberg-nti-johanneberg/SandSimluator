package cell.rules;

//This is just a rule set for the general liquid.
public class LiquidRule implements CellRule{
	
	@Override
	public boolean isStatic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int gravity() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public int spreadFactor() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public float moveChance() {
		// TODO Auto-generated method stub
		return 100;
	}

	@Override
	public boolean moveDiagonally() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean moveHorizontally() {
		// TODO Auto-generated method stub
		return true;
	}
}
