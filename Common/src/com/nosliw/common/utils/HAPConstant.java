package com.nosliw.common.utils;

public class HAPConstant {

	//resolve reference node mode 
	//first one match context name
	public static String RESOLVEPARENTMODE_FIRST = "first";
	//best one that not only match context name, but has most similar path
	public static String RESOLVEPARENTMODE_BEST = "best";

	
	public static final String INHERITMODE_PARENT = "parent";  //when child define the same context node name as parent inheritable, but parent override child for same name
	public static final String INHERITMODE_CHILD = "child";    //inheritable, but child unaffected for same name
	public static final String INHERITMODE_NONE = "none";		//UNheritable

}
