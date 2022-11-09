package com.nosliw.data.core.structure;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

//
@HAPEntityWithAttribute
public class HAPReferenceElementInStructure extends HAPSerializableImp{

	@HAPAttribute
	public static final String ROOTREFERENCE  = "rootReference";

	@HAPAttribute
	public static final String PATH  = "path";

	private HAPReferenceRootInStrucutre m_rootReference;
	
	private String m_path;
	
	public HAPReferenceElementInStructure() {}
	
	public HAPReferenceElementInStructure(String literate){
		this.buildObjectByLiterate(literate);
	}
	
	public HAPReferenceElementInStructure(HAPReferenceRootInStrucutre rootReference, String path){
		this.m_rootReference = rootReference;
		this.m_path = path;
	}
	
	public HAPReferenceElementInStructure appendSegment(String seg){
		HAPReferenceElementInStructure out = new HAPReferenceElementInStructure();
		out.m_rootReference = this.m_rootReference.cloneStructureRootReference();
		out.m_path = HAPUtilityNamingConversion.cascadePath(m_path, seg);
		return out;
	}
	
	public HAPReferenceRootInStrucutre getRootReference() {  return this.m_rootReference;   }
	
	public void setRootReference(HAPReferenceRootInStrucutre root) {   this.m_rootReference = root;   }
	
	public String getSubPath(){		return this.m_path==null?"":this.m_path;	}
	
	public String getPath() { return this.m_path;  }
	
	public HAPReferenceElementInStructure cloneElementReference() {
		HAPReferenceElementInStructure out = new HAPReferenceElementInStructure();
		out.m_path = this.m_path;
		out.m_rootReference = this.m_rootReference.cloneStructureRootReference();
		return out;
	}
	
	public String[] getPathSegments(){
		if(HAPUtilityBasic.isStringEmpty(m_path))  return new String[0];
		else  return HAPUtilityNamingConversion.parseComponentPaths(m_path);     
	}
	
	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		int index = literateValue.indexOf(HAPConstantShared.SEPERATOR_PATH);
		String rootRefLiterate = null;
		if(index==-1){
			rootRefLiterate = literateValue;
		}
		else{
			rootRefLiterate = literateValue.substring(0, index);
			this.m_path = literateValue.substring(index+1);
		}
		this.m_rootReference = new HAPReferenceRootUnknowType(rootRefLiterate); 
		return true;  
	}

	@Override
	protected String buildLiterate(){	return HAPUtilityNamingConversion.buildPath(this.m_rootReference.toStringValue(HAPSerializationFormat.LITERATE), this.m_path);	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_path = (String)jsonObj.opt(PATH);
		this.m_rootReference = new HAPReferenceRootUnknowType(jsonObj.getString(ROOTREFERENCE)); 
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(PATH, this.m_path);
		jsonMap.put(ROOTREFERENCE, this.m_rootReference.toStringValue(HAPSerializationFormat.LITERATE));
	}
}
