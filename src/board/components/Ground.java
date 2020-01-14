package board.components;

import java.io.File;

import compiler.lang.DefaultSettings;
import repositories.GroundsRepository;

/**
 * This class represents a piece of ground. It associates a ground type with its texture.
 * @author Dorian CHENET
 *
 */
public class Ground {

	private String name = DefaultSettings.UNDEFINED_STRING;
	private File texture = DefaultSettings.DEFAULT_TEXTURE;

	public Ground() {

	}
	
	public Ground(String groundtype){
		this.name = groundtype;
		this.texture = GroundsRepository.getInstance().getGround(groundtype).getTexture();
		if(this.texture == null){
			this.texture = DefaultSettings.DEFAULT_TEXTURE;
		}
	}

	public Ground(String groundname, File groundtexture) {
		super();
		this.name = groundname;
		this.texture = groundtexture;
	}

	public String getName() {
		return name;
	}

	public File getTexture() {
		return texture;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((texture == null) ? 0 : texture.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ground other = (Ground) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (texture == null) {
			if (other.texture != null)
				return false;
		} else if (!texture.equals(other.texture))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Ground [groundname=" + name + ", groundtexture=" + texture + "]";
	}

}
