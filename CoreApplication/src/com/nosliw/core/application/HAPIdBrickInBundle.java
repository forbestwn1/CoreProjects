package com.nosliw.core.application;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public class HAPIdBrickInBundle extends HAPSerializableImp{

	@HAPAttribute
	public static final String IDPATH = "idPath";
	
	@HAPAttribute
	public static final String RELATIVEPATH = "relativePath";
	
	@HAPAttribute
	public static final String ALIAS = "alias";
	
	private String m_idPath;
	
	//for runtime purpose, as absolute path may lead to multiple brick
	private String m_relativePath;

	private String m_alias;
	
	public HAPIdBrickInBundle() {}

	public HAPIdBrickInBundle(String idPath) {
		this.m_idPath = idPath;
	}
	
	public String getIdPath() {    return this.m_idPath;    }
	public void setIdPath(String path) {    this.m_idPath = path;     }

	public String getRelativePath() {   return this.m_relativePath;     }
	public void setRelativePath(String path) {    this.m_relativePath = path;     }

	public String getAlias() {    return this.m_alias;     }
	public void setAlias(String alias) {      this.m_alias = alias;      }
	
	@Override
	public HAPIdBrickInBundle cloneValue() {
		HAPIdBrickInBundle out = new HAPIdBrickInBundle(this.m_idPath);
		out.setRelativePath(this.getRelativePath());
		out.setAlias(this.getAlias());
		return out;
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){	
		if(literateValue.startsWith(HAPConstantShared.SYMBOL_ALIAS)) {
			//if alias
			this.m_alias = literateValue.substring(HAPConstantShared.SYMBOL_ALIAS.length());
		}
		else if(literateValue.startsWith(HAPConstantShared.SEPERATOR_PATH)) {
			//if relative path
			this.m_relativePath = literateValue.substring(HAPConstantShared.SEPERATOR_PATH.length());
		}
		else {
			this.m_idPath = literateValue;
		}
		return true;  
	}

	@Override
	protected boolean buildObjectByJson(Object obj){
		if(obj instanceof String) {
			buildObjectByLiterate((String)obj);
		}
		else if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;
			this.m_idPath = (String)jsonObj.opt(IDPATH);
			this.m_relativePath = (String)jsonObj.opt(RELATIVEPATH);
			this.m_alias = jsonObj.optString(ALIAS);
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(IDPATH, m_idPath);
		jsonMap.put(RELATIVEPATH, m_relativePath);
		jsonMap.put(ALIAS, this.m_alias);
	}
}
