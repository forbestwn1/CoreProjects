package com.nosliw.entity.query;

import java.util.Map;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;

public class HAPQueryComponentFactory {

	static long seed = System.currentTimeMillis();
	
	static public HAPQueryComponent createQueryComponent(HAPQueryDefinition queryDef, Map<String, HAPData> queryParms, HAPQueryManager queryManager, HAPDataTypeManager dataTypeMan){
		HAPQueryComponent out = null;
		
		if(queryDef.isMultipleEntityQuery()){
			out = new HAPQueryComponentComplex(createId(), queryDef, queryParms, queryManager, dataTypeMan);
		}
		else{
			out = new HAPQueryComponentSimple(createId(), queryDef, queryParms, queryManager, dataTypeMan);
		}
		return out;
	}
	
	static String createId(){
		return seed++ + "";
	}
}
