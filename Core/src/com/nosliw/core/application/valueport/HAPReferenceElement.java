package com.nosliw.core.application.valueport;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;

//criteria for reference
public class HAPReferenceElement extends HAPReferenceRootElement{

	//path to element (no root name)
	public static final String LEAFPATH = "leafPath";

	//root + leaf path
	public static final String ELEMENTPATH = "elementPath";

	private String m_elementPath;

	public HAPReferenceElement() {}

	public HAPReferenceElement(String eleFullPath) {
		this.m_elementPath = eleFullPath;
	}

	public HAPReferenceElement(HAPIdValuePortInBundle valuePortRef, String eleFullPath) {
		this(eleFullPath);
		this.setValuePortId(valuePortRef);
	}
	
	public HAPReferenceElement(HAPReferenceRootElement rootRef) {
		rootRef.cloneToRootReference(this);
		this.m_elementPath = rootRef.getRootName();
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
	
	public HAPReferenceElement cloneElementReference() {
		HAPReferenceElement out = new HAPReferenceElement();
		this.cloneToRootReference(out);
		out.m_elementPath = this.m_elementPath;
		return out;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}

		boolean out = false;
		if(obj instanceof HAPReferenceElement) {
			HAPReferenceElement ele = (HAPReferenceElement)obj;
			if(!HAPUtilityBasic.isEquals(this.getLeafPath(), ele.getLeafPath())) {
				return false;
			}
			out = true;
		}
		return out;
	}	
}
