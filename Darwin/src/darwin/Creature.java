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
	 * 
	 * @return the species.
	 */
	public Species species() {
		return species;
	}

	/**
	 * Return the current direction of the creature.
	 * 
	 * @return the direction.
	 */
	public int direction() {
		return dir;
	}

	/**
	 * Sets the current direction of the creature to the given value 
	 * 
	 * @param dir
	 */
	public void setDirection(int dir){
		this.dir = dir;
	}

	/**
	 * Return the position of the creature.
	 * 
	 * @return the creature's position
	 */
	public Position position() {
		return pos;
	}

	/**
	 * changes position to given position.
	 * 
	 * @param pos
	 */
	public void setPosition(Position pos) {
		this.pos = pos;
	}

	/**
	 * Execute steps from the Creature's program
	 *   starting at step #1
	 *   continue until a hop, left, right, or infect instruction is executed.
	 */
	public void takeOneTurn() {

		//grab instruction from program
		Instruction nextInstruction;
		nextInstruction = species.programStep(nextInstructNum);

		//identify the opcode and address of the instruction
		int tempOpcode = nextInstruction.getOpcode();
		int tempAddress = nextInstruction.getAddress();

		Position adjacentSq = pos.getAdjacent(dir);
		String speciesName = species.getName();

		//put the creature on the WorldMap
		world.set(pos, this);
		WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor());

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

			world.set(pos, null);
			WorldMap.displaySquare(pos, ' ', 0, "");

			world.set(pos, this);
			WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor());

			nextInstructNum ++;
			return;

			//RIGHT
		}else if( tempOpcode == 3) {
			setDirection(rightFrom(dir));

			world.set(pos, null);
			WorldMap.displaySquare(pos, ' ', 0, "");

			world.set(pos, this);
			WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor());

			nextInstructNum ++;
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

			}else {
				nextInstructNum++;
				return;
			}


			//IFEMPTY
		}else if( tempOpcode == 5) {

			// if the adjacent square exists and is UNOCCUPIED,
			// update the next instruction to the provided address
			if( world.inRange(adjacentSq) && world.get(adjacentSq) == null) {	
				nextInstructNum = tempAddress;
				this.takeOneTurn();

			}else { 
				//otherwise, proceed to the next sequential instruction
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

			}else { 
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


			}else {
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


			}else {
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

			}else {
				nextInstructNum ++;
				this.takeOneTurn();

			}

			//GO	
		}else if( tempOpcode == 10) {
			nextInstructNum = tempAddress;
			this.takeOneTurn();

			//EXTRA CREDIT: IF2ENEMY
		}else if( tempOpcode == 11) {

			Position adjacentSq2 = adjacentSq.getAdjacent(dir);

			// if the 2nd adjacent square exists and is occupied,
			if( world.inRange(adjacentSq2) && world.get(adjacentSq2) != null) {

				String adj2Name = world.get(adjacentSq2).species().getName();

				// if the 2nd adjacent square is occupied by a creature
				// of a DIFFERENT species, update the next instruction 
				// to the provided address
				if ( !adj2Name.equals(speciesName)) {	
					nextInstructNum = tempAddress;
					this.takeOneTurn();

				}else {
					nextInstructNum ++;
					this.takeOneTurn();
				}


			}else {
				nextInstructNum ++;
				this.takeOneTurn();
			}
		}
	}


	/**
	 * Return the compass direction that is 90 degrees left of the one passed in.
	 * 
	 * @param direction
	 * @return the new direction after turning left once.
	 */
	public static int leftFrom(int direction) {
		return (4 + direction - 1) % 4;
	}

	/**
	 * Return the compass direction the is 90 degrees right of the one passed
	 * in.
	 * 
	 * @param direction
	 * @return the new direction after turning right once.
	 */
	public static int rightFrom(int direction) {
		return (direction + 1) % 4;
	}
	
	public static void main(String[] args) {
		
	}
}
