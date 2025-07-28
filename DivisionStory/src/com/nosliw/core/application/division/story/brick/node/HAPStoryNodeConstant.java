package com.nosliw.core.application.division.story.brick.node;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.variable.HAPVariableDataInfo;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.brick.HAPStoryNodeImp;
import com.nosliw.core.application.division.story.change.HAPStoryChangeResult;
import com.nosliw.core.application.division.story.change.HAPStoryUtilityChange;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.data.HAPUtilityData;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public class HAPStoryNodeConstant extends HAPStoryNodeImp{
	
	public final static String STORYNODE_TYPE = HAPConstantShared.STORYNODE_TYPE_CONSTANT; 
	
	@HAPAttribute
	public static final String DATA = "data";
	
	@HAPAttribute
	public static final String DATATYPE = "dataType";
	
	@HAPAttribute
	public static final String ISMANDATORY = "isMandatory";

	private HAPData m_data;
	
	private HAPVariableDataInfo m_dataType;

	private boolean m_isMandatory = true;
	
	public HAPStoryNodeConstant() {
		super(STORYNODE_TYPE);
	}

	public HAPStoryNodeConstant(HAPVariableDataInfo dataType) {
		this(dataType, null, true);
	}

	public HAPStoryNodeConstant(HAPVariableDataInfo dataType, HAPData data, boolean isMandatory) {
		this();
		this.m_dataType = dataType;
		this.m_data = data;
		this.m_isMandatory = isMandatory;
	}

	public HAPData getData() {   return this.m_data;   }
	public void setData(HAPData data) {    this.m_data = data;    }

	public HAPVariableDataInfo getDataType() {   return this.m_dataType;   }
	public void setDataType(HAPVariableDataInfo dataType) {    this.m_dataType = dataType;    }

	public boolean isMandatory() {    return this.m_isMandatory;   }
	
	@Override
	public HAPStoryChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		HAPStoryChangeResult out = super.patch(path, value, runtimeEnv);
		if(out!=null)  return out; 
		else {
			out = new HAPStoryChangeResult();
			if(DATA.equals(path)) {
				out.addRevertChange(HAPStoryUtilityChange.buildChangePatch(this, path, this.m_data));
				this.m_data = HAPUtilityData.buildDataWrapperFromObject(value);
				return out;
			}
		}
		return null;
	}

	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryNodeConstant out = new HAPStoryNodeConstant();
		super.cloneTo(out);
		if(this.m_dataType!=null) out.m_dataType = this.m_dataType.cloneVariableDataInfo();
		if(this.m_data!=null)  out.m_data = this.m_data.cloneData();
		out.m_isMandatory = this.m_isMandatory; 
		return out;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_data = HAPUtilityData.buildDataWrapperFromObject(jsonObj.opt(DATA));
		JSONObject dataTypeObj = jsonObj.optJSONObject(DATATYPE);
		if(dataTypeObj!=null) {
			this.m_dataType = new HAPVariableDataInfo();
			this.m_dataType.buildObject(dataTypeObj, HAPSerializationFormat.JSON);
		}
		this.m_isMandatory = jsonObj.getBoolean(ISMANDATORY);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_data!=null)	jsonMap.put(DATA, this.m_data.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_dataType!=null)	jsonMap.put(DATATYPE, this.m_dataType.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ISMANDATORY, this.m_isMandatory+"");
		typeJsonMap.put(ISMANDATORY, Boolean.class);
	}

}
