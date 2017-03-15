package com.nosliw.data.datatype.importer;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPResourceIdImp extends HAPStringableValueEntity implements HAPResourceId{

	public HAPResourceIdImp(String type, String id){
		this.setId(id);
		this.setType(type);
	}
	
	@Override
	public String getId() {		return this.getAtomicAncestorValueString(ID);	}
	public void setId(String id){   this.updateAtomicChildStrValue(ID, id);  } 
	
	@Override
	public String getType() {  return this.getAtomicAncestorValueString(TYPE); }
	public void setType(String type){  this.updateAtomicChildStrValue(TYPE, type);  }

	@Override
	protected String buildLiterate(){
		return HAPNamingConversionUtility.cascadeDetail(this.getType(), this.getId());
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){	
		String[] segs = HAPNamingConversionUtility.parseDetails(literateValue);
		this.setType(segs[0]);
		this.setId(segs[1]);
		return true;
	}
}
