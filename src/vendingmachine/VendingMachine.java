import java.util.Random;
import java.util.Scanner;

public class VendingMachine {
	private int quarters;
	private int dimes;
	private int nickels;
	private int value;
	private boolean cancel;
	private Random r;
	private Scanner scanner;
	
	public VendingMachine(int coinCountBound){
		r = new Random();
		scanner = new Scanner(System.in);
		quarters = r.nextInt(coinCountBound);
		dimes = r.nextInt(coinCountBound);
		nickels = r.nextInt(coinCountBound);
		value = 0;
		cancel = false;
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
		System.out.println("Cancel = " + cancel + ", Value = " + value);
		System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
	}

	private void printOutput(String output){
		System.out.println("\n===============");
		System.out.println("Output: " + output);
		System.out.println("===============");
	}

	private void delta(String s){
		if(cancel){
			cancel = false;
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
				cancel = true;
			}else if(s.charAt(i) != 'w'){
				System.out.println("'" + s.charAt(i) + "' is not a valid input");
			}
		}
	}

	private String lambda(){
		if(cancel){
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

				/*
					Meaning of the nested if-condition below:
					Protect against unwarranted exception in the
					case where machine is out of nickels, but you CAN
					still make change.
					How:
					Don't use as many quarters as possible if 
					value is not divisible by quarters and not 
					divisible by dimes, instead, use one less 
					quarter and fill in the rest with dimes.
					For instance: A quarter goes into 105 4 times, but if
					there are no nickels, and only dimes left, you can't make
					105 with the 4 quarters, so instead use 3 quarters, then you
					can make 105 by adding 3 dimes: 3Q + 3D = 105.
				*/
				if(value % 10 != 0 && value % 25 != 0){ 
					if(dimes != 0){
						if(i == 0){
							thisMany--;
						}
					}
				}

				for(int j = 0; j < thisMany; j++){
					output += stringVals[i] + ",";
				}
				leftOver -= coinVals[i] * thisMany;
				if(leftOver == 0){
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
