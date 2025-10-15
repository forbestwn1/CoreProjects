package com.nosliw.core.application.division.story.brick.connection;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.xxx.brick.HAPStoryConnectionImp;

@HAPEntityWithAttribute
public class HAPStoryConnectionDataIO extends HAPStoryConnectionImp{
	
	public final static String CONNECTION_TYPE = HAPConstantShared.STORYCONNECTION_TYPE_DATAIO; 
	
	@HAPAttribute
	public static final String PATH1 = "path1";

	@HAPAttribute
	public static final String PATH2 = "path2";

	private String m_path1;
	
	private String m_path2;
	
	public HAPStoryConnectionDataIO() {
		super(CONNECTION_TYPE);
	}

	public String getPath1() {    return this.m_path1;    }
	public void setPath1(String path) {    this.m_path1 = path;    }
	public String getPath2() {    return this.m_path2;    }
	public void setPath2(String path) {    this.m_path2 = path;    }
	
	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryConnectionDataIO out = new HAPStoryConnectionDataIO();
		super.cloneTo(out);
		out.m_path1 = this.m_path1;
		out.m_path2 = this.m_path2;
		return out;
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
