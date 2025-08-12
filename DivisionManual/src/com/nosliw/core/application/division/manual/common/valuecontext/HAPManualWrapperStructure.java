package com.nosliw.core.application.division.manual.common.valuecontext;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.structure.HAPInfoStructureInWrapper;

//wrapper for value structure
//extra info for value structure, group name
public class HAPManualWrapperStructure extends HAPSerializableImp{

	public static final String RUNTIMEID = "runtimeId";
	public static final String STRWUCTUREINFO = "structureInfo";

	private String m_structureRuntimeId;
	
	private HAPInfoStructureInWrapper m_structureInfo = new HAPInfoStructureInWrapper();
	
	
	public HAPManualWrapperStructure() {}

	public HAPManualWrapperStructure(String valueStructureRuntimeId) {
		this.m_structureRuntimeId = valueStructureRuntimeId;
	}
	
	public String getValueStructureRuntimeId() {	return this.m_structureRuntimeId;	}
	public void setValueStructureRuntimeId(String valueStructureRuntimeId) {	this.m_structureRuntimeId = valueStructureRuntimeId;	}
	
	public HAPInfoStructureInWrapper getStructureInfo() {    return this.m_structureInfo;    }

	public void setStructureInfo(HAPInfoStructureInWrapper info) {
		if(info==null) {
			info = new HAPInfoStructureInWrapper();
		}
		this.m_structureInfo = info;   
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(RUNTIMEID, this.m_structureRuntimeId);
		if(this.m_structureInfo!=null) {
			jsonMap.put(STRWUCTUREINFO, this.m_structureInfo.toStringValue(HAPSerializationFormat.JSON));
		}
	}

	public HAPManualWrapperStructure cloneValueStructureWrapper() {
		HAPManualWrapperStructure out = new HAPManualWrapperStructure();
		out.m_structureRuntimeId = this.m_structureRuntimeId;
		out.m_structureInfo = this.m_structureInfo.cloneValueStructureInfoInWrapper();
		return out;
	}
	
	public void cloneToChildValueStructureWrapper(HAPManualWrapperStructure valueStructureInfo) {
		valueStructureInfo.getStructureInfo().setGroupType(valueStructureInfo.getStructureInfo().getGroupType());
	}	
}
