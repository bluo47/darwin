package darwin;

import java.util.*;

/**
 * This class represents one creature on the board. Each creature must remember
 * its species, position, direction, and the world in which it is living.
 * In addition, the Creature must remember the next instruction out of its
 * program to execute.
 * The creature is also responsible for making itself appear in the WorldMap. In
 * fact, you should only update the WorldMap from inside the Creature class.
 */

public class Creature {
	
	private Species species;
	private World world;
	private Position pos;
	private int dir;
	private int nextInstructNum;

	/**
	 * Create a creature of the given species, with the indicated position and
	 * direction. Note that we also pass in the world-- remember this world, so
	 * that you can check what is in front of the creature and update the board
	 * when the creature moves.
	 */
	public Creature(Species species, World world, Position pos, int dir) {
		this.species = species;
		this.world = world;
		this.pos = pos;
		this.dir = dir;
		//set the creature's next instruction as the species' program's first instruction
		nextInstructNum = 1;
		
	}

	/**
	 * Return the species of the creature.
	 */
	public Species species() {
		return species;
	}

	/**
	 * Return the current direction of the creature.
	 */
	public int direction() {
		return dir;
	}

	/**
	 * Sets the current direction of the creature to the given value 
	 */
	public void setDirection(int dir){
		this.dir = dir;
	}

	/**
	 * Return the position of the creature.
	 */
	public Position position() {
		return pos;
	}

	/**
	 * Execute steps from the Creature's program
	 *   starting at step #1
	 *   continue until a hop, left, right, or infect instruction is executed.
	 */
	public void takeOneTurn() {
		Instruction nextInstruction;
		nextInstruction = species.programStep(nextInstructNum);
		
		int tempAddress = nextInstruction.getAddress();
		int tempOpcode = nextInstruction.getOpcode();
		
		//hop
		if( tempOpcode == 1) {
			//north
			if (dir == 0) {
				// update position
				pos.
			
			//south
			}else if (dir == 1) {
			
			//east	
			}else if (dir == 2) {
			
				
			//west
			}else {
				
			}
			
		//left	
		}else if( tempOpcode == 2) {
			
		
			
		//right
		}else if( tempOpcode == 3) {
		
			
		//infect
		}else if (tempOpcode == 4) {
		}
		
		
		//update the program index to the next instruction
		nextInstructNum = nextInstructNum + 1;
	}

	/**
	 * Return the compass direction the is 90 degrees left of the one passed in.
	 */
	public static int leftFrom(int direction) {
		return (4 + direction - 1) % 4;
	}

	/**
	 * Return the compass direction the is 90 degrees right of the one passed
	 * in.
	 */
	public static int rightFrom(int direction) {
		return (direction + 1) % 4;
	}
}
