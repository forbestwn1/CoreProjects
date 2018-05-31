package com.nosliw.miniapp.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.miniapp.data.HAPInstanceMiniAppData;
import com.nosliw.uiresource.module.HAPInstanceUIModuleEntry;

@HAPEntityWithAttribute
public class HAPInstanceMiniAppUIEntry extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String UIMODULES = "uiModules";
	
	@HAPAttribute
	public static final String DATA = "data";
	
	private String m_id;
	
	private Map<String, HAPInstanceUIModuleEntry> m_uiModules;

	private Map<String, List<HAPInstanceMiniAppData>> m_data;
	
	public HAPInstanceMiniAppUIEntry() {}

	public HAPInstanceMiniAppUIEntry(String id) {
		this.m_id = id;
	}
	
	public String getId() {  return this.m_id;   }
	public void setId(String id) {  this.m_id = id;  }
	
	public void addUIModuleInstance(String name, HAPInstanceUIModuleEntry uiModuleInstance) {		this.m_uiModules.put(name, uiModuleInstance);	}
	public HAPInstanceUIModuleEntry getUIModuleInstance(String moduleName) {  return this.m_uiModules.get(moduleName);  }
	
	public void addData(String dataName, HAPInstanceMiniAppData data) {
		List<HAPInstanceMiniAppData> dataByName = this.m_data.get(dataName);
		if(dataByName==null) {
			dataByName = new ArrayList<HAPInstanceMiniAppData>();
			this.m_data.put(dataName, dataByName);
		}
		dataByName.add(data);
	}
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
		jsonMap.put(UIMODULES, HAPSerializeManager.getInstance().toStringValue(this.m_uiModules, HAPSerializationFormat.JSON));
		jsonMap.put(DATA, HAPSerializeManager.getInstance().toStringValue(this.m_data, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}
}
