package com.nosliw.common.strvalue.basic;

public class HAPStringableValueEntityBasic extends HAPStringableValueEntity{
	public final static String ENTITY_PROPERTY_NAME = "name";
	public final static String ENTITY_PROPERTY_DESCRIPTION = "description";

	public HAPStringableValueEntityBasic(){}
	
	public HAPStringableValueEntityBasic(String name, String description){
		this.updateBasicChild(ENTITY_PROPERTY_NAME, name);
		this.updateBasicChild(ENTITY_PROPERTY_DESCRIPTION, description);
	}
 
	public String getName(){  return this.getBasicAncestorValueString(ENTITY_PROPERTY_NAME); }
	public void setName(String name){  this.updateBasicChild(ENTITY_PROPERTY_NAME, name);}
	public String getDescription(){  return this.getBasicAncestorValueString(ENTITY_PROPERTY_DESCRIPTION); }
	public void setDescription(String desc){ this.updateBasicChild(ENTITY_PROPERTY_DESCRIPTION, desc); }
 	
}
 