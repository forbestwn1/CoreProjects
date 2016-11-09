package com.nosliw.common.pattern;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.clss.HAPClassFilter;
import com.nosliw.common.configure.HAPConfigurableImp;
import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.configure.HAPConfigureUtility;
import com.nosliw.common.utils.HAPBasicUtility;

public class HAPPatternManager extends HAPConfigurableImp{

	private static HAPPatternManager m_instance;

	private Map<String, HAPPatternProcessor> m_processors;

	private Map<String, HAPPatternProcessorInfo> m_processorInfos;
	
	public static HAPPatternManager getInstance(){
		return getInstance(null);
	}

	public static HAPPatternManager getInstance(HAPConfigureImp configure){
		if(m_instance==null){
			m_instance=new HAPPatternManager(configure);
		}
		return m_instance;
	}
	
	public HAPPatternManager(HAPConfigureImp customerConfigure){
		this.setConfiguration(HAPConfigureUtility.buildConfigure("patternprocess.properties", HAPPatternManager.class, true, customerConfigure));
		this.m_processorInfos = new LinkedHashMap<String, HAPPatternProcessorInfo>();
		this.m_processors = new LinkedHashMap<String, HAPPatternProcessor>();
		this.init();
	}
	
	public Map<String, HAPPatternProcessor> getAllProcessors(){
		return this.m_processors;
	}
	
	public void registerPatternProcessor(HAPPatternProcessor processor){
		this.m_processors.put(processor.getName(), processor);
	}
	
	public void registerPatternProcessor(HAPPatternProcessorFactory factory){
		Set<HAPPatternProcessor> processors = factory.getPatternProcessors();
		for(HAPPatternProcessor processor : processors){
			this.registerPatternProcessor(processor);
		}
	}

	public String compose(String processorName, Object data, Object obj){
		HAPPatternProcessor processor = this.getPatternProcessor(processorName);
		if(processor==null)  return data.toString();
		return processor.compose(data, obj);
	}
	
	public Object process(String text, String processorName, Object obj){
		HAPPatternProcessor processor = this.getPatternProcessor(processorName);
		if(processor==null)  return null;
		return processor.parse(text, obj);
	}
	
	public HAPPatternProcessor getPatternProcessor(String name){
		return this.m_processors.get(name);
	}
	
	public HAPPatternProcessorInfo getPatterProcessorInfo(String name){
		return this.m_processorInfos.get(name);
	}
	
	public Map<String, HAPPatternProcessorInfo> getAllPatternProcesssorInfos(){
		return this.m_processorInfos;
	}
	
	private void init(){	
		try {
			String loadMode = this.getConfigureValue("loadMode").getStringValue();
			if(HAPBasicUtility.isStringEmpty(loadMode)){
				loadMode = "scan";
			}
			
			//load processor infos
			Set<HAPPatternProcessorInfo> processorInfos = this.loadPatternProcessorInfos(loadMode);
			//register processor according to processorInfos
			for(HAPPatternProcessorInfo processorInfo : processorInfos){
				HAPPatternProcessor processor = processorInfo.createPatternProcessor();
				this.registerPatternProcessor(processor);
				
				this.m_processorInfos.put(processor.getName(), processorInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * load all processor infos by mode
	 * 		scan mode: scan class path and export found processor infos to file 
	 * 		file mode: load processor infos from file, if no file found, then fallback to scan mode
	 */
	private Set<HAPPatternProcessorInfo> loadPatternProcessorInfos(String loadMode){
		Set<HAPPatternProcessorInfo> processorInfos = null;
		switch(loadMode){
		case "scan":
			processorInfos = new HashSet<HAPPatternProcessorInfo>(); 
			
			new HAPClassFilter(){
				@Override
				protected void process(Class checkClass, Object data) {
					Set<HAPPatternProcessorInfo> processorInfos = (Set<HAPPatternProcessorInfo>)data;
					HAPPatternProcessorInfo processInfo = new HAPPatternProcessorInfo();
					processInfo.setClassName(checkClass.getName());
					processorInfos.add(processInfo);
				}

				@Override
				protected boolean isValid(Class cls) {		return HAPPatternProcessor.class.isAssignableFrom(cls);	}
			}.process(processorInfos);
			try {
				HAPPatternXmlResourceUtility.exportProcessorInfos(processorInfos, new FileOutputStream(new File(this.getExportFileByConfigure())));
			} catch (FileNotFoundException e1) {				e1.printStackTrace();			}
			break;
		case "file":
			InputStream inputStream;
			try {
				inputStream = new FileInputStream(new File(this.getExportFileByConfigure()));
				processorInfos = HAPPatternXmlResourceUtility.importProcessorInfos(inputStream);
			} catch (FileNotFoundException e) {
				//if no file found, then load using scan mode
				processorInfos = this.loadPatternProcessorInfos("scan");
			}
			break;
		}
		return processorInfos;
	}

	private String getExportFileByConfigure(){
		return this.getConfigureValue("exportFile").getStringValue();
	}

}
