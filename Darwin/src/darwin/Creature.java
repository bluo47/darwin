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
		//System.out.println("Next Instruction:" + nextInstruction);

		//identify the opcode and address of the instruction
		int tempOpcode = nextInstruction.getOpcode();
		//System.out.println("Opcode: " + tempOpcode);
		int tempAddress = nextInstruction.getAddress();
		//System.out.println("Address: " + tempAddress);


		Position adjacentSq = pos.getAdjacent(dir);
		String speciesName = species.getName();

		//put the creature on the WorldMap
		world.set(pos, this);
		WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor());

		
		//boolean hasMoved = false;
		
		//while ( hasMoved) {


			//HOP
			if( tempOpcode == 1) {

				// only moves if the adjacent square exists in the world matrix
				// and is unoccupied
				if( world.inRange(adjacentSq) && (world.get(adjacentSq) == null)) {

					// make our old position null/empty on the WorldMap
					world.set(pos, null);
					WorldMap.displaySquare(pos, ' ', 0, "");	

					// move the creature one square in its current direction
					setPosition(adjacentSq);
				}

				// update our new position on the WorldMap
				world.set(pos, this);
				WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor());

				nextInstructNum ++;

				//end the turn
				return;

				//LEFT	
			}else if( tempOpcode == 2) {
				setDirection(leftFrom(dir));

				// make our old position null/empty
				world.set(pos, null);
				WorldMap.displaySquare(pos, ' ', 0, "");

				// update our new position on the WorldMap
				world.set(pos, this);
				WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor());

				nextInstructNum ++;

				//end the turn
				return;

			//RIGHT
			}else if( tempOpcode == 3) {
				setDirection(rightFrom(dir));

				// make our old position null/empty
				world.set(pos, null);
				WorldMap.displaySquare(pos, ' ', 0, "");

				// update our new position on the WorldMap
				world.set(pos, this);
				WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor());

				nextInstructNum ++;

				//end the turn
				//return;
				return;

			//INFECT
			}else if( tempOpcode == 4) {			

				// if the adjacent square exists and is occupied,
				if( world.inRange(adjacentSq) && world.get(adjacentSq) != null) {

					String adjName = world.get(adjacentSq).species().getName();
					
					// if the adjacent square is occupied by an ENEMY,
					// (not of the same creature) infect the enemy!
					if (!adjName.equals(speciesName)) {

						// create the new creature
						Creature infectedCreature = world.get(adjacentSq);
						infectedCreature.species = species;
						infectedCreature.world = world;
						infectedCreature.pos = adjacentSq;

						// remove the old creature from the World
						world.set(adjacentSq, null);
						WorldMap.displaySquare(adjacentSq, ' ', 0, "");
						
						// put it in the world, adjacent to our original creature
						world.set(adjacentSq, infectedCreature);
						WorldMap.displaySquare(adjacentSq, species.getSpeciesChar(), infectedCreature.dir, species.getColor());

						// begin the newly infected creature at step n of the host's program,
						// or start it at step 1 if no address is given
						if ( tempAddress == 0) {
							infectedCreature.nextInstructNum = 1;
						}else {
							infectedCreature.nextInstructNum = tempAddress;
						}	
						
						// update the creature's next instruction
						nextInstructNum++;
						return;

					}

				}else {  // if there's no enemy in front of us, just return
					
					// update our new position on the WorldMap
					
					// WAIT FOR ANSWER
					
					nextInstructNum++;
					this.takeOneTurn();;
				}


			//IFEMPTY
			}else if( tempOpcode == 5) {

				// if the adjacent square exists and is UNOCCUPIED,
				// update the next instruction to the provided address
				if( world.inRange(adjacentSq) && world.get(adjacentSq) == null) {	
					nextInstructNum = tempAddress;
					this.takeOneTurn();

				}else { //otherwise, proceed to the next sequential instruction
					nextInstructNum ++;
					this.takeOneTurn();
				}

			//IFWALL
			}else if( tempOpcode == 6) {

				// if the adjacent square does not exist, (creature is facing
				// a wall), update the next instruction to the provided address
				if( !world.inRange(adjacentSq)) {	
					nextInstructNum = tempAddress;
					this.takeOneTurn();
					
				}else { //otherwise, proceed to the next sequential instruction
					nextInstructNum ++;
					this.takeOneTurn();
				}

			//IFSAME
			}else if( tempOpcode == 7) {

				// if the adjacent square exists and is occupied,
				if( world.inRange(adjacentSq) && world.get(adjacentSq) != null) {

					String adjName = world.get(adjacentSq).species().getName();

					// if the adjacent square is occupied by a creature
					// of the same species, update the next instruction to the provided address
					if ( adjName.equals(speciesName)) {	
						nextInstructNum = tempAddress;
						this.takeOneTurn();
						
					}else {
						nextInstructNum ++;
						this.takeOneTurn();
					}
					
					
				}else { //otherwise, proceed to the next sequential instruction
					nextInstructNum ++;
					this.takeOneTurn();
				}

			//IFENEMY
			}else if( tempOpcode == 8) {

				// if the adjacent square exists and is occupied,
				if( world.inRange(adjacentSq) && world.get(adjacentSq) != null) {

					String adjName = world.get(adjacentSq).species().getName();

					// if the adjacent square is occupied by a creature
					// of a DIFFERENT species, update the next instruction 
					// to the provided address
					if ( !adjName.equals(speciesName)) {	
						nextInstructNum = tempAddress;
						this.takeOneTurn();
					
					}else {
						nextInstructNum ++;
						this.takeOneTurn();
					}

					
				}else { //otherwise, proceed to the next sequential instruction
					nextInstructNum ++;
					this.takeOneTurn();
				}

			//IFRANDOM, a.k.a. 'free will'
			}else if( tempOpcode == 9) {
				Random rand = new Random();
				int int_random = rand.nextInt(100);

				// if the random number is even, update the next 
				// instruction to the provided address
				if (int_random % 2 == 0) {
					nextInstructNum = tempAddress;
					this.takeOneTurn();
					
				}else { //otherwise, proceed to the next sequential instruction
					nextInstructNum ++;
					this.takeOneTurn();
					
				}

			//GO	
			} else if (tempOpcode == 10) {
				nextInstructNum = tempAddress;
				this.takeOneTurn();
			}
			
			
		//}

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


	/*
	public static void main (String[] args) {

		//WorldMap wmap;
		//wmap.createWorldMap(10,10);


		try {


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

	/*
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			System.err.println(e.getMessage());
		}*/




}
