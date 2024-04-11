package com.nosliw.data.core.story.element.node;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.variable.HAPVariableInfo;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.change.HAPChangeResult;
import com.nosliw.data.core.story.change.HAPUtilityChange;

@HAPEntityWithAttribute
public class HAPStoryNodeVariable extends HAPStoryNodeImp{
	
	public final static String STORYNODE_TYPE = HAPConstantShared.STORYNODE_TYPE_VARIABLE; 
	
	@HAPAttribute
	public static final String VARAIBLEINFO = "variableInfo";
	
	@HAPAttribute
	public static final String INITDATA = "initData";
	
	private HAPVariableInfo m_variableInfo;
	
	private HAPData m_initData;
	
	public HAPStoryNodeVariable() {
		super(STORYNODE_TYPE);
	}
	
	public HAPStoryNodeVariable(HAPVariableInfo variableInfo, HAPData initData) {
		this();
		this.m_variableInfo = variableInfo;
		this.m_initData = initData;
	}

	public HAPVariableInfo getVariableInfo() {   return this.m_variableInfo;    }
	public HAPData getInitData() {    return this.m_initData;    }
	
	@Override
	public HAPChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		HAPChangeResult out = super.patch(path, value, runtimeEnv);
		if(out!=null)  return out; 
		else {
			out = new HAPChangeResult();
			if(VARAIBLEINFO.equals(path)) {
				out.addRevertChange(HAPUtilityChange.buildChangePatch(this, VARAIBLEINFO, this.m_variableInfo));
				this.m_variableInfo = HAPVariableInfo.buildVariableInfoFromObject(value);
				return out;
			}
			else if(INITDATA.equals(path)) {
				out.addRevertChange(HAPUtilityChange.buildChangePatch(this, INITDATA, this.m_initData));
				this.m_initData = HAPUtilityData.buildDataWrapperFromObject(value);
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
		out.m_initData = this.m_initData;
		return out;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_variableInfo = HAPVariableInfo.buildVariableInfoFromObject(jsonObj.getJSONObject(VARAIBLEINFO));
		JSONObject initDataObj = jsonObj.optJSONObject(INITDATA);
		this.m_initData = HAPUtilityData.buildDataWrapperFromObject(initDataObj);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VARAIBLEINFO, this.m_variableInfo.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_initData!=null)	jsonMap.put(INITDATA, this.m_initData.toStringValue(HAPSerializationFormat.JSON));
	}
}
