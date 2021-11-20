package com.nosliw.uiresource.application;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.component.HAPInfoChildResource;
import com.nosliw.data.core.complex.HAPDefinitionEntityContainer;
import com.nosliw.data.core.complex.HAPElementInContainerEntityDefinition;
import com.nosliw.data.core.component.HAPContainerChildReferenceResource;
import com.nosliw.data.core.resource.HAPResourceDefinition1;
import com.nosliw.uiresource.resource.HAPResourceIdUIAppEntry;
import com.nosliw.uiresource.resource.HAPUIAppEntryId;

@HAPEntityWithAttribute
public class HAPDefinitionApp extends HAPDefinitionEntityContainer<HAPDefinitionAppElement>{

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
	public HAPResourceDefinition1 getElementResourceDefinition(String eleName) {	return new HAPDefinitionAppEntry(this, eleName);	}

	@Override
	public HAPContainerChildReferenceResource getChildrenReferencedResource() {
		HAPContainerChildReferenceResource out = new HAPContainerChildReferenceResource();
		//entry part
		Set<HAPDefinitionAppElement> entrys = this.getContainerElements();
		for(HAPElementInContainerEntityDefinition entry : entrys) {
			out.addChildCompoentId(new HAPInfoChildResource(entry.getName(), new HAPResourceIdUIAppEntry(new HAPUIAppEntryId(this.getId(), entry.getName())), entry.getInfo()), this.getAttachmentContainer());
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(APPLICATIONDATA, HAPJsonUtility.buildJson(this.m_applicationData, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPDefinitionEntityContainer cloneResourceDefinitionContainer() {
		HAPDefinitionApp out = new HAPDefinitionApp(this.getId());
		this.cloneToResourceDefinitionContainer(out);
		for(String name : this.m_applicationData.keySet()) {
			out.m_applicationData.put(name, this.m_applicationData.get(name).cloneAppDataDefinition());
		}
		return out;
	}
}
