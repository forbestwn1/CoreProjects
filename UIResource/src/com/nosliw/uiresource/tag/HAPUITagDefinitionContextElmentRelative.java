package com.nosliw.uiresource.tag;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.uiresource.context.HAPContextNode;
import com.nosliw.uiresource.context.HAPContextNodeRootRelative;
import com.nosliw.uiresource.context.HAPContextPath;

/**
 * Define context element under uiTag 
 */
public class HAPUITagDefinitionContextElmentRelative extends HAPContextNodeRootRelative implements HAPUITagDefinitionContextElment{

	public HAPUITagDefinitionContextElmentRelative(String path){
		this.setPath(path);
	}
	
//	@HAPAttribute
//	public static final String PATH = "path";
//
//	
//	//context element can be a child of parent context
//	//it can be string constant
//	//it can also be script expression with attribute value as parms
//	String m_path;
//
//	public HAPUITagDefinitionContextElmentRelative(String path){
//		this.m_path = path;
//	}
//	
//	public String getPath(){
//		return this.m_path;
//	}
//	
//	@Override
//	public String getType() {		return HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE;	}
//
//	@Override
//	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
//		super.buildJsonMap(jsonMap, typeJsonMap);
//		jsonMap.put(HAPUITagDefinitionContextElment.TYPE, this.getType());
//		jsonMap.put(PATH, this.m_path);
//	}

}
