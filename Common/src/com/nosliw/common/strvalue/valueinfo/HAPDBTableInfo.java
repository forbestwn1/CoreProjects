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
import com.nosliw.common.path.HAPComplexName;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
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
	
	/**
	 * 
	 * @param columnInfo column definition
	 * @param entityProperty  
	 */
	public void addColumnInfo(HAPDBColumnInfo columnInfo, String propertyPath){
		//get path and property from property path
		HAPComplexName complexName = new HAPComplexName(propertyPath);
		String property = complexName.getSimpleName();
		String path = complexName.getPath();
		
		//if no column name specified, use property name
		if(HAPBasicUtility.isStringEmpty(columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.COLUMN))){
			columnInfo.updateAtomicChild(HAPDBColumnInfo.COLUMN, property);
		}
	
		String getter = columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.GETTER);
		this.buildGetSetMethod(getter, columnInfo, HAPDBColumnInfo.GETTER, path, property);
		
		String setter = columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.SETTER);
		this.buildGetSetMethod(setter, columnInfo, HAPDBColumnInfo.SETTER, path, property);

		String type = columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.DATATYPE);
		if(HAPBasicUtility.isStringEmpty(type)){
			//if data type is not specify, try to get from return type of getter method
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
	
	private void buildGetSetMethod(String gs, HAPDBColumnInfo columnInfo, String type, String path, String property){
		String uperProperty = property.substring(0, 1).toUpperCase() + property.substring(1);

		if(HAPDBColumnInfo.SETTER.equals(type) && "no".equals(gs)){
			columnInfo.updateAtomicChild(HAPDBColumnInfo.SETTER, null);
		}
		else{
			String op = null;
			switch(gs){
			case HAPDBColumnInfo.GETTER:
				op = "get";
				break;
			case HAPDBColumnInfo.SETTER:
				op = "set";
				break;
			}
			
			String opPath = path;
			String opProperty = null;
			if(HAPBasicUtility.isStringEmpty(gs)){
				opPath = path;
				opProperty =  op + uperProperty;
			}
			else{
				opPath = path;
				opProperty = gs;
			}
			HAPNamingConversionUtility.cascadePath(opPath, opProperty);
		}
	}
}
