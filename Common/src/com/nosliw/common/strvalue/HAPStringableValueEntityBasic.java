package com.nosliw.common.strvalue;

public class HAPStringableValueEntityBasic extends HAPStringableValueEntity{
	public final static String ENTITY_PROPERTY_NAME = "name";
	public final static String ENTITY_PROPERTY_DESCRIPTION = "description";

	public HAPStringableValueEntityBasic(){}
	
	public HAPStringableValueEntityBasic(String name, String description){
		this.updateAtomicChildStrValue(ENTITY_PROPERTY_NAME, name);
		this.updateAtomicChildStrValue(ENTITY_PROPERTY_DESCRIPTION, description);
	}
 
	public String getName(){  return this.getAtomicAncestorValueString(ENTITY_PROPERTY_NAME); }
	public void setName(String name){  this.updateAtomicChildStrValue(ENTITY_PROPERTY_NAME, name);}
	public String getDescription(){  return this.getAtomicAncestorValueString(ENTITY_PROPERTY_DESCRIPTION); }
	public void setDescription(String desc){ this.updateAtomicChildStrValue(ENTITY_PROPERTY_DESCRIPTION, desc); }
 	
}
 