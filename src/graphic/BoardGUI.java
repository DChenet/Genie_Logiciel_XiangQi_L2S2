package graphic;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import stats.Match;
import test.io.InOutParameters;

/**
 * This is the game window, BoardGUI extends JFrame. When this window is closed,
 * the game closes. This window is displayed when clicking on the play button in
 * {@link GraphicsMainMenu} and contains {@link PlayerBoard, BoardGUI}.
 * 
 * @see PlayerBoard
 * @see BoardGUI
 * @author Alexis CHOLLET, Almamy CAMARA
 *
 */
public class BoardGUI extends JFrame {

	// Dimensions of the window
	private static final Dimension BOARD_DIMENSIONS = new Dimension(700, 550);
	private static final Dimension MAIN_DIMENSIONS = new Dimension(1020, 770);

	private static final long serialVersionUID = 1L;

	/**
	 * The playing board
	 * 
	 * @see BoardGraphic
	 */
	private BoardGraphic board = null;

	/**
	 * the two player board displayed on the sides.
	 * 
	 * @see PlayerBoard
	 */
	private PlayerBoard firstplayerboard = null;
	private PlayerBoard secondplayerboard = null;

	/**
	 * These can be only one single instance of the game.
	 * 
	 * @see BoardGraphic
	 * @see Match
	 */
	private static BoardGUI instance = null;

	// The announcer on the top of the window tells when a player is checked and
	// when a player has won.
	private JLabel announcer = new JLabel();

	private BoardGUI() {
		super("Xiang Qi");
		board = BoardGraphic.getInstance();
		firstplayerboard = new PlayerBoard(Match.getInstance().getCurrentplayer());
		secondplayerboard = new PlayerBoard(Match.getInstance().getWaitingplayer());

		this.setIconImage(new ImageIcon(InOutParameters.LOGO_ICON_PATH).getImage());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container contentPane = getContentPane();
		init(contentPane);
		initBoard(contentPane);
		initPlayerBoards(contentPane);
		updateAnnouncer();
		Image logoicon;
		
		try {
			logoicon = ImageIO.read(new File("src/textures/menu/logo.png"));
			this.setIconImage(logoicon);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Creating the instance of the window.
	 * 
	 * @see GraphicsMainMenu
	 */
	public static void start() {
		instance = new BoardGUI();
	}

	public static BoardGUI getInstance() {
		return instance;
	}

	private void init(Container contentPane) {
		pack();
		setLayout(null);
		setVisible(true);
		setSize(MAIN_DIMENSIONS);
		setResizable(false);

	}

	public void updateAnnouncer() {
		Container contentpane = this.getContentPane();
		Font fxiangqi = new Font("Segoe script", Font.PLAIN, 30);
		announcer.setFont(fxiangqi);

		if (Match.getInstance().getPlayer1().getIschecked()) {
			announcer.setText(Match.getInstance().getPlayer1().getName() + " is checked");
		}

		else if (Match.getInstance().getPlayer2().getIschecked()) {
			announcer.setText(Match.getInstance().getPlayer2().getName() + " is checked");
		}

		else {
			announcer.setText("");
		}

		if (Match.getInstance().getWinner() != null) {
			announcer.setText(Match.getInstance().getWinner().getName() + " wins !");
		}

		announcer.setBounds(350, 20, 400, 50);
		contentpane.add(announcer);
		this.repaint();
	}

	private void initBoard(Container contentPane) {
		board.setPreferredSize(BOARD_DIMENSIONS);
		board.setBounds(200, 100, 600, 550);
		contentPane.add(board);
	}

	private void initPlayerBoards(Container contentPane) {
		firstplayerboard.setBounds(0, 20, 1000, 740);
		secondplayerboard.setBounds(850, 20, 1000, 740);
		contentPane.add(firstplayerboard);
		contentPane.add(secondplayerboard);
	}

}
