package com.nosliw.data.expression.utils;

import java.io.File;

public class HAPJCCApplication {

	public static void main(String[] args) throws Exception {

//		org.javacc.parser.Main.main(new String[]{"expression.jj"});

		File file = new File("expression.jj");
		String filePath = file.getAbsolutePath();
		
		//step 1
//		org.javacc.jjtree.Main.main(new String[]{filePath});
		
		//step 2
		org.javacc.parser.Main.main(new String[]{"c:/Temp/JCCTree/expression/expression.jj.jj"});
		
	}

}
