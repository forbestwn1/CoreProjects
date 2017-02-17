package com.nosliw.common.constant;

import java.util.Iterator;

import com.nosliw.common.strvalue.HAPStringableValueEntityBasic;
import com.nosliw.common.strvalue.HAPStringableValueList;

@HAPEntityWithAttribute(baseName="CONSTANTGROUP")
public class HAPConstantGroup extends HAPStringableValueEntityBasic{

	public final static String TYPE_CONSTANT = "constant"; 
	public final static String TYPE_ATTRIBUTE = "attribute"; 
	public final static String TYPE_CLASSATTR = "classAttr"; 
	
	@HAPAttribute
	public static String TYPE = "type";
	@HAPAttribute
	public static String FILEPATH = "filepath";
	@HAPAttribute
	public static String CLASSNAME = "classname";
	@HAPAttribute
	public static String PACKAGENAME = "packagename";
	@HAPAttribute
	public static String DEFINITIONS = "definitions";

	public HAPConstantGroup(){	}
	public HAPConstantGroup(String type){
		this.updateAtomicChildStrValue(TYPE, type);
	}

	public void addConstantInfo(HAPConstantInfo constantInfo){
		HAPStringableValueList list = this.getListChild(DEFINITIONS);
		list.addChild(constantInfo);
	}

	public Iterator iterateConstant(){
		HAPStringableValueList list = this.getListChild(DEFINITIONS);
		return list.iterate();
	}
	
	public String getType(){ return this.getAtomicAncestorValueString(TYPE); }
	public String getFilePath(){ return this.getAtomicAncestorValueString(FILEPATH); }
	public String getClassName(){ return this.getAtomicAncestorValueString(CLASSNAME); }
	public String getPackageName(){ return this.getAtomicAncestorValueString(PACKAGENAME);  }
}
