package com.nosliw.data.info;

import org.json.JSONObject;

import com.nosliw.common.pattern.HAPPatternManager;
import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;
import com.nosliw.common.strvalue.valueinfo.HAPStringableEntityImporterJSON;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPDataTypeManager;

/*
 * entity store information about a data type information
 */
public class HAPDataTypeInfo extends HAPStringableValueEntity{

	public static String ENTITY_PROPERTY_CATEGARY = "categary";
	public static String ENTITY_PROPERTY_TYPE = "type";
	public static String ENTITY_PROPERTY_KEY = "key";

	public HAPDataTypeInfo(){}
	
	public HAPDataTypeInfo(String categary, String type){
		String realCategary = categary;
		if(HAPBasicUtility.isStringEmpty(realCategary))   realCategary = HAPDataTypeManager.DEFAULT_TYPE;
		this.setCategary(realCategary);
		
		this.setType(type);
		String key =  HAPPatternManager.getInstance().compose(HAPPatternProcessorDataTypeInfo.class.getName(), this, null);
		this.updateBasicChild(ENTITY_PROPERTY_KEY, key);
	}

	public String getCategary(){return this.getBasicAncestorValueString(ENTITY_PROPERTY_CATEGARY);}
	public String getType(){return this.getBasicAncestorValueString(ENTITY_PROPERTY_TYPE);	}
	//get unique key
	public String getKey(){return this.getBasicAncestorValueString(ENTITY_PROPERTY_KEY);}
	public void setCategary(String categary){ this.updateBasicChild(ENTITY_PROPERTY_CATEGARY, categary, HAPConstant.STRINGABLE_BASICVALUETYPE_STRING);}
	public void setType(String type){ this.updateBasicChild(ENTITY_PROPERTY_TYPE, type, HAPConstant.STRINGABLE_BASICVALUETYPE_STRING);}
	
	public HAPDataTypeInfo cloneDataTypeInfo(){
		HAPDataTypeInfo out = new HAPDataTypeInfo();
		out.cloneFrom(this);
		return out;
	}
	
	@Override
	public String toString(){	return this.getKey();	}
	
	public static HAPDataTypeInfo build(JSONObject jsonObj, HAPValueInfoManager valueInfoMan){
		HAPDataTypeInfo out = HAPStringableEntityImporterJSON.parseJson(jsonObj, HAPDataTypeInfo.class, valueInfoMan);
		return out;
	}
	
	//get DataTypeInfo from string  (type:categary)
	public static HAPDataTypeInfo build(String infoStr, HAPDataTypeInfo backup){
		if(HAPBasicUtility.isStringEmpty(infoStr))	return backup;
		else{
			return (HAPDataTypeInfo)HAPPatternManager.getInstance().process(infoStr, HAPPatternProcessorDataTypeInfo.class.getName(), null);
		}
	}

	public static HAPDataTypeInfo parseDataTypeInfo(String infoStr){
		return build(infoStr, null);
	}
	
	@Override
	public boolean equals(Object data){
		if(data instanceof HAPDataTypeInfo)  return super.equals(data);
		return false;
	}
}
