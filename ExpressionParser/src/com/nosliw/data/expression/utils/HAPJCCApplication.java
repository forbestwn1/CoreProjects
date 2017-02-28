package com.nosliw.data.expression.utils;

import java.io.File;

public class HAPJCCApplication {

	public static void main(String[] args) throws Exception {

//		org.javacc.parser.Main.main(new String[]{"expression.jj"});
		
//				File file = new File("expression.jj");
//		String filePath = file.getAbsolutePath();
//		org.javacc.jjtree.Main.main(new String[]{filePath});

		org.javacc.parser.Main.main(new String[]{"c:/Temp/JCCTree/expression.jj.jj"});
		
	}

}
