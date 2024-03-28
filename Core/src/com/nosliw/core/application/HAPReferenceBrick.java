package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPReferenceBrick extends HAPSerializableImp{

	@HAPAttribute
	public static final String IDPATH = "idPath";
	
	@HAPAttribute
	public static final String RELATIVEPATH = "relativePath";
	
	private String m_idPath;
	
	//for runtime purpose, as absolute path may lead to multiple brick
	private String m_relativePath;
	
	public HAPReferenceBrick(String idPath) {
		this.m_idPath = idPath;
	}
	
	public String getIdPath() {    return this.m_idPath;    }
	public void setIdPath(String path) {    this.m_idPath = path;     }

	public String getRelativePath() {   return this.m_relativePath;     }
	public void setRelativePath(String path) {    this.m_relativePath = path;     }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(IDPATH, m_idPath);
		jsonMap.put(RELATIVEPATH, m_relativePath);
	}
}
