package graphic;

import java.awt.event.MouseEvent;

import board.components.Piece;
import engine.MainEngine;
import stats.Match;
import strategy.data.Coordinates;

/**
 * This class extends {@link ActionZone}, it is a MouseAction which reacts when
 * the player releases a click. In {@link BoardGraphic}, a player can pick up a
 * {@link Piece} by clicking on it an holding the click. Then the player can
 * release the click in a DeplacementActionZone to make a move. The
 * DeplacementActionZones can be visualized as yellow / blue dots on the board
 * when holding a piece, these dots also represent all the possible moves for
 * the piece the player is holding.
 * 
 * @see ActionZone
 * @see BoardGraphic
 * @see Piece
 * @author Dorian CHENET
 */
public class DeplacementActionZone extends ActionZone {

	private Piece piece = new Piece();
	private Coordinates coordonates = new Coordinates();

	public DeplacementActionZone(int x1, int y1, int x2, int y2, Piece piece, Coordinates coordonates) {
		super(x1, y1, x2, y2);
		this.piece = piece;
		this.coordonates = coordonates;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		int xmouse = arg0.getX();
		int ymouse = arg0.getY();

		// Checking is the mouse is in the action zone
		if (xmouse >= super.getX1() && xmouse <= super.getX2() && ymouse > super.getY1() && ymouse < super.getY2()) {

			Piece enemypiece = Match.getInstance().getWaitingplayer().pieceAtCoordonates(coordonates);

			// If the move is "taking an enemy piece"
			if (enemypiece != null) {

				// Adding the enemy piece to the enemy's lost pieces
				Match.getInstance().getWaitingplayer().getLostpieces().add(enemypiece);

				// Removing the piece for the piece the enemy player can play.
				Match.getInstance().getWaitingplayer().getPieces().remove(enemypiece);

				// adding the piece to the taken pieces of the player who made
				// the move.
				Match.getInstance().getCurrentplayer().getTakenpieces().add(enemypiece);

				// Moving the piece on the board (overriding the enemy piece).
				Match.getBoard().movePiece(piece.getCoordonates(), coordonates);

			} else {

				// If no enemy piece is taken by the move, only move the piece.
				Match.getBoard().movePiece(piece.getCoordonates(), coordonates);
			}

			// Clearing all the action zones
			BoardGraphic.getInstance().clearPieceSelectionZones();

			// Making the enemy player play
			MainEngine.engine.newTurn();
		}

		// Repainting the graphics.
		BoardGraphic.getInstance().repaint();
		BoardGraphic.getInstance().clearDeplacementZones();
		BoardGraphic.getActionZones().clear();
		BoardGUI.getInstance().repaint();
		BoardGUI.getInstance().updateAnnouncer();

	}

	public Coordinates getCoordonates() {
		return coordonates;
	}

}
