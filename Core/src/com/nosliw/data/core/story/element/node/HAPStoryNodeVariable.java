package com.nosliw.data.core.story.element.node;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.change.HAPChangeResult;
import com.nosliw.data.core.story.change.HAPUtilityChange;

@HAPEntityWithAttribute
public class HAPStoryNodeVariable extends HAPStoryNodeImp{
	
	public final static String STORYNODE_TYPE = HAPConstant.STORYNODE_TYPE_VARIABLE; 
	
	@HAPAttribute
	public static final String DATATYPE = "dataType";
	
	@HAPAttribute
	public static final String VARAIBLENAME = "variableName";
	
	private HAPDataTypeCriteria m_dataType;
	
	private String m_variableName;

	public HAPStoryNodeVariable() {
		super(STORYNODE_TYPE);
	}
	
	public HAPStoryNodeVariable(String variableName, HAPDataTypeCriteria dataType) {
		this();
		this.m_variableName = variableName;
		this.m_dataType = dataType;
	}

	public HAPDataTypeCriteria getDataType() {   return this.m_dataType;   }
	public void setDataType(HAPDataTypeCriteria dataType) {    this.m_dataType = dataType;    }

	public String getVariableName() {    return this.m_variableName;    }
	public void setVariableName(String varName) {   this.m_variableName = varName;   }
	
	@Override
	public HAPChangeResult patch(String path, Object value) {
		HAPChangeResult out = super.patch(path, value);
		if(out!=null)  return out; 
		else {
			out = new HAPChangeResult();
			if(DATATYPE.equals(path)) {
				out.addRevertChange(HAPUtilityChange.buildChangePatch(this, DATATYPE, this.m_dataType));
				this.m_dataType = HAPCriteriaUtility.parseCriteria((String)value);
				return out;
			}
			else if(VARAIBLENAME.equals(path)) {
				out.addRevertChange(HAPUtilityChange.buildChangePatch(this, VARAIBLENAME, this.m_variableName));
				this.m_variableName = (String)value;
				return out;
			}
		}
		return null;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_dataType = HAPCriteriaUtility.parseCriteria(jsonObj.getString(DATATYPE));
		this.m_variableName = jsonObj.getString(VARAIBLENAME);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VARAIBLENAME, this.m_variableName);
		if(this.m_dataType!=null)	jsonMap.put(DATATYPE, this.m_dataType.toStringValue(HAPSerializationFormat.LITERATE));
	}
}
