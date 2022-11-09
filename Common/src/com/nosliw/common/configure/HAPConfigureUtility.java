package com.nosliw.common.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.nosliw.common.interpolate.HAPInterpolateProcessor;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public class HAPConfigureUtility {

	private static String KEY_VARIABLE_GLOBAL = "global";

	//the interpolate processor
	private static HAPInterpolateProcessor m_interpolateProcessor = new HAPInterpolateProcessor(HAPConstantShared.SEPERATOR_VARSTART, HAPConstantShared.SEPERATOR_VAREND){
		@Override
		public String processIterpolate(String expression, Object object) {
			Object[] arrayObj = (Object[]) object;
			boolean resursive = (Boolean)arrayObj[1];
			List<HAPConfigureImp> configures = new ArrayList<HAPConfigureImp>();
			if(arrayObj[0] instanceof HAPConfigureImp)  configures.add((HAPConfigureImp)arrayObj[0]);
			else if(arrayObj[0] instanceof List)   configures = (List<HAPConfigureImp>)arrayObj[0];
			
			String out = null;
			for(HAPConfigureImp configure : configures){
				//through variable
				HAPVariableValue value = configure.getVariableValue(expression);
				if(value!=null){
					//found variable
					if(value.isResolved())  return value.getValue();
					else{
						if(resursive){
							resolveConfigureItem(value, resursive);
							if(value.isResolved())  return value.getValue();
						}
					}
				}
			}
			return out;
		}
	};

	public static String isGlobalVariable(String name){
		if(!name.startsWith(KEY_VARIABLE_GLOBAL)){
			return null;
		}
		else{
			return name.substring(KEY_VARIABLE_GLOBAL.length()+1);
		}
	}
	
	/*
	 * resolve resolvable item (configure value or variable value)
	 */
	public static HAPInterpolateOutput resolveConfigureItem(HAPResolvableConfigureItem resolvableItem, final boolean resursive){
		Map<HAPInterpolateProcessor, Object> interpolateDatas = new LinkedHashMap<HAPInterpolateProcessor, Object>();
		
		List<HAPConfigureImp> configures = new ArrayList<HAPConfigureImp>();
		configures.add(resolvableItem.getParent());
		HAPConfigureImp baseConfigure = resolvableItem.getRootParent().getBaseConfigure();
		if(baseConfigure!=null)  configures.add(baseConfigure);
		interpolateDatas.put(m_interpolateProcessor, new Object[]{configures, new Boolean(resursive)});
		
		HAPInterpolateOutput out = resolvableItem.resolve(interpolateDatas);
		return out;
	}
	
	public static HAPConfigureImp importFromProperty(HAPConfigureImp configure, String file, Class<?> class1){  return importFromProperty(configure, file, class1, null);}
	public static HAPConfigureImp importFromProperty(HAPConfigureImp configure, String file, Class<?> class1, HAPImportConfigure importConfigure){
		HAPConfigureImp out = configure;
		if(!HAPUtilityBasic.isStringEmpty(file)){
			InputStream input = HAPUtilityFile.getInputStreamOnClassPath(class1, file);
			out = importFromProperty(configure, input, importConfigure);
		}
		return out;
	}
	
	/*
	 * read configure items from property as file
	 */
	public HAPConfigureImp importFromProperty(HAPConfigureImp configure, File file){ return this.importFromProperty(configure, file, null);}
	public HAPConfigureImp importFromProperty(HAPConfigureImp configure, File file, HAPImportConfigure importConfigure){
		HAPConfigureImp out = configure;
		try {
			FileInputStream input = new FileInputStream(file);
			out = importFromProperty(configure, input, importConfigure);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}
		
	/*
	 * read configure items from property as inputstream
	 */
	public static HAPConfigureImp importFromProperty(InputStream input){  return importFromProperty(input); }
	public static HAPConfigureImp importFromProperty(HAPConfigureImp configure, InputStream input, HAPImportConfigure importConfigure){
		HAPConfigureImp out = configure;
		try {
			Properties prop = new HAPOrderedProperties();
			prop.load(input);
			out = importFromProperty(configure, prop, importConfigure);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	public static HAPConfigureImp importFromProperty(HAPConfigureImp configure, Properties prop){	return importFromProperty(configure, prop, null);	}
	public static HAPConfigureImp importFromProperty(HAPConfigureImp configure, Properties prop, HAPImportConfigure importConfigure){
		Map<String, String> valueMap = new LinkedHashMap<String, String>();
		Enumeration<?> e = prop.propertyNames();
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			String value = prop.getProperty(name).trim();
			valueMap.put(name, value);
		}
		return importFromValueMap(configure, valueMap, importConfigure);
	}
	
	public static HAPConfigureImp importFromValueMap(HAPConfigureImp configure, Map<String, String> valueMap, HAPImportConfigure importConfigure){
		if(importConfigure==null)   importConfigure = new HAPImportConfigure();

		HAPConfigureImp out = configure;
		if(out==null){
			if(importConfigure.getUseBaseConfigureWhenNotSpecified()){
				out = HAPConfigureManager.getInstance().createConfigure();
			}
			else{
				out = HAPConfigureManager.getInstance().newConfigure();
			}
		}

		if(valueMap!=null){
			for(String name : valueMap.keySet()){
				String path = HAPUtilityNamingConversion.cascadePath(importConfigure.getBasePath(), name);
				String value = valueMap.get(name).trim(); 
				if(importConfigure.isHard()){
					out.addConfigureItem(path, value);
				}
				else{
					HAPConfigureValue configureValue = out.getConfigureValue(path);
					if(configureValue==null)  out.addConfigureItem(path, value);
				}
			}
		}
		return out;
	}

	public static HAPConfigureImp merge(HAPConfigureImp configuration1, HAPConfigureImp configuration2){
		return merge(configuration1, configuration2, new HAPImportConfigure());
	}
		
	public static HAPConfigureImp merge(HAPConfigureImp configuration1, HAPConfigureImp configuration2, HAPImportConfigure importConfigure){
		
		HAPConfigureImp out = configuration1;
		if(importConfigure.isClone())   out = (HAPConfigureImp)configuration1.clone();
		
		if(configuration2==null)  return out;
		//merge child configurs
		for(String attr : configuration2.getChildConfigurables().keySet()){
			HAPConfigureImp configure = out.getChildConfigure(attr);
			HAPConfigureImp mergeConfigure = configuration2.getChildConfigure(attr);
			if(configure!=null)			merge(configure, mergeConfigure, importConfigure);
			else{
				out.addChildConfigure(attr, (HAPConfigureImp)mergeConfigure.clone());
			}
		}

		//merge child configure values
		for(String attr : configuration2.getChildConfigureValues().keySet()){
			HAPConfigureValue configureValue = out.getChildConfigureValue(attr);
			HAPConfigureValueString mergeConfigureValue = configuration2.getChildConfigureValue(attr);
			if(importConfigure.isHard() || configureValue==null)  out.addChildConfigureValue(attr, mergeConfigureValue.clone());
		}
		
		//merge variable values
		for(String name : configuration2.getChildVariables().keySet()){
			HAPVariableValue var = out.getVariableValue(name);
			HAPVariableValue mergeVar = configuration2.getVariableValue(name);
			if(importConfigure.isHard() || var==null)  out.addVariableValue(name, mergeVar);
		}
		return out;
	}
	
	/**
	 * Build a configure with first parm has low priority and last parm has high priority
	 * @param propertyFile
	 * @param cs
	 * @param useBase
	 * @param configure
	 * @return
	 */
	public static HAPConfigureImp buildConfigure(String propertyFile, Class<?> cs, boolean useBase, HAPConfigureImp customerConfigure){
		HAPConfigureImp out = null;
		if(useBase){
			out = HAPConfigureManager.getInstance().createConfigure();
		}
		else{
			out = HAPConfigureManager.getInstance().newConfigure();
		}
		
		out = HAPConfigureUtility.importFromProperty(out, propertyFile, cs, new HAPImportConfigure().setIsHard(false));
		if(customerConfigure!=null) 	HAPConfigureUtility.merge(out, customerConfigure);
		return out;
	}
}
