package com.nosliw.data.core.domain.valueport;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;

//criteria for reference
@HAPEntityWithAttribute
public class HAPReferenceElementInValueStructure extends HAPReferenceRootElement{

	//path to element (no root name)
	@HAPAttribute
	public static final String LEAFPATH = "leafPath";

	//root + leaf path
	@HAPAttribute
	public static final String ELEMENTPATH = "elementPath";

	private String m_elementPath;

	public HAPReferenceElementInValueStructure() {}

	public HAPReferenceElementInValueStructure(String eleFullPath) {
		this.m_elementPath = eleFullPath;
	}

	public HAPReferenceElementInValueStructure(HAPIdValuePort valuePortId, String eleFullPath) {
		this(eleFullPath);
		this.setValuePortRef(valuePortId);
	}

	@Override
	public String getRootName() {		return new HAPComplexPath(this.m_elementPath).getRoot();	}
	
	public String getElementPath() {   return this.m_elementPath;  }
	public void setElementPath(String path) {     this.m_elementPath = path;    }

	public String getLeafPath() {   return new HAPComplexPath(this.m_elementPath).getPathStr();   }
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.setElementPath((String)value);
		}
		else if(value instanceof JSONObject){
			super.buildObject(value, format);
			JSONObject jsonValue = (JSONObject)value;
			this.setElementPath(jsonValue.getString(ELEMENTPATH));
		}
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(LEAFPATH, this.getLeafPath());
		jsonMap.put(ELEMENTPATH, this.getElementPath());
	}
	
	public HAPReferenceElementInValueStructure cloneElementReference() {
		HAPReferenceElementInValueStructure out = new HAPReferenceElementInValueStructure();
		this.cloneToRootReference(out);
		out.m_elementPath = this.m_elementPath;
		return out;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPReferenceElementInValueStructure) {
			HAPReferenceElementInValueStructure ele = (HAPReferenceElementInValueStructure)obj;
			if(!HAPUtilityBasic.isEquals(this.getLeafPath(), ele.getLeafPath()))  return false;
			out = true;
		}
		return out;
	}	
}
