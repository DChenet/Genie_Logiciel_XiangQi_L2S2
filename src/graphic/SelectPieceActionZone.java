package graphic;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import board.components.Piece;
import strategy.data.Coordinates;

/**
 * This class extends {@link ActionZone}, it is used in {@link BoardGraphic} to
 * be able to pick up a {@link Piece} by holding a click, the player will then
 * be able to release the click in an {@link DeplacementActionZone} to make a
 * move.
 * 
 * @see ActionZone
 * @see BoardGraphic
 * @see Piece
 * @see DeplacementActionZone
 * @author Dorian CHENET
 *
 */
public class SelectPieceActionZone extends ActionZone {

	private Piece piece = new Piece();

	private boolean clicked = false;

	public SelectPieceActionZone(int x1, int y1, int x2, int y2, Piece piece) {
		super(x1, y1, x2, y2);
		this.piece = piece;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		int xmouse = arg0.getX();
		int ymouse = arg0.getY();

		// Checking if the click action is in the bounds of the clickable zone.
		if (xmouse >= super.getX1() && xmouse <= super.getX2() && ymouse > super.getY1() && ymouse < super.getY2()
				&& !clicked) {

			clicked = true;
			int length = BoardGraphic.SLOT_LENGTH;
			int heigth = BoardGraphic.SLOT_HEIGTH;

			for (Coordinates coordonates : piece.getPossiblemoves()) {
				if (!BoardGraphic.getActionZones().contains(coordonates)) {
					DeplacementActionZone dpzone = new DeplacementActionZone(coordonates.getX() * length,
							coordonates.getY() * heigth, coordonates.getX() * length + length,
							coordonates.getY() * heigth + heigth, piece, coordonates);
					BoardGraphic.getInstance().addMouseListener(dpzone);
					BoardGraphic.getDeplacementzones().add(dpzone);
					BoardGraphic.getActionZones().add(coordonates);

					// When a piece is picked up, the appearance of the cursor
					// changes to the texture of the piece the player just
					// picked up so that the player doesn't forget which piece
					// he is moving.
					Toolkit toolkit = Toolkit.getDefaultToolkit();
					Image image = toolkit.getImage(piece.getTexture().getPath());
					Cursor cursor = toolkit.createCustomCursor(image, new Point(0, 0), "piececursor");
					BoardGraphic.getInstance().setCursor(cursor);

					BoardGraphic.getInstance().setSelectedpiece(piece);
				}
			}

			BoardGraphic.getInstance().repaint();

		}
	}

	public void mouseReleased(MouseEvent e) {
		clicked = false;

		// When the mouse is released, the cursor has to get back to its normal
		// appearance.
		BoardGraphic.getInstance().setCursor(Cursor.getDefaultCursor());
	}
}
