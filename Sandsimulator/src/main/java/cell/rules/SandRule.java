package cell.rules;

public class SandRule implements CellRule{

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
		return -1;
	}

	@Override
	public float moveChance() {
		// TODO Auto-generated method stub
		return 45f;
	}

	@Override
	public boolean moveDiagonally() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean moveHorizontally() {
		// TODO Auto-generated method stub
		return false;
	}

	

}
