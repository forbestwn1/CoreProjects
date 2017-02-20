package com.nosliw.common.constant;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.clss.HAPClassFilter;
import com.nosliw.common.configure.HAPConfigurableImp;
import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.configure.HAPConfigureUtility;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.strvalue.HAPStringableValueAtomic;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;

public class HAPConstantManager  extends HAPConfigurableImp{

	private List<HAPConstantGroup> m_groups;
	
	private HAPStringableValueAtomic m_jsPath;
	private HAPStringableValueAtomic m_jsAttributeFile;
	private HAPStringableValueAtomic m_jsConstantFile;
	
	public HAPConstantManager(HAPConfigureImp customerConfigure){
		this.setConfiguration(HAPConfigureUtility.buildConfigure("", HAPConstantManager.class, true, customerConfigure));
		
		this.m_groups = new ArrayList<HAPConstantGroup>();
		
		HAPValueInfoManager.getInstance().importFromXML(HAPConstantManager.class, new String[]{
				"constant.xml","group.xml","group_attribute.xml","group_constant.xml"});
	}
	
	public void setJsPath(String path){	this.m_jsPath = new HAPStringableValueAtomic(path, HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING, null);	}
	public void setJsAttributeFile(String file){	this.m_jsAttributeFile = new HAPStringableValueAtomic(file, HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING, null);	}
	public void setJsConstantFile(String file){	this.m_jsConstantFile = new HAPStringableValueAtomic(file, HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING, null);	}
	
	public void addConstantGroup(HAPConstantGroup group){		this.m_groups.add(group);	}
	
	public void resolve(){
		if(this.m_jsPath!=null)   this.m_jsPath.resolveByConfigure(getConfiguration());
		if(this.m_jsAttributeFile!=null)   this.m_jsAttributeFile.resolveByConfigure(getConfiguration());
		if(this.m_jsConstantFile!=null)   this.m_jsConstantFile.resolveByConfigure(getConfiguration());
		
		for(HAPConstantGroup group : this.m_groups){
			group.resolveByConfigure(getConfiguration());
		}
	}
	
	public HAPConstantGroup buildConstantGroupFromClassAttr(){
		final HAPConstantGroup group = new HAPConstantGroup(HAPConstantGroup.TYPE_CLASSATTR);
		new HAPClassFilter(){
			@Override
			protected void process(Class checkClass, Object data) {
				Field[] fields = checkClass.getDeclaredFields();
				for(Field field : fields){
					String fieldName = field.getName();
					if(field.isAnnotationPresent(HAPAttribute.class)){
						try {
							String constantValue = field.get(null).toString();
							String baseName = HAPConstantUtility.getBaseName(checkClass);
							String constantName = HAPNamingConversionUtility.cascadeNameSegment(baseName, fieldName);
							HAPConstantInfo constantInfo = HAPConstantInfo.build(constantName, constantValue);
							group.addConstantInfo(constantInfo);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

			@Override
			protected boolean isValid(Class cls) {		return true;	}
		}.process(group);

		this.addConstantGroup(group);
		return group;
	}
	
	public void exportJS(){
		Map<String, String> attributeJsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> attributeTypesJsonMap = new LinkedHashMap<String, Class<?>>();
		Map<String, String> constantJsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> constantTypesJsonMap = new LinkedHashMap<String, Class<?>>();
		
		for(HAPConstantGroup group : this.m_groups){
			Iterator it = group.iterateConstant();
			while(it.hasNext()){
				HAPConstantInfo constantInfo = (HAPConstantInfo)it.next();
				if(HAPConstantGroup.TYPE_CONSTANT.equals(group.getType())){
					this.processJSItem(constantInfo, constantJsonMap, constantTypesJsonMap);
				}
				else{
					this.processJSItem(constantInfo, attributeJsonMap, attributeTypesJsonMap);
				}
			}
		}
		this.writeJS(constantJsonMap, constantTypesJsonMap, this.m_jsConstantFile.getStringValue());
		this.writeJS(attributeJsonMap, attributeTypesJsonMap, this.m_jsAttributeFile.getStringValue());
	}
	
	public void exportJava(){
		for(HAPConstantGroup group : this.m_groups){
			if(!group.getType().equals(HAPConstantGroup.TYPE_CLASSATTR)){
				this.writeJavaConstant(group);
			}
		}
	}

	/*
	 * process a list of constant and save them to java class
	 */
	private String writeJavaConstant(HAPConstantGroup group){

		Map<String, String> attrJavaTemplateParms = new LinkedHashMap<String, String>();
		
		attrJavaTemplateParms.put("packagename", group.getPackageName());
		attrJavaTemplateParms.put("classname", group.getClassName());
		
		StringBuffer attrDefs = new StringBuffer("\n");
		Iterator it = group.iterateConstant();
		while(it.hasNext()){
			HAPConstantInfo constantInfo = (HAPConstantInfo)it.next();
			String itemString = getJavaItem(constantInfo);
			attrDefs.append(itemString);
		}
		attrJavaTemplateParms.put("attrdef", attrDefs.toString());

		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPConstantManager.class, "ConstantJava.temp");
		String attrJavaContent = HAPStringTemplateUtil.getStringValue(javaTemplateStream, attrJavaTemplateParms);
		
		String outputFilePath = group.getFilePath()+"/"+group.getClassName()+".java";
		System.out.println(outputFilePath);
		HAPFileUtility.writeFile(outputFilePath, attrJavaContent);
		
		return attrJavaContent;
	}

	private void writeJS(Map<String, String> valueMap, Map<String, Class<?>> datatypeMap, String fileName){
		String jsonContent = HAPJsonUtility.buildMapJson(valueMap, datatypeMap);
		String content = "var " + fileName + "=\n" + HAPJsonUtility.formatJson(jsonContent) + ";";
		HAPFileUtility.writeFile(m_jsPath.getStringValue()+"/"+fileName+".js", content);
	}

	/*
	 * process constant definition and create jason map and type map
	 */
	private void processJSItem(HAPConstantInfo info, Map<String, String> valueMap, Map<String, Class<?>> datatypeMap){
		if("js".equals(info.getSkip()))  return;
		
		if(HAPBasicUtility.isStringEmpty(info.getType())){
			valueMap.put(info.getName(), info.getValue());
		}
		else if(info.getType().equals("string")){
			valueMap.put(info.getName(), info.getValue());
			datatypeMap.put(info.getName(), String.class);
		}
		else if(info.getType().equals("int")){
			valueMap.put(info.getName(), info.getValue());
			datatypeMap.put(info.getName(), Integer.class);
		}
		else if(info.getType().equals("space")){
		}
	}
	
	/*
	 * process individual constant def
	 */
	private String getJavaItem(HAPConstantInfo info){
		String out = "";
		if(HAPBasicUtility.isStringEmpty(info.getType()) || info.getType().equals("string")){
			out = "		public static final String " + info.getName() + " = \"" + info.getValue() + "\";\n";
		}
		else if(info.getType().equals("int")){
			out = "		public static final int " + info.getName() + " = " + info.getValue() + ";\n";
		}
		else if(info.getType().equals("space")){
			out = "\n\n\n";
		}
		return out;
	}

}
