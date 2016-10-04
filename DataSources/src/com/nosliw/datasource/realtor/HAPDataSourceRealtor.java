package com.nosliw.datasource.realtor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.datasource.HAPDataSource;
import com.nosliw.datasource.school.HAPDataSourceSchool;

public class HAPDataSourceRealtor implements HAPDataSource{

	private HAPDataTypeManager m_dataTypeMan;
	
	public HAPDataSourceRealtor(HAPDataTypeManager dataTypeMan){
		this.m_dataTypeMan = dataTypeMan;
	}
	
	@Override
	public HAPData getData() {
		HAPData out = null;
		
		try{
			InputStream elementDataInputStream = HAPFileUtility.getInputStreamOnClassPath(HAPDataSourceRealtor.class, "homes.json");
			
			String content = HAPFileUtility.readFile(elementDataInputStream);

			HAPDataTypeInfo listDataTypeInfo = new HAPDataTypeInfo("simple", "list");
			HAPDataType listDataType = this.m_dataTypeMan.getDataType(listDataTypeInfo);
			out = (HAPData)listDataType.newData(null, null).getData();

			JSONArray jsonHomesData = new JSONArray(content);
			for(int i=0; i<jsonHomesData.length(); i++){
				JSONObject jsonHomeData = jsonHomesData.optJSONObject(i);
				
				JSONObject jsonProperty = jsonHomeData.optJSONObject("Property");
				JSONObject jsonAddress = jsonProperty.optJSONObject("Address");
				
				HAPData[] parms1 = {
						HAPDataTypeManager.DOUBLE.createDataByValue(jsonAddress.optDouble("Latitude")),
						HAPDataTypeManager.DOUBLE.createDataByValue(jsonAddress.optDouble("Longitude")),
				};
				HAPData geoLocationData = this.m_dataTypeMan.newData(new HAPDataTypeInfo("simple", "geoLocation"), parms1, null);

				String jsonMlsNo = jsonHomeData.optString("MlsNumber"); 
				HAPData mlsData = HAPDataTypeManager.STRING.createDataByValue(jsonMlsNo);
				
				HAPDataTypeInfo entityDataTypeInfo = new HAPDataTypeInfo("simple", "entity");
				HAPDataType entityDataType = this.m_dataTypeMan.getDataType(entityDataTypeInfo);
				HAPData entityData = (HAPData)entityDataType.newData(null, null).getData();
				
				HAPData[] parms2 = {
						entityData,
						HAPDataTypeManager.STRING.createDataByValue("geoLocation"),
						geoLocationData
				};
				entityData = (HAPData)entityDataType.operate("setAttribute", parms2, null).getData();

				HAPData[] parms4 = {
						entityData,
						HAPDataTypeManager.STRING.createDataByValue("MlsNumber"),
						mlsData
				};
				entityData = (HAPData)entityDataType.operate("setAttribute", parms4, null).getData();
				
				List<HAPData> parms3 = new ArrayList<HAPData>();
				parms3.add(entityData);
				out.operate("add", parms3, null);
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

}
