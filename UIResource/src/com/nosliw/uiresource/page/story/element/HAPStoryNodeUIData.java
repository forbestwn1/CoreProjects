package com.nosliw.uiresource.page.story.element;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.matcher.HAPMatchersCombo;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.data.core.story.change.HAPChangeResult;
import com.nosliw.data.core.story.change.HAPUtilityChange;
import com.nosliw.uiresource.page.story.model.HAPUIDataInfo;

@HAPEntityWithAttribute
public class HAPStoryNodeUIData extends HAPStoryNodeUITag{
	
	public final static String STORYNODE_TYPE = HAPConstant.STORYNODE_TYPE_UIDATA; 
	
	@HAPAttribute
	public static final String DATAINFO = "dataInfo";
	
	@HAPAttribute
	public static final String MATCHERS = "matchers";
	
	@HAPAttribute
	public static final String ATTRIBUTE_DATAFLOW = "dataflow";
	
	//data info for external tag data to handlle
	private HAPUIDataInfo m_uiDataInfo;

	//matchers for convert external tag data to internal data
	private Map<String, HAPMatchersCombo> m_matchers;

	public HAPStoryNodeUIData() {
		this.m_matchers = new LinkedHashMap<String, HAPMatchersCombo>();
	}

	public HAPStoryNodeUIData(String tagName, String id, HAPUIDataInfo uiDataInfo, String dataFlow, Map<String, HAPMatchersCombo> matchers) {
		super(STORYNODE_TYPE, tagName, id);
		this.m_matchers = new LinkedHashMap<String, HAPMatchersCombo>();
		this.m_uiDataInfo = uiDataInfo;
		this.addAttribute(ATTRIBUTE_DATAFLOW, dataFlow);
		if(matchers!=null) {
			this.m_matchers = new LinkedHashMap<String, HAPMatchersCombo>();
			this.m_matchers.putAll(matchers);
		}
	}

	@Override
	public HAPChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		HAPChangeResult out = super.patch(path, value, runtimeEnv);
		if(out!=null)  return out; 
		else {
			out = new HAPChangeResult();
			if(MATCHERS.equals(path)) {
				out.addRevertChange(HAPUtilityChange.buildChangePatch(this, MATCHERS, this.m_matchers));
				this.m_matchers = (Map<String, HAPMatchersCombo>)value;
				return out;
			}
		}
		return null;
	}

	
	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryNodeUIData out = new HAPStoryNodeUIData();
		this.cloneToUITagStoryNode(out);
		out.m_uiDataInfo = this.m_uiDataInfo.cloneUIDataInfo();
		
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		
		JSONObject dataInfoObj = jsonObj.optJSONObject(DATAINFO);
		if(dataInfoObj!=null) {
			this.m_uiDataInfo = new HAPUIDataInfo();
			this.m_uiDataInfo.buildObject(dataInfoObj, HAPSerializationFormat.JSON);
		}

		JSONObject matchersObj = jsonObj.optJSONObject(MATCHERS);
		if(matchersObj!=null) {
			this.m_matchers = new LinkedHashMap<String, HAPMatchersCombo>();
			for(Object key : matchersObj.keySet()) {
				String name = (String)key;
				HAPMatchersCombo matchers = new HAPMatchersCombo();
				matchers.buildObject(matchersObj.get(name), HAPSerializationFormat.JSON);
				this.m_matchers.put(name, matchers);
			}
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATAINFO, this.m_uiDataInfo.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(MATCHERS, HAPJsonUtility.buildJson(this.m_matchers, HAPSerializationFormat.JSON));
	}
}
