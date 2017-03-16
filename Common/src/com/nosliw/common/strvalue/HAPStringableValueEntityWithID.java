package com.nosliw.common.strvalue;

import com.nosliw.common.constant.HAPAttribute;

public class HAPStringableValueEntityWithID extends HAPStringableValueEntity{

	@HAPAttribute
	public final static String ID = "id";

	public HAPStringableValueEntityWithID(){}
	
	public HAPStringableValueEntityWithID(String id){
		this.updateAtomicChildStrValue(ID, id);
	}

	public String getId(){  return this.getAtomicAncestorValueString(ID); }
	public void setId(String id){  this.updateAtomicChildStrValue(ID, id);}
}
