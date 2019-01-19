package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

//process that is part of system
//it should include data association that mapping result to system context 
public class HAPDefinitionEmbededProcess extends HAPDefinitionProcess{

	@HAPAttribute
	public static String OUTPUTMAPPING = "outputMapping";

	//data association from result to system context 
	private Map<String, HAPDefinitionDataAssociationGroup> m_outputMapping;
	
	public HAPDefinitionEmbededProcess() {
		this.m_outputMapping = new LinkedHashMap<String, HAPDefinitionDataAssociationGroup>();
	}
	
	public void addOutputMapping(String name, HAPDefinitionDataAssociationGroup dataAssociation) {   this.m_outputMapping.put(name, dataAssociation);    }
	
	public Map<String, HAPDefinitionDataAssociationGroup> getOutputMapping(){   return this.m_outputMapping;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(OUTPUTMAPPING, HAPJsonUtility.buildJson(this.m_outputMapping, HAPSerializationFormat.JSON));
	}
}
