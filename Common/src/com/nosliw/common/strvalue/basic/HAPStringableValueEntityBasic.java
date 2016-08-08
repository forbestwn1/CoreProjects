package com.nosliw.common.strvalue.basic;

public class HAPStringableValueEntityBasic extends HAPStringableValueEntity{
	public final static String ATTR_NAME = "name";
	public final static String ATTR_DESCRIPTION = "description";

	public HAPStringableValueEntityBasic(){}
	
	public HAPStringableValueEntityBasic(String name, String description){
		this.updateBasicChild(ATTR_NAME, name);
		this.updateBasicChild(ATTR_DESCRIPTION, description);
	}
 
	public String getName(){  return this.getBasicAncestorValueString(ATTR_NAME); }
	public void setName(String name){  this.updateBasicChild(ATTR_NAME, name);}
	public String getDescription(){  return this.getBasicAncestorValueString(ATTR_DESCRIPTION); }
	public void setDescription(String desc){ this.updateBasicChild(ATTR_DESCRIPTION, desc); }
 	
}
 