package com.nosliw.data.core.story.element.node;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPUtilityData;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.story.HAPStoryNodeImp;

@HAPEntityWithAttribute
public class HAPStoryNodeConstant extends HAPStoryNodeImp{
	
	public final static String STORYNODE_TYPE = HAPConstant.STORYNODE_TYPE_CONSTANT; 
	
	@HAPAttribute
	public static final String DATA = "data";
	
	@HAPAttribute
	public static final String DATATYPE = "dataType";
	
	private HAPData m_data;
	
	private HAPDataTypeCriteria m_dataType;

	public HAPStoryNodeConstant() {
		super(STORYNODE_TYPE);
	}

	public HAPStoryNodeConstant(HAPDataTypeCriteria dataType) {
		this();
		this.m_dataType = dataType;
	}

	public HAPData getData() {   return this.m_data;   }
	public void setData(HAPData data) {    this.m_data = data;    }

	public HAPDataTypeCriteria getDataType() {   return this.m_dataType;   }
	public void setDataType(HAPDataTypeCriteria dataType) {    this.m_dataType = dataType;    }

	@Override
	public boolean patch(String path, Object value) {
		if(!super.patch(path, value)) {
			if(DATA.equals(path)) {
				this.m_data = HAPUtilityData.buildDataWrapperFromObject(value);
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_data = HAPUtilityData.buildDataWrapperFromObject(jsonObj.opt(DATA));
		this.m_dataType = HAPCriteriaUtility.parseCriteria(jsonObj.getString(DATATYPE));
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_data!=null)	jsonMap.put(DATA, this.m_data.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_dataType!=null)	jsonMap.put(DATATYPE, this.m_dataType.toStringValue(HAPSerializationFormat.LITERATE));
	}

}
