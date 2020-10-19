package com.nosliw.data.core.story.element.node;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.data.variable.HAPVariableInfo;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.change.HAPChangeResult;
import com.nosliw.data.core.story.change.HAPUtilityChange;

@HAPEntityWithAttribute
public class HAPStoryNodeVariable extends HAPStoryNodeImp{
	
	public final static String STORYNODE_TYPE = HAPConstant.STORYNODE_TYPE_VARIABLE; 
	
	@HAPAttribute
	public static final String VARAIBLEINFO = "variableInfo";
	
	private HAPVariableInfo m_variableInfo;
	
	public HAPStoryNodeVariable() {
		super(STORYNODE_TYPE);
	}
	
	public HAPStoryNodeVariable(HAPVariableInfo variableInfo) {
		this();
		this.m_variableInfo = variableInfo;
	}

	public HAPVariableInfo getVariableInfo() {   return this.m_variableInfo;    }
	
	@Override
	public HAPChangeResult patch(String path, Object value) {
		HAPChangeResult out = super.patch(path, value);
		if(out!=null)  return out; 
		else {
			out = new HAPChangeResult();
			if(VARAIBLEINFO.equals(path)) {
				out.addRevertChange(HAPUtilityChange.buildChangePatch(this, VARAIBLEINFO, this.m_variableInfo));
				this.m_variableInfo = HAPVariableInfo.buildVariableInfoFromObject(value);
				return out;
			}
		}
		return null;
	}

	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryNodeVariable out = new HAPStoryNodeVariable();
		super.cloneTo(out);
		out.m_variableInfo = this.m_variableInfo.cloneVariableInfo();
		return out;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_variableInfo = HAPVariableInfo.buildVariableInfoFromObject(jsonObj.getJSONObject(VARAIBLEINFO));
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VARAIBLEINFO, this.m_variableInfo.toStringValue(HAPSerializationFormat.LITERATE));
	}
}
