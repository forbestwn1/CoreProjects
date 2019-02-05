package com.nosliw.data.core.script.context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.runtime.HAPResourceData;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

public class HAPHAPExecutableDataAssociationGroupWithTargetImp implements HAPExecutableDataAssociationGroupWithTarget{

	//match from data association output to target context variable
	private Map<String, HAPMatchers> m_outputMatchers;

	//associate output of activity to variable in process 
	private HAPExecutableDataAssociationGroup m_outputAssociation;

	@Override	
	public HAPExecutableDataAssociationGroup getOutputDataAssociation() {   return this.m_outputAssociation;   }
	@Override
	public void setOutputDataAssociation(HAPExecutableDataAssociationGroup output) {   this.m_outputAssociation = output;    }

	@Override
	public void addOutputMatchers(String path, HAPMatchers matchers) {   this.m_outputMatchers.put(path, matchers);     }
	@Override
	public Map<String, HAPMatchers> getOutputMatchers() {  return this.m_outputMatchers; }
	
	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(OUTPUTASSOCIATION, m_outputAssociation.toResourceData(runtimeInfo).toString());
		jsonMap.put(OUTPUTMATCHERS, HAPJsonUtility.buildJson(m_outputMatchers, HAPSerializationFormat.JSON));
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap));
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {  return new ArrayList<HAPResourceDependent>();  }
}
