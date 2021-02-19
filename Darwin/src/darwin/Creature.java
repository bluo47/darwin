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
		
		//identify the address and opcode of the instruction
		int tempAddress = nextInstruction.getAddress();
		int tempOpcode = nextInstruction.getOpcode();

		
		//hop
		if( tempOpcode == 1) {
			// only if the adjacent square exists in the world matrix,
			if(world.inRange(pos.getAdjacent(dir))) {
				// move the creature one square in its current direction
				setPosition(pos.getAdjacent(dir));
				nextInstructNum ++;
			}
			
			
		//left	
		}else if( tempOpcode == 2) {
			setDirection(leftFrom(dir));
			nextInstructNum ++;

			
		//right
		}else if( tempOpcode == 3) {
			setDirection(rightFrom(dir));
			nextInstructNum ++;
			
		//infect n
		}else if( tempOpcode == 4) {			
			if ( world.get(pos.getAdjacent(dir)) != null) {
				
			}
			nextInstructNum = tempAddress;
	
			
		
		//go	
		}else if ( tempOpcode == 10) {
			nextInstructNum = tempAddress;
			
			}	
		}	
		
		
		//update the program index to the next instruction
		
	

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
		
		BufferedReader in = new BufferedReader(new FileReader("Creatures/IfEnemyTest.txt"));
		Species ifEnemyTest =  new Species(in);
			
		World w3 = new World (4,5);
		Position pos3 = new Position(6,7);
		int dir3 = 2;
				
		Creature c3 = new Creature(ifEnemyTest, w3, pos3, dir3);
				
		System.out.println(c3.pos); //(2,1)
		System.out.println(c3.dir); //2
				
		System.out.println("hop");
		c3.takeOneTurn(); //hop 	
		
			
			
			
			
		

		/*
		BufferedReader in = new BufferedReader(new FileReader("Creatures/Test1.txt"));
		Species test1 =  new Species(in);
		
		World w2 = new World (9,8);
		Position pos2 = new Position(6,8);
		int dir2 = 1;
			
		Creature c2 = new Creature(test1, w2, pos2, dir2);
			
		System.out.println(c2.pos); //(6,8)
		System.out.println(c2.dir); //1
			
		System.out.println("hop");
		c2.takeOneTurn(); //hop 
		
		System.out.println(c2.pos); //(7, 8)
		System.out.println(c2.dir); //1
		
		System.out.println("turn left");
		c2.takeOneTurn(); //left
		
		System.out.println(c2.pos); //(7,8)
		System.out.println(c2.dir); // 0
		
		System.out.println("hop");
		c2.takeOneTurn(); //hop
		
		System.out.println(c2.pos); //(7, 7)
		System.out.println(c2.dir); // 0
		
		System.out.println("turn right");
		c2.takeOneTurn(); //right
		
		System.out.println(c2.pos); //(7, 7)
		System.out.println(c2.dir); // 1
		
		System.out.println("hop");
		c2.takeOneTurn(); //hop
		
		System.out.println(c2.pos); //(8,7)
		System.out.println(c2.dir); // 1
		
		System.out.println("left");
		c2.takeOneTurn(); //left
		
		System.out.println(c2.pos); //(8,7)
		System.out.println(c2.dir); // 0
		
		System.out.println("go 3");
		c2.takeOneTurn(); //go 3
		
		System.out.println(c2.pos); //(8,7)
		System.out.println(c2.dir); // 0
		
		//should now be at line 3, which is a hop
		c2.takeOneTurn(); //hop
		System.out.println("hop");
		
		System.out.println(c2.pos); //(8,6)
		System.out.println(c2.dir); //0
		
		//next instruction should be line 4's, which is a right turn
		c2.takeOneTurn(); //right
		System.out.println("right");
		
		System.out.println(c2.pos); //(8,6)
		System.out.println(c2.dir); //1
		*/
		
		
		
			
			
		/*
		BufferedReader in = new BufferedReader(new FileReader("Creatures/Hop.txt"));
		Species hop =  new Species(in);
		
		World w1 = new World (5, 5);
		Position pos1 = new Position(3, 2);
		int dir1 = 0;
		
		Creature c1 = new Creature(hop, w1, pos1, dir1);
		
		System.out.println(c1.pos); //(3,2)
		System.out.println(c1.dir); //0
		
		c1.takeOneTurn(); //hop
		System.out.println("hop");
		
		System.out.println(c1.pos); //(3,1)
		System.out.println(c1.dir); //0
		
		c1.takeOneTurn(); //go 1
		System.out.println("go 1");
		
		System.out.println(c1.pos); //(3,1)
		System.out.println(c1.dir); //0
		
		c1.takeOneTurn(); //hop
		System.out.println("hop (line 1), again");
		
		System.out.println(c1.pos); //(3,0)
		System.out.println(c1.dir); //0
		
		*/
		
		
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			System.err.println(e.getMessage());
		}

		
		
	}
}
