package darwin;

/**
 * This class includes the functions necessary to keep track of the creatures in
 * a two-dimensional world. 
 */

public class World {
	private Matrix<Creature> world;
	private int width, height;
	/**
	 * This function creates a new world consisting of width columns and height
	 * rows, each of which is numbered beginning at 0. A newly created world
	 * contains no objects.
	 */
	public World(int w, int h) {
		// BE CAREFUL: think about how width/heights translates to row/col in a matrix
		width = w;
		height = h;
		
		world = new Matrix<Creature>(height, width);
		
		
	}

	/**
	 * Returns the height of the world.
	 */
	public int height() {
		return height; // FIX
	}

	/**
	 * Returns the width of the world.
	 */
	public int width() {
		return width; // FIX
	}

	/**
	 * Returns whether pos is in the world or not.
	 * 
	 * returns true *if* pos is an (x,y) location within the bounds of the board.
	 */
	public boolean inRange(Position pos) {
		for (int i = 0; i < height; i++) {
			if (i == pos.getY()) {
				for (int j = 0; j < width; j++) {
					if (j == pos.getX()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Set a position on the board to contain e.
	 * 
	 * @throws IllegalArgumentException if pos is not in range
	 */
	public void set(Position pos, Creature e) {
		if (!inRange(pos)) {
			throw new IllegalArgumentException("pos is not in range.");
		} else {
			world.set(pos.getY(), pos.getX(), e);
		}
	}

	/**
	 * Return the contents of a position on the board.
	 * 
	 * @throws IllegalArgumentException if pos is not in range
	 */
	public Creature get(Position pos) {
		// BE CAREFUL: think about how x,y translates to row/col in a matrix
		if (!inRange(pos)) {
			throw new IllegalArgumentException("pos is not in range.");
		} else {
			return world.get(pos.getY(), pos.getX());
		}
	}


	public static void main(String args[]) {
		World w = new World(5, 6);
		//System.out.println(w.height());
		//System.out.println(w.width());
		
		World w2 = new World(5,3);
		Position p2 = new Position(4,2);
		System.out.println(String.valueOf(w2.inRange(p2))); //true
		
		Position goodp = new Position(1,2);
		Position badp = new Position(6,5);
		
		System.out.println(String.valueOf(w.inRange(goodp))); //true
		System.out.println(String.valueOf(w.inRange(badp))); //false
		
		
		
		
		
	}
	}
