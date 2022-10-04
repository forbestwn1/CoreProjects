package com.nosliw.uiresource.page.tag;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionWrapperValueStructure;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;

@HAPEntityWithAttribute
public abstract class HAPUITagDefinition extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String TYPE = "type";
	@HAPAttribute
	public static final String BASE = "base";
	@HAPAttribute
	public static final String SCRIPT = "script";
	@HAPAttribute
	public static final String ATTRIBUTES = "attributes";
	@HAPAttribute
	public static final String VALUESTRUCTURE = "valueStructure";
	@HAPAttribute
	public static final String VALUESTRUCTUREEXE = "valueStructureExe";
	@HAPAttribute
	public static final String REQUIRES = "requires";
	@HAPAttribute
	public static final String EVENT = "event";
	
	private String m_type;

	private String m_base;
	
	//javascript
	private HAPJsonTypeScript m_script;

	//attribute definition
	private Map<String, HAPUITagDefinitionAttribute> m_attributes;
	
	//context definition
	private HAPDefinitionWrapperValueStructure m_valueStructureDefinitionWrapper;

	//dependency resources
	private List<HAPResourceDependency> m_resourceDependency;

	private List<HAPDefinitionUIEvent> m_eventsDefinition;
	
	//file name for tag definition, it is mainly used for uploading resource file
	private File m_sourceFile;
	
	public HAPUITagDefinition(String type){
		this.m_type = type;
		this.m_attributes = new LinkedHashMap<String, HAPUITagDefinitionAttribute>();
		this.m_resourceDependency = new ArrayList<HAPResourceDependency>();
		this.m_eventsDefinition = new ArrayList<HAPDefinitionUIEvent>();
	}
	
	public String getType() {    return this.m_type;    }
	
	public String getBase() {    return this.m_base;     }
	public void setBase(String base) {     this.m_base = base;      }
	
	public HAPJsonTypeScript getScript(){return this.m_script;}
	public void setScript(String script) {  this.m_script = new HAPJsonTypeScript(script);    }
	
	public HAPValueStructureDefinitionGroup getValueStructureDefinition(){  return HAPUtilityValueStructure.getGroupFromWrapper(this.m_valueStructureDefinitionWrapper);  }
	public HAPDefinitionWrapperValueStructure getValueStructureDefinitionWrapper(){  return this.m_valueStructureDefinitionWrapper;  }
	public void setValueStructureDefinitionWrapper(HAPDefinitionWrapperValueStructure valueStructureWrapper){  this.m_valueStructureDefinitionWrapper = valueStructureWrapper;  }

	public List<HAPResourceDependency> getResourceDependency(){   return this.m_resourceDependency;    }
	public void addResourceDependency(HAPResourceDependency dep){  this.m_resourceDependency.add(dep);  }
	
	public File getSourceFile(){  return this.m_sourceFile;   }
	public void setSourceFile(File file){   this.m_sourceFile = file;   }
	
	public List<HAPDefinitionUIEvent> getEventDefinition(){  return this.m_eventsDefinition;   }
	public void addEventDefinition(HAPDefinitionUIEvent eventDef) {   this.m_eventsDefinition.add(eventDef);    }
	
	public void addAttributeDefinition(HAPUITagDefinitionAttribute attrDef) {   this.m_attributes.put(attrDef.getName(), attrDef);   }
	public Set<HAPUITagDefinitionAttribute> getAllAttributeDef(){    return new HashSet<HAPUITagDefinitionAttribute>(this.m_attributes.values());  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.m_type);
		jsonMap.put(BASE, this.m_base);
		HAPValueStructureDefinitionGroup valueStructure = HAPUtilityValueStructure.getGroupFromWrapper(m_valueStructureDefinitionWrapper);
		jsonMap.put(VALUESTRUCTURE, HAPJsonUtility.buildJson(valueStructure, HAPSerializationFormat.JSON));
		jsonMap.put(VALUESTRUCTUREEXE, HAPJsonUtility.buildJson(HAPUtilityValueStructure.buildExecuatableValueStructure(valueStructure), HAPSerializationFormat.JSON));
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
