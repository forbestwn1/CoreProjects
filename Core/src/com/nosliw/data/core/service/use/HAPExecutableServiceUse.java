package com.nosliw.data.core.service.use;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPExecutableDataAssociationGroup;

public class HAPExecutableServiceUse extends HAPExecutableImp{

	@HAPAttribute
	public static String PARMMAPPING = "parmMapping";
	@HAPAttribute
	public static String PARMMATCHERS = "parmMatchers";

	@HAPAttribute
	public static String RESULTMAPPING = "resultMapping";
	@HAPAttribute
	public static String RESULTMATCHERS = "resultMatchers";

	//parms path
	private HAPExecutableDataAssociationGroup m_parmMapping;
	private Map<String, HAPMatchers> m_parmMatchers;
	
	private Map<String, HAPExecutableDataAssociationGroup> m_resultMapping;
	private Map<String, Map<String, Map<String, HAPMatchers>>> m_resultMatchers;
	
	private HAPDefinitionServiceUse m_definition;
	
	public HAPExecutableServiceUse(HAPDefinitionServiceUse definition) {
		this.m_definition = definition;
		this.m_parmMatchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_resultMapping = new LinkedHashMap<String, HAPExecutableDataAssociationGroup>();
		this.m_resultMatchers = new LinkedHashMap<String, Map<String, Map<String, HAPMatchers>>>();
	}
	
	public void setParmMapping(HAPExecutableDataAssociationGroup parmMapping) {   this.m_parmMapping = parmMapping;   }
	public void setParmMatchers(String parmName, HAPMatchers matchers) {   this.m_parmMatchers.put(parmName, matchers);  }
	
	public void addResultMapping(String result, HAPExecutableDataAssociationGroup mapping) {   this.m_resultMapping.put(result, mapping);  }
	public void addResultMatchers(String result, String eleName, Map<String, HAPMatchers> matchers) {
		Map<String, Map<String, HAPMatchers>> resultMatchers = this.m_resultMatchers.get(result);
		if(resultMatchers==null) {
			resultMatchers = new LinkedHashMap<String, Map<String, HAPMatchers>>();
			this.m_resultMatchers.put(result, resultMatchers);
		}
		resultMatchers.put(eleName, matchers);
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		return new ArrayList<HAPResourceDependent>();
	}

}
