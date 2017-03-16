package com.nosliw.data.datatype.importer;

import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.HAPRelationshipPath;
import com.nosliw.data.core.HAPRelationshipPathSegment;

public class HAPRelationshipImp extends HAPStringableValueEntityWithID implements HAPRelationship{

	public static String _VALUEINFO_NAME;

	public HAPRelationshipImp(){
		this.updateAtomicChildObjectValue(PATH, new HAPRelationshipPathImp());
	}
	
	public HAPRelationshipImp(HAPDataTypeImp target){
		this.updateAtomicChildObjectValue(PATH, new HAPRelationshipPathImp());
		this.cloneFrom(target);
	}

	@Override
	public HAPDataTypeId getTargetDataTypeName() {		return this.getTarget().getName();	}
	
	@Override
	public HAPDataTypeId getSourceDataTypeName(){	return this.getSource().getName();	}

	@Override
	public HAPRelationshipPath getPath() {		return (HAPRelationshipPathImp)this.getAtomicAncestorValueObject(PATH, HAPRelationshipPathImp.class);	}

	public HAPDataTypeImp getTarget() {		return (HAPDataTypeImp)this.getEntityAncestorByPath(TARGET);	}

	public HAPDataTypeImp getSource() {		return (HAPDataTypeImp)this.getEntityAncestorByPath(SOURCE);	}
	
	public void setSource(HAPDataTypeImp source){  this.updateChild(SOURCE, source);  }
	public void setTarget(HAPDataTypeImp source){  this.updateChild(TARGET, source);  }
	public void setPath(HAPRelationshipPath path){		this.getPath().setPath(path);	}
	
	public HAPRelationshipImp extendPathSegmentSource(HAPRelationshipPathSegment segment, HAPDataTypeImp source){
		HAPRelationshipImp out = this.clone(HAPRelationshipImp.class);
		out.setSource(source);
		out.getPath().insert(segment);
		return out;
	}

	public HAPRelationshipImp extendPathSegmentTarget(HAPRelationshipPathSegment segment, HAPDataTypeImp target){
		HAPRelationshipImp out = this.clone(HAPRelationshipImp.class);
		out.setTarget(target);
		out.getPath().append(segment);
		return out;
	}
}
