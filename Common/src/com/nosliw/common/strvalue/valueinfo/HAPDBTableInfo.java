package com.nosliw.common.strvalue.valueinfo;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPConstantManager;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.literate.HAPLiterateManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPDBTableInfo {

	private String m_tableName;
	
	private Class m_entityClass;
	
	private List<HAPDBColumnInfo> m_columns;
	
	public HAPDBTableInfo(String table, String className){
		this.m_tableName = table;
		this.m_columns = new ArrayList<HAPDBColumnInfo>();
		try {
			this.m_entityClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String getTableName(){ return this.m_tableName; }
	public List<HAPDBColumnInfo> getColumnsInfo(){  return this.m_columns;  }
	
	public void addColumnInfo(HAPDBColumnInfo columnInfo, String entityProperty){
		String column = columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.COLUMN);
		if(HAPBasicUtility.isStringEmpty(column)){
			column = entityProperty;
			columnInfo.updateAtomicChild(HAPDBColumnInfo.COLUMN, column);
		}
		
		String getter = columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.GETTER);
		if(HAPBasicUtility.isStringEmpty(getter)){
			getter = "get"+entityProperty.substring(0, 1).toUpperCase() + entityProperty.substring(1);
			columnInfo.updateAtomicChild(HAPDBColumnInfo.GETTER, getter);
		}
		
		String setter = columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.SETTER);
		if(HAPBasicUtility.isStringEmpty(setter)){
			setter = "set"+entityProperty.substring(0, 1).toUpperCase() + entityProperty.substring(1);
			columnInfo.updateAtomicChild(HAPDBColumnInfo.SETTER, setter);
		}
		
		String type = columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.DATATYPE);
		if(HAPBasicUtility.isStringEmpty(type)){
			try {
				Method getMethod = this.m_entityClass.getMethod(getter);
				Class returnType = getMethod.getReturnType();
				type = HAPLiterateManager.getInstance().getSubLiterateTypeByClass(returnType);
				columnInfo.updateAtomicChild(HAPDBColumnInfo.DATATYPE, type);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
	}
	
}
