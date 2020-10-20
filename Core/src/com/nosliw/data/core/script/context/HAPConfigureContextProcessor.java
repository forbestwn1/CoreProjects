package com.nosliw.data.core.script.context;

public class HAPConfigureContextProcessor {

	//resolve reference node mode 
	//first one match context name
	public static String VALUE_RESOLVEPARENTMODE_FIRST = "first";
	//best one that not only match context name, but has most similar path
	public static String VALUE_RESOLVEPARENTMODE_BEST = "best";

	//when child define the same context node name as parent
	//inheritable, but parent override child for same name
	public static String VALUE_INHERITMODE_PARENT = "parent";
	//inheritable, but child unaffected for same name
	public static String VALUE_INHERITMODE_CHILD = "child";
	//UNheritable
	public static String VALUE_INHERITMODE_NONE = "none";

	//how to find referenced parent node
	public String relativeResolveMode = VALUE_RESOLVEPARENTMODE_BEST;

	//how to handle rule defined in parent node
	public boolean relativeInheritRule = false;
	
	//relative can track to sold parent
	public boolean relativeTrackingToSolid = false;
	
	//context categary to find referenced parent node 
	public String[] parentCategary;
	
	//
	public String inheritMode = VALUE_INHERITMODE_CHILD;
	
	public HAPConfigureContextProcessor cloneConfigure() {
		HAPConfigureContextProcessor out = new HAPConfigureContextProcessor();
		out.relativeResolveMode = this.relativeResolveMode;
		out.relativeInheritRule = this.relativeInheritRule;
		out.relativeTrackingToSolid = this.relativeTrackingToSolid;
		out.inheritMode = this.inheritMode;
		out.parentCategary = this.parentCategary;
		return out;
	}
}
