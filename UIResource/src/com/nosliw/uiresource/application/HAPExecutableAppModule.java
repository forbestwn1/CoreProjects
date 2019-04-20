package com.nosliw.uiresource.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImpWrapper;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableGroupDataAssociation;
import com.nosliw.uiresource.module.HAPExecutableModule;

@HAPEntityWithAttribute
public class HAPExecutableAppModule extends HAPEntityInfoImpWrapper implements HAPExecutable{

	@HAPAttribute
	public static final String MODULEDEFID = "moduleDefId";

	@HAPAttribute
	public static final String ROLE = "role";

	@HAPAttribute
	public static final String MODULE = "module";
	
	@HAPAttribute
	public static final String INPUTMAPPING = "inputMapping";
	
	@HAPAttribute
	public static final String OUTPUTMAPPING = "outputMapping";

	@HAPAttribute
	public static final String APPDATA = "appData";

	private HAPExecutableModule m_module;
	
	private HAPExecutableGroupDataAssociation m_inputMapping;
	
	private HAPExecutableGroupDataAssociation m_outputMapping;

	private HAPDefinitionAppModule m_definition;
	
	private List<String> m_appDataNames;
	
	public HAPExecutableAppModule(HAPDefinitionAppModule def) {
		super(def);
		this.m_definition = def;
		this.m_inputMapping = new HAPExecutableGroupDataAssociation();
		this.m_outputMapping = new HAPExecutableGroupDataAssociation();
		this.m_appDataNames = new ArrayList<String>();
	}

	public void setModule(HAPExecutableModule module) {  this.m_module = module;  }
	
	public void addInputDataAssociation(String name, HAPExecutableDataAssociation dataAssociation) {    this.m_inputMapping.addDataAssociation(name, dataAssociation);   }
	
	public void addOutputDataAssociation(String name, HAPExecutableDataAssociation dataAssociation) {   this.m_outputMapping.addDataAssociation(name, dataAssociation);  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MODULEDEFID, this.m_definition.getModule());
		jsonMap.put(ROLE, this.m_definition.getRole());
		jsonMap.put(MODULE, HAPJsonUtility.buildJson(this.m_module, HAPSerializationFormat.JSON));
		jsonMap.put(INPUTMAPPING, HAPJsonUtility.buildJson(this.m_inputMapping, HAPSerializationFormat.JSON));
		jsonMap.put(OUTPUTMAPPING, HAPJsonUtility.buildJson(this.m_outputMapping, HAPSerializationFormat.JSON));
		jsonMap.put(APPDATA, HAPJsonUtility.buildJson(this.m_appDataNames, HAPSerializationFormat.JSON));
	}
	
	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MODULE, this.m_module.toResourceData(runtimeInfo).toString());
		jsonMap.put(INPUTMAPPING, this.m_inputMapping.toResourceData(runtimeInfo).toString());
		jsonMap.put(OUTPUTMAPPING, this.m_outputMapping.toResourceData(runtimeInfo).toString());
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		out.addAll(this.m_module.getResourceDependency(runtimeInfo));
		return out;
	}
}
