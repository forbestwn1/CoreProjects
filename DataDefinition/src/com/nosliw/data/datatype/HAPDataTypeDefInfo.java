package com.nosliw.data.datatype;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.utils.HAPAttributeConstant;

/*
 * Class for data type definition information
 * it is used for entity attribute definition
 * therefor it has more information than HAPDataTypeInfo
 */
public class HAPDataTypeDefInfo extends HAPDataTypeInfo{

	
	//child categary and type are for defining container
	//they define the container element type
	private String m_childDataCategary;
	private String m_childDataType;
	private Set<String> m_childEntityGroups;

	//sometimes, when defining attribute, we don't know which entity should be
	//however, we know which entity group those entity should belong to
	//in these case, set entityGroups instead of set type
	private Set<String> m_entityGroups;
	
	public HAPDataTypeDefInfo(HAPDataTypeInfo info){
		super(info.getCategary(), info.getType());
		this.m_entityGroups = new HashSet<String>();
		this.m_childEntityGroups = new HashSet<String>();
	}
	
	public HAPDataTypeDefInfo(String categary, String type) {
		super(categary, type);
		this.m_entityGroups = new HashSet<String>();
		this.m_childEntityGroups = new HashSet<String>();
	}
	
	public HAPDataTypeInfo getDataTypeInfo(){
		return new HAPDataTypeInfo(this.getCategary(), this.getType());
	}
	
	public String getChildDataCategary(){return this.m_childDataCategary;}
	public String getChildDataType(){return this.m_childDataType;}
	public Set<String> getChildEntityGroups(){return this.m_childEntityGroups;}
	
	public Set<String> getEntityGroups(){return this.m_entityGroups;}
	
	public void setChildDataCategary(String categary){this.m_childDataCategary=categary;}
	public void setChildDataType(String type){this.m_childDataType=type;}
	public void addChildEntityGroup(String group){	this.m_childEntityGroups.add(group);	}
	
	public void addEntityGroup(String group){		this.m_entityGroups.add(group);	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> jsonTypeMap, String format){
		super.buildJsonMap(jsonMap, jsonTypeMap, format);
		jsonMap.put(HAPAttributeConstant.DATATYPEINFO_ENTITYGROUPS, HAPJsonUtility.getArrayJson(this.getEntityGroups().toArray(new String[0])));
		jsonMap.put(HAPAttributeConstant.DATATYPEINFO_CHILD_CATEGARY, this.getChildDataCategary());
		jsonMap.put(HAPAttributeConstant.DATATYPEINFO_CHILD_TYPE, this.getChildDataType());
		jsonMap.put(HAPAttributeConstant.DATATYPEINFO_CHILD_ENTITYGROUPS, HAPJsonUtility.getArrayJson(this.getChildEntityGroups().toArray(new String[0])));
	}

	public static HAPDataTypeDefInfo parse(JSONObject jsonObj){
		String type = jsonObj.optString(HAPAttributeConstant.DATATYPEINFO_TYPE);
		String categary = jsonObj.optString(HAPAttributeConstant.DATATYPEINFO_CATEGARY);
		HAPDataTypeDefInfo out = new HAPDataTypeDefInfo(categary, type);

		String childDataCategary = jsonObj.optString(HAPAttributeConstant.DATATYPEINFO_CHILD_CATEGARY);
		if(HAPBasicUtility.isStringNotEmpty(childDataCategary))	out.setChildDataCategary(childDataCategary);
		
		String childDataType = jsonObj.optString(HAPAttributeConstant.DATATYPEINFO_CHILD_TYPE);
		if(HAPBasicUtility.isStringNotEmpty(childDataType))  out.setChildDataType(childDataType);
		
		JSONArray entityGroups = jsonObj.optJSONArray(HAPAttributeConstant.DATATYPEINFO_ENTITYGROUPS);
		for(int i=0; i<entityGroups.length(); i++){
			try {
				out.addEntityGroup(entityGroups.get(i).toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		JSONArray childEntityGroups = jsonObj.optJSONArray(HAPAttributeConstant.DATATYPEINFO_CHILD_ENTITYGROUPS);
		for(int i=0; i<childEntityGroups.length(); i++){
			try {
				out.addChildEntityGroup(childEntityGroups.get(i).toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return out;
	}

	@Override
	public boolean equals(Object data){
		if(data instanceof HAPDataTypeDefInfo){
			HAPDataTypeDefInfo info = (HAPDataTypeDefInfo)data;
			//check super.equals
			if(!super.equals(info))   return false;
			if(HAPBasicUtility.isEquals(info.getChildDataCategary(), this.getChildDataCategary()) &&
			   HAPBasicUtility.isEquals(info.getChildDataType(), this.getChildDataType()) &&
			   HAPBasicUtility.isEqualSet(info.getEntityGroups(), this.getEntityGroups()) && 
			   HAPBasicUtility.isEqualSet(info.getChildEntityGroups(), this.getChildEntityGroups())){
					return true;
			}
			else return false;
		}
		return false;
	}
}
