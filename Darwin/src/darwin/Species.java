package darwin;

import java.io.*;
import java.util.ArrayList;

/**
 * The individual creatures in the world are all representatives of some
 * species class and share certain common characteristics, such as the species
 * name and the program they execute. Rather than copy this information into
 * each creature, this data can be recorded once as part of the description for
 * a species and then each creature can simply include the appropriate species
 * reference as part of its internal data structure.
 * 
 * Note: The instruction addresses start at one, not zero.
 */
public class Species {
	private String name;
	private String color;
	private char speciesChar; // the first character of Species name
	private ArrayList<Instruction> program;

	/**
	 * Create a species for the given fileReader. 
	 */
	public Species(BufferedReader fileReader) {
			try {
			BufferedReader in = new BufferedReader(fileReader);
			String name = in.readLine();
			String color = in.readLine();
			this.name = name;
			this.color = color;
			speciesChar = name.charAt(0);
			while (in != null) {
				String nextLine = in.readLine();
				int opcode;
				String address;
				int intAddress;
				if (nextLine.split(nextLine).length > 1) {
					// ifempty, ifwall, ifsame, ifenemy, ifrandom, go (require address)
					String[] split = nextLine.split(nextLine);
					opcode = Integer.valueOf(split[0]);
					address = split[1];
					if( address.equals("ifempty")) {
						intAddress = 5;
					}else if( address.equals("ifwall")) {
						intAddress = 6;				
					}else if( address.equals("ifsame")) {
						intAddress = 7;
					}else if( address.equals("ifenemy")) {
						intAddress = 8;
					}else if( address.equals("ifrandom")) {
						intAddress = 9;
					}else if( address.equals("go")) {
						intAddress = 10;
					}
					
					Instruction instruct = new Instruction(opcode, intAddress);
					program.add(instruct);
										
				}else { //hop, left, right, infect (do not require an address)	
					address = nextLine;
					if(address.equals("hop")) {
						intAddress = 1;
					}else if(address.equals("left")) {
						intAddress = 2;
					}else if( address.equals("right")) {
						intAddress = 3;
					}else if( address.equals("go")) {
						intAddress = 10;
					}
					Instruction instruct = new Instruction(0, intAddress);
					program.add(instruct);
					
				}
				
			}
			
			// insert code to read from Creatures file here (use readLine() )
			} catch (IOException e) {
				System.out.println(
					"Could not read file '"
						+ fileReader
						+ "'");
				System.exit(1);
			}
		
	}


	/**
	* Return the char for the species
	*/
	public char getSpeciesChar() {
		return speciesChar;
	}

	/**
	 * Return the name of the species.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the color of the species.
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Return the number of instructions in the program.
	 */
	public int programSize() {
		return program.size()-1;
	}

	/**
	 * Return an instruction from the program.
	 * @pre 1 <= i <= programSize().
	 * @post returns instruction i of the program.
	 */
	public Instruction programStep(int i) {
		return null;    // FIX
		// return program.get[i]
	}

	/**
	 * Return a String representation of the program.
	 * 
	 * do not change
	 */
	public String programToString() {
		String s = "";
		for (int i = 1; i <= programSize(); i++) {
			s = s + (i) + ": " + programStep(i) + "\n";
		}
		return s;
	}
	
	public static void main(String args[]) {
		try {
			BufferedReader in
			= new BufferedReader(new FileReader("Creatures/Rover.txt"));
					// "C:\\Users\\Benjamin\\git\\assign3-benl\\Darwin\\Creatures\\Rover.txt"));
			Species rover = new Species(in);
			System.out.println(rover.getName());
			System.out.println(rover.getColor());
			System.out.println(rover.getSpeciesChar());
			
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			System.err.println(e.getMessage());
		}
	}

}
