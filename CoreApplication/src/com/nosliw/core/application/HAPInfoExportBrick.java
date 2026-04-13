package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.path.HAPPath;

@HAPEntityWithAttribute
public class HAPInfoExportBrick extends HAPEntityInfoImp{

	@HAPAttribute
	public final static String PATHFROMROOT = "pathFromRoot"; 

	private HAPPath m_pathFromRoot;
	
	public HAPInfoExportBrick(HAPPath pathFromRoot) {
		this.m_pathFromRoot = pathFromRoot;
	}
	
	public HAPPath getPathFromRoot() {     return this.m_pathFromRoot;      }
	public void setPathFromRoot(String path) {    this.m_pathFromRoot = new HAPPath(path);       }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PATHFROMROOT, this.m_pathFromRoot.getPath());
	}
	
}
