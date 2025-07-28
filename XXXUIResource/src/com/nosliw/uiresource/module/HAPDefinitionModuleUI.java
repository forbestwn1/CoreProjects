package com.nosliw.uiresource.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilitySerialize;
import com.nosliw.data.core.component.HAPDefinitionEmbededComponent;
import com.nosliw.uiresource.common.HAPInfoDecoration;

//each module ui is page unit in module that is alive in a module
//as it defined:
//		what it look like
//		where data come from: service provider
//		how to interact with page : page event handler
@HAPEntityWithAttribute
public class HAPDefinitionModuleUI extends HAPDefinitionEmbededComponent{

	@HAPAttribute
	public static String PAGE = "page";
	
	@HAPAttribute
	public static String UIDECORATION = "uiDecoration";
	
	@HAPAttribute
	public static String TYPE = "type";

	//ui page
	private String m_page;

	private List<HAPInfoDecoration> m_uiDecoration;
	
	//provide extra information about this module ui so that container can render it properly
	private String m_type;
	
	private String m_status;
	
	public HAPDefinitionModuleUI() {
		this.m_uiDecoration = new ArrayList<HAPInfoDecoration>();
	}
	
	public String getPage() {   return this.m_page;    }
	public void setPage(String page) {   this.m_page = page;   }

	public String getType() {   return this.m_type;    }
	public void setType(String type) {   this.m_type = type;   }

	public void setUIDecoration(List<HAPInfoDecoration> decs) {  this.m_uiDecoration = decs;    }
	public List<HAPInfoDecoration> getUIDecoration(){  return this.m_uiDecoration;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;

		this.setPage(jsonObj.optString(HAPDefinitionModuleUI.PAGE));
		this.setType(jsonObj.optString(HAPDefinitionModuleUI.TYPE));
		this.setStatus(jsonObj.optString(HAPDefinitionModuleUI.STATUS));

		//ui decoration
		JSONArray uiDecJsonArray = jsonObj.optJSONArray(HAPDefinitionModuleUI.UIDECORATION);
		if(uiDecJsonArray!=null) {
			this.setUIDecoration(HAPUtilitySerialize.buildListFromJsonArray(HAPInfoDecoration.class.getName(), uiDecJsonArray));
		}
		
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PAGE, this.m_page);
		jsonMap.put(UIDECORATION, HAPUtilityJson.buildJson(this.m_uiDecoration, HAPSerializationFormat.JSON));
	}
	
	public HAPDefinitionModuleUI cloneModuleUIDef() {
		HAPDefinitionModuleUI out = new HAPDefinitionModuleUI();
		this.cloneToEmbededComponent(out);
		out.m_page = this.m_page;
		out.m_type = this.m_type;
		out.m_status = this.m_status;
		for(HAPInfoDecoration dec : this.m_uiDecoration) {
			out.m_uiDecoration.add(dec.cloneDecorationInfo());
		}
		return out;
	}
}
