package com.nosliw.entity.path;

import java.util.Map;

import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.utils.HAPAttributeConstant;

public class HAPEntityPathSegment extends HAPEntityPath{

	//
	private String m_expectPath;

	public HAPEntityPathSegment(HAPEntityID ID, String path, String expectPath) {
		super(ID, path);
		this.m_expectPath = expectPath;
	}

	public String getExpectPath(){return this.m_expectPath;}
	public void setExpectPath(String path){this.m_expectPath=path;}
	
	public HAPEntityPathSegment clone(){
		return new HAPEntityPathSegment(this.getEntityID(), this.getPath(), this.getExpectPath());
	}
	
	protected void setJsonMap(Map<String, String> jsonMap){
		super.setJsonMap(jsonMap);
		jsonMap.put(HAPAttributeConstant.ATTR_ENTITYPATH_EXPECTPATH, this.m_expectPath);
	}
}
