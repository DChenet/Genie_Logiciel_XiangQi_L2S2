package graphic;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import compiler.interpreters.BoardSettingsCompiler;
import compiler.interpreters.GroundSettingsCompiler;
import compiler.interpreters.PieceSettingsCompiler;
import engine.MainEngine;
import intelligence.Chesster;
import stats.Match;
import test.io.InOutParameters;

/**
 * This class is the main menu of the game, in this screen you can choose
 * options like playing against an other player or against an AI
 * {@link Chesster} or changing the playing board. You can then launch the game
 * by clicking on the play button, when you do so the game window pups up
 * {@link BoardGUI}.
 * 
 * @see Chesster
 * @see BoardGUI
 * @author Alexis CHOLLET, Almamy CAMARA
 *
 */
public class GraphicsMainMenu extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String COPYRIGHT = "\u00a9";

	// The labels telling you what options are selected
	private JLabel selectedboardlabel = new JLabel();
	private JLabel selectedplayerlabel = new JLabel();

	// Copyright label on the bottom
	private JLabel copyright = new JLabel();

	// The buttons that allow you to choose between the different boards.
	private JButton plain = new JButton();
	private JButton ice = new JButton();
	private JButton sand = new JButton();
	private JButton mountain = new JButton();

	// The button to launch the game.
	private JButton playbutton = new JButton();

	// The name of the game.
	private JLabel xiangqi = new JLabel();

	/**
	 * the buttons that allow you to choose if you want to play against on other
	 * player or against an AI {@link Chesster} {@link Match}.
	 */
	private JButton oneplayerbutton = new JButton();
	private JButton twoplayerbutton = new JButton();

	// Strings to save the selected options?
	private String selectedboard = "Plaine";
	private String selectedmode = "PvP";

	// The font of the game.
	Font fxiangqi = new Font("Segoe script", Font.PLAIN, 50);

	public GraphicsMainMenu() {
		initLayout();

		initStyle();

	}

	// Initializing the JFrame
	protected void initLayout() {
		setTitle("Xiang Qi");
		setResizable(false);
		setSize(900, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		Image logoicon;
		
		try {
			logoicon = ImageIO.read(new File("src/textures/menu/logo.png"));
			this.setIconImage(logoicon);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// initializing the frame components.
	protected void initStyle() {

		Container mainpanel = this.getContentPane();
		mainpanel.setLayout(null);
		mainpanel.setBackground(Color.WHITE);

		// Copyright

		copyright.setBounds(165, 550, 570, 30);
		copyright.setText("Copyright " + COPYRIGHT
				+ " Alexis Chollet - Dorian Chenet - Almamy Camara - 2019, GLP_CHESS - All rights reserved");
		mainpanel.add(copyright);

		// Plain Button

		plain.setBounds(700, 150, 150, 70);
		plain.setIcon(new ImageIcon("src/textures/menu/bouton_plaine.png"));
		plain.addActionListener(new SetSelectedBoardAction("Plaine"));
		mainpanel.add(plain);

		// Ice Button

		ice.setBounds(700, 240, 150, 70);
		ice.setIcon(new ImageIcon("src/textures/menu/bouton_glacier.png"));
		ice.addActionListener(new SetSelectedBoardAction("Glacier"));
		mainpanel.add(ice);

		// Sand Button

		sand.setBounds(700, 330, 150, 70);
		sand.setIcon(new ImageIcon("src/textures/menu/bouton_sand.png"));
		sand.addActionListener(new SetSelectedBoardAction("Désert"));
		mainpanel.add(sand);

		// Mountain Button

		mountain.setBounds(700, 420, 150, 70);
		mountain.setIcon(new ImageIcon("src/textures/menu/bouton_montagne.png"));
		mountain.addActionListener(new SetSelectedBoardAction("Montagne"));
		mainpanel.add(mountain);

		// Play button

		playbutton.setBounds(375, 200, 150, 150);
		playbutton.setIcon(new ImageIcon("src/textures/menu/logo.png"));
		playbutton.addActionListener(new LancerJeu());
		mainpanel.add(playbutton);

		// Game name

		xiangqi.setText("Xiang Qi");
		xiangqi.setFont(fxiangqi);
		xiangqi.setBounds(325, 25, 300, 100);
		mainpanel.add(xiangqi);

		// Selection of the player vs AI mode

		oneplayerbutton.setText("IA");
		oneplayerbutton.setBounds(50, 250, 150, 50);
		oneplayerbutton.addActionListener(new SetSelectedModeAction("IA"));
		mainpanel.add(oneplayerbutton);

		// Selection of the player vs player mode (by default)

		twoplayerbutton.setText("PvP");
		twoplayerbutton.setBounds(50, 350, 150, 50);
		twoplayerbutton.addActionListener(new SetSelectedModeAction("PvP"));
		mainpanel.add(twoplayerbutton);

		// Labels informing the player of the selected options.

		selectedboardlabel.setText("Plaine");
		selectedboardlabel.setFont(new Font("Segoe script", Font.PLAIN, 45));
		selectedboardlabel.setBounds(360, 325, 500, 150);
		mainpanel.add(selectedboardlabel);

		selectedplayerlabel.setText("PvP");
		selectedplayerlabel.setFont(new Font("Segoe script", Font.PLAIN, 45));
		selectedplayerlabel.setBounds(360, 400, 500, 150);
		mainpanel.add(selectedplayerlabel);

		mainpanel.repaint();
	}

	// Action to launch the game, associated to the play button.
	private class LancerJeu implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String boardpath = "";

			/**
			 * If the player has chosen to play against an AI, set up
			 * {@link Chesster} as the second player {@link Match}.
			 */
			if (selectedmode.equals("IA")) {
				Match.getInstance().setIA();
			}

			/**
			 * Setting the board path as the absolute path of the text file
			 * associated with the selected board for compilation.
			 * 
			 * @see InOutParameters
			 */
			if (selectedboard.equals("Plaine")) {
				boardpath = InOutParameters.BOARD_SETTING_PATH;
			}

			else if (selectedboard.equals("Desert")) {
				boardpath = InOutParameters.BOARD_SAND_SETTING_PATH;
			}

			else if (selectedboard.equalsIgnoreCase("Glacier")) {
				boardpath = InOutParameters.BOARD_ICE_SETTING_PATH;
			}

			else if (selectedboard.equals("Montagne")) {
				boardpath = InOutParameters.BOARD_MONTAIN_SETTING_PATH;
			}

			/**
			 * Compiling the settings relatives to the ground types.
			 * 
			 * @see Ground
			 * @see GroundSettingsCompiler
			 * @see InOutParameters
			 */
			GroundSettingsCompiler gsc = new GroundSettingsCompiler();
			gsc.loadSettings(InOutParameters.GROUND_SETTING_PATH);

			/**
			 * Compiling the settings relatives to the pieces.
			 * 
			 * @see Piece
			 * @see PieceSettingsCompiler
			 * @see InOutParameters
			 */
			PieceSettingsCompiler psc = new PieceSettingsCompiler();
			psc.loadSettings(InOutParameters.MOVEMENT_SETTINGS_PATH);

			/**
			 * Compiling the settings relative to the selected playing board.
			 * 
			 * @see Board
			 * @see BoardSettingsCompiler
			 */
			BoardSettingsCompiler bsc = new BoardSettingsCompiler();
			bsc.loadSettings(boardpath);

			/**
			 * Initializing the game for the first turn.
			 * 
			 * @see MainEngine
			 */
			MainEngine.engine.newTurn();

			/**
			 * Creating the game window.
			 * 
			 * @see BoardGUI
			 */
			BoardGUI.start();

		}

	}

	// Action associated to the board selecting buttons.
	private class SetSelectedBoardAction implements ActionListener {
		private String board;

		private SetSelectedBoardAction(String board) {
			// TODO Auto-generated constructor stub
			this.board = board;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			setSelectedBoard(board);
		}

	}

	// Action associated to the game mode selection buttons.
	private class SetSelectedModeAction implements ActionListener {
		private String mode;

		private SetSelectedModeAction(String mode) {
			// TODO Auto-generated constructor stub
			this.mode = mode;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			setSelectedMode(mode);
		}

	}

	private void setSelectedBoard(String board) {
		selectedboard = board;
		selectedboardlabel.setText(board);
	}

	private void setSelectedMode(String mode) {
		selectedmode = mode;
		selectedplayerlabel.setText(mode);
	}
}
