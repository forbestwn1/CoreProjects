package com.nosliw.data.datatype.importer;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeId;
import com.nosliw.data.HAPDataTypeVersion;

@HAPEntityWithAttribute(parent="com.nosliw.data.HAPDataType")
public class HAPDataTypeImp extends HAPStringableValueEntity implements HAPDataType{

	@HAPAttribute
	public static String ID = "id";
	
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
		this.updateAtomicChildStrValue(ID, HAPDataTypeInfoImp.buildStringValue(name, version), HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeInfoImp.class.getName());
		this.updateAtomicChildStrValue(INFO, description);
		this.updateAtomicChildStrValue(PARENTINFO, parent, HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeInfoImp.class.getName());
		this.updateAtomicChildStrValue(LINKEDVERSION, parent, HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeVersionImp.class.getName());
	}
	
	@Override
	public HAPDataTypeId getId() {		return (HAPDataTypeInfoImp)this.getAtomicValueAncestorByPath(ID);	}

	public HAPDataTypeInfoImp getConntectedDataTypeInfo(int connectType){
		HAPDataTypeInfoImp out = null;
		switch(connectType){
		case HAPConstant.DATATYPE_PATHSEGMENT_LINKED:
			out = (HAPDataTypeInfoImp)this.getLinkedDataTypeInfo();
			break;
		case HAPConstant.DATATYPE_PATHSEGMENT_PARENT:
			out = (HAPDataTypeInfoImp)this.getParentInfo();
			break;
		}
		return out;
	}
	
	public String getId(){ return this.getAtomicAncestorValueString(ID); }
	public void setId(String id){		this.updateAtomicChildStrValue(ID, id, HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING);	}

	
	@Override
	public String getInfo() {	return this.getAtomicAncestorValueString(INFO); }

	@Override
	public HAPDataTypeId getParentInfo() {	return (HAPDataTypeId)this.getAtomicValueAncestorByPath(PARENTINFO);	}

	public HAPDataTypeInfoImp getLinkedDataTypeInfo(){
		return new HAPDataTypeInfoImp(this.getId().getName(), this.getLinkedVersion());
	}
	
	@Override
	public HAPDataTypeVersion getLinkedVersion() {  return (HAPDataTypeVersion)this.getAtomicValueAncestorByPath(LINKEDVERSION);	}

}
