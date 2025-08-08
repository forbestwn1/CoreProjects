package com.nosliw.core.application.common.structure.reference;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPUtilityParserElement;

@HAPEntityWithAttribute
public class HAPInfoRelativeResolve extends HAPSerializableImp{

	@HAPAttribute
	public static final String STRUCTUREID = "structureId";

	@HAPAttribute
	public static final String PATH = "path";
	
	@HAPAttribute
	public static final String REMAINPATH = "remainPath";
	
	@HAPAttribute
	public static final String SOLIDELEMENT = "solidElement";

	//resolved structure runtime id
	private String m_structureId;
	//path after resolve (root id + path)
	private HAPComplexPath m_path;
	//unsolved path part
	private HAPPath m_remainPath;
	//final element, solid (maybe logic element which embeded in real element)
	private HAPElementStructure m_solidElement;

	public HAPInfoRelativeResolve() {}
	
	public HAPInfoRelativeResolve(String structureId, HAPComplexPath path, HAPPath remainPath, HAPElementStructure element) {
		this.m_structureId = structureId;
		this.m_path = path;
		this.m_remainPath = remainPath;
		this.m_solidElement = element;
	}

	public String getResolvedStructureId() {   return this.m_structureId;    }
	
	public HAPComplexPath getResolvedElementPath() {    return this.m_path;    }
	
	public HAPPath getUnresolvedElementPath() {    return this.m_remainPath;    }
	
	public HAPElementStructure getSolidElement() {    return this.m_solidElement;    }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_structureId = (String)jsonObj.opt(STRUCTUREID);
		this.m_path = HAPComplexPath.newInstance(jsonObj.opt(PATH));
		this.m_remainPath = new HAPPath(jsonObj.optString(REMAINPATH));
		this.m_solidElement = HAPUtilityParserElement.parseStructureElement(jsonObj.optJSONObject(SOLIDELEMENT));
		return false;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(STRUCTUREID, this.m_structureId);
		jsonMap.put(PATH, this.m_path.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_path!=null)  jsonMap.put(REMAINPATH, this.m_path.toString());
		jsonMap.put(SOLIDELEMENT, this.m_solidElement.toStringValue(HAPSerializationFormat.JSON));
	}
}
