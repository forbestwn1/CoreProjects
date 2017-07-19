package com.nosliw.app.instance;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.app.utils.HAPApplicationUtility;
import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.query.HAPQueryDefinition;
import com.nosliw.entity.query.HAPQueryDefinitionManager;

public class HAPApplicationQueryDefinitionManager extends HAPQueryDefinitionManager{

	private HAPDataTypeManager m_dataTypeMan; 
	private HAPEntityDefinitionManager m_entityDefMan;
	
	public HAPApplicationQueryDefinitionManager(HAPConfigure configure, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan) {
		super(configure);
		
		List<String> fileNames = HAPApplicationUtility.getFileNames(configure);
		for(String fileName : fileNames){
			String fileContent = HAPFileUtility.readFile(fileName).trim();
			try {
				if(fileContent.startsWith("{")){
					//json object, a single query definition
					JSONObject queryDefJson = new JSONObject(fileContent);
					HAPQueryDefinition queryDef = HAPQueryDefinition.parse(queryDefJson, this.getDataTypeMan(), this.getEntityDefinitionManager());
					this.addQueryDefinition(queryDef);
				}
				else if(fileContent.startsWith("[")){
					//array, an array of query definition
					JSONArray queryDefArray = new JSONArray(fileContent);
					for(int i=0; i<queryDefArray.length(); i++){
						HAPQueryDefinition queryDef = HAPQueryDefinition.parse(queryDefArray.getJSONObject(i), this.getDataTypeMan(), this.getEntityDefinitionManager());
						this.addQueryDefinition(queryDef);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected HAPDataTypeManager getDataTypeMan(){return this.m_dataTypeMan;}
	protected HAPEntityDefinitionManager getEntityDefinitionManager(){return this.m_entityDefMan;}

}
