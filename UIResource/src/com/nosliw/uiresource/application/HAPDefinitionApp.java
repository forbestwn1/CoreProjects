package com.nosliw.uiresource.application;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.component.HAPChildrenComponentId;
import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPResourceDefinitionContainer;
import com.nosliw.data.core.component.HAPResourceDefinitionContainerElement;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.uiresource.resource.HAPResourceIdUIAppEntry;
import com.nosliw.uiresource.resource.HAPUIAppEntryId;

@HAPEntityWithAttribute
public class HAPDefinitionApp extends HAPResourceDefinitionContainer<HAPDefinitionAppElement>{

	@HAPAttribute
	public static final String ID = "id";

	@HAPAttribute
	public static final String APPLICATIONDATA = "applicationData";
	
	//global data definition 
	//it can be stateful data(the data that can retrieve next time you use the app)
	private Map<String, HAPDefinitionAppData> m_applicationData;

	private String m_id;
	
	public HAPDefinitionApp(String id) {
		this.m_id = id;
		this.m_applicationData = new LinkedHashMap<String, HAPDefinitionAppData>();
	}
	
	@Override
	public String getId() {   return this.m_id;   }
	@Override
	public void setId(String id) {   this.m_id = id;   }
	public Map<String, HAPDefinitionAppData> getApplicationData(){   return this.m_applicationData;   }
	public void setApplicationData(Map<String, HAPDefinitionAppData> dataDef) {		if(dataDef!=null)   this.m_applicationData = dataDef;	}

	public void addEntry(HAPDefinitionAppElementUI entry) {
		String id = entry.getId();
		if(HAPBasicUtility.isStringEmpty(id)) {
			id = HAPUtilityApp.ENTRY_DEFAULT;
			entry.setName(id);
		}
		this.addContainerElement(entry);
	}
	
	@Override
	public HAPResourceDefinition getElementResourceDefinition(String eleName) {	return new HAPDefinitionAppEntry(this, eleName);	}

	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		HAPChildrenComponentIdContainer out = new HAPChildrenComponentIdContainer();
		//entry part
		Set<HAPDefinitionAppElement> entrys = this.getContainerElements();
		for(HAPResourceDefinitionContainerElement entry : entrys) {
			out.addChildCompoentId(new HAPChildrenComponentId(entry.getName(), new HAPResourceIdUIAppEntry(new HAPUIAppEntryId(this.getId(), entry.getName())), entry.getInfo()), this.getAttachmentContainer());
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(APPLICATIONDATA, HAPJsonUtility.buildJson(this.m_applicationData, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPResourceDefinitionContainer cloneResourceDefinitionContainer() {
		HAPDefinitionApp out = new HAPDefinitionApp(this.getId());
		this.cloneToResourceDefinitionContainer(out);
		for(String name : this.m_applicationData.keySet()) {
			out.m_applicationData.put(name, this.m_applicationData.get(name).cloneAppDataDefinition());
		}
		return out;
	}
}
