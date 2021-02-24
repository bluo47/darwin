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
	 * 
	 *  @param fileReader
	 */
	public Species(BufferedReader fileReader) {
		try {
			BufferedReader in = new BufferedReader(fileReader);
			String name = in.readLine();
			String color = in.readLine();
			
			this.name = name;
			this.color = color;
			speciesChar = name.charAt(0);
			
			//create a program to hold the instructions from the file
			program = new ArrayList<Instruction>();
			String nextLine = in.readLine();

			while (!nextLine.isEmpty()) {
				String instruction;
				int address;

				if (nextLine.split(" ").length > 1) {
					// for instructions with addresses:
					// ifempty, ifwall, ifsame, ifenemy, ifrandom, go
					// separate and identify the instruction & address
					String[] split = nextLine.split(" ");

					instruction = split[0];
					address = Integer.valueOf(split[1]);

					int opcode = 0;

					// assign the correct opcode for the given instruction
					if( instruction.equals("ifempty")) {
						opcode = 5;
					}else if( instruction.equals("ifwall")) {
						opcode = 6;				
					}else if( instruction.equals("ifsame")) {
						opcode = 7;
					}else if( instruction.equals("ifenemy")) {
						opcode = 8;
					}else if( instruction.equals("ifrandom")) {
						opcode = 9;
					}else if( instruction.equals("go")) {
						opcode = 10;
					} 

					Instruction instruct = new Instruction(opcode, address);

					// add the new instruction to the program
					program.add(instruct);


				}else { 
					//instructions: hop, left, right, and infect 
					//do not require an address	
					instruction = nextLine;

					int opcode = 0;

					// assign the correct opcode for the given instruction
					if( instruction.equals("hop")) {
						opcode = 1;
					}else if( instruction.equals("left")) {
						opcode = 2;
					}else if( instruction.equals("right")) {
						opcode = 3;
					}else if( instruction.equals("infect")) {
						opcode = 4;
					}
					Instruction instruct = new Instruction(opcode, 0);

					// add the new instruction to the program
					program.add(instruct);

				}

				nextLine = in.readLine();

			}

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
	 * 
	 * @return the first letter of the species name.
	 */
	public char getSpeciesChar() {
		return speciesChar;
	}

	/**
	 * Return the name of the species.
	 * 
	 * @return the species name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the color of the species.
	 * 
	 * @return the species' icon color.
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Return the number of instructions in the program.
	 * 
	 * @return the number of instructions in the program.
	 */
	public int programSize() {
		return program.size();
	}

	/**
	 * Return an instruction from the program.
	 * 
	 * @pre 1 <= i <= programSize().
	 * @post returns instruction i of the program.
	 */
	public Instruction programStep(int i) {
		return program.get(i-1);
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

			//Rover tests
			BufferedReader in
			= new BufferedReader(new FileReader("Creatures/Rover.txt"));
			Species rover = new Species(in);
			System.out.println(rover.getName()); //Rover
			System.out.println(rover.getColor()); //red
			System.out.println(rover.getSpeciesChar()); //R
			System.out.println(rover.programSize()); //12
			System.out.println(rover.programStep(3)); //ifsame 6

			/*
			//Flytrap tests
			BufferedReader in2
			= new BufferedReader(new FileReader("Creatures/Flytrap.txt"));
			Species fly = new Species(in2);
			System.out.println(fly.getName()); //Flytrap
			System.out.println(fly.getColor()); //magenta
			System.out.println(fly.getSpeciesChar()); //F
			System.out.println(fly.programSize()); //5
			System.out.println(fly.programStep(1)); //ifenemy 4

			//Food tests
			BufferedReader in3
			= new BufferedReader(new FileReader("Creatures/Food.txt"));
			Species food = new Species(in3);
			System.out.println(food.getName()); //Oodfay
			System.out.println(food.getColor()); //green
			System.out.println(food.getSpeciesChar()); //O
			System.out.println(food.programSize()); //2
			System.out.println(food.programStep(2)); //go 1

			//Hop tests
			BufferedReader in4
			= new BufferedReader(new FileReader("Creatures/Hop.txt"));
			Species hop = new Species(in4);
			System.out.println(hop.getName()); //Hop
			System.out.println(hop.getColor()); //blue
			System.out.println(hop.getSpeciesChar()); //H
			System.out.println(hop.programSize()); //2
			System.out.println(hop.programStep(1)); //hop
			System.out.println(hop.programToString());
			 */

		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			System.err.println(e.getMessage());
		}
	} 

}
