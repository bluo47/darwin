package darwin;

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

/**
 * This class controls the simulation. The design is entirely up to you. You
 * should include a main method that takes the array of species file names
 * passed in and populates a world with species of each type. You class should
 * be able to support anywhere between 1 to 4 species.
 * 
 * Be sure to call the WorldMap.pause() method every time through the main
 * simulation loop or else the simulation will be too fast. For example:
 * 
 * 
 * public void simulate() { 
 * 	for (int rounds = 0; rounds < numRounds; rounds++) {
 * 		giveEachCreatureOneTurn(); 
 * 		WorldMap.pause(500); 
 * 	} 
 * }
 * 
 */
class Darwin {
	private World world;
	private static int rows; 
	private static int cols;
	ArrayList<Creature> CreatureArray;
	private static int numRounds = 30;


	/**
	 * Takes command line input and adds them to the Darwin stimulation.
	 * 
	 * @param speciesFilenames
	 */
	public Darwin(String[] speciesFilenames) {

		rows = 15;
		cols = 15;



		//initialize world with constructor
		world = new World(rows, cols);

		CreatureArray = new ArrayList<Creature>();

		//populate world
		for( int i = 0; i < speciesFilenames.length; i++) {
			
			for( int j = 0; j < 10; j++) {

				try {
					
					String formattedSpeciesFilename = "./Creatures/" + speciesFilenames[i];
					
					BufferedReader in = new BufferedReader(new FileReader(formattedSpeciesFilename));
					Species speciesTemp =  new Species(in);

					boolean isNull = true;
					Random rand = new Random();
					Creature c;

					// run this until the Creature is in an unoccupied position
					// on the board
					while( isNull) {
						// create a random position on the board
						int x = rand.nextInt(rows);
						int y = rand.nextInt(cols);
						int dir = rand.nextInt(4);
						Position pos = new Position(x, y);

						// if the square is unoccupied, place the Creature there
						if ( world.get(pos) == null) {
							c = new Creature(speciesTemp, world, pos, dir);
							CreatureArray.add(c);
							isNull = false;
						}
					}
				}
				catch (FileNotFoundException e) {
					System.err.println("File not found");
					System.err.println(e.getMessage());
				}
			}
		}

	}


	/**
	 * Gives each creature one turn.
	 * 
	 */
	public void giveEachCreatureOneTurn() {
		for( int i = 0; i < CreatureArray.size(); i++) {
			CreatureArray.get(i).takeOneTurn();
		}

	}


	/**
	 * The array passed into main will include the arguments that appeared on the
	 * command line. For example, running "java Darwin Hop.txt Rover.txt" will call
	 * the main method with s being an array of two strings: "Hop.txt" and
	 * "Rover.txt".
	 * 
	 * The autograder will always call the full path to the creature files, for
	 * example "java Darwin /home/user/Desktop/Assignment02/Creatures/Hop.txt" So
	 * please keep all your creates in the Creatures in the supplied
	 * Darwin/Creatures folder.
	 *
	 * To run your code you can either: supply command line arguments through
	 * Eclipse ("Run Configurations -> Arguments") or by creating a temporary array
	 * with the filenames and passing it to the Darwin constructor. If you choose
	 * the latter options, make sure to change the code back to: Darwin d = new
	 * Darwin(s); before submitting. If you want to use relative filenames for the
	 * creatures they should be of the form "./Creatures/Hop.txt".
	 */
	
	public static void main(String s[]) {
		WorldMap.createWorldMap(15, 15);
		Darwin d = new Darwin(s);
		d.simulate();
	}

	/**
	 * Runs the Darwin simulation for numRounds amount of times.
	 * 
	 */
	public void simulate() {
		for (int rounds = 0; rounds < numRounds; rounds++) {
			giveEachCreatureOneTurn(); 
			WorldMap.pause(1000); 
		} 
	}

}
