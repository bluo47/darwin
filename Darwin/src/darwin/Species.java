package darwin;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

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
			program = new ArrayList<Instruction>();
			Instruction instruct;
			
			String nextLine = in.readLine();
			
			while (nextLine != null ) {
				
				String opcode;
				int address;

				if (nextLine.split(" ").length > 1) {
					// ifempty, ifwall, ifsame, ifenemy, ifrandom, go (require address)
					String[] split = nextLine.split(" ");
					
					opcode = split[0];
					int intOpcode = 0;
					address = Integer.valueOf(split[1]);
					
					if( opcode.equals("ifempty")) {
						System.out.println("hi there");
						
						intOpcode = 5;
					}else if( opcode.equals("ifwall")) {
						intOpcode = 6;				
					}else if( opcode.equals("ifsame")) {
						intOpcode = 7;
					}else if( opcode.equals("ifenemy")) {
						intOpcode = 8;
					}else if( opcode.equals("ifrandom")) {
						intOpcode = 9;
					}else if( opcode.equals("go")) {
						intOpcode = 10;
					} 
					instruct = new Instruction(intOpcode, address);
					program.add(instruct);

										
				}else { //hop, left, right, infect (do not require an address)	
					opcode = nextLine;
					
					int intOpcode = 0;
					if(opcode.equals("hop")) {
						intOpcode = 1;
					}else if( opcode.equals("left")) {
						intOpcode = 2;
					}else if( opcode.equals("right")) {
						intOpcode = 3;
					}else if( opcode.equals("infect")) {
						intOpcode = 4;
					}
					instruct = new Instruction(intOpcode, 0);
					program.add(instruct);
					
				}
			nextLine = in.readLine();
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
		return program.size()-2;
	}

	/**
	 * Return an instruction from the program.
	 * @pre 1 <= i <= programSize().
	 * @post returns instruction i of the program.
	 */
	public Instruction programStep(int i) {
		return program.get(i-1);   // FIX
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
			System.out.println(rover.programSize());
			System.out.println(rover.programToString());
			System.out.println(rover.programStep(3));
			
			
			BufferedReader in2
			= new BufferedReader(new FileReader("Creatures/Flytrap.txt"));					// "C:\\Users\\Benjamin\\git\\assign3-benl\\Darwin\\Creatures\\Rover.txt"));
			Species fly = new Species(in2);
			System.out.println(fly.getName());
			System.out.println(fly.getColor());
			System.out.println(fly.getSpeciesChar());
			System.out.println(fly.programSize());
			
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			System.err.println(e.getMessage());
		}
	}

}
