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

	private HAPDataTypeImp m_parent;
	
	private HAPDataTypeImp m_linked;
	
	@HAPAttribute
	public static String OPERATIONS = "operations";

	public static String ID = "id";
	
	public HAPDataTypeImp(String Id, String name, String version, String description, String parent, String linked){
		this.updateAtomicChild(ID, Id);
		this.updateAtomicChild(NAME, HAPDataTypeInfoImp.buildStringValue(name, version), HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeInfoImp.class.getName());
		this.updateAtomicChild(DESCRIPTION, description);
		this.updateAtomicChild(PARENTINFO, parent, HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeInfoImp.class.getName());
		this.updateAtomicChild(LINKEDVERSION, parent, HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeVersionImp.class.getName());
	}
	
	@Override
	public HAPDataTypeInfo getDataTypeInfo() {	
		return (HAPDataTypeInfo)this.getAtomicValueAncestorByPath(NAME);
	}

	@Override
	public String getDescription() {	return this.getAtomicAncestorValueString(DESCRIPTION); }

	@Override
	public HAPDataTypeInfo getParentDataTypeInfo() {	return (HAPDataTypeInfo)this.getAtomicValueAncestorByPath(PARENTINFO);	}

	@Override
	public HAPDataTypeVersion getLinkedVersion() {  return (HAPDataTypeVersion)this.getAtomicValueAncestorByPath(LINKEDVERSION);	}

	public String getId(){ return this.getAtomicAncestorValueString(ID); }
	
	public List<HAPOperationInfo> getDataOperationInfos(){
		HAPStringableValueList list = (HAPStringableValueList)this.getListAncestorByPath(OPERATIONS);
		return (List<HAPOperationInfo>)list.getListValue();
	}
}
