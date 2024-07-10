package com.nosliw.data.core.story.element.node;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.variable.HAPVariableDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.change.HAPChangeResult;

@HAPEntityWithAttribute
public class HAPStoryNodeServiceOutputItem extends HAPStoryNodeImp{

	public final static String STORYNODE_TYPE = HAPConstantShared.STORYNODE_TYPE_SERVICEOUTPUTITEM; 

	@HAPAttribute
	public static final String OUTPUTITEM = "outputItem";

	private HAPVariableDefinition m_outputItem;
	
	public HAPStoryNodeServiceOutputItem() {}
	
	public HAPStoryNodeServiceOutputItem(HAPVariableDefinition outputItem) {
		super(STORYNODE_TYPE);
		this.m_outputItem = outputItem;
	}

	@Override
	public HAPChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		return super.patch(path, value, runtimeEnv);
	}

	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryNodeServiceOutputItem out = new HAPStoryNodeServiceOutputItem();
		super.cloneTo(out);
		if(this.m_outputItem!=null)   out.m_outputItem = this.m_outputItem.cloneVariableInfo(); 
		return out;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		JSONObject defJsonObj = jsonObj.optJSONObject(OUTPUTITEM);
		if(defJsonObj!=null) {
			this.m_outputItem = HAPVariableDefinition.buildVariableInfoFromObject(defJsonObj); 
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_outputItem!=null)	jsonMap.put(OUTPUTITEM, this.m_outputItem.toStringValue(HAPSerializationFormat.JSON));
	}
}
