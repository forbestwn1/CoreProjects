package com.nosliw.core.application.division.story.change;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.division.story.HAPStoryInfoElement;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPStoryChangeDataGroupElement extends HAPSerializableImp{

	@HAPAttribute
	public static final String IDINDEX = "idIndex";

	@HAPAttribute
	public static final String ELEMENTINFO = "elementInfo";

	private int m_idIndex;
	
	private HAPStoryInfoElement m_elementInfo;

	public HAPStoryChangeDataGroupElement(HAPStoryInfoElement m_elementInfo) {
		
	}
	
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_idIndex = jsonObj.getInt(IDINDEX);
		this.m_elementInfo = new HAPStoryInfoElement();
		this.m_elementInfo.buildObject(jsonObj.getJSONObject(ELEMENTINFO), HAPSerializationFormat.JSON);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENTINFO, HAPUtilityJson.buildJson(this.m_elementInfo, HAPSerializationFormat.JSON));
		jsonMap.put(IDINDEX, this.m_idIndex+"");
		typeJsonMap.put(IDINDEX, Integer.class);
	}
	
}
