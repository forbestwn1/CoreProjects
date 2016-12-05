package com.nosliw.data.datatype.importer;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPOperationInfo;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeInfo;
import com.nosliw.data.HAPDataTypeVersion;

@HAPEntityWithAttribute(parent="com.nosliw.data.HAPDataType")
public class HAPDataTypeImp extends HAPStringableValueEntity implements HAPDataType{

	public static String ID = "id";

	@HAPAttribute
	public static String OPERATIONS = "operations";

	public HAPDataTypeImp(String Id, String name, String version, String description, String parent, String linked){
		this.updateAtomicChild(ID, Id);
		this.updateAtomicChild(NAME, HAPDataTypeInfoImp.buildStringValue(name, version), HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeInfoImp.class.getName());
		this.updateAtomicChild(DESCRIPTION, description);
		this.updateAtomicChild(PARENTINFO, parent, HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeInfoImp.class.getName());
		this.updateAtomicChild(LINKEDVERSION, parent, HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeVersionImp.class.getName());
	}
	
	@Override
	public HAPDataTypeInfo getDataTypeInfo() {	
		HAPDataTypeInfoImp out = (HAPDataTypeInfoImp)this.getAtomicValueAncestorByPath(NAME);
		out.setId(this.getId());
		return out;
	}

	public HAPDataTypeInfoImp getConntectedDataTypeInfo(int connectType){
		HAPDataTypeInfoImp out = null;
		switch(connectType){
		case HAPConstant.DATATYPE_PATHSEGMENT_LINKED:
			out = (HAPDataTypeInfoImp)this.getLinkedDataTypeInfo();
			break;
		case HAPConstant.DATATYPE_PATHSEGMENT_PARENT:
			out = (HAPDataTypeInfoImp)this.getParentDataTypeInfo();
			break;
		}
		return out;
	}
	
	public String getId(){ return this.getAtomicAncestorValueString(ID); }
	
	@Override
	public String getDescription() {	return this.getAtomicAncestorValueString(DESCRIPTION); }

	@Override
	public HAPDataTypeInfo getParentDataTypeInfo() {	return (HAPDataTypeInfo)this.getAtomicValueAncestorByPath(PARENTINFO);	}

	public HAPDataTypeInfoImp getLinkedDataTypeInfo(){
		return new HAPDataTypeInfoImp(this.getDataTypeInfo().getName(), this.getLinkedVersion());
	}
	
	@Override
	public HAPDataTypeVersion getLinkedVersion() {  return (HAPDataTypeVersion)this.getAtomicValueAncestorByPath(LINKEDVERSION);	}

	public List<HAPOperationInfo> getDataOperationInfos(){
		HAPStringableValueList list = (HAPStringableValueList)this.getListAncestorByPath(OPERATIONS);
		return (List<HAPOperationInfo>)list.getListValue();
	}
}
