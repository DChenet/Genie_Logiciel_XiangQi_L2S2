package compiler.builders;

import java.util.ArrayList;

import board.components.PieceModel;
import compiler.exceptions.UndeclaredDefaultMovementsException;
import compiler.interpreters.PieceSettingsCompiler;
import compiler.lang.Grammar;
import strategy.data.MovementPatern;
import strategy.data.TaggedPath;
import tags.MovementTag;

/**
 * This treatment class builds movement paterns {@link MovementPatern} from a
 * list of {@link TaggedPath}. A MovementPaternBuilder is used in
 * {@link PieceSettingsCompiler} to create a list of MovementPaterns thus to
 * help setting the movementpaterns attribute of the {@link PieceModel} more
 * easily.
 * 
 * @see MovementPatern
 * @see TaggedPath
 * @see PieceSettingsCompiler
 * @see PieceModel
 * 
 * @author Dorian CHENET
 *
 */
public class MovementPaternBuilder {

	public MovementPaternBuilder() {

	}

	/**
	 * This method takes a list of {@link TaggedPath} and returns a list of
	 * {@link MovementPatern}. It uses the {@link MovementTag} attribute of the
	 * TaggedPaths to make groups of paths which we call MovementPaterns.
	 * 
	 * @see TaggedPath
	 * @see MovementPatern
	 * @see MovementTag
	 * 
	 * @param pathlist
	 * @return
	 * @throws UndeclaredDefaultMovementsException
	 */
	public ArrayList<MovementPatern> buildMovementPaternList(ArrayList<TaggedPath> pathlist)
			throws UndeclaredDefaultMovementsException {

		ArrayList<MovementPatern> paternlist = new ArrayList<MovementPatern>();
		MovementPatern patern = null;
		Boolean checkdefaultmoves = false;

		ArrayList<MovementTag> taglist = new ArrayList<MovementTag>();

		// Recovering all the existing tags from all the tags contained in the
		// tagged paths.
		for (TaggedPath path : pathlist) {
			if (!taglist.contains(path.getTag())) {
				taglist.add(path.getTag());
			}
		}

		// Regrouping the tagged paths into movement paterns.
		for (MovementTag tag : taglist) {

			patern = new MovementPatern();
			patern.setTag(tag);

			// Filling the movement patern with the paths associated to the
			// corresponding tag.
			for (TaggedPath path : pathlist) {
				if (path.getTag().equals(tag)) {
					patern.addMove(path.getPath());
				}

				if (tag.getGroundtype().equals(Grammar.DEFAULT_GROUND)) {
					checkdefaultmoves = true;
				}
			}

			/**
			 * If no default movement patern has been set, sends an exception.
			 * Default movement paterns are used on every type of ground if no
			 * indications have been set about a specific ground type.
			 * 
			 * @see Ground
			 */
			if (!checkdefaultmoves) {
				throw new UndeclaredDefaultMovementsException(tag.getPiecetype());
			}

			else if (patern.getPathlist().size() != 0) {
				paternlist.add(patern);
			}
		}

		if (paternlist.isEmpty()) {
			return null;
		}

		else {
			return paternlist;
		}
	}
}
