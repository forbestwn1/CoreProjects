package com.nosliw.data.imp.expression.parser;

public class HAPExpressionEscape {

	public static String[][] mapping = {
			{".", "##"},
		};
		
		public static String escape(String content){
			String out = content;
			for(int i=0; i<mapping.length; i++){
				out = out.replaceAll(mapping[i][0], mapping[i][1]);
			}
			return out;
		}
		
		public static String deescape(String content){
			String out = content;
			for(int i=0; i<mapping.length; i++){
				out = out.replaceAll(mapping[i][1], mapping[i][0]);
			}
			return out;
		}
}
