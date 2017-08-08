package com.nosliw.data.imp.criteria.utils;

import java.io.File;

public class HAPJCCApplication {

	public static void main(String[] args) throws Exception {

//		org.javacc.parser.Main.main(new String[]{"expression.jj"});

		File file = new File("criteria.jj");
		String filePath = file.getAbsolutePath();
//		org.javacc.jjtree.Main.main(new String[]{filePath});
		
		org.javacc.parser.Main.main(new String[]{"c:/Temp/JCCTree/criteria/criteria.jj.jj"});
		
	}

}
