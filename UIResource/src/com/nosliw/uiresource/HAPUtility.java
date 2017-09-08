package com.nosliw.uiresource;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPUtility {

/*	
	//script file name to store all the script infor for uiresource
	public static String getUIResourceScriptFileName(String resourceName, HAPUIResourceManager uiResourceMan){
		return HAPFileUtility.buildFullFileName(uiResourceMan.getTempFileLocation(), resourceName, "js");
	}
	
	//create script file for this ui resource
	private void processScript(HAPUIResource resource, Set<HAPUITag> tags){
		//find all resource + child tags
		Set<HAPUIResourceBasic> resources = new HashSet<HAPUIResourceBasic>();
		resources.add(resource);
		resources.addAll(tags);
		
		//create script content for resource + child tag 
		StringBuffer scriptContent = new StringBuffer();
		String templateStr = HAPStringTemplateUtil.getTemplateFromFile(HAPFileUtility.getInputStreamOnClassPath(this.getClass(), "UIResourceScript.txt"));
		for(HAPUIResourceBasic res : resources){
			scriptContent.append(this.createResourceScript(res, templateStr));
		}
		
		//save the content to file, file location is from configuration
		HAPFileUtility.writeFile(HAPUIResourceParserUtility.getUIResourceScriptFileName(resource.getId(), this.getUIResourceManager()), scriptContent.toString());
	}
	
	//create script for resource or tag
	private String createResourceScript(HAPUIResourceBasic resource, String templateStr){
		Map<String, String> parms = new LinkedHashMap<String, String>();

		//process script name
		String scriptObjName = this.createScriptObjectName(resource);
		resource.setScriptFactoryName(scriptObjName);
		parms.put("scriptObjectName", scriptObjName);
		
		//process script block
		List<HAPJSBlock> jsBlocks = resource.getJSBlocks();
		StringBuffer blocksScript = new StringBuffer();
		for(int i=0; i<jsBlocks.size(); i++){
			HAPJSBlock jsBlock = jsBlocks.get(i);
			String block = jsBlock.getBlock();
			int start = block.indexOf(HAPConstant.UIRESOURCE_SCRIPTBLOCK_TOKEN_OPEN);
			int end = block.lastIndexOf(HAPConstant.UIRESOURCE_SCRIPTBLOCK_TOKEN_CLOSE);
			String blockScript = block.substring(start+HAPConstant.UIRESOURCE_SCRIPTBLOCK_TOKEN_OPEN.length(), end).trim();
			blocksScript.append(blockScript);
			if(!blockScript.endsWith(",") && i<jsBlocks.size()-1){
				blocksScript.append(",");
			}
			blocksScript.append("\n");
		}
		parms.put("script", blocksScript.toString());
		
		//process expression
		StringBuffer expressionScript = new StringBuffer();
		for(HAPUIExpressionContent content : resource.getExpressionContents()){
			Set<HAPUIResourceExpression> exps = content.getUIExpressions();
			for(HAPUIResourceExpression exp : exps){
				expressionScript.append(HAPUIResourceParserUtility.createExpressionScript(exp));
			}
		}
		for(HAPUIExpressionAttribute attr : resource.getExpressionAttributes()){
			Set<HAPUIResourceExpression> exps = attr.getUIExpressions();
			for(HAPUIResourceExpression exp : exps){
				expressionScript.append(HAPUIResourceParserUtility.createExpressionScript(exp));
			}
		}
		for(HAPUIExpressionAttribute attr : resource.getExpressionTagAttributes()){
			Set<HAPUIResourceExpression> exps = attr.getUIExpressions();
			for(HAPUIResourceExpression exp : exps){
				expressionScript.append(HAPUIResourceParserUtility.createExpressionScript(exp));
			}
		}
		parms.put("uiexpression", expressionScript.toString());
		
		String scriptContent = HAPStringTemplateUtil.getStringValue(templateStr, parms);
		return scriptContent;
	}
	
	//create object name for script
	private String createScriptObjectName(HAPUIResourceBasic resource){
		String out = null;
		switch(resource.getType())
		{
		case HAPConstant.UIRESOURCE_TYPE_RESOURCE:{
			out = resource.getId();
			break;
		}
		case HAPConstant.UIRESOURCE_TYPE_TAG:{
			out = this.m_resource.getId() + HAPConstant.SEPERATOR_PREFIX + resource.getId();
			break;
		}
		}
		return out;
	}

	//create script for expression
	public static String createExpressionScript(HAPUIResourceExpression expression){
		String name = expression.getExpressionId();
		String script = expression.getFunctionScript();
		return name + ":" + script + ",";
	}

*/	
}
