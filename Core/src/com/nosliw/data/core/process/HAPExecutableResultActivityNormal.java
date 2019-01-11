package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPResourceData;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

@HAPEntityWithAttribute
public class HAPExecutableResultActivityNormal extends HAPEntityInfoWritableImp implements HAPExecutable{

	@HAPAttribute
	public static String FLOW = "flow";

	@HAPAttribute
	public static String OUTPUTASSOCIATION = "outputAssociation";
	
	private HAPDefinitionResultActivityNormal m_definition;
	
	//associate output of activity to variable in process 
	private HAPExecutableDataAssociationGroup m_outputAssociation;
	
	//next activity
	public HAPExecutableResultActivityNormal(HAPDefinitionResultActivityNormal definition) {
		this.m_definition = definition;
	}
	
	public HAPDefinitionSequenceFlow getFlow() {  return this.m_definition.getFlow();  }
	
	public HAPExecutableDataAssociationGroup getOutputDataAssociation() {   return this.m_outputAssociation;   }
	public void setOutputDataAssociation(HAPExecutableDataAssociationGroup output) {   this.m_outputAssociation = output;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(FLOW, this.getFlow().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(OUTPUTASSOCIATION, m_outputAssociation.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(FLOW, this.getFlow().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(OUTPUTASSOCIATION, m_outputAssociation.toResourceData(runtimeInfo).toString());
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap));
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {		return null;	}
}
