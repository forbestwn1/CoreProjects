package com.nosliw.common.path;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

@HAPEntityWithAttribute
public class HAPComplexPath extends HAPSerializableImp{

	@HAPAttribute
	public static final String ROOT = "root";

	@HAPAttribute
	public static final String PATH = "path";

	private String m_fullName;

	private HAPPath m_path;
	
	private String m_root;
	
	public static HAPComplexPath newInstance(Object obj) {
		HAPComplexPath out = null;
		if(obj instanceof String)  out = new HAPComplexPath((String)obj);
		else if(obj instanceof JSONObject) {
			out = new HAPComplexPath();
			out.buildObject(obj, HAPSerializationFormat.JSON);
		}
		return out;
	}
	
	public HAPComplexPath() {}
	
	public HAPComplexPath(String rootName, HAPPath path){
		this.m_root = rootName;
		this.m_path = path;
		this.m_fullName = HAPUtilityNamingConversion.cascadePath(this.m_root, this.m_path.getPath());
	}
	
	public HAPComplexPath(String rootName, String path){
		this(rootName, new HAPPath(path));
	}
	
	public HAPComplexPath(String fullName){
		if(HAPBasicUtility.isStringNotEmpty(fullName)){
			this.m_fullName = fullName;
			
			int index = this.m_fullName.indexOf(HAPConstantShared.SEPERATOR_PATH);
			if(index==-1){
				//name only
				this.m_root = this.m_fullName;
				this.m_path = new HAPPath();
			}
			else{
				this.m_root = this.m_fullName.substring(0, index);
				this.m_path = new HAPPath(this.m_fullName.substring(index+1));
			}
		}
	}
	
	public String getRoot(){	return this.m_root; 	}
	
	public HAPPath getPath() {   return this.m_path;    }
	
	public String getPathStr(){
		if(this.m_path==null)   return null;
		return this.m_path.getPath();
	}

	public String[] getPathSegs(){
		if(this.m_path==null)  return new String[0];
		return this.m_path.getPathSegments();
	}
	
	public String getFullName(){
		return this.m_fullName;
	}

	public HAPComplexPath appendSegment(String segment) {
		return new HAPComplexPath(this.m_root, this.m_path.appendSegment(segment));
	}
	
	public HAPComplexPath updateRootName(String name) {   return new HAPComplexPath(name, this.m_path);   }
	
	public HAPComplexPath cloneComplexPath() {
		return new HAPComplexPath(this.m_fullName);
	}
	
	private void init(String rootName, HAPPath path){
		this.m_root = rootName;
		this.m_path = path;
		this.m_fullName = HAPUtilityNamingConversion.cascadePath(this.m_root, this.m_path==null?null:this.m_path.getPath());
	}
	
	@Override
	protected String buildLiterate(){  return this.getFullName(); }

	@Override	
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		String root = jsonObj.getString(ROOT);
		String path = (String)jsonObj.opt(PATH);
		this.init(root, path==null?null:new HAPPath(path));
		return true;
	}
		
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ROOT, this.m_root);
		if(this.m_path!=null && !this.m_path.isEmpty()) jsonMap.put(PATH, this.m_path.getPath());
	}

	@Override
	public String toString() {    return this.getFullName();    }
	
}
