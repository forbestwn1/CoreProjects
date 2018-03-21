package com.nosliw.data.core.datasource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.clss.HAPClassFilter;
import com.nosliw.data.core.HAPDataTypeProvider;

public class HAPDataSourceDefinitionManager {

	private Map<String, HAPDefinition> m_definitions;
	
	public HAPDataSourceDefinitionManager(){
		this.m_definitions = new LinkedHashMap<String, HAPDefinition>();
	}
	
//	private void init() {
//		new HAPClassFilter(){
//			@Override
//			protected void process(Class cls, Object data) {
//				loadDataType(cls);
//			}
//
//			@Override
//			protected boolean isValid(Class cls) {
//				Class[] interfaces = cls.getInterfaces();
//				for(Class inf : interfaces){
//					if(inf.equals(HAPDataTypeProvider.class)){
//						return true;
//					}
//				}
//				return false;
//			}
//		}.process(null);
//
//	}
	
	public void registerDataSourceDefinition(String name, HAPDefinition dataSourceDefinition){
		this.m_definitions.put(name, dataSourceDefinition);
	}
	
	public HAPDefinition getDataSourceDefinition(String name){
		return this.m_definitions.get(name);
	}
	
}
