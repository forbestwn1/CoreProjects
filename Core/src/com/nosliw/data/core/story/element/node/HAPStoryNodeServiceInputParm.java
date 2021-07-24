package com.nosliw.data.core.story.element.node;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.variable.HAPVariableInfo;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.change.HAPChangeResult;

@HAPEntityWithAttribute
public class HAPStoryNodeServiceInputParm extends HAPStoryNodeImp{

	public final static String STORYNODE_TYPE = HAPConstantShared.STORYNODE_TYPE_SERVICEINPUTPARM; 

	@HAPAttribute
	public static final String PARMDEFINITION = "parmDefinition";

	private HAPVariableInfo m_parmDefinition;
	
	public HAPStoryNodeServiceInputParm() {}
	
	public HAPStoryNodeServiceInputParm(HAPVariableInfo parmDefinition) {
		super(STORYNODE_TYPE);
		this.m_parmDefinition = parmDefinition;
	}

	@Override
	public HAPChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		return super.patch(path, value, runtimeEnv);
	}

	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryNodeServiceInputParm out = new HAPStoryNodeServiceInputParm();
		super.cloneTo(out);
		out.m_parmDefinition = this.m_parmDefinition.cloneVariableInfo();
		return out;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		JSONObject defJsonObj = jsonObj.optJSONObject(PARMDEFINITION);
		if(defJsonObj!=null) {
			this.m_parmDefinition = HAPVariableInfo.buildVariableInfoFromObject(defJsonObj);
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_parmDefinition!=null)	jsonMap.put(PARMDEFINITION, this.m_parmDefinition.toStringValue(HAPSerializationFormat.JSON));
	}
}
