import java.net.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.*;

public class Client {
	
	protected Socket socket; //socket to connect to the server
	
	protected DataInputStream input; //for recieving info from server
	protected DataOutputStream output; //for sending info to the server
	
	protected Scanner scanner; //for getting user input
	
	protected String playerName;
	
	protected int playerNumber;
	
	protected boolean playGame = true; //lets the client know to continue playing the game, activating any end condition will change this to false
	
	public Client(String ip, int port) {
		
		try {
			
			socket = new Socket(ip, port); //connects client to server at a given address and port number, "localhost" and port 5000
			System.out.println("You have connected to the game");
			
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
		    
			scanner = new Scanner(System.in);
		    System.out.print("Please enter your name: ");
			playerName = scanner.next();
			
			run(); 
			
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	protected void run() {
		
		try {
			
			Scanner scan = new Scanner(System.in);
				
			char play;
			
			System.out.print("Are you ready to play (Y/N)? ");
			play = scan.nextLine().charAt(0); //get a y/n prompt from the user and validates it
			
			while (play != 'y' && play != 'Y') {
			    
				if(play == 'n' || play == 'N') {
					
					System.out.print("Are you ready to play (Y/N)? ");
					play = scan.nextLine().charAt(0);
				}
				
				else {
					
					System.out.print("Invalid response. Please try again: ");
					play = scan.nextLine().charAt(0); 
				}
			}
			
			output.writeChar(play); //sends answer of y/n prompt to the server
			
			playerNumber = input.readInt(); //client receives integer from server that lets it know which player it is, 1 or 2
			
			if(playerNumber == 1) {
				
				System.out.println("Waiting for Player 2");
			}
			
			getBoard(); //gets board from server and prints it out, it is currently empty
			
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
		while(playGame) { //this runs until an end condition is met
			
			if(playerNumber == 1) { //player 1 moves first
				
				getPlayerMove(); //gets user input for the column that the player wants to place its token in

				if(playGame == false) { //checks if an end condition has been met and ends the loop if it has
					
					break;
				}

				getInfo(); //receives info from the server, the info is the current status of the game, if it is over or not, also prints updated board showing opponents move
			}
			
			else if(playerNumber == 2) {
				
				getInfo();
				
				if(playGame == false) {
					
					break;
				}
				
				getPlayerMove();
			}
		}
		
		//getBoard();
	}
	
	protected void getInfo() { //server lets client know the status of the game and prints updated board
		
		System.out.println("Waiting for opponent to make move!");
		System.out.println();
		System.out.println();
		
		try {
			
			int status;
			status = input.readInt();
			
			if(status == 1) { //status == 1 means that player 1 has won the game
				
				getBoard();
				playGame = false;
				
				if(playerNumber == 1) {
					
					System.out.println("You have won the game. Congratulations " + playerName + ".");
				}
				
				else if(playerNumber == 2) {
					
					System.out.println("Player 1 has won the game. Sorry " + playerName + " you have lost.");
				}
			}
			
			else if(status == 2) { //status == 2 means that player 2 has won the game
				
				getBoard();
				playGame = false;
				
				if(playerNumber == 1) {
					
					System.out.println("Player 2 has won the game. Sorry " + playerName + " you have lost.");
				}
				
				else if(playerNumber == 2) {
					
					System.out.println("You have won the game. Congratulations " + playerName + ".");
				}
				
				getBoard();
			}
			
			else if(status == 3) {//status == 3 means that the game has ended a draw
				
				System.out.println("The game has finished a draw!");
				playGame = false;
		
			}
			
			else if(status == 4) {//status == 4 means that a player has made an illegal move and attempted to overflow the board
				
				System.out.println("The opponent made an illegal move! Their turn is void.");
			}
			
			if(playGame == true) {
			
			getBoard();
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	protected void getBoard() { //receives the board form the server as an object and reads it into a 2D integer array, then prints it out displaying each players tokens correctly
		
		InputStream is;
		try {
			is = socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);  
			int[][] board = (int[][])ois.readObject();
			
			for(int i = 0; i < 6; i++) {
			
			for(int j = 0; j < 9; j++) {
				
				if(board[i][j] == 0) {
					
					System.out.print("[ ] ");
				}
				
				else if(board[i][j] == 1) {
					
					System.out.print("[" + "X" + "] ");
				}
				
				else if(board[i][j] == 2) {
					
					System.out.print("[" + "O" + "] ");
				}
			}
			
			System.out.println();
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}  
	}
	
	protected void getPlayerMove() { //gets each players move and sends the move to the server
		
		try {
			
			int column = 0;
			
			do { //this validates the players move
				
				Scanner scan = new Scanner(System.in);
				System.out.print("It's your turn " + playerName + ", please enter column (1-9): ");
				
				try {
					
					column = scan.nextInt();
					
					if(column < 1 || column > 9) {
						
						System.out.print("Please enter a valid choice. \n");
					}
					
				} catch(InputMismatchException a) {
					
					System.out.print("Please enter an integer. \n");
				}
				
			} while(column < 1 || column > 9);
			
			System.out.println();
			System.out.println();
			
			output.writeInt(column-1);
			
			if(playGame == true) {
				
				getBoard();
			}
		
		} catch (IOException e) {
			
			e.printStackTrace();
		} 
	}

	public static void main(String[] args) {
		
		Client client = new Client("localhost", 5555);
	}
}
