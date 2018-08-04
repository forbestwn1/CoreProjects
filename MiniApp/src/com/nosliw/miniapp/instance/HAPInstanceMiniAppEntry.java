package com.nosliw.miniapp.instance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.miniapp.data.HAPInstanceData;

@HAPEntityWithAttribute
public class HAPInstanceMiniAppEntry extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String UIMODULES = "uiModules";
	
	@HAPAttribute
	public static final String RESOURCES = "resources";

	@HAPAttribute
	public static final String DATA = "data";
	
	private String m_id;
	
	private Map<String, HAPInstanceModule> m_uiModules;

	//dependent resources
	private Set<HAPResourceId> m_resourcesId;
	
	private Map<String, List<HAPInstanceData>> m_data;
	
	public HAPInstanceMiniAppEntry() {
		this.m_resourcesId = new HashSet<HAPResourceId>();
		this.m_data = new LinkedHashMap<String, List<HAPInstanceData>>();
		this.m_uiModules = new LinkedHashMap<String, HAPInstanceModule>();
	}

	public HAPInstanceMiniAppEntry(String id) {		this.m_id = id;	}
	
	public String getId() {  return this.m_id;   }
	public void setId(String id) {  this.m_id = id;  }
	
	public void addDependentResourceId(HAPResourceId resourceId) {   this.m_resourcesId.add(resourceId);  }
	
	public void addUIModuleInstance(String name, HAPInstanceModule uiModuleInstance) {		this.m_uiModules.put(name, uiModuleInstance);	}
	public HAPInstanceModule getUIModuleInstance(String moduleName) {  return this.m_uiModules.get(moduleName);  }
	
	public void addData(String dataName, HAPInstanceData data) {
		List<HAPInstanceData> dataByName = this.m_data.get(dataName);
		if(dataByName==null) {
			dataByName = new ArrayList<HAPInstanceData>();
			this.m_data.put(dataName, dataByName);
		}
		dataByName.add(data);
	}
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
		jsonMap.put(UIMODULES, HAPSerializeManager.getInstance().toStringValue(this.m_uiModules, HAPSerializationFormat.JSON));
		jsonMap.put(DATA, HAPSerializeManager.getInstance().toStringValue(this.m_data, HAPSerializationFormat.JSON));
		List<String> resourcesList = new ArrayList<String>();
		for(HAPResourceId resourceId : this.m_resourcesId)    resourcesList.add(resourceId.toString());
		jsonMap.put(RESOURCES, HAPJsonUtility.buildArrayJson(resourcesList.toArray(new String[0])));
		
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}
}
