package stats;

import java.util.ArrayList;

import board.Board;
import board.components.Piece;
import engine.MainEngine;
import intelligence.Chesster;

/**
 * This class uses the singleton patern.
 * All the data concerning chess matches is stored here.
 * @author Dorian CHENET
 *
 */
public class Match {

	//Only one match at a time is possible.
	private static Match instance = new Match();

	//The match must have a playing board.
	private static Board board = new Board();

	//The match opposes two players, player 2 can be an AI.
	private Player player1 = new Player("Player 1", MatchParameters.RED_COLOR, new ArrayList<Piece>(),
			new ArrayList<Piece>(), new ArrayList<Piece>());
	private Player player2 = new Player("Player 2",MatchParameters.BLACK_COLOR, new ArrayList<Piece>(),
			new ArrayList<Piece>(), new ArrayList<Piece>());
	
	//The curent player is the player curently playing.
	private Player currentplayer = player1;
	
	//The waiting player is  the player who has played.
	private Player waitingplayer = player2;
	
	//The winner of the match.
	private Player winner = null;
	
	//The number of turns.
	private int turnnumber = 0;

	private Match() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * this method is used to setup the AI.
	 * @see Chesster
	 */
	public void setIA(){
		player2 = new Chesster(MatchParameters.BLACK_COLOR, new ArrayList<Piece>(),
				new ArrayList<Piece>(), new ArrayList<Piece>());
		waitingplayer = player2;
	}

	/**
	 * This method is used to swap players when the current player has played.
	 * @see MainEngine
	 */
	public void swap() {
		if (currentplayer.equals(player1)) {
			currentplayer = player2;
			waitingplayer = player1;
		} else if (currentplayer.equals(player2)) {
			currentplayer = player1;
			waitingplayer = player2;
		}
	}

	//This method is used to get a player of a given color easily.
	public Player getColorPlayer(String color) {
		if (player1.getColor().equals(color)) {
			return player1;
		} else if (player2.getColor().equals(color)) {
			return player2;
		} else {
			return null;
		}
	}

	public int getTurnnumber() {
		return turnnumber;
	}

	public void setTurnnumber(int turnnumber) {
		this.turnnumber = turnnumber;
	}
	
	public static Board getBoard() {
		return board;
	}

	public static void setBoard(Board board) {
		Match.board = board;
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public Player getCurrentplayer() {
		return currentplayer;
	}

	public void setCurrentplayer(Player curentplayer) {
		this.currentplayer = curentplayer;
	}

	public static Match getInstance() {
		return instance;
	}

	public Player getWaitingplayer() {
		return waitingplayer;
	}

	public void setWaitingplayer(Player waitingplayer) {
		this.waitingplayer = waitingplayer;
	}

	public static void setInstance(Match instance) {
		Match.instance = instance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentplayer == null) ? 0 : currentplayer.hashCode());
		result = prime * result + ((player1 == null) ? 0 : player1.hashCode());
		result = prime * result + ((player2 == null) ? 0 : player2.hashCode());
		result = prime * result + turnnumber;
		result = prime * result + ((waitingplayer == null) ? 0 : waitingplayer.hashCode());
		result = prime * result + ((winner == null) ? 0 : winner.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Match other = (Match) obj;
		if (currentplayer == null) {
			if (other.currentplayer != null)
				return false;
		} else if (!currentplayer.equals(other.currentplayer))
			return false;
		if (player1 == null) {
			if (other.player1 != null)
				return false;
		} else if (!player1.equals(other.player1))
			return false;
		if (player2 == null) {
			if (other.player2 != null)
				return false;
		} else if (!player2.equals(other.player2))
			return false;
		if (turnnumber != other.turnnumber)
			return false;
		if (waitingplayer == null) {
			if (other.waitingplayer != null)
				return false;
		} else if (!waitingplayer.equals(other.waitingplayer))
			return false;
		if (winner == null) {
			if (other.winner != null)
				return false;
		} else if (!winner.equals(other.winner))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Match [player1=" + player1 + ", player2=" + player2 + ", curentplayer=" + currentplayer
				+ ", waitingplayer=" + waitingplayer + ", winner=" + winner + ", turnnumber=" + turnnumber + "]";
	}

}
