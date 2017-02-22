package com.nosliw.data.datatype.importer;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeId;
import com.nosliw.data.HAPInfo;
import com.nosliw.data.HAPDataTypeVersion;

@HAPEntityWithAttribute(parent="com.nosliw.data.HAPDataType")
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
		this.updateAtomicChildStrValue(NAME, HAPDataTypeIdImp.buildStringValue(name, version), HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeIdImp.class.getName());
		this.updateAtomicChildStrValue(INFO, description);
		this.updateAtomicChildStrValue(PARENTINFO, parent, HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeIdImp.class.getName());
		this.updateAtomicChildStrValue(LINKEDVERSION, parent, HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeVersionImp.class.getName());
	}
	
	@Override
	public HAPDataTypeId getName() {		return (HAPDataTypeIdImp)this.getAtomicValueAncestorByPath(NAME);	}
	public void setId(String id){		this.updateAtomicChildStrValue(NAME, id, HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING);	}

	
	
	public HAPDataTypeIdImp getConntectedDataTypeId(int connectType){
		HAPDataTypeIdImp out = null;
		switch(connectType){
		case HAPConstant.DATATYPE_PATHSEGMENT_LINKED:
			out = (HAPDataTypeIdImp)this.getLinkedDataTypeId();
			break;
		case HAPConstant.DATATYPE_PATHSEGMENT_PARENT:
			out = (HAPDataTypeIdImp)this.getParentInfo();
			break;
		}
		return out;
	}
	

	
	@Override
	public HAPInfo getInfo() {	return (HAPInfo)this.getEntityAncestorByPath(INFO); }

	@Override
	public HAPDataTypeId getParentInfo() {	return (HAPDataTypeId)this.getAtomicValueAncestorByPath(PARENTINFO);	}

	public HAPDataTypeIdImp getLinkedDataTypeId(){
		if(this.getLinkedVersion()==null)  return null;
		else return new HAPDataTypeIdImp(this.getName().getName(), this.getLinkedVersion());	
	}
	
	@Override
	public HAPDataTypeVersion getLinkedVersion() {  return (HAPDataTypeVersion)this.getAtomicValueAncestorByPath(LINKEDVERSION);	}
}
