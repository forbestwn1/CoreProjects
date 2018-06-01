package com.nosliw.uiresource.module;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitResource;

@HAPEntityWithAttribute
public class HAPInstanceUIModuleEntry extends HAPSerializableImp{

	@HAPAttribute
	public static final String ENTRYPAGE = "entryPage";
	
	@HAPAttribute
	public static final String PAGES = "pages";
	
	private String m_entryPage;
	
	private Map<String, HAPUIDefinitionUnitResource> m_pages;
	
	public HAPInstanceUIModuleEntry(String entryPage) {
		this.m_entryPage = entryPage;
		this.m_pages = new LinkedHashMap<String, HAPUIDefinitionUnitResource>();
	}

	public String getEntryPage() {   return this.m_entryPage;   }
	
	public void addPage(String pageName, HAPUIDefinitionUnitResource page) {	this.m_pages.put(pageName, page);	}
	public Map<String, HAPUIDefinitionUnitResource> getPages(){ return this.m_pages;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ENTRYPAGE, this.m_entryPage);
		jsonMap.put(PAGES, HAPJsonUtility.buildJson(this.m_pages, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_entryPage = (String)jsonObj.opt(ENTRYPAGE);
		this.m_pages = HAPSerializeUtility.buildMapFromJsonObject(HAPUIDefinitionUnitResource.class.getName(), jsonObj.optJSONObject(PAGES));
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}
}
