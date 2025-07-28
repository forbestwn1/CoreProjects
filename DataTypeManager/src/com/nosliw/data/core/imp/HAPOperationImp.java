package com.nosliw.data.core.imp;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.data.HAPDataTypeId;
import com.nosliw.core.data.HAPOperation;
import com.nosliw.core.data.HAPOperationOutInfo;
import com.nosliw.core.data.HAPOperationParmInfo;

@HAPEntityWithAttribute(parent="com.nosliw.core.data.HAPOperation")
public class HAPOperationImp extends HAPStringableValueEntityWithID implements HAPOperation{

	public static String _VALUEINFO_NAME;

	//store baseParm name
	@HAPAttribute
	public static String BASEPARM = "baseParm";
	
	@HAPAttribute
	public static String DATATYPNAME = "dataTypeName";
	
	public HAPOperationImp(){
		this.updateAtomicChildStrValue(TYPE, HAPConstantShared.DATAOPERATION_TYPE_NORMAL);
	}
	
	@Override
	public String getName() {		return this.getAtomicAncestorValueString(NAME);	}

	@Override
	public HAPInfo getInfo() {		return (HAPInfo)this.getAtomicAncestorValueObject(INFO, HAPInfo.class);  }

	public HAPDataTypeId getDataTypeName(){ return (HAPDataTypeId)this.getAtomicAncestorValueObject(DATATYPNAME, HAPDataTypeId.class); }
	public void setDataTypeName(HAPDataTypeId dataTypeId){  this.updateAtomicChildObjectValue(DATATYPNAME, dataTypeId);  }
	
	@Override
	public List<HAPOperationParmInfo> getParmsInfo() {
		HAPStringableValueList list = this.getListAncestorByPath(PAMRS);
		return list.getListValue();
	}
	
	public void addParmsInfo(HAPOperationParmInfo parmInfo){
		HAPStringableValueList list = this.getListAncestorByPath(PAMRS);
		list.addChild((HAPStringableValue)parmInfo);
	}

	@Override
	public HAPOperationOutInfo getOutputInfo() {		return (HAPOperationOutInfo)this.getEntityAncestorByPath(OUTPUT);	}
	public void setOutputInfo(HAPOperationOutInfo outputInfo){	this.updateChild(OUTPUT, (HAPStringableValue)outputInfo);	}

	@Override
	public String getType() {  return this.getAtomicAncestorValueString(TYPE); }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, this.getName());
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(OUTPUT, HAPManagerSerialize.getInstance().toStringValue(this.getOutputInfo(), HAPSerializationFormat.JSON));
		jsonMap.put(INFO, HAPManagerSerialize.getInstance().toStringValue(this.getInfo(), HAPSerializationFormat.JSON));
		
		String baseParm = null;
		Map<String, String> parmsMapJson = new LinkedHashMap<String, String>();
		for(HAPOperationParmInfo parmInfo : this.getParmsInfo()){
			parmsMapJson.put(parmInfo.getName(), HAPManagerSerialize.getInstance().toStringValue(parmInfo, HAPSerializationFormat.JSON));
			if(parmInfo.getIsBase())   baseParm = parmInfo.getName();
		}
		jsonMap.put(PAMRS, HAPUtilityJson.buildMapJson(parmsMapJson));
		jsonMap.put(BASEPARM, baseParm);
	}
}
