package com.nosliw.data.core.imp;

import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.HAPRelationshipPath;
import com.nosliw.data.core.HAPRelationshipPathSegment;

public class HAPRelationshipImp extends HAPStringableValueEntityWithID implements HAPRelationship{

	public static String _VALUEINFO_NAME;
	
	public static String SOURCEDATATYPE = "sourceDataType";
	public static String TARGETDATATYPE = "targetDataType";
	
	public HAPRelationshipImp(){
		this.updateAtomicChildObjectValue(PATH, new HAPRelationshipPathImp());
	}
	
	@Override
	public HAPDataTypeId getTarget() {		return this.getTargetDataType().getName();	}
	
	@Override
	public HAPDataTypeId getSource(){	return this.getSourceDataType().getName();	}

	@Override
	public HAPRelationshipPath getPath() {		return (HAPRelationshipPathImp)this.getAtomicAncestorValueObject(PATH, HAPRelationshipPathImp.class);	}

	public HAPDataTypeImp getTargetDataType() {		return (HAPDataTypeImp)this.getEntityAncestorByPath(TARGETDATATYPE);	}

	public HAPDataTypeImp getSourceDataType() {		return (HAPDataTypeImp)this.getEntityAncestorByPath(SOURCEDATATYPE);	}
	
	public void setSourceDataType(HAPDataTypeImp source){  this.updateChild(SOURCEDATATYPE, source);  }
	public void setTargetDataType(HAPDataTypeImp source){  this.updateChild(TARGETDATATYPE, source);  }
	public void setPath(HAPRelationshipPath path){		this.getPath().setPath(path);	}
	
	public HAPRelationshipImp extendPathSegmentSource(HAPRelationshipPathSegment segment, HAPDataTypeImp source){
		HAPRelationshipImp out = this.clone(HAPRelationshipImp.class);
		out.setSourceDataType(source);
		out.getPath().insert(segment);
		return out;
	}

	public HAPRelationshipImp extendPathSegmentTarget(HAPRelationshipPathSegment segment, HAPDataTypeImp target){
		HAPRelationshipImp out = this.clone(HAPRelationshipImp.class);
		out.setTargetDataType(target);
		out.getPath().append(segment);
		return out;
	}
}
