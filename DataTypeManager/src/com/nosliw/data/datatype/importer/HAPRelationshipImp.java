package com.nosliw.data.datatype.importer;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeId;
import com.nosliw.data.HAPRelationship;
import com.nosliw.data.HAPRelationshipPath;
import com.nosliw.data.HAPRelationshipPathSegment;

public class HAPRelationshipImp extends HAPDataTypeImp implements HAPRelationship{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String SOURCE = "source";
	
	public HAPRelationshipImp(){}
	
	public HAPRelationshipImp(HAPDataTypeImp target){
		this.updateAtomicChildObjectValue(PATH, new HAPRelationshipPathImp());
		this.cloneFrom(target);
	}
	
	@Override
	public HAPDataTypeId getTargetDataTypeName() {		return this.getName();	}
	
	@Override
	public HAPRelationshipPath getPath() {		return (HAPRelationshipPathImp)this.getAtomicAncestorValueObject(PATH, HAPRelationshipPathImp.class);	}

	public HAPDataType getTargetDataType() {		return this;	}

	public String getId(){ return this.getAtomicAncestorValueString(ID);  }
	
	public HAPDataTypeIdImp getSource(){	return (HAPDataTypeIdImp)this.getAtomicAncestorValueObject(SOURCE, HAPDataTypeIdImp.class);	}

	public void setSource(HAPDataTypeIdImp source){  this.updateAtomicChildObjectValue(SOURCE, source);  }
	public void setId(String id){  this.updateAtomicChildStrValue(ID, id);  }
	public void setPath(HAPRelationshipPath path){		this.getPath().setPath(path);	}
	
	public HAPRelationshipImp extendPathSegment(HAPRelationshipPathSegment segment, HAPDataTypeIdImp sourceId){
		HAPRelationshipImp out = this.clone(HAPRelationshipImp.class);
		out.setSource(sourceId);
		out.getPath().insert(segment);
		return out;
	}
}
