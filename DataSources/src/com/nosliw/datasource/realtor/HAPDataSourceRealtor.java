package com.nosliw.datasource.realtor;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.datasource.HAPDataSource;
import com.nosliw.datasource.school.HAPDataSourceSchool;

public class HAPDataSourceRealtor implements HAPDataSource{

	private HAPDataTypeManager m_dataTypeMan;
	
	@Override
	public HAPData getData() {
		HAPData out = null;
		
		try{
			InputStream elementDataInputStream = HAPFileUtility.getInputStreamOnClassPath(HAPDataSourceSchool.class, "element.json");
			
			String content = HAPFileUtility.readFile(elementDataInputStream);

			HAPDataTypeInfo listDataTypeInfo = new HAPDataTypeInfo("simple", "list");
			HAPDataType listDataType = this.m_dataTypeMan.getDataType(listDataTypeInfo);
			out = (HAPData)listDataType.newData(null).getData();

			JSONArray jsonHomesData = new JSONArray(content);
			for(int i=0; i<jsonHomesData.length(); i++){
				JSONObject jsonHomeData = jsonHomesData.optJSONObject(i);
				
				JSONObject jsonProperty = jsonHomeData.optJSONObject("Property");
				JSONObject jsonAddress = jsonProperty.optJSONObject("Address");
				
				HAPData[] parms1 = {
						HAPDataTypeManager.DOUBLE.createDataByValue(jsonAddress.optDouble("Latitude")),
						HAPDataTypeManager.DOUBLE.createDataByValue(jsonAddress.optDouble("Longitude")),
				};
				HAPData geoLocationData = this.m_dataTypeMan.newData(new HAPDataTypeInfo("simple", "geoLocation"), parms1);

				
				HAPDataTypeInfo entityDataTypeInfo = new HAPDataTypeInfo("simple", "entity");
				HAPDataType entityDataType = this.m_dataTypeMan.getDataType(entityDataTypeInfo);
				HAPData entityData = (HAPData)entityDataType.newData(null).getData();
				
				HAPData[] parms2 = {
						entityData,
						HAPDataTypeManager.STRING.createDataByValue("geoLocation"),
						geoLocationData
				};
				entityData = (HAPData)entityDataType.operate("setAttribute", parms2).getData();
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

}
