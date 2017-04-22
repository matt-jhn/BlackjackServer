import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	static ServerSocket server;
	//creates sockets for each client and reader and writers for each
    static Socket client1;
    static Socket client2;
    static DataOutputStream writer1;
    static DataOutputStream writer2;
    static DataInputStream reader1;
    static DataInputStream reader2;
    
    public static void main(String [] args)
    {
    	try
    	{
    		server = new ServerSocket(9999);
    		System.out.println("Waiting for player 1...");
    		client1 = server.accept();
    		writer1 = new DataOutputStream(client1.getOutputStream());
    		reader1 = new DataInputStream(client1.getInputStream());
    		int i = 1;
    		//tells player 1 that he or she is player 1
    		writer1.write(i);
    		System.out.println("\nPlayer 1 connected. Waiting for player 2...");
    		client2 = server.accept();
    		writer2 = new DataOutputStream(client2.getOutputStream());
    		reader2 = new DataInputStream(client2.getInputStream());
    		int j = 2;
    		//tells player 2 that he or she is player 2
    		writer2.write(j);
    		System.out.println("\nPlayer 2 connected. Beginning game.");
    		//scores will be sent to clients as a turn update after each turn
    		int p1score = 0;
    		int p2score = 0;
    		//simple integer that will be sent to the client to let it resume
    		int turn = 1;
    		int card;
    		//these strings will tell the server whether the player hit or stood
    		String p1Status = new String();
    		String p2Status = new String();
    		
    		do
    		{
    			System.out.println("Waiting on Player 1...");
    			//sends the turn integer to let player 1 know it can continue
    			writer1.writeInt(turn);
    			//reads the action player 1 took
    			p1Status = reader1.readUTF();
    			
    			if (p1Status.charAt(0) == 'H')
    			{
    				System.out.println("Got 'H' from Client");
    				card = drawCard();
    				//sends what the player drew to the player
    				writer1.writeInt(card);
    				p1score += card;
    				System.out.println("Player 1 Score: " + p1score);
    			}
    			else
    			{
    				System.out.println("Got 'S' from Client");
    				System.out.println("Player 1 Score: " + p1score);
    			}
    			
    			System.out.println("Waiting on Player 2...");
    			writer2.writeInt(turn);
    			p2Status = reader2.readUTF();
    			
    			if (p2Status.charAt(0) == 'H')
    			{
    				System.out.println("Got 'H' from Client");
    				card = drawCard();
    				writer2.writeInt(card);
    				p2score += card;
    				System.out.println("Player 2 Score: " + p2score);
    			}
    			else
    			{
    				System.out.println("Got 'S' from Client");
    				System.out.println("Player 2 Score: " + p2score);
    			}
    			
    			//sends the scores of each player and what each player did to know whether to end
    			//the game if both players stood
    			writer1.writeInt(p1score);
    			writer1.writeInt(p2score);
    			writer1.writeUTF(p1Status);
    			writer1.writeUTF(p2Status);
    			writer2.writeInt(p1score);
    			writer2.writeInt(p2score);
    			writer2.writeUTF(p1Status);
    			writer2.writeUTF(p2Status);
    			
    			//will end the game if either player busted
    			if (p1score > 21 || p2score > 21)
    			{
    				break;
    			}
    		}
    		while (p1Status.charAt(0) == 'H' || p2Status.charAt(0) == 'H');
    		
    		System.out.println("Game Over.\nPlayer 1 Score: " + p1score + "\nPlayer 2 Score: " + p2score);
    		
    		server.close();
    		reader1.close();
    		reader2.close();
    		writer1.close();
    		writer2.close();
    	}
    	
    	catch (IOException IOex)
    	{
    		IOex.printStackTrace();
    	}
    }
    
    public static int drawCard() throws IOException
    {
    	Random rand = new Random();
    	int card = rand.nextInt(13) + 1;
    	//if 11, 12, or 13 is drawn, they will reassign to 10 as per the rules given
    	if (card == 11 || card == 12 || card == 13)
    	{
    		card = 10;
    	}
    	return card;
    }
}
