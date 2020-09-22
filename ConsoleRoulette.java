import java.io.*;  
import java.util.Scanner;  
import java.util.ArrayList;
import java.util.Random;

public class ConsoleRoulette {
	public static ArrayList<String> players;
	public static void main(String[] args) { 
		players = loadPlayers();
		
		System.out.println("Welcome to Console Roulette");
		System.out.println("Players");
		System.out.println("---------------------------");
		
		for(int i = 0; i < players.size(); i++){
			System.out.println(i + 1 + " - " + players.get(i));
		}
		System.out.println("---------------------------");
		
		Scanner in = new Scanner(System.in);
	    Random rand = new Random();
	    
	    int rouletteNumber;
	    
	    ArrayList<String> nameBetAmount = new ArrayList<String>();
	    
	    System.out.println("Enter Name Bet Amount e.g Tiki_Monkey 2 1.0, 's' to start Spinning.");
    	String line = in.nextLine();
    	
    	while (!line.equalsIgnoreCase("s")){
    		if(isValid(line, players)){
    			nameBetAmount.add(line);
    		}else{
    			System.out.println("Invalid input");
    		}
    		line = in.nextLine();
    	}
	    
	    try {
	        while (true) {    	
		    	if(!nameBetAmount.isEmpty()){
			    	System.out.println("Bets");
					System.out.println("-----------------------------------");
					
					for(int i = 0; i < nameBetAmount.size(); i++){
						System.out.println(i+1 + " - " + nameBetAmount.get(i));
					}
					
					System.out.println("-----------------------------------");
		    	}
		    	
		        rouletteNumber = rand.nextInt(37);
		        System.out.println("\nNumber: " + rouletteNumber);
//		        
		        String leftAlignFormat = "%-15s %-15s %-15s %-5s %n";

		        System.out.format("%n");
		        System.out.format("Player     	 Bet   	     Outcome        Winnings%n");        
		        System.out.format("---%n");
		        
		        String[] nameBetAmountArray = new String[3];
		        
		        for(int i = 0; i < nameBetAmount.size(); i++){      	
		        	nameBetAmount.set(i, evaluateBet(nameBetAmount.get(i), rouletteNumber));
		        	
		        	nameBetAmountArray = nameBetAmount.get(i).split(" ");
		        	System.out.format(leftAlignFormat, nameBetAmountArray[0], nameBetAmountArray[1], nameBetAmountArray[3], nameBetAmountArray[4]);
				}
		        
		        System.out.format("---%n");
		        
		        String[] player;
		        leftAlignFormat = "%-15s %-15s %-15s %n";

		        System.out.format("%n");
		        System.out.format("Player     Total Win   	  Total Bet%n");        
		        System.out.format("---%n");
		    	for(int i = 0; i < players.size(); i++){
		    		
		    		player = players.get(i).split(",");
		    		if(player.length == 3)
		    			System.out.format(leftAlignFormat, player[0], player[1], player[2]);
				}
		    	
		    	 System.out.format("---%n");
		    	
	            Thread.sleep(30 * 1000);
	        }
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }	
	}
	
	public static String evaluateBet(String nameBetAmount, int rouletteNumber){
			
		int bet = 0;
    	double winning = 0.0;
    	String outCome = "";
    	
    	String[] nameBetAmountArray = nameBetAmount.split(" ");
    	
    	if(rouletteNumber == 0){
			return nameBetAmountArray[0] + " " + nameBetAmountArray[1] + " " + nameBetAmountArray[2] + " " + " LOSE 0.0";
		}
		
    	if(isNumeric(nameBetAmountArray[1])){
    		bet = Integer.parseInt(nameBetAmountArray[1]);
    		if (bet == rouletteNumber){
				winning = 36.0*Double.parseDouble(nameBetAmountArray[2]);
				outCome = "WIN";
			}else{
				winning = 0.0;
				outCome = "LOSE";
			}
    		
    	}else{
    		
    		if(rouletteNumber % 2 == 0){
    			if(nameBetAmountArray[1].equalsIgnoreCase("EVEN")){
            		winning = 2.0*Double.parseDouble(nameBetAmountArray[2]);
            		outCome = "WIN";
            	}else{
            		outCome = "LOSE";
            	}
    		}else{
    			if(nameBetAmountArray[1].equalsIgnoreCase("ODD")){
            		winning = 2.0*Double.parseDouble(nameBetAmountArray[2]);
        			outCome = "WIN";
            	}else{
            		outCome = "LOSE";
            	}
    		}
    		
    	}
    	tallyBet(nameBetAmountArray[0], winning, Double.parseDouble(nameBetAmountArray[2]));
    	return nameBetAmountArray[0] + " " + nameBetAmountArray[1] + " " + nameBetAmountArray[2] + " " + outCome + " " + winning;
	}
	
	public static ArrayList<String> loadPlayers(){
		ArrayList<String> playerNames = new ArrayList<String>();
		
		try{    
			FileInputStream fis = new FileInputStream("Console-Roulette.txt");       
			Scanner sc = new Scanner(fis);
		
			while(sc.hasNextLine()){  
				playerNames.add(sc.nextLine());
			}  
			sc.close(); 
		}catch(IOException e){  
			e.printStackTrace();  
		}

		return playerNames;
	}
	
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        double i = Double.parseDouble(strNum);
	    } catch (NumberFormatException e) {
	        return false;
	    }
	    return true;
	}
	
	public static boolean isValidBet(String value){
		boolean result = false;
		if(isNumeric(value)){
			if((Integer.parseInt(value) >= 1 || Integer.parseInt(value) <= 36))
				result = true;
		}else{
			if(value.equalsIgnoreCase("EVEN") || value.equalsIgnoreCase("ODD"))
				result = true;
		}
		return result;
	}
	
	public static boolean isValid(String s, ArrayList<String> players){
		if(s.isEmpty())
			return false;
		
		String[] nameBetAmountArray = s.split(" ");
		if(nameBetAmountArray.length != 3)
			return false;
		
		String name = nameBetAmountArray[0];
		String bet = nameBetAmountArray[1];
		String amount = nameBetAmountArray[2];
		
		boolean result = false;
		
		if(players.contains(name) && isValidBet(bet) && isNumeric(amount))
			result = true;
		
		return result;
	}
	
	public static void tallyBet(String name, double winning, double bet){   	
    	double totalWin = 0.0;
    	double totalBet = 0.0;

    	String[] player;
    
    	for(int i = 0; i < players.size(); i++){
    		
    		player = players.get(i).split(",");
    		
    		if(name.equals(player[0])){
    			if(player.length == 3){
    				//previous winning plus current winning
        			totalWin = Double.parseDouble(player[1]) + winning;
        			//previous bet plus current bet
            		totalBet = Double.parseDouble(player[2]) + bet;
            		players.set(i, name + "," + totalWin + "," + totalBet);
        		}else{
        			//initial results
        			players.set(i, name + "," + winning + "," + bet);
        		}
    			
    		}
		}
	}
}