package com.nosliw.data.core.structure;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializableImp;

//identify relative to root node
//used for ui tag to track source variable
@HAPEntityWithAttribute
public class HAPInfoPathToSolidRoot extends HAPSerializableImp{

	@HAPAttribute
	public static final String ROOTNODEID = "rootNodeId";

	@HAPAttribute
	public static final String PATH = "path";

	private String m_rootNodeId;
	
	private HAPPath m_path;

	public HAPInfoPathToSolidRoot() {}

	public HAPInfoPathToSolidRoot(String rootNodeId, HAPPath path) {
		this.m_rootNodeId = rootNodeId;
		this.m_path = path;
	}

	public HAPPath getPath() {    return this.m_path;   }
	public String getRootNodeId() {   return this.m_rootNodeId;    }

	public HAPInfoPathToSolidRoot cloneContextNodeReference() {
		HAPInfoPathToSolidRoot out = new HAPInfoPathToSolidRoot();
		out.m_path = this.m_path;
		out.m_rootNodeId = this.m_rootNodeId;
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PATH, this.m_path);
		jsonMap.put(ROOTNODEID, this.m_rootNodeId);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_rootNodeId = (String)jsonObj.opt(ROOTNODEID);
		this.m_path = (String)jsonObj.opt(PATH);
		return true;  
	}

}
