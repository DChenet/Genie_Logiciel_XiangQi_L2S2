package test.io;

import java.util.Iterator;
import java.util.Set;

import board.components.PieceModel;
import compiler.interpreters.GroundSettingsCompiler;
import compiler.interpreters.PieceSettingsCompiler;
import repositories.PiecesRepository;
import rules.Rule;
import strategy.data.MovementPatern;

public class TestPieceCompiler {

	public static void main(String[] args) {

		GroundSettingsCompiler gsc = new GroundSettingsCompiler();
		gsc.loadSettings(InOutParameters.GROUND_SETTING_PATH);

		PieceSettingsCompiler psc = new PieceSettingsCompiler();
		psc.loadSettings(InOutParameters.MOVEMENT_SETTINGS_PATH);

		Set<String> types = PiecesRepository.getInstance().getPieces().keySet();
		Iterator<String> typesit = types.iterator();

		System.out.println("==" + types.size() + " entries==");

		String type;
		PieceModel piece = null;
		int paterncount = 0;

		while (typesit.hasNext()) {
			type = typesit.next();

			piece = PiecesRepository.getInstance().getPiece(type);
			System.out.println("-------Piece type:" + type);
			System.out.println("TEXTURE: "+piece.getRedtexture().getAbsolutePath());

			for (Rule rule : piece.getRules()) {
				System.out.println(rule.toString());
			}

			MovementPatern patern = null;
			Iterator<MovementPatern> paternlistit = piece.getMovementpaterns().iterator();

			while (paternlistit.hasNext()) {
				patern = paternlistit.next();

				paterncount++;
				System.out.println("-----Patern " + paterncount + "\n" + patern.toString());
			}

			paterncount = 0;
		}

	}

}
