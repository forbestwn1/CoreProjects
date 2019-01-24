package com.nosliw.uiresource.page.tag;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;

@HAPEntityWithAttribute
public class HAPUITagDefinition extends HAPSerializableImp{

	@HAPAttribute
	public static final String NAME = "name";
	@HAPAttribute
	public static final String SCRIPT = "script";
	@HAPAttribute
	public static final String ATTRIBUTES = "attributes";
	@HAPAttribute
	public static final String CONTEXT = "context";
	@HAPAttribute
	public static final String REQUIRES = "requires";
	@HAPAttribute
	public static final String EVENT = "event";
	
	
	//tag id
	private HAPUITagId m_name;
	
	//javascript
	private HAPScript m_script;

	//attribute definition
	private Map<String, HAPUITagDefinitionAttribute> m_attributes;
	
	//context definition
	private HAPUITagDefinitionContext m_context;

	//dependency resources
	private List<HAPResourceDependent> m_resourceDependency;

	private List<HAPDefinitionUIEvent> m_eventsDefinition;
	
	//file name for tag definition, it is mainly used for uploading resource file
	private File m_sourceFile;
	
	public HAPUITagDefinition(HAPUITagId name, String script){
		this.m_name = name;
		this.m_script = new HAPScript(script);
		this.m_attributes = new LinkedHashMap<String, HAPUITagDefinitionAttribute>();
		this.m_context = new HAPUITagDefinitionContext();
		this.m_resourceDependency = new ArrayList<HAPResourceDependent>();
		this.m_eventsDefinition = new ArrayList<HAPDefinitionUIEvent>();
	}
	
	public HAPUITagId getName(){return this.m_name;}
	
	public HAPScript getScript(){return this.m_script;}
	
	public HAPUITagDefinitionContext getContext(){  return this.m_context;   }

	public List<HAPResourceDependent> getResourceDependency(){   return this.m_resourceDependency;    }
	public void addResourceDependency(HAPResourceDependent dep){  this.m_resourceDependency.add(dep);  }
	
	public File getSourceFile(){  return this.m_sourceFile;   }
	public void setSourceFile(File file){   this.m_sourceFile = file;   }
	
	public List<HAPDefinitionUIEvent> getEventDefinition(){  return this.m_eventsDefinition;   }
	public void addEventDefinition(HAPDefinitionUIEvent eventDef) {   this.m_eventsDefinition.add(eventDef);    }
	
	public void addAttributeDefinition(HAPUITagDefinitionAttribute attrDef) {   this.m_attributes.put(attrDef.getName(), attrDef);   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, this.m_name.toStringValue(HAPSerializationFormat.LITERATE));
		jsonMap.put(CONTEXT, this.m_context.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ATTRIBUTES, HAPJsonUtility.buildJson(this.m_attributes, HAPSerializationFormat.JSON));
		jsonMap.put(EVENT, HAPJsonUtility.buildJson(this.m_eventsDefinition, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SCRIPT, this.m_script.getScript());
		typeJsonMap.put(SCRIPT, m_script.getClass());
	}
}
