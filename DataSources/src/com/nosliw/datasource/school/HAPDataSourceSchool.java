package com.nosliw.datasource.school;

import java.io.InputStream;

import org.json.JSONArray;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.datasource.HAPDataSource;

public class HAPDataSourceSchool implements HAPDataSource{

	public static int INDEX_LAT = 1;
	public static int INDEX_LON = 2;
	public static int INDEX_URL = 3;
	public static int INDEX_NAME = 4;
	public static int INDEX_TYPE = 5;
	public static int INDEX_SCORE = 6;
	public static int INDEX_COLOR = 8;

	private HAPDataTypeManager m_dataTypeMan;
	
	public HAPDataSourceSchool(HAPDataTypeManager dataTypeMan){
		this.m_dataTypeMan = dataTypeMan;
	}
	
	@Override
	public HAPData getData(){

		HAPData out = null;
		
		try{
			InputStream elementDataInputStream = HAPFileUtility.getInputStreamOnClassPath(HAPDataSourceSchool.class, "element.json");
			
			String content = HAPFileUtility.readFile(elementDataInputStream);

			HAPDataTypeInfo listDataTypeInfo = new HAPDataTypeInfo("simple", "list");
			HAPDataType listDataType = this.m_dataTypeMan.getDataType(listDataTypeInfo);
			out = (HAPData)listDataType.newData(null, null).getData();

			JSONArray jsonSchoolsData = new JSONArray(content);
			for(int i=0; i<jsonSchoolsData.length(); i++){
				JSONArray jsonSchoolData = jsonSchoolsData.optJSONArray(i);
				
				HAPData[] parms1 = {
						HAPDataTypeManager.DOUBLE.createDataByValue(jsonSchoolData.getDouble(INDEX_LAT)),
						HAPDataTypeManager.DOUBLE.createDataByValue(jsonSchoolData.getDouble(INDEX_LON)),
				};
				HAPData geoLocationData = this.m_dataTypeMan.newData(new HAPDataTypeInfo("simple", "geoLocation"), parms1, null);

				
				HAPData typeData = HAPDataTypeManager.STRING.createDataByValue(jsonSchoolData.getString(INDEX_TYPE));
				
				HAPData nameData = HAPDataTypeManager.STRING.createDataByValue(jsonSchoolData.getString(INDEX_NAME));

				HAPData scoreData = HAPDataTypeManager.DOUBLE.createDataByValue(jsonSchoolData.getDouble(INDEX_SCORE));
				
				
				HAPDataTypeInfo entityDataTypeInfo = new HAPDataTypeInfo("simple", "entity");
				HAPDataType entityDataType = this.m_dataTypeMan.getDataType(entityDataTypeInfo);
				HAPData entityData = (HAPData)entityDataType.newData(null, null).getData();
				
				HAPData[] parms2 = {
						entityData,
						HAPDataTypeManager.STRING.createDataByValue("geoLocation"),
						geoLocationData
				};
				entityData = (HAPData)entityDataType.operate("setAttribute", parms2, null).getData();

				HAPData[] parms3 = {
						entityData,
						HAPDataTypeManager.STRING.createDataByValue("name"),
						nameData
				};
				entityData = (HAPData)entityDataType.operate("setAttribute", parms3, null).getData();
				
				HAPData[] parms4 = {
						entityData,
						HAPDataTypeManager.STRING.createDataByValue("type"),
						typeData
				};
				entityData = (HAPData)entityDataType.operate("setAttribute", parms4, null).getData();
				
				HAPData[] parms5 = {
						entityData,
						HAPDataTypeManager.STRING.createDataByValue("score"),
						scoreData
				};
				entityData = (HAPData)entityDataType.operate("setAttribute", parms5, null).getData();
				
				
				HAPData[] parms6 = {out, entityData	};
				out = (HAPData)listDataType.operate("add", parms6, null).getData();
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return out;
	}

}
