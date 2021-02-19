package darwin;

import java.io.*;
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
	
	public void setPosition(Position pos) {
		this.pos = pos;
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
		System.out.println(tempOpcode);
		
		
		int pos_x = pos.getX();
		int pos_y = pos.getY();

		
		//hop
		if( tempOpcode == 1) {

			// why doesn't this actually update the position when
			// called in the main function??
			
			setPosition(pos.getAdjacent(dir));
			
			
		//left	
		}else if( tempOpcode == 2) {
			setDirection(leftFrom(dir));

			
		//right
		}else if( tempOpcode == 3) {
			setDirection(rightFrom(dir));
			
		
		//go	
		}else if ( tempOpcode == 10) {
			
			for (int i = 0; i < tempAddress; i ++) {
				pos.getAdjacent(dir);
			}	
		}	
		
		
		//update the program index to the next instruction
		nextInstructNum = nextInstructNum + 1;
	}

	/**
	 * Return the compass direction that is 90 degrees left of the one passed in.
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
	
	public static void main (String[] args) {

		try {
		BufferedReader in = new BufferedReader(new FileReader("Creatures/Hop.txt"));
		Species hop =  new Species(in);
		
		World w = new World (5, 5);
		Position pos = new Position(3, 2);
		int dir = 0;
		
		Creature c1 = new Creature(hop, w, pos, dir);
		
		System.out.println(pos); //(3,2)
		System.out.println(dir); //0
		
		c1.takeOneTurn();
		
		System.out.println(c1.pos); //(3,1)
		System.out.println(dir); //0
		
		System.out.println(rightFrom(dir)); //1
		
		
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			System.err.println(e.getMessage());
		}

		
		
	}
}
