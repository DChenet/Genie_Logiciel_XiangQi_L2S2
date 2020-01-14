package repositories;

import java.util.HashMap;

import board.components.Piece;
import board.components.PieceModel;
import compiler.interpreters.PieceSettingsCompiler;

/**
 * This repository uses the singleton pattern, it contains all the objects
 * {@link PieceModel} created during the compilation process
 * {@link PieceSettingsCompiler}. These piece models are then used to create
 * {@link Piece} objects easily.
 * 
 * @see PieceModel
 * @see PieceSettingsCompiler
 * @see Piece
 * @author Dorian CHENET
 *
 */
public class PiecesRepository {

	private HashMap<String, PieceModel> pieces = new HashMap<String, PieceModel>();

	private static PiecesRepository instance = new PiecesRepository();

	private PiecesRepository() {

	}

	public HashMap<String, PieceModel> getPieces() {
		return pieces;
	}

	public static PiecesRepository getInstance() {
		return instance;
	}

	public PieceModel getPiece(String name) {
		return pieces.get(name);
	}

	public void register(PieceModel piece) {
		pieces.put(piece.getType(), piece);
	}

	public boolean exists(String name) {
		return !(pieces.get(name) == null);
	}
}
