package com.nosliw.data.core.domain;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;

public class HAPInfoParentComplex extends HAPSerializableImp{

	public static final String PARENTID = "parentId";
	public static final String RELATION = "relation";

	//parent complex entity
	private HAPIdEntityInDomain m_parentId;
	private String m_parentAlias;

	//parent info for complex
	private HAPConfigureParentRelationComplex m_parentRelation;

	public HAPInfoParentComplex() {
		this.m_parentRelation = new HAPConfigureParentRelationComplex();
	}
	
	public HAPIdEntityInDomain getParentId() {    return this.m_parentId;   }
	
	public void setParentId(HAPIdEntityInDomain parentId) {    this.m_parentId = parentId;     }
	
	public HAPConfigureParentRelationComplex getParentRelationConfigure() {    return this.m_parentRelation;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(PARENTID, this.m_parentId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(RELATION, this.m_parentRelation.toStringValue(HAPSerializationFormat.JSON));
	}

}
