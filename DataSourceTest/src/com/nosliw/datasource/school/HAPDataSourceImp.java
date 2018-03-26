package com.nosliw.datasource.school;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.datasource.HAPExecutableDataSource;

public class HAPDataSourceImp implements HAPExecutableDataSource{

	public static int INDEX_ID = 0;
	public static int INDEX_LAT = 1;
	public static int INDEX_LON = 2;
	public static int INDEX_URL = 3;
	public static int INDEX_NAME = 4;
	public static int INDEX_TYPE = 5;
	public static int INDEX_SCORE = 6;
	public static int INDEX_COLOR = 8;

	public static void main(String[] argus) throws Exception{
		HAPDataSourceImp dataSource = new HAPDataSourceImp();
		HAPData data = dataSource.getData(null);
		System.out.println(data);
	}
	
	@Override
	public HAPData getData(Map<String, HAPData> parms){

//		InputStream elementDataInputStream = new FileInputStream(new File("elementSchoolArray.js"));
		
		InputStream inputStream = HAPFileUtility.getInputStreamOnClassPath(getClass(), "elementSchoolArray_simple.js");
		String content =  HAPFileUtility.readFile(inputStream);
		
		JSONArray schoolArrayData = new JSONArray();
		try{
			JSONArray originalDataArray = new JSONArray(content);
			for(int i=0; i<originalDataArray.length(); i++){
				try{
					JSONObject outSchool = new JSONObject();
					
					JSONArray jsonSchoolData = originalDataArray.optJSONArray(i);

					String schoolType = jsonSchoolData.getString(INDEX_TYPE);
					Double schoolScore = jsonSchoolData.getDouble(INDEX_SCORE);
					if(!(parms.get("schoolType").getValue()+"").equalsIgnoreCase(schoolType))  continue;
					if(schoolScore < Double.valueOf(parms.get("score").getValue()+""))  continue;
					
					outSchool.put("type", createJSONData("test.string;1.0.0", schoolType));
					outSchool.put("score", createJSONData("test.float;1.0.0", schoolScore));
					
					
					JSONObject outGeoValue = new JSONObject();
					outGeoValue.put("latitude", jsonSchoolData.getDouble(INDEX_LAT));
					outGeoValue.put("longitude", jsonSchoolData.getDouble(INDEX_LON));
					outSchool.put("geo", createJSONData("test.geo;1.0.0", outGeoValue));
					
					
					outSchool.put("id", createJSONData("test.string;1.0.0", jsonSchoolData.getString(INDEX_ID)));
					outSchool.put("name", createJSONData("test.string;1.0.0", jsonSchoolData.getString(INDEX_NAME)));
					outSchool.put("color", createJSONData("test.string;1.0.0", jsonSchoolData.getString(INDEX_COLOR)));
					
					schoolArrayData.put(createJSONData("test.map;1.0.0", outSchool));
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			
			HAPDataWrapper out = new HAPDataWrapper(new HAPDataTypeId("test.array;1.0.0"), schoolArrayData);
			return out;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private static JSONObject createJSONData(String dataTypeId, Object value) throws JSONException{
		JSONObject out = new JSONObject();
		out.put(HAPData.VALUE, value);
		out.put(HAPData.DATATYPEID, dataTypeId);
		return out;
	}
	
}
