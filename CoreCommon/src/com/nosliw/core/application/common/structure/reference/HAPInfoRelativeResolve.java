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
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

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
	public void setResolvedStructureId(String structureId) {    this.m_structureId = structureId;      }
	
	public HAPComplexPath getResolvedElementPath() {    return this.m_path;    }
	public void setResolvedElementPath(HAPComplexPath path) {    this.m_path = path;    }
	
	public HAPPath getUnresolvedElementPath() {    return this.m_remainPath;    }
	public void settUnresolvedElementPath(HAPPath path) {    this.m_remainPath = path;    }
	
	public HAPElementStructure getSolidElement() {    return this.m_solidElement;    }
	public void setSolidElement(HAPElementStructure element) {     this.m_solidElement = element;        }
	
	public static HAPInfoRelativeResolve parse(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPInfoRelativeResolve out = new HAPInfoRelativeResolve();
		out.setResolvedStructureId((String)jsonObj.opt(STRUCTUREID));
		out.setResolvedElementPath(HAPComplexPath.newInstance(jsonObj.opt(PATH)));
		out.settUnresolvedElementPath(new HAPPath(jsonObj.optString(REMAINPATH)));
		out.setSolidElement(HAPUtilityParserElement.parseStructureElement(jsonObj.optJSONObject(SOLIDELEMENT), dataRuleMan));
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(STRUCTUREID, this.m_structureId);
		jsonMap.put(PATH, this.m_path.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_remainPath!=null) {
			jsonMap.put(REMAINPATH, this.m_remainPath.toString());
		}
		jsonMap.put(SOLIDELEMENT, this.m_solidElement.toStringValue(HAPSerializationFormat.JSON));
	}
}
