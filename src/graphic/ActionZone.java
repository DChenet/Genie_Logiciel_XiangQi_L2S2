package graphic;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This class implements {@link MouseListener} and represents a clickable zone
 * on a graphic panel. It is the superclass of {@link SelectPieceActionZone} and
 * {@link DeplacementActionZone}.
 * 
 * @see SelectPieceActionZone
 * @see DeplacementActionZone
 * @author Dorian CHENET
 *
 */
public class ActionZone implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Top left corner of the clickable zone.
	private int x1 = 0;
	private int y1 = 0;

	// Bottom right corner of the zone.
	private int x2 = 0;
	private int y2 = 0;

	public ActionZone(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

}
