package com.nosliw.uiresource.page.story.element;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.change.HAPChangeResult;
import com.nosliw.data.core.story.change.HAPUtilityChange;

public class HAPStoryNodeUI extends HAPStoryNodeImp{

	@HAPAttribute
	public static final String DATASTRUCTURE = "dataStructure";

	private HAPUIDataStructureInfo m_dataStructureInfo;
	
	public HAPStoryNodeUI(String type) {
		super(type);
		this.m_dataStructureInfo = new HAPUIDataStructureInfo();
	}
	
	public HAPUIDataStructureInfo getDataStructureInfo() {
		return this.m_dataStructureInfo;
	}
	
	@Override
	public HAPChangeResult patch(String path, Object value) {
		HAPChangeResult out = super.patch(path, value);
		if(out!=null)  return out; 
		else {
			out = new HAPChangeResult();
			if(DATASTRUCTURE.equals(path)) {
				HAPUIDataStructureInfo dataStructureInfo = (HAPUIDataStructureInfo)value;
				out.addRevertChange(HAPUtilityChange.buildChangePatch(this, DATASTRUCTURE, this.m_dataStructureInfo.cloneUIDataStructureInfo()));
				this.m_dataStructureInfo = dataStructureInfo;
				return out;
			}
		}
		return null;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		JSONObject dataStructureObj = jsonObj.optJSONObject(DATASTRUCTURE);
		if(dataStructureObj!=null) {
			this.m_dataStructureInfo = new HAPUIDataStructureInfo();
			this.m_dataStructureInfo.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATASTRUCTURE, this.m_dataStructureInfo.toStringValue(HAPSerializationFormat.JSON));
	}
}
