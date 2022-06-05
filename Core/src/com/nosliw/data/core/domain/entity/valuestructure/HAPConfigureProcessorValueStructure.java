package com.nosliw.data.core.domain.entity.valuestructure;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

//configure 
public class HAPConfigureProcessorValueStructure extends HAPSerializableImp{

	private static final String RELATIVE = "relative";

	private static final String INHERIT = "inherit";

	private HAPConfigureProcessorRelative m_relativeProcessorConfigure;

	private HAPConfigureProcessorInherit m_inheritProcessorConfigure;

	public HAPConfigureProcessorValueStructure() {
		this.m_inheritProcessorConfigure = new HAPConfigureProcessorInherit();
		this.m_relativeProcessorConfigure = new HAPConfigureProcessorRelative();
	}

	public HAPConfigureProcessorValueStructure cloneConfigure() {
		HAPConfigureProcessorValueStructure out = new HAPConfigureProcessorValueStructure();
		return out;
	}

	public HAPConfigureProcessorInherit getInheritProcessorConfigure() {    return this.m_inheritProcessorConfigure;     }
	public HAPConfigureProcessorRelative getRelativeProcessorConfigure() {    return this.m_relativeProcessorConfigure;    }
	
	public void mergeHard(HAPConfigureProcessorValueStructure configure) {
		this.m_inheritProcessorConfigure.mergeHard(configure.getInheritProcessorConfigure());
		this.m_relativeProcessorConfigure.mergeHard(configure.getRelativeProcessorConfigure());
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_inheritProcessorConfigure.buildObject(jsonObj.optJSONObject(INHERIT), HAPSerializationFormat.JSON);
		this.m_relativeProcessorConfigure.buildObject(jsonObj.optJSONObject(RELATIVE), HAPSerializationFormat.JSON);
		return true;
	}

}
