package com.nosliw.data.core.process;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPResourceData;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.script.context.HAPExecutableDataAssociationGroup;

@HAPEntityWithAttribute
public class HAPExecutableResultActivityNormal extends HAPEntityInfoWritableImp implements HAPBackToGlobalContext, HAPExecutable{

	@HAPAttribute
	public static String FLOW = "flow";

	private HAPDefinitionResultActivityNormal m_definition;
	
	//match from data association output to target context variable
	private Map<String, HAPMatchers> m_outputMatchers;

	//associate output of activity to variable in process 
	private HAPExecutableDataAssociationGroup m_outputAssociation;
	
	//next activity
	public HAPExecutableResultActivityNormal(HAPDefinitionResultActivityNormal definition) {
		this.m_definition = definition;
		this.m_outputMatchers = new LinkedHashMap<String, HAPMatchers>();
	}
	
	public HAPDefinitionSequenceFlow getFlow() {  return this.m_definition.getFlow();  }
	
	@Override	
	public HAPExecutableDataAssociationGroup getOutputDataAssociation() {   return this.m_outputAssociation;   }
	@Override
	public void setOutputDataAssociation(HAPExecutableDataAssociationGroup output) {   this.m_outputAssociation = output;    }

	@Override
	public void addOutputMatchers(String path, HAPMatchers matchers) {   this.m_outputMatchers.put(path, matchers);     }
	@Override
	public Map<String, HAPMatchers> getOutputMatchers() {  return this.m_outputMatchers; }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(FLOW, this.getFlow().toStringValue(HAPSerializationFormat.JSON));
		if(this.m_outputAssociation!=null)		jsonMap.put(OUTPUTASSOCIATION, m_outputAssociation.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(OUTPUTMATCHERS, HAPJsonUtility.buildJson(m_outputMatchers, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(FLOW, this.getFlow().toStringValue(HAPSerializationFormat.JSON));
		if(this.m_outputAssociation!=null)  jsonMap.put(OUTPUTASSOCIATION, m_outputAssociation.toResourceData(runtimeInfo).toString());
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap));
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {		return new ArrayList<HAPResourceDependent>();	}

}
