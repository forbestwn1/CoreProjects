package com.nosliw.core.application.division.story.brick.node;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.brick.HAPStoryNodeImp;
import com.nosliw.core.application.division.story.change.HAPStoryChangeResult;
import com.nosliw.core.application.division.story.change.HAPStoryUtilityChange;
import com.nosliw.core.application.division.story.design.wizard.servicedriven.HAPStoryUIDataStructureInfo;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public class HAPStoryNodeUI extends HAPStoryNodeImp{

	@HAPAttribute
	public static final String DATASTRUCTURE = "dataStructure";

	private HAPStoryUIDataStructureInfo m_dataStructureInfo;

	public HAPStoryNodeUI() {
		this.setEmptyDisplayName();
	}

	public HAPStoryNodeUI(String type) {
		super(type);
		this.setEmptyDisplayName();
		this.m_dataStructureInfo = new HAPStoryUIDataStructureInfo();
	}

	public HAPStoryUIDataStructureInfo getDataStructureInfo() {		return this.m_dataStructureInfo;	}
	public void setDataStructureInfo(HAPStoryUIDataStructureInfo dataStructureInfo) {     this.m_dataStructureInfo = dataStructureInfo;       }
	
	@Override
	public HAPStoryChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		HAPStoryChangeResult out = super.patch(path, value, runtimeEnv);
		if(out!=null) {
			return out;
		} else {
			out = new HAPStoryChangeResult();
			if(DATASTRUCTURE.equals(path)) {
				HAPStoryUIDataStructureInfo dataStructureInfo = (HAPStoryUIDataStructureInfo)HAPManagerSerialize.getInstance().buildObject(HAPStoryUIDataStructureInfo.class.getName(), value, HAPSerializationFormat.JSON);
				out.addRevertChange(HAPStoryUtilityChange.buildChangePatch(this, DATASTRUCTURE, this.m_dataStructureInfo.cloneUIDataStructureInfo()));
				this.m_dataStructureInfo = dataStructureInfo;
				return out;
			}
		}
		return null;
	}

	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryNodeUI out = new HAPStoryNodeUI();
		this.cloneToUIStoryNode(out);
		return out;
	}
	
	protected void cloneToUIStoryNode(HAPStoryNodeUI uiStoryNode) {
		super.cloneTo(uiStoryNode);
		if(this.m_dataStructureInfo!=null) {
			uiStoryNode.m_dataStructureInfo = this.m_dataStructureInfo.cloneUIDataStructureInfo();
		}
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		JSONObject dataStructureObj = jsonObj.optJSONObject(DATASTRUCTURE);
		if(dataStructureObj!=null) {
			this.m_dataStructureInfo = new HAPStoryUIDataStructureInfo();
			this.m_dataStructureInfo.buildObject(dataStructureObj, HAPSerializationFormat.JSON);
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATASTRUCTURE, this.m_dataStructureInfo.toStringValue(HAPSerializationFormat.JSON));
	}
}
