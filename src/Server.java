import java.net.*;
import java.io.*;

public class Server {
	
	protected ServerSocket connectFiveServer;
	
	public Server(int port) {
		
		System.out.println("Connect Five Server Running");
		
		gameSetup(port);
	}

	public void gameSetup(int port) {
		
		try {
			
			connectFiveServer = new ServerSocket(port); //creates the server socket for the clients to connect to at port 5000
			
			int sessionID = 1; 

			while(true) {
				
				System.out.println("Waiting for players to join session " + sessionID);
				
				Socket playerOneSocket = connectFiveServer.accept(); //socket for player 1 to connect to
				System.out.println("Player 1 has joined the session");
				DataOutputStream playerOneOutput = new DataOutputStream(playerOneSocket.getOutputStream()); //data output stream to allow the server to send data to the client through the clients socket
				playerOneOutput.writeInt(1); //lets the client know that it is player 1
				
				Socket playerTwoSocket = connectFiveServer.accept();
				System.out.println("Player 2 has joined the session");
				DataOutputStream playerTwoOutput = new DataOutputStream(playerTwoSocket.getOutputStream());
				playerTwoOutput.writeInt(2);
				
				sessionID++;
				
				GameSession gs = new GameSession(playerOneSocket, playerTwoSocket); //class for controlling the game session
				gs.run();
			}
		} catch (IOException e) { //if one player disconnects then the game ends
			
			System.out.println("Player has disconnected");
			System.out.println("Game Over");
		}
	}
	
	public static void main(String[] args) {
		
		Server server = new Server(5555);
	}
}
