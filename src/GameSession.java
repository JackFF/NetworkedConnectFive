import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

	public class GameSession { //game session thread

		protected Socket playerOneSocket, playerTwoSocket; //sockets for player 1 and player 2
		
		DataInputStream playerOneInput;
		DataOutputStream playerOneOutput;
		DataInputStream playerTwoInput;
		DataOutputStream playerTwoOutput;
		
		public int[][] board = new int[6][9]; //this is the board for the connect five game
		
		protected int totalMovesMade = 0; //keeps track of total moves made in the game
		
		protected boolean overflow = false; //keeps track of whether one of the columns has overflowed

		public GameSession(Socket playerOneSocket, Socket playerTwoSocket) {
			
			this.playerOneSocket = playerOneSocket;
			this.playerTwoSocket = playerTwoSocket;
		}
		
		public void run() {
			
			try {
				
				playerOneInput = getplayerInput(playerOneSocket);
				playerOneOutput = getplayerOutput(playerOneSocket);
				playerTwoInput = getplayerInput(playerTwoSocket);
				playerTwoOutput = getplayerOutput(playerTwoSocket);
				
				char play = isPlayerReadyToBegin(playerOneInput); //yes no prompt from players to begin game
				
				play = isPlayerReadyToBegin(playerTwoInput);
				
				sendBoard(playerOneSocket, playerTwoSocket); //this sends the board to the players, the board is currently empty
				
				while(true) { //this runs until some game ending condition is met
					
					int column = playerOneInput.readInt(); //reads in player 1s column choice
					
					int x = updateBoard(column, 1);
					
					if(didPlayerWin(x-1, column)) { //checks if player 1 has won the game
						
						sendBoard(playerOneSocket);
						sendStatus(playerOneOutput, 1);
						sendStatus(playerTwoOutput, 1);
						sendBoard(playerOneSocket, playerTwoSocket);
						break;
					}
					
					else if(isGameADraw(totalMovesMade)) { //checks if the game is a draw
						
						sendStatus(playerOneOutput, 3);
						sendStatus(playerTwoOutput, 3);
						break;
					}
					
					else { //if no end condition is met then the game keeps playing
						
						if(overflow == false) {
							
							sendStatus(playerTwoOutput, 5);
						}
						
						else if(overflow == true) {
							
							sendStatus(playerTwoOutput, 4);
							overflow = false;
						}
					}
					
					sendBoard(playerOneSocket, playerTwoSocket);
					
					column = playerTwoInput.readInt();
					
					x = updateBoard(column, 2);
					
					if(didPlayerWin(x-1, column)) {
						
						sendBoard(playerTwoSocket);
						sendStatus(playerOneOutput, 2);
						sendStatus(playerTwoOutput, 2);
						sendBoard(playerOneSocket, playerTwoSocket);
						break;
					}
					
					else if(isGameADraw(totalMovesMade)) {
						
						sendBoard(playerTwoSocket);
						sendStatus(playerOneOutput, 3);
						sendStatus(playerTwoOutput, 3);
						sendBoard(playerOneSocket, playerTwoSocket);
						break;
					}
					
					else {
						
						if(overflow == false) {
							
							sendStatus(playerOneOutput, 5);
						}
						
						else if(overflow == true) {
							
							sendStatus(playerOneOutput, 4);
							overflow = false;
						}
					}
					
					sendBoard(playerOneSocket, playerTwoSocket);
					
				}
			} catch (IOException e) {
				
				System.out.println("Player has disconnected");
				System.out.println("Game Over");
			}
		}
		
		protected DataOutputStream getplayerOutput(Socket playerSocket) throws IOException {
			
			return new DataOutputStream(playerSocket.getOutputStream());
		}


		protected DataInputStream getplayerInput(Socket playerSocket) throws IOException {
			
			return new DataInputStream(playerSocket.getInputStream());
		}


		protected void sendStatus(DataOutputStream playerOutput, int status) throws IOException {
			
			playerOutput.writeInt(status);
		}


		protected int updateBoard(int column, int player) {
			
			int i = 0;
			
			try { //places player 1s 'X' into the board, also checks what row the 'X' will land on and checks if one of the columns has overflowed
				
			for(i = 0; i < 6; i++) {
				
				if((board[i][column] == 1) || (board[i][column] == 2)) {
					
					board[i-1][column] = player;
					break;
				}
			}
			} catch(ArrayIndexOutOfBoundsException a) {
				
				i = 1;
				overflow = true;
			}
			
			if(i == 6) {
				
				board[i-1][column] = player;
			}
			
			if(overflow == false) {
				
				updateMoveTotal();
			}
			
			return i;
		}


		protected void updateMoveTotal() { //updates the total number of moves
			
			totalMovesMade++;
		}


		protected char isPlayerReadyToBegin(DataInputStream playerInput) { //yes no prompt from players to begin game
			
			char play = 0;
			
			try {
				
				play = playerInput.readChar();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			return play;
		}


		public boolean isGameADraw(int totalMovesMade) { //checks is the game is a draw, the board is 9x6 which is a max of 54 moves
			
			if(totalMovesMade == 54) {
				
				return true;
			}
			
			return false;
		}

		public boolean didPlayerWin(int x, int y) { //checks if a player has won the game by connecting 5 in a row
			
			 int token = board[x][y];
			 int count = 0;
		     int i = y;
		     
		     while(i < 9 && board[x][i] == token) { //horizontal check
		    	 
		    	 count++;
		    	 i++;
		     }
		     
		     i = y - 1;
		     
		     while(i >= 0 && board[x][i] == token) {
		    	 
		    	 count++;
		    	 i--;
		     }
		     
		     if(count == 5) { 
		    	 
		    	 return true;
		     }
		     
		     count = 0;
		     int j = x;
		     
		     while(j < 6 && board[j][y] == token) { //vertical count check
		    	 
		    	 count++;
		    	 j++;
		     }
		     
		     if(count == 5) {
		    	 
		    	 return true;
		     }
		     
		     count = 0;
		     i = x;
		     j = y;
		     
		     while(i < 6 && j < 9 && board[i][j] == token) { //left to right diagonal check
		    	 
		    	 count++;
		    	 i++;
		    	 j++;
		     }
		     
		     i = x - 1;
		     j = y - 1;
		     
		     while(i >= 0 && j >= 0 && board[i][j] == token) {
		    	 
		    	 count++;
		    	 i--;
		    	 j--;
		     }
		     
		     if(count == 5) {
		    	 
		    	 return true;
		     }
		     
		     count = 0;
		     i = x;
		     j = y;
		     
		     while(i < 6 && j >= 0 && board[i][j] == token) {  //right to left diagonal check
		    	 
		    	 count++;
		    	 i++;
		    	 j--;
		     }
		     
		     i = x - 1;
		     j = y + 1;
		     
		     while(i >= 0 && j < 9 && board[i][j] == token) {
		    	 
		    	 count++;
		    	 i--;
		    	 j++;
		     }
		     
		     if(count == 5) {
		    	 
		    	 return true;
		     }
			
			return false;
		}

		public void sendBoard(Socket playerOneSocket, Socket playerTwoSocket) { //sends board to clients
			
			try {
				
				OutputStream os1 = playerOneSocket.getOutputStream();  //sends the board which is a 2 dimensional integer array
				ObjectOutputStream oos1 = new ObjectOutputStream(os1);  
				oos1.writeObject(board);
				
				OutputStream os2 = playerTwoSocket.getOutputStream();  
				ObjectOutputStream oos2 = new ObjectOutputStream(os2);  
				oos2.writeObject(board);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		public void sendBoard(Socket playerSocket) {
			
			try {
				
				OutputStream os1 = playerSocket.getOutputStream();  
				ObjectOutputStream oos1 = new ObjectOutputStream(os1);  
				oos1.writeObject(board);
				
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}