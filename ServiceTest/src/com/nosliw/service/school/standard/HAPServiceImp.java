package com.nosliw.service.school.standard;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPDataWrapper;
import com.nosliw.data.core.service.definition.HAPExecutableService;
import com.nosliw.data.core.service.definition.HAPProviderService;
import com.nosliw.data.core.service.definition.HAPResultService;
import com.nosliw.data.core.service.definition.HAPUtilityService;
import com.nosliw.service.realtor.HAPDataImporter;

public class HAPServiceImp implements HAPExecutableService, HAPProviderService{

	public static int INDEX_ID = 0;
	public static int INDEX_LAT = 1;
	public static int INDEX_LON = 2;
	public static int INDEX_URL = 3;
	public static int INDEX_NAME = 4;
	public static int INDEX_TYPE = 5;
	public static int INDEX_SCORE = 6;
	public static int INDEX_COLOR = 8;

	public static void main(String[] argus) throws Exception{
		HAPServiceImp dataSource = new HAPServiceImp();
		HAPResultService result = dataSource.execute(null);
		System.out.println(result);
	}
	
	@Override
	public HAPResultService execute(Map<String, HAPData> parms){

		InputStream inputStream = HAPUtilityFile.getInputStreamOnClassPath(getClass(), "elementSchoolArray.js");
		
//		InputStream inputStream = HAPFileUtility.getInputStreamOnClassPath(getClass(), "elementSchoolArray_simple.js");
		String content =  HAPUtilityFile.readFile(inputStream);
		
		JSONArray schoolArrayData = new JSONArray();
		try{
			JSONArray originalDataArray = new JSONArray(content);
			for(int i=0; i<originalDataArray.length(); i++){
				try{
					JSONObject outSchool = new JSONObject();
					
					JSONArray jsonSchoolData = originalDataArray.optJSONArray(i);

					String schoolType = jsonSchoolData.getString(INDEX_TYPE);
					Double schoolScore = jsonSchoolData.getDouble(INDEX_SCORE);
					if(!(((JSONObject)parms.get("schoolTypeInService").getValue()).getString("value")).equalsIgnoreCase(schoolType))  continue;
					if(schoolScore < Double.valueOf(parms.get("schoolRatingInService").getValue()+""))  continue;
					
					JSONObject schoolTypeOptionJson = new JSONObject();
					schoolTypeOptionJson.put("value", schoolType);
					schoolTypeOptionJson.put("optionsId", "schoolType");
					outSchool.put("schoolType", createJSONData("test.options;1.0.0", schoolTypeOptionJson));
					outSchool.put("schoolRating", createJSONData("test.float;1.0.0", schoolScore));
					
					JSONObject outGeoValue = new JSONObject();
					double lat = jsonSchoolData.getDouble(INDEX_LAT);
					double lon = jsonSchoolData.getDouble(INDEX_LON);
					outGeoValue.put("latitude", lat);
					outGeoValue.put("longitude", lon);
					outSchool.put("geo", createJSONData("test.geo;1.0.0", outGeoValue));
					
					if(lat<HAPDataImporter.minLat||lat>HAPDataImporter.maxLat) continue;
					if(lon<HAPDataImporter.minLon||lon>HAPDataImporter.maxLon) continue;
					
					outSchool.put("id", createJSONData("test.string;1.0.0", jsonSchoolData.getString(INDEX_ID)));
					outSchool.put("schoolName", createJSONData("test.string;1.0.0", jsonSchoolData.getString(INDEX_NAME)));
					outSchool.put("color", createJSONData("test.string;1.0.0", jsonSchoolData.getString(INDEX_COLOR)));
					
					schoolArrayData.put(createJSONData("test.map;1.0.0", outSchool));
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			
			Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
			output.put("outputInService", new HAPDataWrapper(new HAPDataTypeId("test.array;1.0.0"), schoolArrayData));
			return HAPUtilityService.generateSuccessResult(output);
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
