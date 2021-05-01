package com.nosliw.data.core.structure;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPNamingConversionUtility;

//
@HAPEntityWithAttribute
public class HAPReferenceElement extends HAPSerializableImp{

	@HAPAttribute
	public static final String ROOT  = "root";

	@HAPAttribute
	public static final String PATH  = "path";

	private HAPReferenceRoot m_root;
	
	private String m_path;
	
	public HAPReferenceElement(){}
	
	public HAPReferenceElement(HAPReferenceRoot rootId, String path){
		this.m_root = rootId;
		this.m_path = path;
	}
	
	public HAPReferenceElement appendSegment(String seg){
		HAPReferenceElement out = new HAPReferenceElement();
		out.m_root = this.m_root.cloneStructureRootReference();
		out.m_path = HAPNamingConversionUtility.cascadePath(m_path, seg);
		return out;
	}
	
	public HAPReferenceRoot getRootReference() {  return this.m_root;   }
	
	public String getSubPath(){		return this.m_path==null?"":this.m_path;	}
	
	public String getPath() { return this.m_path;  }
	
	public HAPReferenceElement clonePathStructure() {
		HAPReferenceElement out = new HAPReferenceElement();
		out.m_path = this.m_path;
		out.m_root = this.m_root.cloneStructureRootReference();
		return out;
	}
	
	public String[] getPathSegments(){
		if(HAPBasicUtility.isStringEmpty(m_path))  return new String[0];
		else  return HAPNamingConversionUtility.parseComponentPaths(m_path);     
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_path = (String)jsonObj.opt(PATH);
		this.m_root = HAPUtilityStructurePath.parseRootReference(jsonObj.opt(ROOT)); 
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(PATH, this.m_path);
		jsonMap.put(ROOT, this.m_root.toStringValue(HAPSerializationFormat.JSON));
	}
}
