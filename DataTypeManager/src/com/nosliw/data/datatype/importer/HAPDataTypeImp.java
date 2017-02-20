package com.nosliw.data.datatype.importer;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeId;
import com.nosliw.data.HAPDataTypeInfo;
import com.nosliw.data.HAPDataTypeVersion;

@HAPEntityWithAttribute(parent="com.nosliw.data.HAPDataType")
public class HAPDataTypeImp extends HAPStringableValueEntity implements HAPDataType{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String PARENTINFO = "parentInfo";

	@HAPAttribute
	public static String LINKEDVERSION = "linkedVersion";
	
	public HAPDataTypeImp(){}

	public HAPDataTypeImp(HAPDataTypeImp dataType){
		this.cloneFrom(dataType);
	}
	
	public HAPDataTypeImp(String Id, String name, String version, String description, String parent, String linked){
		this.init(Id, name, version, description, parent, linked);
	}

	public void init(String Id, String name, String version, String description, String parent, String linked){
		this.updateAtomicChildStrValue(ID, Id);
		this.updateAtomicChildStrValue(ID, HAPDataTypeIdImp.buildStringValue(name, version), HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeIdImp.class.getName());
		this.updateAtomicChildStrValue(INFO, description);
		this.updateAtomicChildStrValue(PARENTINFO, parent, HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeIdImp.class.getName());
		this.updateAtomicChildStrValue(LINKEDVERSION, parent, HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeVersionImp.class.getName());
	}
	
	@Override
	public HAPDataTypeId getId() {		return (HAPDataTypeIdImp)this.getAtomicValueAncestorByPath(NAME);	}
	public void setId(String id){		this.updateAtomicChildStrValue(NAME, id, HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING);	}

	
	
	public HAPDataTypeIdImp getConntectedDataTypeInfo(int connectType){
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
	public HAPDataTypeInfo getInfo() {	return (HAPDataTypeInfo)this.getEntityAncestorByPath(INFO); }

	public HAPDataTypeId getParentInfo() {	return (HAPDataTypeId)this.getAtomicValueAncestorByPath(PARENTINFO);	}

	public HAPDataTypeIdImp getLinkedDataTypeId(){	return new HAPDataTypeIdImp(this.getId().getName(), this.getLinkedVersion());	}
	
	public HAPDataTypeVersion getLinkedVersion() {  return (HAPDataTypeVersion)this.getAtomicValueAncestorByPath(LINKEDVERSION);	}
}
