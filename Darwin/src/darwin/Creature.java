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
		System.out.println("Next Instruction:" + nextInstruction);

		//identify the opcode and address of the instruction
		int tempOpcode = nextInstruction.getOpcode();
		System.out.println("Opcode: " + tempOpcode);
		int tempAddress = nextInstruction.getAddress();
		System.out.println("Address: " + tempAddress);


		Position adjacentSq = pos.getAdjacent(dir);


		//HOP
		if( tempOpcode == 1) {

			// only moves if the adjacent square exists in the world matrix
			// and is unoccupied
			if( world.inRange(adjacentSq)) {
				if( world.get(adjacentSq) == null) {

					// move the creature one square in its current direction
					setPosition(adjacentSq);
				}
				nextInstructNum ++;
			}


		//LEFT	
		}else if( tempOpcode == 2) {
			setDirection(leftFrom(dir));
			nextInstructNum ++;


		//RIGHT
		}else if( tempOpcode == 3) {
			setDirection(rightFrom(dir));
			nextInstructNum ++;

		//INFECT
		}else if( tempOpcode == 4) {			

			// if the adjacent square exists and is occupied,
			if( world.inRange(adjacentSq) && world.get(adjacentSq) != null) {

				// if the adjacent square is occupied by an enemy,
				// (not of the same creature) infect the enemy!
				if ( world.get(adjacentSq).species() != species) {

					// create the new creature and put it in the world, adjacent to our creature
					Creature infectedCreature = world.get(adjacentSq);
					infectedCreature.species = species;
					infectedCreature.world = world;
					infectedCreature.pos = adjacentSq;
					infectedCreature.dir = dir;

					world.set(adjacentSq, infectedCreature);

					// begin the newly infected creature at step n of the host's program
					infectedCreature.nextInstructNum = tempAddress;

				}

			}else {

				// if the address is missing, assign it as 1
				if ( tempAddress == 0) {
					nextInstructNum = 1;
				}else {
					nextInstructNum = tempAddress;
				}				
			}
		//IFEMPTY
		}else if( tempOpcode == 5) {
			
			// if the adjacent square exists and is UNOCCUPIED,
			// update the next instruction to the provided address
			if( world.inRange(adjacentSq) && world.get(adjacentSq) == null) {	
				nextInstructNum = tempAddress;
				
			}else { //otherwise, proceed to the next sequential instruction
				nextInstructNum ++;
			}
			
		//IFWALL
		}else if( tempOpcode == 6) {
			
			// if the adjacent square does not exist, (creature is facing
			// a wall), update the next instruction to the provided address
			if( !world.inRange(adjacentSq)) {	
				nextInstructNum = tempAddress;
			}else { //otherwise, proceed to the next sequential instruction
				nextInstructNum ++;
			}
			
		//IFSAME
		}else if( tempOpcode == 7) {
		
			// if the adjacent square exists and is occupied,
			if( world.inRange(adjacentSq) && world.get(adjacentSq) != null) {

				// if the adjacent square is occupied by a creature
				// of the same species, update the next instruction to the provided address
				if ( world.get(adjacentSq).species() == species) {	
					nextInstructNum = tempAddress;
				}
			}else {//otherwise, proceed to the next sequential instruction
				nextInstructNum ++;
			}
			
		//IFENEMY
		}else if( tempOpcode == 8) {
			
			// if the adjacent square exists and is occupied,
			if( world.inRange(adjacentSq) && world.get(adjacentSq) != null) {

				// if the adjacent square is occupied by a creature
				// of a DIFFERENT species, update the next instruction 
				// to the provided address
				if ( world.get(adjacentSq).species() != species) {	
					nextInstructNum = tempAddress;
				}
			}else { //otherwise, proceed to the next sequential instruction
				nextInstructNum ++;			
			}
			
		//IFRANDOM, a.k.a. 'free will'
		}else if( tempOpcode == 9) {
			Random rand = new Random();
			int int_random = rand.nextInt(100);
			
			// if the random number is even, update the next 
			// instruction to the provided address
			if (int_random % 2 == 0) {
				nextInstructNum = tempAddress;
			
			}else { //otherwise, proceed to the next sequential instruction
				nextInstructNum ++;
			}

		//GO	
		}else {
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

			// testing infect n in the CASE WITH NO ENEMY
			BufferedReader in = new BufferedReader(new FileReader("Creatures/InfectTest.txt"));
			Species infectTestWithEnemy =  new Species(in);

			BufferedReader in2 = new BufferedReader(new FileReader("Creatures/Food.txt"));
			Species enemy =  new Species(in2);


			World w3 = new World (5,5);
			Position pos3 = new Position(2,2);
			int dir3 = 2;

			Creature c3 = new Creature(infectTestWithEnemy, w3, pos3, dir3);

			//enemy is planted in (2,3)
			//Creature enemy3 = new Creature(enemy, w3, pos3.getAdjacent(dir3), dir3);
			//w3.set(pos3.getAdjacent(dir3), enemy3);

			System.out.println("Infector Species:" + c3.species());
			//System.out.println("Enemy Species: " + enemy3.species());

			System.out.println("starting position & direction:");

			System.out.println(c3.pos); //(2,2)
			System.out.println(c3.dir); //2

			c3.takeOneTurn();  	//hop

			System.out.println(c3.pos); //
			System.out.println(c3.dir); //

			//System.out.println("Instruction: infect");
			c3.takeOneTurn(); 

			System.out.println(c3.pos); //
			System.out.println(c3.dir); //

			//System.out.println("Enemy's 'new' Species: " + enemy3.species());

			//System.out.println("Instruction: hop");
			c3.takeOneTurn(); 

			System.out.println(c3.pos); //
			System.out.println(c3.dir); //

			//System.out.println("Instruction: infect");
			c3.takeOneTurn();  

			System.out.println(c3.pos); //
			System.out.println(c3.dir); //	

			//System.out.println("Instruction: hop");
			c3.takeOneTurn();  

			System.out.println(c3.pos); //
			System.out.println(c3.dir); //				

			/*
			// testing infect n in the CASE WITH NO ENEMY
			BufferedReader in = new BufferedReader(new FileReader("Creatures/InfectTest.txt"));
			Species infectTest =  new Species(in);

			World w3 = new World (5,5);
			Position pos3 = new Position(2,2);
			int dir3 = 2;

			Creature c3 = new Creature(infectTest, w3, pos3, dir3);

			System.out.println("starting position & direction:");

			System.out.println(c3.pos); //(2,2)
			System.out.println(c3.dir); //2

			c3.takeOneTurn();  	//hop

			System.out.println(c3.pos); //
			System.out.println(c3.dir); //

			//System.out.println("Instruction: right");
			c3.takeOneTurn(); 

			System.out.println(c3.pos); //
			System.out.println(c3.dir); //

			//System.out.println("Instruction: infect 4");
			c3.takeOneTurn(); 

			System.out.println(c3.pos); //
			System.out.println(c3.dir); //

			//System.out.println("Instruction: right");
			c3.takeOneTurn();  

			System.out.println(c3.pos); //
			System.out.println(c3.dir); //	

			//System.out.println("Instruction: hop");
			c3.takeOneTurn();  

			System.out.println(c3.pos); //
			System.out.println(c3.dir); //	
			 */


			/*
		// testing with enemy in the CASE WITH NO ENEMY
		BufferedReader in = new BufferedReader(new FileReader("Creatures/IfEnemyTest.txt"));
		Species ifEnemyTest =  new Species(in);

		World w3 = new World (4,5);
		Position pos3 = new Position(2,3);
		int dir3 = 2;

		Creature c3 = new Creature(ifEnemyTest, w3, pos3, dir3);

		System.out.println("starting position & direction:");

		System.out.println(c3.pos); //(2,3)
		System.out.println(c3.dir); //2

		System.out.println("Instruction: ifenemy 3");
		c3.takeOneTurn(); //hop 	

		System.out.println(c3.pos); //(2,3)
		System.out.println(c3.dir); //2

		System.out.println("Instruction: hop");
		c3.takeOneTurn(); //hop 	

		System.out.println(c3.pos); //(2,4)
		System.out.println(c3.dir); //2

		System.out.println("Instruction: right");
		c3.takeOneTurn(); //hop 	

		System.out.println(c3.pos); //(2,4)
		System.out.println(c3.dir); //3

		System.out.println("Instruction: hop");
		c3.takeOneTurn(); //hop 

		System.out.println(c3.pos); //(1,4)
		System.out.println(c3.dir); //2
		/*


		/*		// testing with enemy in the CASE WITH AN ENEMY PRESENT
		BufferedReader in = new BufferedReader(new FileReader("Creatures/IfEnemyTest.txt"));
		Species ifEnemyTest =  new Species(in);

		World w3 = new World (4,5);
		Position pos3 = new Position(2,3);
		int dir3 = 2;

		Creature c3 = new Creature(ifEnemyTest, w3, pos3, dir3);

		System.out.println(c3.pos); //(2,3)
		System.out.println(c3.dir); //2

		System.out.println("hop");
		c3.takeOneTurn(); //hop 	

		System.out.println(c3.pos); //(3,3)
		System.out.println(c3.dir); //2

		System.out.println(c3.pos); //(2,1)
		System.out.println(c3.dir); //2
		/*




		/*
		BufferedReader in = new BufferedReader(new FileReader("Creatures/Test1.txt"));
		Species test1 =  new Species(in);

		World w2 = new World (10,10);
		Position pos2 = new Position(6,8);
		int dir2 = 1;

		Creature c2 = new Creature(test1, w2, pos2, dir2);

		//System.out.println(pos2.getAdjacent(dir2));
		//System.out.println(w2.inRange(pos2.getAdjacent(dir2)));
		//System.out.println(w2.get(pos2.getAdjacent(dir2)));	

		// Checking that the position doesn't change if we're at a boundary or moving
		// into another creature

		System.out.println(c2.pos); //(6,8)
		System.out.println(c2.dir); //1


		System.out.println("hop");
		c2.takeOneTurn(); //hop 

		System.out.println(c2.pos); //(7, 8)
		System.out.println(c2.dir); //1

		System.out.println("hop");
		c2.takeOneTurn(); //hop 

		System.out.println(c2.pos); //(7,8)
		System.out.println(c2.dir); // 0

		System.out.println("hop");
		c2.takeOneTurn(); //hop

		System.out.println(c2.pos); //(7, 7)
		System.out.println(c2.dir); // 0

		System.out.println("hop");
		c2.takeOneTurn(); //hop 

		System.out.println(c2.pos); //(7, 7)
		System.out.println(c2.dir); // 1
		/*



		/*
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
			//System.out.println("hop");

			System.out.println(c1.pos); //(3,1)
			System.out.println(c1.dir); //0

			c1.takeOneTurn(); //go 1
			//System.out.println("go 1");

			System.out.println(c1.pos); //(3,1)
			System.out.println(c1.dir); //0

			c1.takeOneTurn(); //hop
			//System.out.println("hop (line 1), again");

			System.out.println(c1.pos); //(3,0)
			System.out.println(c1.dir); //0
			 */



		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			System.err.println(e.getMessage());
		}



	}
}
