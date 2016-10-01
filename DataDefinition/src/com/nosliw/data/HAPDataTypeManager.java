package com.nosliw.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.resource.HAPResource;
import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.basic.bool.HAPBoolean;
import com.nosliw.data.basic.doubl.HAPDouble;
import com.nosliw.data.basic.floa.HAPFloat;
import com.nosliw.data.basic.list.HAPList;
import com.nosliw.data.basic.map.HAPMap;
import com.nosliw.data.basic.number.HAPInteger;
import com.nosliw.data.basic.string.HAPString;
import com.nosliw.data.basic.string.HAPStringData;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.data.info.HAPDataTypeInfoWithVersion;
import com.nosliw.data.utils.HAPAttributeConstant;

public class HAPDataTypeManager implements HAPStringable, HAPResource{

	public final static String DEFAULT_CATEGARY = HAPConstant.DATATYPE_CATEGARY_SIMPLE;
	public final static String DEFAULT_TYPE = HAPConstant.DATATYPE_TYPE_STRING;
	
	//static object for default data type: Bool, Integer, String, Float
	public static HAPBoolean BOOLEAN;
	public static HAPInteger INTEGER;
	public static HAPString STRING;
	public static HAPFloat FLOAT;
	public static HAPDouble DOUBLE;
	public static HAPMap MAP;
	public static HAPList LIST;
	
	//map for data type string vs data type obj
	private Map<String, HAPDataType> m_dataTypes;
	//store information for all the data type for particular categary
	private Map<String, Set<String>> m_dataTypeCategary;

	//store data operation scripts
	private Map<String, String> m_dataTypeOperationScripts;
	
	//configure info
	private HAPConfigure m_configures;

	public HAPDataTypeManager(HAPConfigure configures){
		this.m_configures = configures;
		
		this.m_dataTypes = new LinkedHashMap<String, HAPDataType>();
		this.m_dataTypeCategary = new LinkedHashMap<String, Set<String>>();
		this.m_dataTypeOperationScripts = new LinkedHashMap<String, String>();
		this.loadBasicDataType();
	}
	
	@Override
	public void init() {
		for(String key : this.m_dataTypes.keySet()){
			HAPDataType dataType = this.m_dataTypes.get(key);
			dataType.buildOperation();
			//store data type operation script of file and buffer
//			this.processDataTypeOperationScript(dataType);
			
		}
	}
	
	public HAPDataType registerDataType(HAPDataType dataType){
		HAPDataTypeInfo dataTypeInfo = dataType.getDataTypeInfo(); 
		String key = this.getDataTypeKey(dataTypeInfo);
		this.m_dataTypes.put(key, dataType);
		
		Set<String> types = this.m_dataTypeCategary.get(dataTypeInfo.getCategary());
		if(types==null){
			types = new HashSet<String>();
			this.m_dataTypeCategary.put(dataTypeInfo.getCategary(), types);
		}
		types.add(dataTypeInfo.getType());

		return dataType;
	}
	
	public String[] getAllDataCategarys(){		return this.m_dataTypeCategary.keySet().toArray(new String[0]);	}
	
	public Set<String> getDataTypesByCategary(String categary){
		return this.m_dataTypeCategary.get(categary);
	}
	
	public HAPDataType[] getAllDataTypes(){		return this.m_dataTypes.values().toArray(new HAPDataType[0]);	}
	
	public HAPDataType getDataType(HAPDataTypeInfo dataTypeInfo){
		if(dataTypeInfo==null)  return null;
		String key = this.getDataTypeKey(dataTypeInfo);
		return this.m_dataTypes.get(key);
	}

	public HAPDataType getDataType(HAPDataTypeInfoWithVersion dataTypeInfo){
		String key = this.getDataTypeKey(dataTypeInfo);
		HAPDataType dataType = this.m_dataTypes.get(key);
		return dataType.getDataTypeByVersion(dataTypeInfo.getVersionNumber());
	}
	
	public HAPDataTypeInfo getDataTypeInfoByTypeName(String type){
		for(String categary : this.getAllDataCategarys()){
			if(this.isCategary(categary, type)){
				return new HAPDataTypeInfo(categary, type);
			}
		}
		return null;
	}
	
	public boolean isCategary(String categary, String type){
		return this.getDataType(new HAPDataTypeInfo(categary, type))!=null;
	}
	
	public HAPData getDefaultValue(HAPDataTypeInfo dataTypeInfo){
		HAPData out = null;
		HAPDataType dataType = this.getDataType(dataTypeInfo);
		if(dataType!=null){
			out = dataType.getDefaultData();
		}
		return out;
	}

	public static HAPDataTypeInfo getDefaultDataTypeInfo(){		return new HAPDataTypeInfo(DEFAULT_CATEGARY, DEFAULT_TYPE);	}
	
	private void loadBasicDataType(){
		
		HAPDataTypeInfoWithVersion booleanDataTypeInfo = new HAPDataTypeInfoWithVersion(HAPConstant.DATATYPE_CATEGARY_SIMPLE, HAPConstant.DATATYPE_TYPE_BOOLEAN);
		HAPDataTypeManager.BOOLEAN = (HAPBoolean)this.registerDataType(HAPBoolean.createDataType(booleanDataTypeInfo, null, null, null, "", this));

		HAPDataTypeInfoWithVersion integerDataTypeInfo = new HAPDataTypeInfoWithVersion(HAPConstant.DATATYPE_CATEGARY_SIMPLE, HAPConstant.DATATYPE_TYPE_INTEGER);
		HAPDataTypeManager.INTEGER = (HAPInteger)this.registerDataType(HAPInteger.createDataType(integerDataTypeInfo, null, null, null, "", this));

		HAPDataTypeInfoWithVersion stringDataTypeInfo = new HAPDataTypeInfoWithVersion(HAPConstant.DATATYPE_CATEGARY_SIMPLE, HAPConstant.DATATYPE_TYPE_STRING);
		HAPDataTypeManager.STRING = (HAPString)this.registerDataType(HAPString.createDataType(stringDataTypeInfo, null, null, null, "", this));
		
		HAPDataTypeInfoWithVersion floatDataTypeInfo = new HAPDataTypeInfoWithVersion(HAPConstant.DATATYPE_CATEGARY_SIMPLE, HAPConstant.DATATYPE_TYPE_FLOAT);
		HAPDataTypeManager.FLOAT = (HAPFloat)this.registerDataType(HAPFloat.createDataType(floatDataTypeInfo, null, null, null, "", this));

		HAPDataTypeInfoWithVersion doubleDataTypeInfo = new HAPDataTypeInfoWithVersion(HAPConstant.DATATYPE_CATEGARY_SIMPLE, HAPConstant.DATATYPE_TYPE_DOUBLE);
		HAPDataTypeManager.DOUBLE = (HAPDouble)this.registerDataType(HAPDouble.createDataType(doubleDataTypeInfo, null, null, null, "", this));

		HAPDataTypeInfoWithVersion mapDataTypeInfo = new HAPDataTypeInfoWithVersion(HAPConstant.DATATYPE_CATEGARY_SIMPLE, HAPConstant.DATATYPE_TYPE_MAP);
		HAPDataTypeManager.MAP = (HAPMap)this.registerDataType(HAPMap.createDataType(mapDataTypeInfo, null, null, null, "", this));
	
		HAPDataTypeInfoWithVersion listDataTypeInfo = new HAPDataTypeInfoWithVersion(HAPConstant.DATATYPE_CATEGARY_SIMPLE, HAPConstant.DATATYPE_TYPE_LIST);
		HAPDataTypeManager.LIST = (HAPList)this.registerDataType(HAPList.createDataType(listDataTypeInfo, null, null, null, "", this));
	}

	public HAPStringData getStringData(String value){
		return STRING.createDataByValue(value);
	}
	
	private String getDataTypeKey(HAPDataTypeInfo info){
		return info.toString();
	}

	/****************************** operation ********************************/
	public HAPData newData(HAPDataTypeInfoWithVersion dataTypeInfo, HAPData[] parms, HAPOperationContext opContext){
		return (HAPData)this.getDataType(dataTypeInfo).newData(parms, opContext).getData();
	}

	public HAPData newData(HAPDataTypeInfoWithVersion dataTypeInfo, String name, HAPData[] parms, HAPOperationContext opContext){
		return (HAPData)this.getDataType(dataTypeInfo).newData(name, parms, opContext).getData();
	}

	public HAPData newData(HAPDataTypeInfo dataTypeInfo, String name, HAPData[] parms, HAPOperationContext opContext){
		return (HAPData)this.getDataType(dataTypeInfo).newData(name, parms, opContext).getData();
	}

	public HAPData newData(HAPDataTypeInfo dataTypeInfo, HAPData[] parms, HAPOperationContext opContext){
		return (HAPData)this.getDataType(dataTypeInfo).newData(parms, opContext).getData();
	}
	
	public HAPData dataOperate(HAPDataTypeInfo dataTypeInfo, String operation, HAPData[] parms, HAPOperationContext opContext){
		return (HAPData)this.getDataType(dataTypeInfo).operate(operation, parms, opContext).getData();
	}

	public HAPData dataOperate(HAPDataType dataType, String operation, HAPData[] parms, HAPOperationContext opContext){
		return (HAPData)dataType.operate(operation, parms, opContext).getData();
	}
	
	/****************************** operation script ********************************/
	public Set<String> getSupportedScripts(){
		Set<String> out = new HashSet<String>();
		out.add(HAPConstant.OPERATIONDEF_SCRIPT_JAVASCRIPT);
		return out;
	}
	
	/*
	 * create data operation script for data type 
	 */
	private String buildDataTypeOperationScript(HAPDataType dataType){
		StringBuffer out = new StringBuffer();
		List<HAPDataType> dataTypes = this.getAllVersionsDataType(dataType);
		for(HAPDataType dataTypeWithVersion : dataTypes){
			String script = dataTypeWithVersion.buildLocalOperationScript(HAPConstant.OPERATIONDEF_SCRIPT_JAVASCRIPT);
			out.append(script);
		}
		return out.toString();
	}
	
	private String buildDataTypeOperationScriptFileName(HAPDataTypeInfo dataTypeInfo){
		return dataTypeInfo.toString()+".js";
	}
	
	/*
	 * store data type operation script of file and buffer
	 */
	private String processDataTypeOperationScript(HAPDataType dataType){
		HAPDataTypeInfo dataTypeInfo = dataType.getDataTypeInfo();
		
		String operationScript = this.buildDataTypeOperationScript(dataType);
		InputStream templateStream = HAPFileUtility.getInputStreamOnClassPath(HAPDataTypeImp.class, "DataTypeOperations.txt");
		Map<String, String> parms = new LinkedHashMap<String, String>();
		parms.put("dataTypeInfo", dataTypeInfo.toString());
		parms.put("dataTypeOperations", operationScript);
		String script = HAPStringTemplateUtil.getStringValue(templateStream, parms);
		
		
		//write the operation script to temp file
		String operationScriptFileName = HAPFileUtility.buildFullFileName(this.getTempFileLocation(), this.buildDataTypeOperationScriptFileName(dataTypeInfo));
		HAPFileUtility.writeFile(operationScriptFileName, script);
		
		//store operation script to buffer
		m_dataTypeOperationScripts.put(this.getDataTypeKey(dataTypeInfo), script);
		return operationScript;
	}
	

	/****************************** service ********************************/
	public String getDataTypeOperationScript(HAPDataTypeInfo dataTypeInfo){
		String out = this.m_dataTypeOperationScripts.get(this.getDataTypeKey(dataTypeInfo));
		if(out==null){
			out = this.processDataTypeOperationScript(this.getDataType(dataTypeInfo));
		}
		return out;
	}

	public String getRelatedDataTypeOperationScript(HAPDataTypeInfo dataTypeInfo){
		StringBuffer out = new StringBuffer();
		this.buildRelatedDataTypeOperationScript(dataTypeInfo, out);
		return out.toString();
	}

	private void buildRelatedDataTypeOperationScript(HAPDataTypeInfo dataTypeInfo, StringBuffer out){
		HAPDataType dataType = this.getDataType(dataTypeInfo);
		out.append(this.getDataTypeOperationScript(dataTypeInfo));
		
		HAPDataTypeInfo parentDataTypeInfo = dataType.getParentDataTypeInfo();
		if(parentDataTypeInfo!=null){
			this.buildRelatedDataTypeOperationScript(parentDataTypeInfo, out);
		}
	}

	
	public List<HAPDataType> getAllVersionsDataType(HAPDataTypeInfo dataTypeInfo){
		HAPDataType dataType = this.getDataType(dataTypeInfo);
		return this.getAllVersionsDataType(dataType);
	}

	private List<HAPDataType> getAllVersionsDataType(HAPDataType dataType){
		List<HAPDataType> out = new ArrayList<HAPDataType>();
		HAPDataType dataTypeTemp = dataType;
		while(dataTypeTemp!=null){
			out.add(dataTypeTemp);
			dataTypeTemp = dataTypeTemp.getOlderDataType();
		}
		return out;
	}
	
	public Map<String, List<HAPDataType>> getRelatedAllVersionsDataType(HAPDataTypeInfo dataTypeInfo){
		Map<String, List<HAPDataType>> out = new LinkedHashMap<String, List<HAPDataType>>();
		this.buildRelatedAllVersionsDataType(dataTypeInfo, out);
		return out;
	}
	
	private void buildRelatedAllVersionsDataType(HAPDataTypeInfo dataTypeInfo, Map<String, List<HAPDataType>> out){
		List<HAPDataType> dataType = this.getAllVersionsDataType(dataTypeInfo);
		out.put(dataTypeInfo.getKey(), dataType);
		
		HAPDataTypeInfo parentDataTypeInfo = dataType.get(0).getParentDataTypeInfo();
		if(parentDataTypeInfo!=null){
			this.buildRelatedAllVersionsDataType(parentDataTypeInfo, out);
		}
	}
	
	
	
	/****************************** configure ********************************/
	public HAPConfigure getConfiguration(){return this.m_configures;}
	
	/*
	 * get temporate file location
	 */
	public String getTempFileLocation(){
		String scriptLocation = this.getConfiguration().getConfigureValue(HAPConstant.DATATYPEMAN_SETTINGNAME_SCRIPTLOCATION).getStringValue();
		return scriptLocation;
	}

	
	/****************************** transform between data object and json string ********************************/
	
	/*
	 * transform string to data object, according to the structure of the string, for instance: 
	 * 		json : start with { 
	 * 		literal : #type:categary:value
	 * 		otherwise, treat as simple text
	 */
	public HAPData parseString(String text, String categary, String type){
		if(text==null)  return null;
		if(text.equals(""))  return this.getStringData(text);
		
		String token = text.substring(0, 1);
		if(token.equals("{")){
			//json
			try {
				 JSONObject jsonObj = new JSONObject(text);
				 return this.parseJson(jsonObj, categary, type);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		else if(token.equals("#")){
			//literate
			int p1 = text.indexOf(HAPConstant.SEPERATOR_PART);
			int p2 = text.indexOf(HAPConstant.SEPERATOR_PART, p1+1);
			type = text.substring(1, p1);
			categary = text.substring(p1+1, p2);
			String value = text.substring(p2+1);
			HAPDataType dataType = this.getDataType(new HAPDataTypeInfo(categary, type));
			return dataType.toData(value, HAPConstant.SERIALIZATION_TEXT);
		}
		else{
			if(categary!=null && type!=null){
				HAPDataType dataType = this.getDataType(new HAPDataTypeInfo(categary, type));
				return dataType.toData(text, HAPConstant.SERIALIZATION_TEXT);
			}
			else{
				//simple / text
				return this.getStringData(text);
			}
		}
	}

	public HAPData parseJson(JSONObject jsonObj, String categary, String type){
		if(HAPBasicUtility.isStringEmpty(categary)){
			categary = jsonObj.optJSONObject(HAPAttributeConstant.DATA_DATATYPEINFO).optString(HAPAttributeConstant.DATATYPEINFO_CATEGARY);
		}
		if(HAPBasicUtility.isStringEmpty(type)){
			type = jsonObj.optJSONObject(HAPAttributeConstant.DATA_DATATYPEINFO).optString(HAPAttributeConstant.DATATYPEINFO_TYPE);
		}
		Object valueObj = jsonObj.opt(HAPAttributeConstant.DATA_VALUE);
		if(valueObj==null)  valueObj = jsonObj;
		return this.getDataType(new HAPDataTypeInfo(categary, type)).toData(valueObj, HAPConstant.SERIALIZATION_JSON);
	}

	public HAPWraper parseWraper(JSONObject jsonObj){
		return null;
	}

	public static HAPStringData createStringData(String text){	return new HAPStringData(text);	}
	
	@Override
	public String toStringValue(String format) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		
		Map<String, String> jsonTypeMap = new HashMap<String, String>();
		for(String key : this.m_dataTypes.keySet()){
			jsonTypeMap.put(key, this.m_dataTypes.get(key).toStringValue(format));
		}
		jsonMap.put("data", HAPJsonUtility.getMapJson(jsonTypeMap));
		
		return HAPJsonUtility.getMapJson(jsonMap);
	}

	@Override
	public String toString(){
		StringBuffer out = new StringBuffer();
		
		out.append("\n\n\n**************************     DataTypeManager  Start   *****************************\n");
		out.append(HAPJsonUtility.formatJson(this.toStringValue(HAPConstant.SERIALIZATION_JSON)));
		out.append("\n**************************     DataTypeManager  End   *****************************\n\n\n");
		
		return out.toString();
	}

}
