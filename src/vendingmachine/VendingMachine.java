import java.util.Random;
import java.util.Scanner;

public class VendingMachine {
	private int quarters;
	private int dimes;
	private int nickels;
	private int value;
	private boolean cancle;
	private Random r;
	private Scanner scanner;
	
	public VendingMachine(int coinCountBound){
		r = new Random();
		scanner = new Scanner(System.in);
		quarters = r.nextInt(coinCountBound);
		nickels = r.nextInt(coinCountBound);
		dimes = r.nextInt(coinCountBound);
		value = 0;
		cancle = false;
	}

	public void start(){
		int ticks = 0;
		System.out.println("\nTick " + ticks + ":");
		printState();
		System.out.println("\nEnter input: ");
		String input = scanner.nextLine();
		while(!input.equals("exit")){
			String output = lambda();
			delta(input);
			printOutput(output);
			ticks++;
			System.out.println("\nTick " + ticks + ":");
			printState();
			System.out.println("\nEnter input: ");
			input = scanner.nextLine();
		}
		System.out.println("\nGoodbye.");
	}

	private void printState(){
		System.out.println("||Current State||");
		System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
		System.out.println(quarters + " Quarters, " + dimes + " Dimes, " + nickels + " Nickels");
		System.out.println("Cancle = " + cancle + ", Value = " + value);
		System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
	}

	private void printOutput(String output){
		System.out.println("\n===============");
		System.out.println("Output: " + output);
		System.out.println("===============");
	}

	private void delta(String s){
		if(cancle){
			cancle = false;
			if(value > 0){
				decrementCoins();
			}
			value = 0;
		}else if(value >= 100){
			value -= 100;
		}
		for(int i = 0; i < s.length(); i++){
			if(s.charAt(i) == 'q'){
				quarters++;
				value += 25;
			}else if(s.charAt(i) == 'd'){
				dimes++;
				value += 10;
			}else if(s.charAt(i) == 'n'){
				nickels++;
				value += 5;
			}else if(s.charAt(i) == 'c'){
				cancle = true;
			}else if(s.charAt(i) != 'w'){
				System.out.println("'" + s.charAt(i) + "' is not a valid input");
			}
		}
	}

	private String lambda(){
		if(cancle){
			if(value > 0){
				String change = makeChange();
				return change;
			}
		}else if(value >= 100){
			return "Coffee";
		}
		return "Nothing";
	}

	private void decrementCoins(){
		String deltaCoins = makeChange();
		for(int i = 0; i < deltaCoins.length(); i++){
			if(deltaCoins.charAt(i) == 'q'){
				quarters--;
			}else if(deltaCoins.charAt(i) == 'd'){
				dimes--;
			}else if(deltaCoins.charAt(i) == 'n'){
				nickels--;
			}
		}
	}
	
	private String makeChange(){
		int[] coinCounts = {quarters, dimes, nickels};
		int[] coinVals = {25, 10, 5};
		String[] stringVals = {"q", "d", "n"};
		String output = "";
		int leftOver = value;
		for(int i = 0; i < 3; i++){
			int remainder = leftOver % coinVals[i];
			if(remainder != leftOver){
				int thisMany = (leftOver - remainder) / coinVals[i];
				while(thisMany > coinCounts[i]){
					thisMany--;
				}
				for(int j = 0; j < thisMany; j++){
					output += stringVals[i] + ",";
				}
				leftOver -= coinVals[i] * thisMany;
				if(value - leftOver == 0){
					break;
				}
			}
		}
		if(leftOver > 0){
			throw new AtomicModelException("There are not enough coins in the machine to make change.");
		}
		return output.substring(0, output.length() - 1);
	}

}