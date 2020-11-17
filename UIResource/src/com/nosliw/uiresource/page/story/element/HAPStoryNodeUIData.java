package com.nosliw.uiresource.page.story.element;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.uiresource.page.story.model.HAPUIDataInfo;

@HAPEntityWithAttribute
public class HAPStoryNodeUIData extends HAPStoryNodeUITag{
	
	public final static String STORYNODE_TYPE = HAPConstant.STORYNODE_TYPE_UIDATA; 
	
	@HAPAttribute
	public static final String DATAINFO = "dataInfo";
	
	@HAPAttribute
	public static final String ATTRIBUTE_DATAFLOW = "dataflow";
	
	private HAPUIDataInfo m_uiDataInfo;
	
	public HAPStoryNodeUIData() {}

	public HAPStoryNodeUIData(String tagName, HAPUIDataInfo uiDataInfo, String dataFlow) {
		super(STORYNODE_TYPE, tagName);
		this.m_uiDataInfo = uiDataInfo;
		this.addAttribute(ATTRIBUTE_DATAFLOW, dataFlow);
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
		
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATAINFO, this.m_uiDataInfo.toStringValue(HAPSerializationFormat.JSON));
	}
}
