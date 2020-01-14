package test.io;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import compiler.interpreters.GroundSettingsCompiler;
import repositories.GroundsRepository;

public class TestGroundCompilator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		GroundSettingsCompiler gsc = new GroundSettingsCompiler();
		gsc.loadSettings(InOutParameters.GROUND_SETTING_PATH);
		
		Set<String> names = GroundsRepository.getInstance().getTextures().keySet();
		Iterator<String> nameit = names.iterator();
		
		String name = "";
		File texture = null;
		
		while(nameit.hasNext()){
			name = nameit.next();
			texture = GroundsRepository.getInstance().getGround(name).getTexture();
			System.out.println("NAME: "+name+" FILE: "+texture.getAbsolutePath());
		}
	}

}
