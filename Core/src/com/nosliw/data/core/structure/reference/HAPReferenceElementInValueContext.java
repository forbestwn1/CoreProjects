package com.nosliw.data.core.structure.reference;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

//criteria for reference
@HAPEntityWithAttribute
public class HAPReferenceElementInValueContext extends HAPReferenceRootElement{

	@HAPAttribute
	public static final String LEAFPATH = "leafPath";

	@HAPAttribute
	public static final String ELEMENTPATH = "elementPath";

	//path to element (root name + path)
	private String m_leafPath;

	public HAPReferenceElementInValueContext() {}
	
	public HAPReferenceElementInValueContext(String eleFullPath) {
		HAPComplexPath complexPath = new HAPComplexPath(eleFullPath);
		this.setRootName(complexPath.getRoot());
		this.m_leafPath = complexPath.getPathStr();
	}

	public HAPReferenceElementInValueContext(String parent, String eleFullPath) {
		this(eleFullPath);
		this.setParentValueContextName(parent);
	}
	
	public String getElementPath() {   return HAPUtilityNamingConversion.buildPath(this.getRootName(), this.m_leafPath);    }
	public void setElementPath(String path) {
		HAPComplexPath complexPath = new HAPComplexPath(path);
		this.m_leafPath = complexPath.getPathStr();
		this.setRootName(complexPath.getRoot());
	}

	public String getLeafPath() {   return this.m_leafPath;   }
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.setElementPath((String)value);
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			this.setParentValueContextName((String)jsonValue.opt(PARENTVALUECONTEXT));
			this.setElementPath(jsonValue.getString(ELEMENTPATH));
		}
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(LEAFPATH, this.m_leafPath);
		jsonMap.put(ELEMENTPATH, this.getElementPath());
	}
	
	public HAPReferenceElementInValueContext cloneElementReference() {
		HAPReferenceElementInValueContext out = new HAPReferenceElementInValueContext();
		this.cloneToRootReference(out);
		out.m_leafPath = this.m_leafPath;
		return out;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPReferenceElementInValueContext) {
			HAPReferenceElementInValueContext ele = (HAPReferenceElementInValueContext)obj;
			if(!HAPUtilityBasic.isEquals(this.getLeafPath(), ele.getLeafPath()))  return false;
			out = true;
		}
		return out;
	}	
}
