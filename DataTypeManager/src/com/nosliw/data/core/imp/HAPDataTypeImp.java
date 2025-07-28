package com.nosliw.data.core.imp;

import java.util.List;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.data.HAPDataType;
import com.nosliw.core.data.HAPDataTypeId;
import com.nosliw.core.data.HAPDataTypeVersion;

@HAPEntityWithAttribute(parent="com.nosliw.core.data.HAPDataType")
public class HAPDataTypeImp extends HAPStringableValueEntity implements HAPDataType{

	public HAPDataTypeImp(){}

	public HAPDataTypeImp(HAPDataTypeImp dataType){
		this.cloneFrom(dataType);
	}
	
	public HAPDataTypeImp(String Id, String name, String version, String description, String parent, String linked){
		this.init(Id, name, version, description, parent, linked);
	}

	public void init(String Id, String name, String version, String description, String parent, String linked){
		this.updateAtomicChildStrValue(NAME, Id);
		this.updateAtomicChildStrValue(NAME, HAPDataTypeId.buildStringValue(name, version), HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeId.class.getName());
		this.updateAtomicChildStrValue(INFO, description);
		this.updateAtomicChildStrValue(PARENTSINFO, parent, HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_ARRAY, HAPDataTypeId.class.getName());
		this.updateAtomicChildStrValue(LINKEDVERSION, parent, HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeVersion.class.getName());
	}
	
	@Override
	public HAPDataTypeId getName() {		return (HAPDataTypeId)this.getAtomicValueAncestorByPath(NAME);	}
	public void setId(String id){		this.updateAtomicChildStrValue(NAME, id, HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_STRING);	}

	@Override
	public boolean getIsComplex() { return this.getAtomicAncestorValueBoolean(ISCOMPLEX); }

	
	@Override
	public HAPInfo getInfo() {	return (HAPInfo)this.getEntityAncestorByPath(INFO); }

	@Override
	public List<HAPDataTypeId> getParentsInfo() {	return this.getAtomicAncestorValueArray(PARENTSINFO, HAPDataTypeId.class.getName());	}

	public HAPDataTypeId getLinkedDataTypeId(){
		if(this.getLinkedVersion()==null)  return null;
		else return new HAPDataTypeId(this.getName().getName(), this.getLinkedVersion());	
	}
	
	@Override
	public HAPDataTypeVersion getLinkedVersion() {  return (HAPDataTypeVersion)this.getAtomicValueAncestorByPath(LINKEDVERSION);	}
}
