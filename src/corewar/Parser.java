package corewar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Object used to read <file>.red program and turn it into a List of Cells that can be used by the Interpreter.
 */
public class Parser {
	ScriptEngine scriptEngine;

	/**
	 * Creates a Parser.
	 */
	public Parser() {
		ScriptEngineManager manager = new ScriptEngineManager();
		scriptEngine = manager.getEngineByName("js");
	}

	/**
	 * Return the Type Operand matching the given String.
	 * @param op String representing the wanted Type.
	 * @return The corresponding Type.
	 */
	protected static Type parseOperandType(String op) {
		if (op.length() == 0)
			return Type.DIRECT;
		switch (op.charAt(0)) {
		  case '#':
			return Type.OCTOTHORPE;
		  case '@':
			return Type.INDIRECT;
		  case '<':
			return Type.PREDECREMENT;
		  case '>':
			return Type.PREINCREMENT;
		  default:
			return Type.DIRECT;
		}
	}

	/**
	 * Transform a label or a constant into its real value.
	 * @param word The name of the label or the constant to resolve.
	 * @param lineNumber The current line number where the word is referenced.
	 * @param constants The Map containing the constants
	 * @param labels The Map containing the labels
	 * @return
	 */
	String resolveWord(String word, int lineNumber, HashMap<String, String> constants, HashMap<String, Integer> labels) {
		if (constants.containsKey(word)) {
			return constants.get(word);
		} else {
			return Integer.toString(labels.get(word)-lineNumber);
		}
	}

	/**
	 * Creates the Operand according to the given String.
	 * @param op The string representing the Operand.
	 * @param lineNumber Is the line number of the given string.
	 * @param constants Map matching constants name to what they represent.
	 * @param labels Map matching labels to their line number.
	 * @return The corresponding Operand.
	 */
	protected Operand parseOperand(String op, int lineNumber, HashMap<String, String> constants, HashMap<String, Integer> labels) {
		Type type = parseOperandType(op);
		if (type != Type.DIRECT)
			op = op.substring(1);

		StringBuilder opBuilder = new StringBuilder(op.length());
		int wordStart = -1;
		for (int i = 0; i<op.length(); ++i) {
			if (Character.isLetter(op.charAt(i)) || (Character.isDigit(op.charAt(i)) && wordStart != -1)) {
				if (wordStart == -1) {
					wordStart = i;
				}
				if (i == op.length()-1) {
					opBuilder.append(resolveWord(op.substring(wordStart, i+1), lineNumber, constants, labels));
				}
			} else {
				if (wordStart != -1) {
					opBuilder.append(resolveWord(op.substring(wordStart, i), lineNumber, constants, labels));
				}
				wordStart = -1;
				opBuilder.append(op.charAt(i));
			}
		}
		
		String finalOp = opBuilder.toString().replaceAll("--", ""); // ScriptEngine doesn't like double negative signs

		int pos;
		try {
			pos = (int)scriptEngine.eval(finalOp);
			return new Operand(type, pos);
		} catch (ScriptException e) {
			throw new RuntimeException("Invalid address: "+finalOp);
		}
	}

	/**
	 * Return the Instruction matching the given String.
	 * @param instruction String that we want to transform.
	 * @return Corresponding Intruction.
	 */
	protected Instruction parseInstruction(String instruction) {
		switch (instruction) {
			case "dat":
				return Instruction.DAT;
			case "mov":
				return Instruction.MOV;
			case "add":
				return Instruction.ADD;
			case "sub":
				return Instruction.SUB;
			case "jmp":
				return Instruction.JMP;
			case "jmz":
				return Instruction.JMZ;
			case "jmn":
				return Instruction.JMN;
			case "cmp":
				return Instruction.CMP;
			case "slt":
				return Instruction.SLT;
			case "djn":
				return Instruction.DJN;
			case "spl":
				return Instruction.SPL;
		}
		throw new RuntimeException("Instruction not defined: "+instruction);
	}

	/**
	 * Method to get the index of the next word in a String array.
	 * @param words The String array from which we want the next word.
	 * @param i The current word index.
	 * @return The next word in the array index.
	 */
	int nextWord(String[] words, int i) {
		do {
			i++;
		} while (words[i].length() == 0);
		return i;
	}

	/**
	 * Parse the given program file into a List of Cells.
	 * @param fileName Name and path to the file we want to parse.
	 * @param team The team number to give to the program for parse.
	 * @return The program into its List of Cells form.
	 * @throws IllegalArgumentException Throws Exception when illegal instructions or when illegal addresses
	 * @throws IOException Throws when error in the file reading.
	 */
	public ArrayList<Cell> parseFile(String fileName, int team) throws IllegalArgumentException, IOException {
		File f = new File(fileName);
		Scanner reader = new Scanner(f);
		int currentLineNumber = 1;
		int lineNumber = (int)Files.lines(Paths.get(fileName)).count();
		ArrayList<String[]> lines = new ArrayList<>(lineNumber);
		HashMap<String, String> constants = new HashMap<>();
		HashMap<String, Integer> labels = new HashMap<>();
		while (reader.hasNextLine() && currentLineNumber < 100) {
			String line = reader.nextLine();
			int end = line.indexOf(";");
			if (end != -1) {
				line = line.substring(0, end);
			}
			if (line.length() > 0) {
				String[] words = line.split(" ");
				int i = 0;
				if (line.charAt(0) != ' ') { //label present
					String labelName = words[0];
					i = nextWord(words, i);
					if (words[i].equals("equ")) {
						i = nextWord(words, i);
						constants.put(labelName, words[i]);
						continue;
					} else {
						labels.put(labelName, currentLineNumber);
					}
				}
				lines.add(words);
				currentLineNumber++;
			}
		}

		currentLineNumber = 1;
		reader.close();
		ArrayList<Cell> cells = new ArrayList<Cell>();
		for (String[] words: lines) {
			int i = 0;
			if (words[i].length() == 0 || words[i].charAt(0) != ' ') { //label present
				i = nextWord(words, i);
			}
			Instruction instruction = parseInstruction(words[i].toLowerCase());
			i = nextWord(words, i);
			String val1;
			Operand op2;
			if(words[i].charAt(words[i].length()-1)==',') {
				val1 = words[i].substring(0, words[i].length()-1);
				i = nextWord(words, i);
				op2 = parseOperand(words[i], currentLineNumber, constants, labels);
				
			}
			else {
				val1 = words[i];
				op2 = new Operand(Type.DIRECT, 0);
			}
			Operand op1 = parseOperand(val1, currentLineNumber, constants, labels);
			
			Cell cell = new Cell(instruction, op1, op2, team);
			if (Interpreter.isIllegalInstruction(instruction, op1, op2))
				throw new IllegalInstructionException(cell);
			cells.add(cell);
			currentLineNumber++;
		}
		//displayProg(cells);
		return cells;
	}

	/**
	 * Print a program from its List of Cells from.
	 * @param p The program to display.
	 */
	void displayProg(List<Cell> p) {
		System.out.println("program size: "+p.size());
		for (Cell c: p) {
			System.out.println(c);
		}
	}
}
