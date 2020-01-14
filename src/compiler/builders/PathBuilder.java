package compiler.builders;

import java.util.ArrayList;
import java.util.Iterator;

import board.BoardParameters;
import compiler.exceptions.UnknownParameterException;
import compiler.interpreters.PieceSettingsCompiler;
import compiler.lang.DefaultSettings;
import compiler.lang.Grammar;
import stats.MatchParameters;
import strategy.data.Coordinates;
import strategy.data.TaggedPath;
import tags.MovementTag;

/**
 * A PathBuilder is used in the {@link PieceSettingsCompiler} to extract data
 * from a String line and gather it into a set of {@link TaggedPath}.
 * The string line was extracted by 
 * 
 * @see PieceSettingsCompiler
 * @see TaggedPath
 * @author Dorian CHENET
 *
 */

public class PathBuilder {

	private ArrayList<TaggedPath> pathlist = new ArrayList<TaggedPath>();
	private TaggedPath newpath = new TaggedPath();

	// The generation modifier will influence the way the instructions are
	// interpreted / the way the paths are generated.
	private String generationmodifier = DefaultSettings.UNDEFINED_STRING;

	/*
	 * The polarity modifier tells the builder to create a path and it's
	 * "mirror" version. For instance,
	 */
	private boolean polaritymodifier = false;

	private String specification = DefaultSettings.UNDEFINED_STRING;
	private String color = DefaultSettings.UNDEFINED_STRING;

	public PathBuilder() {

	}

	/**
	 * This method extracts the required informations from the String line in
	 * order to build the tagged paths. It then calls the method buildChain().
	 * It also fills the {@link MovementTag} attribute of newpath (TaggedPath)
	 * for referencing purposes.
	 * 
	 * @param line
	 * @return
	 * @throws UnknownParameterException
	 */
	public ArrayList<TaggedPath> buildPath(String line) throws UnknownParameterException {

		// Initialisation
		pathlist = new ArrayList<TaggedPath>();
		newpath = new TaggedPath();
		specification = DefaultSettings.UNDEFINED_STRING;
		color = DefaultSettings.UNDEFINED_STRING;

		//Splitting the line 
		if (line.contains(Grammar.SEPARATOR_PARAMETERS)) {
			String[] informationsplit = line.split(Grammar.SEPARATOR_PARAMETERS);

			for (int index1 = 0; index1 < informationsplit.length; index1++) {
				String parameter = informationsplit[index1];

				if (parameter.equals(Grammar.TAG_STANDARD)) {
					specification = Grammar.TAG_STANDARD;
					newpath.getTag().setSpecification(parameter);
				}

				else if (parameter.equals(Grammar.TAG_ALTERNATIVE)) {
					specification = Grammar.TAG_ALTERNATIVE;
					newpath.getTag().setSpecification(parameter);
				}

				else if (parameter.equals(Grammar.MODIFIER_INFINITE)) {
					generationmodifier = Grammar.MODIFIER_INFINITE;
				}

				else if (parameter.equals(MatchParameters.RED_COLOR)) {
					color = parameter;
				}

				else if (parameter.equals(MatchParameters.BLACK_COLOR)) {
					color = parameter;
				}

				else if (parameter.equals(Grammar.MODIFIER_POLARIZED)) {
					polaritymodifier = true;
				}

				else if (parameter.contains(Grammar.SEPARATOR_COORDONATES)) {
					buildChain(parameter);
				}

				else {
					throw new UnknownParameterException(parameter);
				}
			}
		}

		return pathlist;
	}

	/**
	 * This method builds paths according to the previously set instructions and
	 * adds them to a TaggedPath.
	 * 
	 * @param parameter
	 */
	private void buildChain(String parameter) {

		if (parameter.contains(Grammar.SEPARATOR_PATHING)) {

			String[] coordonatesplit1 = parameter.split(Grammar.SEPARATOR_PATHING);
			String[] coordonatesplit2;

			for (int index2 = 0; index2 < coordonatesplit1.length; index2++) {
				coordonatesplit2 = coordonatesplit1[index2].split(Grammar.SEPARATOR_COORDONATES);

				if (coordonatesplit2.length == 2) {
					addPosition(coordonatesplit2[0], coordonatesplit2[1]);
				}
			}

			addPath();
		}

		else {
			String[] coordonatesplit = parameter.split(Grammar.SEPARATOR_COORDONATES);

			if (coordonatesplit.length == 2) {

				if (generationmodifier.equals(Grammar.MODIFIER_INFINITE)) {
					int index = 0;
					int capindex = 1;

					int ceiling = Math.max(BoardParameters.BOARD_X_LENGTH, BoardParameters.BOARD_Y_LENGTH);

					while (capindex <= ceiling) {

						index = 0;

						while (index < capindex) {
							addPosition(coordonatesplit[0], coordonatesplit[1]);
							index++;
						}

						addPath();
						capindex++;
					}

				} else {
					addPosition(coordonatesplit[0], coordonatesplit[1]);
					addPath();
				}
			}
		}
		modifiersReset();
	}

	private void addPath() {
		if (newpath != null && newpath.size() != 0) {

			if (specification.equals(DefaultSettings.UNDEFINED_STRING)) {
				specification = Grammar.MACRO_ALL;
			}

			if (polaritymodifier) {

				MovementTag tag = new MovementTag();
				tag.setSpecification(specification);

				if (!color.equals(DefaultSettings.UNDEFINED_STRING)) {
					tag.setColor(color);
				} else {
					tag.setColor(MatchParameters.RED_COLOR);
				}

				newpath.setTag(tag);
				pathlist.add(newpath);
				pathlist.add(getMirorPath(newpath));

				newpath = new TaggedPath();
			} else {
				MovementTag tag = new MovementTag();
				tag.setSpecification(specification);

				if (color.equals(DefaultSettings.UNDEFINED_STRING)) {
					tag.setColor(Grammar.MACRO_ALL);
				} else {
					tag.setColor(color);
				}

				newpath.setTag(tag);

				pathlist.add(newpath);
				newpath = new TaggedPath();
			}
		}
	}

	private void addPosition(String xcoordonate, String ycoordonate) {
		if (isConstant(xcoordonate) && isConstant(ycoordonate)) {
			int parsedxcoordonate = (int) Integer.parseInt(xcoordonate);
			int parsedycoordonate = (int) Integer.parseInt(ycoordonate);
			Coordinates coordonates = new Coordinates(parsedxcoordonate, parsedycoordonate);
			// System.out.println(coordonates.toString());
			newpath.addPosition(coordonates);
		}
	}

	private TaggedPath getMirorPath(TaggedPath path) {
		TaggedPath mirorpath = new TaggedPath();

		if (path.getTag().getColor().equals(MatchParameters.RED_COLOR)) {
			mirorpath.setTag(new MovementTag(path.getTag().getPiecetype(), path.getTag().getGroundtype(),
					path.getTag().getSpecification(), MatchParameters.BLACK_COLOR));
		}

		else if (path.getTag().getColor().equals(MatchParameters.BLACK_COLOR)) {
			mirorpath.setTag(new MovementTag(path.getTag().getPiecetype(), path.getTag().getGroundtype(),
					path.getTag().getSpecification(), MatchParameters.RED_COLOR));
		}

		mirorpath.getTag().setSpecification(path.getTag().getSpecification());

		Coordinates coordonates = null;
		Iterator<Coordinates> coordonatesit = path.getPath().iterator();

		while (coordonatesit.hasNext()) {
			coordonates = coordonatesit.next();
			mirorpath.addPosition(new Coordinates(-coordonates.getX(), -coordonates.getY()));
		}

		return mirorpath;
	}

	private boolean isConstant(String value) {
		try {
			Integer.valueOf(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private void modifiersReset() {
		generationmodifier = DefaultSettings.UNDEFINED_STRING;
		polaritymodifier = false;
		color = DefaultSettings.UNDEFINED_STRING;
	}

}
