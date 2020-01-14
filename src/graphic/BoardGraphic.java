package graphic;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import board.BoardParameters;
import board.components.Ground;
import board.components.Piece;
import compiler.lang.DefaultSettings;
import stats.Match;
import stats.MatchParameters;
import strategy.data.Coordinates;

/**
 * This class is a graphic container (extends JPanel) in which will be painted
 * the playing board and to which will be associated the different
 * MouseListeners {@link ActionZone, SelectPieceActionZone,
 * DeplacementActionZone}. This class is used in the game window
 * {@link BoardGUI}. This class uses the singleton pattern, there can be only
 * one game at a time {@link Match} so a single board displayed at a time. The
 * board displayed depends of the previously selected board in
 * {@link GraphicsMainMenu}.
 * 
 * @see ActionZone
 * @see SelectPieceActionZone
 * @see DeplacementActionZone
 * @see BoardGUI
 * @see GraphicsMainMenu
 * @see Match
 * @author Alexis CHOLLET, Almamy CAMARA
 *
 */
public class BoardGraphic extends JPanel {

	// Instance of the board.
	private static BoardGraphic instance = new BoardGraphic();

	// Dimensions of the board.
	public static final int LENGTH = 600;
	public static final int HEIGTH = 550;

	// The dimensions of a slot in the board
	public static final int SLOT_LENGTH = (int) LENGTH / BoardParameters.BOARD_X_LENGTH;
	public static final int SLOT_HEIGTH = (int) HEIGTH / BoardParameters.BOARD_Y_LENGTH;

	// Offsets to display the pieces in the slots
	private static final int PIECE_X_OFFSET = 6;
	private static final int PIECE_Y_OFFSET = 6;

	// Painting the player's covered zones for debug
	private final Boolean PAINT_COVERED_ZONES = false;

	/**
	 * The piece selected by the player.
	 * 
	 * @see SelectPieceActionZone
	 */
	private Piece selectedpiece = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Referencing all the action listeners on the board so we can remove them
	// when the player has finished his turn/when a move is not accepted.
	private static ArrayList<SelectPieceActionZone> piecezones = new ArrayList<SelectPieceActionZone>();
	private static ArrayList<DeplacementActionZone> deplacementzones = new ArrayList<DeplacementActionZone>();
	private static ArrayList<Coordinates> actionzones = new ArrayList<Coordinates>();

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.removeAll();

		/**
		 * Painting the ground textures.
		 * 
		 * @see Ground
		 */
		paintEnvironement(g);

		// Painting the grid
		paintGrid(g);

		// Painting the covered zones for debug (if enabled).
		if (PAINT_COVERED_ZONES) {
			paintCoveredZone(g);
		}

		// Painting the check indicator if the current player is checked
		if (Match.getInstance().getCurrentplayer().getIschecked()) {
			paintCheckIndicator(g);
		}

		// Painting the units above everything else.
		paintUnits(g);

		// Painting the deplacement points if the player is holding a piece.
		paintDeplacementPoints(g);
	}

	/**
	 * Painting the ground textures.
	 * 
	 * @see Ground
	 * @param g
	 */
	private void paintEnvironement(Graphics g) {

		for (int index1 = 0; index1 < BoardParameters.BOARD_X_LENGTH; index1++) {
			for (int index2 = 0; index2 < BoardParameters.BOARD_Y_LENGTH; index2++) {
				Image groundtexture;
				try {
					if (Match.getBoard().getGround(index1, index2) == null) {
						groundtexture = ImageIO.read(DefaultSettings.DEFAULT_TEXTURE);
					} else {
						groundtexture = ImageIO.read((Match.getBoard().getGround(index1, index2).getTexture()));
					}
					g.drawImage(groundtexture, index1 * SLOT_LENGTH, index2 * SLOT_HEIGTH, SLOT_LENGTH, SLOT_HEIGTH,
							this);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// Painting the grid
	private void paintGrid(Graphics g) {
		g.setColor(Color.BLACK);
		for (int index1 = 0; index1 <= BoardParameters.BOARD_X_LENGTH; index1++) {
			for (int index2 = 0; index2 <= BoardParameters.BOARD_Y_LENGTH; index2++) {
				if (index1 == BoardParameters.BOARD_X_LENGTH / 2) {
					g.setColor(Color.BLUE);
					g.drawLine(index1 * SLOT_LENGTH, 0, index1 * SLOT_LENGTH, HEIGTH);
				} else {
					g.setColor(Color.BLACK);
					g.drawLine(index1 * SLOT_LENGTH, 0, index1 * SLOT_LENGTH, HEIGTH);
					g.drawLine(0, index2 * SLOT_HEIGTH, LENGTH, index2 * SLOT_HEIGTH);
				}
			}
		}
	}

	/**
	 * Painting the units.
	 * 
	 * @see Piece
	 * @param g
	 */
	public void paintUnits(Graphics g) {
		try {
			Image piecetexture;

			Graphics2D g2d = (Graphics2D) g;

			for (int index1 = 0; index1 < BoardParameters.BOARD_X_LENGTH; index1++) {
				for (int index2 = 0; index2 < BoardParameters.BOARD_Y_LENGTH; index2++) {

					Piece piece = Match.getBoard().getPiece(index1, index2);

					if (selectedpiece == null || !selectedpiece.equals(piece)) {
						if (piece != null) {
							// Setting the piece graphic to a transparent color
							// when the piece is held by the player so he
							// doesn't forget which piece he is moving.
							g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
							piecetexture = ImageIO.read(piece.getTexture());
							g2d.drawImage(piecetexture, index1 * SLOT_LENGTH + PIECE_X_OFFSET,
									index2 * SLOT_HEIGTH + PIECE_Y_OFFSET, 50, 50, this);
						}
					}

					else {
						if (piece != null) {
							piecetexture = ImageIO.read(piece.getTexture());

							// Same thing
							g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
							g2d.drawImage(piecetexture, index1 * SLOT_LENGTH + PIECE_X_OFFSET,
									index2 * SLOT_HEIGTH + PIECE_Y_OFFSET, 50, 50, this);
						}
					}
				}
			}

			// If the player currently playing is the AI then no action zone
			// should be added.
			if (!Match.getInstance().getCurrentplayer().getClass().getName().equals("Chesster")) {
				ArrayList<Piece> pieces = Match.getInstance().getCurrentplayer().getPieces();

				for (Piece piece : pieces) {
					createPieceSelectionZone(piece);
				}

				createPieceSelectionZone(Match.getInstance().getCurrentplayer().getWinconditionpiece());
				setSelectedpiece(null);
			}

		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Creating the {@link SelectPieceActionZone} for every piece on the board
	 * that belongs to the player currently playing.
	 *
	 * @see SelectPieceActionZone
	 * @see Piece
	 * @see Match
	 * @param piece
	 */
	private void createPieceSelectionZone(Piece piece) {
		SelectPieceActionZone zone = new SelectPieceActionZone(BoardGraphic.SLOT_LENGTH * piece.getCoordonates().getX(),
				BoardGraphic.SLOT_HEIGTH * piece.getCoordonates().getY(),
				BoardGraphic.SLOT_LENGTH * piece.getCoordonates().getX() + BoardGraphic.SLOT_LENGTH,
				BoardGraphic.SLOT_HEIGTH * piece.getCoordonates().getY() + BoardGraphic.SLOT_HEIGTH, piece);
		Coordinates coord = new Coordinates(piece.getCoordonates().getX(), piece.getCoordonates().getY());
		if (!actionzones.contains(coord)) {
			actionzones.add(coord);
			piecezones.add(zone);
			this.addMouseListener(zone);
		}
	}

	// If a piece is held by the player, paint the deplacement points associated
	// to the piece, i.e displays all the possible moves the player can make
	// with the piece he holds. These points are not displayed if the player
	// doens't hold any piece.
	private void paintDeplacementPoints(Graphics g) {
		for (DeplacementActionZone zone : deplacementzones) {
			Coordinates coordonates = zone.getCoordonates();
			Graphics2D g2d = (Graphics2D) g;
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			if (Match.getBoard().getPiece(coordonates) != null) {
				g2d.setColor(Color.cyan);
			} else {
				g2d.setColor(Color.YELLOW);
			}
			g2d.fillOval(coordonates.getX() * SLOT_LENGTH + 15, coordonates.getY() * SLOT_HEIGTH + 15, 20, 20);
		}
	}

	private void paintCheckIndicator(Graphics g) {
		g.setColor(Color.RED);
		Coordinates coord = new Coordinates(
				Match.getInstance().getCurrentplayer().getWinconditionpiece().getCoordonates().getX(),
				Match.getInstance().getCurrentplayer().getWinconditionpiece().getCoordonates().getY());
		g.fillRect(coord.getX() * SLOT_LENGTH, coord.getY() * SLOT_HEIGTH, SLOT_LENGTH, SLOT_HEIGTH);
	}

	private void paintCoveredZone(Graphics g) {
		if (Match.getInstance().getWaitingplayer().getColor().equals(MatchParameters.RED_COLOR)) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.BLACK);
		}
		for (Piece piece : Match.getInstance().getWaitingplayer().getPieces()) {
			for (Coordinates coord : piece.getPossiblemoves()) {
				g.fillRect(coord.getX() * SLOT_LENGTH, coord.getY() * SLOT_HEIGTH, SLOT_LENGTH, SLOT_HEIGTH);
			}
		}
	}

	public void clearPieceSelectionZones() {
		for (SelectPieceActionZone zone : piecezones) {
			this.removeMouseListener(zone);
		}
		piecezones.clear();
	}

	public void clearDeplacementZones() {
		for (DeplacementActionZone zone : deplacementzones) {
			this.removeMouseListener(zone);
		}
		deplacementzones.clear();
	}

	public void setSelectedpiece(Piece piece) {
		this.selectedpiece = piece;
	}

	public static BoardGraphic getInstance() {
		return instance;
	}

	public static ArrayList<Coordinates> getActionZones() {
		return actionzones;
	}

	public static ArrayList<DeplacementActionZone> getDeplacementzones() {
		return deplacementzones;
	}

	public static ArrayList<SelectPieceActionZone> getPiecezones() {
		return piecezones;
	}

	public static int getLength() {
		return LENGTH;
	}

	public static int getHeigth() {
		return HEIGTH;
	}

	public static int getSlotLength() {
		return SLOT_LENGTH;
	}

	public static int getSlotHeigth() {
		return SLOT_HEIGTH;
	}

	public static int getPieceXOffset() {
		return PIECE_X_OFFSET;
	}

	public static int getPieceYOffset() {
		return PIECE_Y_OFFSET;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
