package com.nosliw.data.core.dataassociation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableGroupDataAssociationForTask extends HAPExecutableImp{

	@HAPAttribute
	public static String OUT = "out";

	@HAPAttribute
	public static String IN = "in";

	private HAPExecutableDataAssociation m_inDataAssociation;
	
	//data association from process to external
	private Map<String, HAPExecutableDataAssociation> m_outDataAssociations;

	public HAPExecutableGroupDataAssociationForTask() {
		this.m_outDataAssociations = new LinkedHashMap<String, HAPExecutableDataAssociation>();
	}

	public HAPExecutableDataAssociation getInDataAssociation() {   return this.m_inDataAssociation;   }
	public void setInDataAssociation(HAPExecutableDataAssociation inDataAssociation) {  this.m_inDataAssociation = inDataAssociation;  }
	public Map<String, HAPExecutableDataAssociation> getOutDataAssociations(){    return this.m_outDataAssociations;     }
	public HAPExecutableDataAssociation getOutDataAssociation(String name) {   return this.m_outDataAssociations.get(name);   }
	public void addOutDataAssociation(String resultName, HAPExecutableDataAssociation dataAssociation) {   this.m_outDataAssociations.put(resultName, dataAssociation);   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(json);
		
		JSONObject inputMappingJson = jsonObj.optJSONObject(IN);
		if(inputMappingJson!=null) 	this.m_inDataAssociation = HAPParserDataAssociation.buildExecutalbeByJson(inputMappingJson);
		
		JSONObject outputMappingJson = jsonObj.optJSONObject(OUT);
		if(outputMappingJson!=null) {
			for(Object key : outputMappingJson.keySet()) {
				this.m_outDataAssociations.put((String)key, HAPParserDataAssociation.buildExecutalbeByJson(outputMappingJson.getJSONObject((String)key)));
			}
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_inDataAssociation!=null)   jsonMap.put(IN, HAPUtilityJson.buildJson(this.m_inDataAssociation, HAPSerializationFormat.JSON));
		if(!this.m_outDataAssociations.isEmpty())   jsonMap.put(OUT, HAPUtilityJson.buildJson(this.m_outDataAssociations, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		if(this.m_inDataAssociation!=null) 	jsonMap.put(IN, this.m_inDataAssociation.toResourceData(runtimeInfo).toString());
		
		if(!this.m_outDataAssociations.isEmpty()) {
			Map<String, String> outputMappingMap = new LinkedHashMap<String, String>();
			for(String resultName : this.m_outDataAssociations.keySet()) {
				outputMappingMap.put(resultName, this.m_outDataAssociations.get(resultName).toResourceData(runtimeInfo).toString());
			}
			jsonMap.put(OUT, HAPUtilityJson.buildMapJson(outputMappingMap));
		}
	}

	
	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		if(this.m_inDataAssociation!=null)  out.addAll(this.m_inDataAssociation.getResourceDependency(runtimeInfo, resourceManager));
		for(String resultName : this.m_outDataAssociations.keySet()) {
			out.addAll(this.m_outDataAssociations.get(resultName).getResourceDependency(runtimeInfo, resourceManager));
		}
		return out;
	}
}
