package com.nosliw.common.strvalue.valueinfo;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.literate.HAPLiterateManager;
import com.nosliw.common.literate.HAPLiterateType;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.common.strvalue.HAPStringableValueUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;

public class HAPDBAccess {

	private HAPValueInfoManager m_valueInfoMan = null;
	private long m_id;
	
	public HAPDBAccess(HAPValueInfoManager valueInfoMan){
		this.m_valueInfoMan = valueInfoMan;
		this.m_id = System.currentTimeMillis();
	}
	
	protected HAPStringableValueEntityWithID saveEntity(HAPStringableValueEntityWithID entity, Connection connection){
		entity.setId(this.getId()+"");
		this.saveToDB(entity, connection);
		return entity;
	}

	protected long getId(){
		this.m_id++;
		return this.m_id;
	}
	
	protected List queryEntitysFromDB(String valueInfoName, String query, Object[] parms, Connection connection){
		List<Object> out = new ArrayList<Object>();
		
		try {
			HAPDBTableInfo dbTableInfo = this.m_valueInfoMan.getDBTableInfo(valueInfoName);

			String sql = this.buildEntityQuerySql(dbTableInfo.getTableName(), query);
			PreparedStatement statement = connection.prepareStatement(sql);
			if(parms!=null){
				for(int i=0; i<parms.length; i++){
					statement.setObject(i+1, parms[i]);
				}
			}
			out = this.queryFromDB(valueInfoName, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}
	
	protected Object queryEntityFromDB(String valueInfoName, String query, Object[] parms, Connection connection){
		Object out = null;
		List entitys = this.queryEntitysFromDB(valueInfoName, query, parms, connection);
		if(entitys.size()>0)  out = entitys.get(0);
		return out;
	}

	protected void createDBTable(String dataTypeName, Connection connection) {
		HAPDBTableInfo tableInfo = HAPValueInfoManager.getInstance().getDBTableInfo(dataTypeName);
		HAPDBUtility.createDBTable(tableInfo, connection);
	}

	
	protected String dropoffTableSql(HAPDBTableInfo tableInfo){
		return "DROP TABLE IF EXISTS " + tableInfo.getTableName() + ";";
	}
	
	protected String buildEntityQuerySql(String tableName, String query){
		String out = "SELECT * FROM " + tableName;
		if(HAPBasicUtility.isStringNotEmpty(query)){
			out = "SELECT * FROM " + tableName + " WHERE " + query;
		}
		return out;
	}
	
	protected void createDBTable(HAPDBTableInfo tableInfo, Connection connection){
		String dropoffSql = this.dropoffTableSql(tableInfo);
		String createSql = this.createTableSql(tableInfo);
		
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(dropoffSql);
			stmt.executeUpdate(createSql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void saveToDB(HAPStringableValueEntity obj, Connection connection){
		HAPValueInfoEntity valueInfoEntity = this.m_valueInfoMan.getEntityValueInfoByClass(obj.getClass());
		saveToDB(obj, valueInfoEntity, connection);
	}

	protected void saveToDB(HAPStringableValueEntity obj, HAPValueInfoEntity valueInfoEntity, Connection connection){
		HAPDBTableInfo dbTableInfo = this.m_valueInfoMan.getDBTableInfo(valueInfoEntity.getName());
		saveToDB(obj, dbTableInfo, connection);
	}

	protected void saveToDB(HAPStringableValueEntity obj, HAPDBTableInfo dbTableInfo, Connection connection){
		try {
			String insertSql = buildInstertSql(dbTableInfo);
			PreparedStatement statement = connection.prepareStatement(insertSql);
			
			saveToDB(obj, dbTableInfo, statement);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Build create table sql 
	 * @param tableInfo
	 * @return
	 */
	protected String createTableSql(HAPDBTableInfo tableInfo){
		StringBuffer sqlColumns = new StringBuffer();
		List<HAPDBColumnInfo> columns = tableInfo.getColumnsInfo(); 
		for(int i=0; i<columns.size(); i++){
			HAPDBColumnInfo columnInfo = columns.get(i);
			String columnSql = createColumnSql(columnInfo);
			if(i!=0){
				sqlColumns.append(",\n");
			}
			sqlColumns.append(columnSql);
		}

		InputStream createTableTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPDBTableInfo.class, "CreateTable.temp");
		Map<String, String> createTableTemplateMap = new LinkedHashMap<String, String>();
		createTableTemplateMap.put("tableName", tableInfo.getTableName());
		createTableTemplateMap.put("columns", sqlColumns.toString());
		String out = HAPStringTemplateUtil.getStringValue(createTableTemplateStream, createTableTemplateMap);
		return out;
	}
	
	private static String createColumnSql(HAPDBColumnInfo columnInfo){
		String columnName = columnInfo.getColumnName();
		String columnDef = columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.DEFINITION);
		return columnName + " " + columnDef + (columnInfo.isPrimaryKey()?" PRIMARY KEY " : "");
	}

	/**
	 * Build insert sql according to db table info
	 * @param dbTableInfo
	 * @return
	 */
	protected String buildInstertSql(HAPDBTableInfo dbTableInfo){
		StringBuffer insertSql = new StringBuffer().append("INSERT INTO "+ dbTableInfo.getTableName() +" ( ");

		StringBuffer questions = new StringBuffer();
		List<HAPDBColumnInfo> columnInfos = dbTableInfo.getColumnsInfo();
		for(int i=0; i<columnInfos.size(); i++){
			HAPDBColumnInfo columnInfo = columnInfos.get(i);
			if(i>0)    {
				insertSql.append(", ");
				questions.append(",");
			}
			insertSql.append(columnInfo.getColumnName());
			questions.append("?");
		}
		insertSql.append(") VALUES (" + questions.toString() + ")");
		return insertSql.toString();
	}
	
	protected void saveToDB(HAPStringableValueEntity obj, HAPDBTableInfo dbTableInfo, PreparedStatement statement){
		try {
			List<HAPDBColumnInfo> columnInfos = dbTableInfo.getColumnsInfo();
			for(int i=0; i<columnInfos.size(); i++){
				HAPDBColumnInfo columnInfo = columnInfos.get(i);
				String getterMethod = columnInfo.getGetter();
				String getterPath = columnInfo.getGetterPath();
				HAPStringableValue columnStrableValue = obj.getAncestorByPath(getterPath);
				Object columnValue = null;
				if(columnStrableValue!=null){
					Object columnObj = HAPValueInfoUtility.getObjectFromStringableValue(columnStrableValue);
					columnValue = columnObj.getClass().getMethod(getterMethod).invoke(columnObj);
				}
				String dataType = columnInfo.getDataType();
				if(HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_STRING.equals(dataType)){
					statement.setString(i+1, (String)columnValue);
				}
				else if(HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_INTEGER.equals(dataType)){
					if(statement==null || columnValue==null){
						statement.setObject(i+1, null);
					}
					else{
						statement.setInt(i+1, (Integer)columnValue);
					}
				}
				else if(HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_BOOLEAN.equals(dataType)){
					statement.setString(i+1, columnValue+"");
				}
				else if(HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_FLOAT.equals(dataType)){
					statement.setFloat(i+1, (Float)columnValue);
				}
				else if(HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_OBJECT.equals(dataType)){
					statement.setString(i+1, HAPSerializeManager.getInstance().toStringValue(columnValue, HAPSerializationFormat.LITERATE));
				}
				else if(HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_ARRAY.equals(dataType)){
					statement.setString(i+1, HAPSerializeManager.getInstance().toStringValue(columnValue, HAPSerializationFormat.LITERATE));
				}
			}
			statement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	protected String buildQuerySql(String dataTypeName, String query){
		HAPDBTableInfo dbTableInfo = this.m_valueInfoMan.getDBTableInfo(dataTypeName);
		String tableName = dbTableInfo.getTableName();
		StringBuffer out = new StringBuffer();
		out.append("SELECT * FROM ").append(tableName).append(" WHERE ").append(query);
		return out.toString();
	}
	
	protected List<Object> queryFromDB(String dataTypeName, PreparedStatement statement){
		List<Object> out = new ArrayList<Object>();
		
		try {
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				HAPValueInfoEntity valueInfo = (HAPValueInfoEntity)this.m_valueInfoMan.getValueInfo(dataTypeName).getSolidValueInfo();
				HAPStringableValueEntity entity = (HAPStringableValueEntity)valueInfo.newValue(); 
				
				HAPDBTableInfo dbTableInfo = this.m_valueInfoMan.getDBTableInfo(dataTypeName);
				List<HAPDBColumnInfo> columns = dbTableInfo.getColumnsInfo();
				for(HAPDBColumnInfo column : columns){
					
					String setterMethodName = column.getSetter();
					if(HAPBasicUtility.isStringNotEmpty(setterMethodName)){
						String setterPath = column.getSetterPath();
						HAPStringableValue columnStrableValue = HAPStringableValueUtility.buildAncestorByPath(entity, setterPath, valueInfo);
						Object obj = HAPValueInfoUtility.getObjectFromStringableValue(columnStrableValue);
						
						HAPLiterateType literateType = new HAPLiterateType(column.getDataType(), column.getSubDataType());
						Class parmClass = HAPLiterateManager.getInstance().getClassByLiterateType(literateType);
						
						Method setterMethod = null;
						Object columnObject = null;
						try{
							Object columnValue = resultSet.getObject(column.getColumnName());
							if(columnValue!=null){
								if(columnValue instanceof String){
									columnObject = HAPLiterateManager.getInstance().stringToValue((String)columnValue, literateType);
									if(columnObject==null){
										//if literateType is not a valid one, then use the type info from property
										HAPValueInfo propertyValueInfo = valueInfo.getChildByPath(column.getProperty()).getSolidValueInfo();
										String propertyValueInfoType = propertyValueInfo.getValueInfoType();
										if(HAPConstantShared.STRINGABLE_VALUESTRUCTURE_ENTITY.equals(propertyValueInfoType)){
											literateType = new HAPLiterateType(HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_OBJECT, ((HAPValueInfoEntity)propertyValueInfo).getClassName());
										}
										else if(HAPConstantShared.STRINGABLE_VALUESTRUCTURE_ATOMIC.equals(propertyValueInfoType)){
											literateType = ((HAPValueInfoAtomic)propertyValueInfo).getLiterateType();
										}
									}
									
									columnObject = HAPLiterateManager.getInstance().stringToValue((String)columnValue, literateType);
								}
								setterMethod = obj.getClass().getMethod(setterMethodName, parmClass);
								setterMethod.invoke(obj, columnObject);
							}
						}
						catch(NoSuchMethodException e){
							if(obj instanceof HAPStringableValueEntity){
								HAPValueInfo propertyValueInfo = valueInfo.getChildByPath(column.getProperty()).getSolidValueInfo();
								String propertyValueInfoType = propertyValueInfo.getValueInfoType();
								if(HAPConstantShared.STRINGABLE_VALUESTRUCTURE_ENTITY.equals(propertyValueInfoType)){
									((HAPStringableValueEntity)obj).updateChild(column.getColumnName(), (HAPStringableValue)columnObject);
								}
								else if(HAPConstantShared.STRINGABLE_VALUESTRUCTURE_ATOMIC.equals(propertyValueInfoType)){
									((HAPStringableValueEntity)obj).updateAtomicChildStrValue(column.getColumnName(), HAPLiterateManager.getInstance().valueToString(columnObject), literateType.getType(), literateType.getSubType());
								}
							}
						}
					}
				}
				out.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

}
