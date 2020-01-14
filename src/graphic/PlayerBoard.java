package graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import board.components.Piece;
import stats.Match;
import stats.MatchParameters;
import stats.Player;

/**
 * This class is a graphic container (extends JPanel). Player boards are used on
 * the sides of the game interface {@link BoardGUI} to display the pieces taken
 * by each player.
 * 
 * @see BoardGUI
 * @see Piece
 * @author Alexis CHOLLET
 *
 */
public class PlayerBoard extends JPanel {

	Font fontjoueur = new Font("Arial", Font.PLAIN, 20);

	// A player is associated to each player board.
	Player player = null;

	private static final long serialVersionUID = 1L;

	public PlayerBoard(Player player) {
		this.player = player;
	}

	public void paintComponent(Graphics g) {
		super.paintComponents(g);

		// Left Border
		g.drawLine(10, 0, 150, 0);
		g.drawLine(10, 0, 10, 680);
		g.drawLine(10, 680, 150, 680);
		g.drawLine(150, 0, 150, 680);

		// Players
		g.setFont(fontjoueur);
		
		if(player.getColor().equals(MatchParameters.RED_COLOR)){
			g.setColor(Color.RED);
		}
		g.drawString(player.getName(), 30, 25);

		paintTakenPieces(g);
		paintTurnIndicator(g);
	}
	
	private void paintTurnIndicator(Graphics g){
		if(player.equals(Match.getInstance().getCurrentplayer())){
			g.setColor(Color.GREEN);
			g.fillOval(120, 10, 15, 15);
		}
	}

	// this method paints all the pieces taken by the associated player in the
	// player board panel.
	public void paintTakenPieces(Graphics g) {
		int linepos = 0;
		int columnpos = 0;

		for (Piece piece : player.getTakenpieces()) {
			linepos += 50;
			if (linepos % 800 == 0 && linepos != 0) {
				columnpos += 60;
				linepos = 50;
			}

			Image pieceimage;

			try {
				if (piece != null) {
					pieceimage = ImageIO.read(piece.getTexture().getAbsoluteFile());
					g.drawImage(pieceimage, columnpos + 30, linepos, 50, 50, this);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
