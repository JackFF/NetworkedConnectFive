import static org.junit.Assert.*;
import java.io.IOException;
import java.net.Socket;
import org.junit.Test;

public class Tests {

	@Test
	public void checkBoardIsEmpty() throws IOException {
		
		Socket playerOneSocket = null, playerTwoSocket = null;
		
		GameSession gs = new GameSession(playerOneSocket, playerTwoSocket);
		
		for(int i = 0; i < 6; i++) {
			
			for(int j = 0; j < 9; j++) {
				
				assertEquals(gs.board[i][j], 0);
			}
		}
	}
	
	@Test
	public void checkBoardIsUpdatingForPlayerOne() throws IOException {
		
		Socket playerOneSocket = null, playerTwoSocket = null;
		
		GameSession gs = new GameSession(playerOneSocket, playerTwoSocket);
		
		gs.updateBoard(0, 1);
		
		assertEquals(gs.board[5][0], 1);
	}
	
	@Test
	public void checkBoardIsUpdatingForPlayerTwo() throws IOException {
		
		Socket playerOneSocket = null, playerTwoSocket = null;
		
		GameSession gs = new GameSession(playerOneSocket, playerTwoSocket);
		
		gs.updateBoard(0, 2);
		
		assertEquals(gs.board[5][0], 2);
	}
	
	@Test
	public void checkBoardIsStackingForPlayerOne() throws IOException {
		
		Socket playerOneSocket = null, playerTwoSocket = null;
		
		GameSession gs = new GameSession(playerOneSocket, playerTwoSocket);
		
		for(int i = 5; i > 0; i--) {
			
			gs.updateBoard(0, 1);
			assertEquals(gs.board[i][0], 1);
		}
	}
	
	@Test
	public void checkBoardIsStackingForPlayerTwo() throws IOException {
		
		Socket playerOneSocket = null, playerTwoSocket = null;
		
		GameSession gs = new GameSession(playerOneSocket, playerTwoSocket);
		
		for(int i = 5; i > 0; i--) {
			
			gs.updateBoard(0, 2);
			assertEquals(gs.board[i][0], 2);
		}
	}
	
	@Test
	public void checkBoardIsFillingUpForBothPlayers() throws IOException {
		
		Socket playerOneSocket = null, playerTwoSocket = null;
		
		GameSession gs = new GameSession(playerOneSocket, playerTwoSocket);
		
		gs.updateBoard(3, 2);
		gs.updateBoard(3, 1);
		gs.updateBoard(3, 1);
		gs.updateBoard(3, 2);
		gs.updateBoard(3, 2);
		
		assertEquals(gs.board[5][3], 2);
		assertEquals(gs.board[4][3], 1);
		assertEquals(gs.board[3][3], 1);
		assertEquals(gs.board[2][3], 2);
		assertEquals(gs.board[1][3], 2);
	}
	
	@Test
	public void checkBoardIsBoardIsHandlingOverflow() throws IOException {
		
		Socket playerOneSocket = null, playerTwoSocket = null;
		
		GameSession gs = new GameSession(playerOneSocket, playerTwoSocket);
		
		gs.updateBoard(3, 2);
		gs.updateBoard(3, 1);
		gs.updateBoard(3, 1);
		gs.updateBoard(3, 2);
		gs.updateBoard(3, 2);
		gs.updateBoard(3, 1);
		gs.updateBoard(3, 1);
		
		assertTrue(gs.overflow);
	}
	
	@Test
	public void checkIfDrawConditionWorks() throws IOException {
		
		Socket playerOneSocket = null, playerTwoSocket = null;
		
		GameSession gs = new GameSession(playerOneSocket, playerTwoSocket);
		
		int count = 0;
		
		for(int i = 0; i < 6; i ++) {
			
			for(int j = 0; j < 9; j++) {
				
				gs.updateBoard(j, 1);
				count++;
			}
		}
		
		assertTrue(gs.isGameADraw(gs.totalMovesMade));
		
	}
	
	@Test
	public void checkIfVerticalWinConditionWorksForPlayerOne() throws IOException { 
		
		Socket playerOneSocket = null;
		Socket playerTwoSocket = null;
		
		GameSession gs = new GameSession(playerOneSocket, playerTwoSocket);
		
		gs.board[0][0] = 1;
		gs.board[1][0] = 1;
		gs.board[2][0] = 1;
		gs.board[3][0] = 1;
		gs.board[4][0] = 1;
		
		assertTrue(gs.didPlayerWin(0, 0));
	}
	
	@Test
	public void checkIfHorizontalWinConditionWorksForPlayerOne() throws IOException { 
		
		Socket playerOneSocket = null;
		Socket playerTwoSocket = null;
		
		GameSession gs = new GameSession(playerOneSocket, playerTwoSocket);
		
		gs.board[0][0] = 1;
		gs.board[0][1] = 1;
		gs.board[0][2] = 1;
		gs.board[0][3] = 1;
		gs.board[0][4] = 1;
		
		assertTrue(gs.didPlayerWin(0, 0));
	}
	
	@Test
	public void checkIfForwardsDiagonalWinConditionWorksForPlayerOne() throws IOException { 
		
		Socket playerOneSocket = null;
		Socket playerTwoSocket = null;
		
		GameSession gs = new GameSession(playerOneSocket, playerTwoSocket);
		
		gs.board[0][0] = 1;
		gs.board[1][1] = 1;
		gs.board[2][2] = 1;
		gs.board[3][3] = 1;
		gs.board[4][4] = 1;
		
		assertTrue(gs.didPlayerWin(0, 0));
	}
	
	@Test
	public void checkIfBackwardsDiagonalWinConditionWorksForPlayerOne() throws IOException { 
		
		Socket playerOneSocket = null;
		Socket playerTwoSocket = null;
		
		GameSession gs = new GameSession(playerOneSocket, playerTwoSocket);
		
		gs.board[4][0] = 1;
		gs.board[3][1] = 1;
		gs.board[2][2] = 1;
		gs.board[1][3] = 1;
		gs.board[0][4] = 1;
		
		assertTrue(gs.didPlayerWin(4, 0));
	}
	
	@Test
	public void checkIfVerticalWinConditionWorksForPlayerTwo() throws IOException { 
		
		Socket playerOneSocket = null;
		Socket playerTwoSocket = null;
		
		GameSession gs = new GameSession(playerOneSocket, playerTwoSocket);
		
		gs.board[0][0] = 2;
		gs.board[1][0] = 2;
		gs.board[2][0] = 2;
		gs.board[3][0] = 2;
		gs.board[4][0] = 2;
		
		assertTrue(gs.didPlayerWin(0, 0));
	}
	
	@Test
	public void checkIfHorizontalWinConditionWorksForPlayerTwo() throws IOException { 
		
		Socket playerOneSocket = null;
		Socket playerTwoSocket = null;
		
		GameSession gs = new GameSession(playerOneSocket, playerTwoSocket);
		
		gs.board[0][0] = 2;
		gs.board[0][1] = 2;
		gs.board[0][2] = 2;
		gs.board[0][3] = 2;
		gs.board[0][4] = 2;
		
		assertTrue(gs.didPlayerWin(0, 0));
	}
	
	@Test
	public void checkIfForwardsDiagonalWinConditionWorksForPlayerTwo() throws IOException { 
		
		Socket playerOneSocket = null;
		Socket playerTwoSocket = null;
		
		GameSession gs = new GameSession(playerOneSocket, playerTwoSocket);
		
		gs.board[0][0] = 2;
		gs.board[1][1] = 2;
		gs.board[2][2] = 2;
		gs.board[3][3] = 2;
		gs.board[4][4] = 2;
		
		assertTrue(gs.didPlayerWin(0, 0));
	}
	
	@Test
	public void checkIfBackwardsDiagonalWinConditionWorksForPlayerTwo() throws IOException { 
		
		Socket playerOneSocket = null;
		Socket playerTwoSocket = null;
		
		GameSession gs = new GameSession(playerOneSocket, playerTwoSocket);
		
		gs.board[4][0] = 2;
		gs.board[3][1] = 2;
		gs.board[2][2] = 2;
		gs.board[1][3] = 2;
		gs.board[0][4] = 2;
		
		assertTrue(gs.didPlayerWin(4, 0));
	}
}
