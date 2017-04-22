import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

public class Client {
	static Socket s;
    static DataInputStream reader;
    static DataOutputStream writer;
    static Scanner keyboard = new Scanner(System.in);
    static final String IP = "localhost";
    static final int PORT = 9999;

    public static void main(String [] args)
    {
    	try
    	{
    		System.out.println("Fetching server connection...");
    		s = new Socket(IP, PORT);
    		reader = new DataInputStream(s.getInputStream());
    		writer = new DataOutputStream(s.getOutputStream());
    		System.out.println("Connection Established.");
    		System.out.println("Fetching player ID...");
    		int playerNumber = reader.read();
    		//holds the number of the opposing player
    		int otherPlayer;
    		if (playerNumber == 1)
    		{
    			otherPlayer = 2;
    		}
    		else
    		{
    			otherPlayer = 1;
    		}
    		System.out.println("Game found. You are Player " + playerNumber);
    		String standOrHit = new String();
    		int p1Score = 0;
    		int p2Score = 0;
    		int card;
    		//akin to the strings in the server, these will hold the action each player took to know
    		//whether to end the game or not
    		String p1Status = new String();
    		String p2Status = new String();
    		int score = 0;
    		do
    		{
    			//will hang until the server sends the turn integer 
    			reader.readInt();
    			do
    			{
    			System.out.println("It is your turn. Enter 'H' for a Hit or 'S' to stand.\nScore: " + score + "\n");
    			standOrHit = keyboard.next();
    			}
    			//will re-prompt the user to enter their choice again if they enter invalid input
    			while (Pattern.matches("[SH]", standOrHit) == false);
    			writer.writeUTF(standOrHit);
    			
    			if (standOrHit.charAt(0) == 'H')
    			{
    				card = reader.readInt();
    				score += card;
    				if (playerNumber == 1)
    				{
    					System.out.println("Drew a " + card + "\nWaiting for Player " + otherPlayer);
    				}
    				else
    				{
    					System.out.println("Drew a " + card + "\nWaiting for new turn.");
    				}
    			}
    			else
    			{
    				if (playerNumber == 1)
    				{
    					System.out.println("Chose to stand. Waiting for Player " + otherPlayer);
    				}
    				else
    				{
    					System.out.println("Chose to stand. Waiting for new turn.");
    				}
    			}
    			
    			//reads the score and action of each player to see if the game will end
    			//and to update each player of the score standings
    			p1Score = reader.readInt();
    			p2Score = reader.readInt();
    			p1Status = reader.readUTF();
    			p2Status = reader.readUTF();
    			
    			System.out.println("\nTurn Update:\nPlayer 1 Score: " + p1Score + "\nPlayer 2 Score: " + p2Score + "\n");
    			
    			//ends the game if either player has a busting score
    			if (p1Score > 21 || p2Score > 21)
    			{
    				break;
    			}
    		}
    		while (p1Status.charAt(0) == 'H' || p2Status.charAt(0) == 'H');
    		
    		if (p1Score <= 21 && p1Score > p2Score)
    		{
    			//prints this outcome if player 1 didn't bust and had a higher score than player 2
    			System.out.println("\nGame Over.\nPlayer 1 Score: " + p1Score + "\nPlayer 2 Score: " + p2Score + "\nPlayer 1 Wins!");
    		}
    		else if (p1Score > 21)
    		{
    			//prints this outcome if player 1 busted
    			System.out.println("\nGame Over.\nPlayer 1 Score: " + p1Score + "\nPlayer 2 Score: " + p2Score + "\nPlayer 1 Busted!\nPlayer 2 Wins!");
    		}
    		else if (p2Score <= 21 && p2Score > p1Score)
    		{
    			//prints this outcome if player 2 didn't bust and had a higher score than player 1
    			System.out.println("\nGame Over.\nPlayer 1 Score: " + p1Score + "\nPlayer 2 Score: " + p2Score + "\nPlayer 2 Wins!");
    		}
    		else if (p2Score > 21)
    		{
    			//prints this outcome if player 2 busted
    			System.out.println("\nGame Over.\nPlayer 1 Score: " + p1Score + "\nPlayer 2 Score: " + p2Score + "\nPlayer 2 Busted!\nPlayer 1 Wins!");
    		}
    		else
    		{
    			//prints this outcome if the two players got an equal score
    			System.out.println("\nGame Over.\nPlayer 1 Score: " + p1Score + "\nPlayer 2 Score: " + p2Score + "\nGame tied, no winner!");
    		}
    		
    		keyboard.close();
    		s.close();
    		reader.close();
    		writer.close();
    	}
    	catch (Exception ex){}
    }
}
