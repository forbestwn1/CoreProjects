package com.nosliw.data.core.imp;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.data.HAPDataType;
import com.nosliw.core.data.HAPDataTypeId;
import com.nosliw.core.data.HAPDataTypePicture;
import com.nosliw.core.data.HAPRelationship;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPDataTypePictureImp extends HAPSerializableImp implements HAPDataTypePicture{

	@HAPAttribute
	public static String SOURCE = "source";

	@HAPAttribute
	public static String RELATIONSHIPS = "relationship";
	
	private HAPDataTypeImp m_sourceDataType;
	
	private Map<HAPDataTypeId, HAPRelationshipImp> m_relationships;
	
	public HAPDataTypePictureImp(HAPDataTypeImp mainDataType){
		this.m_relationships = new LinkedHashMap<HAPDataTypeId, HAPRelationshipImp>();
		this.m_sourceDataType = mainDataType;
	}
	
	@Override
	public HAPDataType getSourceDataType(){		return this.m_sourceDataType;  }

	@Override
	public HAPRelationshipImp getRelationship(HAPDataTypeId dataTypeInfo){
		return this.m_relationships.get(dataTypeInfo);
	}

	@Override
	public Set<? extends HAPRelationship> getRelationships(){
		return new HashSet( this.m_relationships.values());
	}

	public void addRelationship(HAPRelationshipImp relationship){
		relationship.setSourceDataType(this.m_sourceDataType);
		this.m_relationships.put(relationship.getTarget(), relationship);
	}

	@Override
	protected String buildLiterate(){  return this.toStringValue(HAPSerializationFormat.JSON_FULL); }

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(SOURCE, this.m_sourceDataType.toStringValue(HAPSerializationFormat.JSON));
		
		Map<String, String> relationJsonMap = new LinkedHashMap<String, String>();
		for(HAPDataTypeId dataTypeId : this.m_relationships.keySet()){
			relationJsonMap.put(dataTypeId.toStringValue(HAPSerializationFormat.LITERATE), this.m_relationships.get(dataTypeId).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(RELATIONSHIPS, HAPUtilityJson.buildMapJson(relationJsonMap));
	}	
}
