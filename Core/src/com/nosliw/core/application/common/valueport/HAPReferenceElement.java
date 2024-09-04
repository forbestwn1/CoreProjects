package com.nosliw.core.application.common.valueport;

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

	public static final String IODIRECTION = "ioDirection";

	private String m_elementPath;

	private String m_ioDirection;
	
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
	
	public String getIODirection() {    return this.m_ioDirection;      }
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.setElementPath((String)value);
		}
		else if(value instanceof JSONObject){
			super.buildObject(value, format);
			JSONObject jsonValue = (JSONObject)value;
			this.setElementPath(jsonValue.getString(ELEMENTPATH));
			this.m_ioDirection = (String)jsonValue.opt(IODIRECTION);
		}
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(LEAFPATH, this.getLeafPath());
		jsonMap.put(ELEMENTPATH, this.getElementPath());
		jsonMap.put(IODIRECTION, this.m_ioDirection);
	}
	
	public HAPReferenceElement cloneElementReference() {
		HAPReferenceElement out = new HAPReferenceElement();
		this.cloneToRootReference(out);
		out.m_elementPath = this.m_elementPath;
		out.m_ioDirection = this.m_ioDirection;
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
			if(!HAPUtilityBasic.isEquals(this.m_ioDirection, ele.m_ioDirection)) {
				return false;
			}
			out = true;
		}
		return out;
	}	
}
