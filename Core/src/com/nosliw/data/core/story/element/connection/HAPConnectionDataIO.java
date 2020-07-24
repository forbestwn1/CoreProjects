package com.nosliw.data.core.story.element.connection;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPConnectionImp;

@HAPEntityWithAttribute
public class HAPConnectionDataIO extends HAPConnectionImp{
	
	public final static String CONNECTION_TYPE = HAPConstant.STORYCONNECTION_TYPE_DATAIO; 
	
	@HAPAttribute
	public static final String PATH1 = "path1";

	@HAPAttribute
	public static final String PATH2 = "path2";

	private String m_path1;
	
	private String m_path2;
	
	public HAPConnectionDataIO() {
		super(CONNECTION_TYPE);
	}

	public String getPath1() {    return this.m_path1;    }
	public void setPath1(String path) {    this.m_path1 = path;    }
	public String getPath2() {    return this.m_path2;    }
	public void setPath2(String path) {    this.m_path2 = path;    }
	
	@Override
	public void patch(String path, Object value) {
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_path1 = (String)jsonObj.opt(PATH1);
		this.m_path2 = (String)jsonObj.opt(PATH2);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PATH1, this.m_path1);
		jsonMap.put(PATH2, this.m_path2);
	}

}
