package com.nosliw.uiresource;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.expression.HAPExpression;
import com.nosliw.expression.HAPExpressionInfo;

/*
 * This is a utility class that process ui resource file and create ui resource object
 * the id index start with 1 every processing start so that for same ui resource, we would get same result
 */
public class HAPUIResourceProcessor {
	//for creating ui id
	private int m_idIndex;
	//configuration object
	private HAPConfigure m_setting;
	//current ui resource 
	private HAPUIResource m_resource = null;
	
	private HAPDataTypeManager m_dataTypeMan;
	private HAPUIResourceManager m_uiResourceMan;
	
	public HAPUIResourceProcessor(HAPConfigure setting, HAPDataTypeManager dataTypeMan, HAPUIResourceManager uiResourceMan){
		this.m_uiResourceMan = uiResourceMan;
		this.m_dataTypeMan = dataTypeMan;
		this.m_idIndex = 1;
		this.m_setting = setting;
	}
	
	public HAPUIResource getUIResource(){return this.m_resource;}
	
	public HAPUIResource readUIResource(String fileName) throws Exception{
		
		File input = new File(fileName);
		//use file name as ui resource name
		String resourceName = HAPFileUtility.getFileName(input);
		
		m_resource = new HAPUIResource(resourceName);

		Document doc = Jsoup.parse(input, "UTF-8");
		Element bodyEle = doc.body();

		//process script block
		this.processScriptBlocks(bodyEle, m_resource);
		//process constant block
		this.processConstantBlocks(bodyEle, m_resource);
		
		//process body tag's attribute
		processAttribute(bodyEle, m_resource);

		//convert all standard child tags that have data key attribute to default custom tag
		adjustTagAccordToDataBinding(bodyEle);
		
		//process expressions within text content
		processExpressionContent(bodyEle, m_resource);
		
		//process all descendant tags under body element
		precessDescendantTags(bodyEle, m_resource);
		
		//add span structure around all pain text
		HAPUIResourceUtility.addSpanToText(bodyEle);

		m_resource.postRead();
		
		//set html content after processing
		m_resource.setContent(bodyEle.html());

		//get all decedant tags
		Set<HAPUITag> tags = new HashSet<HAPUITag>();
		HAPUIResourceUtility.getAllChildTags(m_resource, tags);
		for(HAPUITag tag : tags)  this.m_resource.addUITagLib(tag.getTagName());
		
		//create script file for this ui resource
		this.processScript(m_resource, tags);
		
		return m_resource;
	}

	/*
	 * create script file for this ui resource
	 */
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
		HAPFileUtility.writeFile(HAPUIResourceUtility.getUIResourceScriptFileName(resource.getId(), this.getUIResourceManager()), scriptContent.toString());
	}
	
	/*
	 * create script for resource or tag
	 */
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
				expressionScript.append(HAPUIResourceUtility.createExpressionScript(exp));
			}
		}
		for(HAPUIExpressionAttribute attr : resource.getExpressionAttributes()){
			Set<HAPUIResourceExpression> exps = attr.getUIExpressions();
			for(HAPUIResourceExpression exp : exps){
				expressionScript.append(HAPUIResourceUtility.createExpressionScript(exp));
			}
		}
		for(HAPUIExpressionAttribute attr : resource.getExpressionTagAttributes()){
			Set<HAPUIResourceExpression> exps = attr.getUIExpressions();
			for(HAPUIResourceExpression exp : exps){
				expressionScript.append(HAPUIResourceUtility.createExpressionScript(exp));
			}
		}
		parms.put("uiexpression", expressionScript.toString());
		
		String scriptContent = HAPStringTemplateUtil.getStringValue(templateStr, parms);
		return scriptContent;
	}
	
	/*
	 * create object name for script
	 */
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
	
	/*
	 * convert all standard descendant tags that have data key attribute to default custom tag 
	 */
	private void adjustTagAccordToDataBinding(Element ele){
		Elements eles = ele.children();
		for(Element e : eles){
			//for each child element
			String tag = e.tagName();
			if(HAPUIResourceUtility.isCustomTag(e)==null){
				//for none custom tag
				Attributes attrs = e.attributes();
				boolean dataKeyAttr = false;
				for(Attribute attr : attrs){
					//check if there is any data key attribute, if so, then this tag should be costom tag
					//here we cannot just check key attribute as some regular tag may have key attribute for instance "event"
					dataKeyAttr = HAPUIResourceUtility.isDataKeyAttribute(attr.getKey());
					if(dataKeyAttr)	break;
				}
				if(dataKeyAttr){
					//if have data key attribute, then change the standard tag name to default custome tag name
					tag = HAPUIResourceUtility.makeCustomTagName(tag);
					e.tagName(tag);
				}
			}
			//do the same on child element
			adjustTagAccordToDataBinding(e);
		}
	}
	
	/*
	 * process expression in child text content withing element 
	 */
	private void processExpressionContent(Element ele, HAPUIResourceBasic resource){
		List<TextNode> textNodes = ele.textNodes();
		for(TextNode textNode : textNodes){
			String text = textNode.text();
			int start = text.indexOf(HAPConstant.UIRESOURCE_UIEXPRESSION_TOKEN_OPEN);
			while(start != -1){
				int expEnd = text.indexOf(HAPConstant.UIRESOURCE_UIEXPRESSION_TOKEN_CLOSE, start);
				int end = expEnd + HAPConstant.UIRESOURCE_UIEXPRESSION_TOKEN_CLOSE.length();
				int expStart = start + HAPConstant.UIRESOURCE_UIEXPRESSION_TOKEN_OPEN.length();
				String expression = text.substring(start, end);
				String textId = this.createId();
				text=text.substring(0, start) + "<span "+HAPConstant.UIRESOURCE_ATTRIBUTE_UIID+"="+textId+"></span>" + text.substring(end);
				List<Object> expEles = this.getExpressionContentElements(expression, resource.getConstants(), getDataTypeManager());
				HAPUIExpressionContent expressionContent = new HAPUIExpressionContent(textId, expEles, this.getDataTypeManager());
				resource.addExpressionContent(expressionContent);
				start = text.indexOf(HAPConstant.UIRESOURCE_UIEXPRESSION_TOKEN_OPEN);
			}
			
			textNode.after(text);
			textNode.remove();
		}
	}
	
	/*
	 * process all the descendant tags under element
	 */
	private void precessDescendantTags(Element ele, HAPUIResourceBasic resource){
		List<Element> removes = new ArrayList<Element>();
		Elements eles = ele.children();
		for(Element e : eles){
			if(HAPBasicUtility.isStringEmpty(HAPUIResourceUtility.getUIId(e))){
				//if tag have no ui id, then create ui id for it
				createUIId(e, resource);
			}
			
			boolean ifRemove = processTag(e, resource);
			if(ifRemove)  removes.add(e);
		}
		
		for(Element remove : removes){
			remove.remove();
		}
	}
	
	/*
	 * process all script blocks
	 */
	private void processScriptBlocks(Element ele, HAPUIResourceBasic resource){
		List<Element> removes = new ArrayList<Element>();
		
		Elements childEles = ele.children();
		for(int i=0; i<childEles.size(); i++){
			Element childEle = childEles.get(i);
			String childTagName = childEle.tag().getName();
			if(HAPConstant.UIRESOURCE_TAG_SCRIPT.equals(childTagName)){
				processJSBlock(childEle, resource);
				removes.add(childEle);
			}
		}
/*		
		Elements scriptEles = ele.getElementsByTag(HAPConstant.UIRESOURCE_TAG_SCRIPT);
		for(int i=0; i<scriptEles.size(); i++){
			processJSBlock(scriptEles.get(i), resource);
			removes.add(scriptEles.get(i));
		}
*/		
		//remove script ele from doc
		for(Element remove : removes)		remove.remove();
	}

	/*
	 * process all constant blocks
	 */
	private void processConstantBlocks(Element ele, HAPUIResourceBasic resource){
		List<Element> removes = new ArrayList<Element>();
		
		Map<String, HAPExpression> expressions = new LinkedHashMap<String, HAPExpression>();
		Map<String, HAPData> datas = new LinkedHashMap<String, HAPData>();
		
		Elements constantEles = ele.getElementsByTag(HAPConstant.UIRESOURCE_TAG_CONSTANT);
		for(int i=0; i<constantEles.size(); i++){
			try {
				String content = constantEles.get(i).html();
				JSONObject defsJson = new JSONObject(content);
				Iterator<String> defNames = defsJson.keys();
				while(defNames.hasNext()){
					String defName = defNames.next();
					String constantDef = defsJson.getString(defName);

					HAPExpressionInfo expInfo = new HAPExpressionInfo(constantDef, null, null);
					HAPExpression expression = new HAPExpression(expInfo, this.getDataTypeManager());
					expressions.put(defName, expression);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			removes.add(constantEles.get(i));
		}
		//remove script ele from doc
		for(Element remove : removes)	remove.remove();

		for(String constantName : expressions.keySet()){
			HAPData data = this.processConstantExpression(constantName, expressions, datas);
			resource.addConstant(constantName, data);
		}
	}

	private HAPData processConstantExpression(String name, Map<String, HAPExpression> expressions, Map<String, HAPData> datas){
		HAPData out = datas.get(name);
		if(out!=null)  return out;
		
		HAPExpression expression = expressions.get(name);
		Set<String> varNames = expression.getAllVariableNames();
		for(String varName : varNames){
			HAPData varData = this.processConstantExpression(varName, expressions, datas);
			datas.put(varName, varData);
		}
		out = expression.execute(datas, null);
		datas.put(name, out);
		return out;
	}
	
	/*
	 * process constant definition
	 */
//	private void processConstantDefinition(Element ele, HAPUIResourceBasic resource){
//		try{
//			String content = ele.html();
//			
//			JSONObject defsJson = new JSONObject(content);
//			Iterator<String> defNames = defsJson.keys();
//			while(defNames.hasNext()){
//				String defName = defNames.next();
//				JSONObject defJson = defsJson.getJSONObject(defName);
//				HAPData data = this.getDataTypeManager().parseJson(defJson, null, null);
//				resource.addConstant(defName, data);
//			}
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
	
	/*
	 * process a tag element 
	 * return true : this element should be removed after processing
	 * 		  false : this element should not be removed after processiong
	 */
	private boolean processTag(Element ele, HAPUIResourceBasic resource){
		String eleTag = ele.tagName();
		String customTag = HAPUIResourceUtility.isCustomTag(ele);
		if(customTag!=null){
			//process custome tag
			processCustomTag(customTag, ele, resource);
		}
		else{
			//process regular tag
			processExpressionContent(ele, resource);
			//process key attribute
			processKeyAttribute(ele, resource, false);
			//process elements's attribute that have expression value 
			processExpressionAttribute(ele, resource, false);
			//process all descendant tags under this elment
			precessDescendantTags(ele, resource);
		}
		return false;
	}

	
	/*
	 * process customer tag
	 */
	private void processCustomTag(String tagName, Element ele, HAPUIResourceBasic resource){
		String uiId = HAPUIResourceUtility.getUIId(ele); 
		
		HAPUITag uiTag = new HAPUITag(tagName, uiId);

		//process script block
		this.processScriptBlocks(ele, uiTag);
		//process constant block
		this.processConstantBlocks(ele, uiTag);
		
		//process key attribute
		processKeyAttribute(ele, resource, true);

		//process data binding attribute
		processUITagDataBindingAttribute(ele, uiTag);
		
		//process elements's attribute that have expression value 
		processExpressionAttribute(ele, resource, true);
		
		//process element's normal attribute
		processAttribute(ele, uiTag);
		
		//add placeholder element to the customer tag's postion and then remove the original tag from html structure 
		ele.after("<"+HAPConstant.UIRESOURCE_TAG_PLACEHOLDER+" style=\"display:none;\" "+HAPConstant.UIRESOURCE_ATTRIBUTE_UIID+"="+ uiId +HAPConstant.UIRESOURCE_CUSTOMTAG_WRAPER_END_POSTFIX+"></"+HAPConstant.UIRESOURCE_TAG_PLACEHOLDER+">");
		ele.after("<"+HAPConstant.UIRESOURCE_TAG_PLACEHOLDER+" style=\"display:none;\" "+HAPConstant.UIRESOURCE_ATTRIBUTE_UIID+"="+ uiId +HAPConstant.UIRESOURCE_CUSTOMTAG_WRAPER_START_POSTFIX+"></"+HAPConstant.UIRESOURCE_TAG_PLACEHOLDER+">");
		ele.remove();
		
		//process contents within customer ele
		processExpressionContent(ele, uiTag);
		precessDescendantTags(ele, uiTag);
		
		HAPUIResourceUtility.addSpanToText(ele);
		
		uiTag.postRead();
		
		uiTag.setContent(ele.html());
		
		resource.addUITag(uiTag);
	}

	/*
	 * process attribute of Element for resource(UI resource or custom tag)
	 */
	private void processAttribute(Element ele, HAPUIResourceBasic resource){
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			resource.addAttribute(eleAttr.getKey(), eleAttr.getValue());
		}
	}
	
	/*
	 * process data binding attribute within customer tag
	 */
	private void processUITagDataBindingAttribute(Element ele, HAPUITag resource){
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			String eleAttrValue = eleAttr.getValue();
			String eleAttrName = eleAttr.getKey();
			String keyAttrName = HAPUIResourceUtility.isKeyAttribute(eleAttrName);
			
			if(keyAttrName!=null){
				if(keyAttrName.startsWith(HAPConstant.UIRESOURCE_ATTRIBUTE_DATABINDING)){
					//create DataSourceEle
					HAPDataBinding dataBinding = new HAPDataBinding(keyAttrName, eleAttrValue);
					resource.addDataBinding(dataBinding);
					ele.removeAttr(eleAttrName);
				}
			}
		}
	}

	/*
	 * process element's attribute that have expression value 
	 */
	private void processExpressionAttribute(Element ele, HAPUIResourceBasic resource, boolean isCustomerTag){
		String uiId = HAPUIResourceUtility.getUIId(ele); 
		
		//read attributes
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			String eleAttrKey = eleAttr.getKey();
			//replace express attribute value with; create ExpressEle object
			String expression = HAPUIResourceUtility.isExpressionAttribute(eleAttr);
			if(expression!=null){
				List<Object> expEles = getExpressionContentElements(expression, resource.getConstants(), this.getDataTypeManager());
				//handle expression attribute
				HAPUIExpressionAttribute eAttr = new HAPUIExpressionAttribute(uiId, eleAttrKey, expEles, this.getDataTypeManager());
				if(isCustomerTag)  resource.addExpressionTagAttribute(eAttr);
				else  resource.addExpressionAttribute(eAttr);
				ele.attr(eleAttrKey, "");
			}
		}
	}
	
	/*
	 * process key attribute within element 
	 * key attribute means attribute that have predefined meaning within ui resource
	 * isCustomertag : whether this element is a customer tag
	 */
	private void processKeyAttribute(Element ele, HAPUIResourceBasic resource, boolean isCustomerTag){
		String uiId = HAPUIResourceUtility.getUIId(ele); 
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			String eleAttrValue = eleAttr.getValue();
			String eleAttrName = eleAttr.getKey();
			String keyAttrName = HAPUIResourceUtility.isKeyAttribute(eleAttrName);
			
			if(keyAttrName!=null){
				if(keyAttrName.contains(HAPConstant.UIRESOURCE_ATTRIBUTE_EVENT)){
					//process event key attribute
					HAPSegmentParser events = new HAPSegmentParser(eleAttrValue, HAPConstant.SEPERATOR_ELEMENT);
					while(events.hasNext()){
						String event = events.next();
						if(isCustomerTag){
							//this attribute belong to customer tag
							HAPElementEvent tagEvent = new HAPElementEvent(uiId, event);
							resource.addTagEvent(tagEvent);
						}
						else{
							//this attribute blong to regular tag
							HAPElementEvent eleEvent = new HAPElementEvent(uiId, event);
							resource.addElementEvent(eleEvent);
						}
					}
					//remove this attribute from element
					ele.removeAttr(eleAttrName);
				}
			}
		}
	}
	
	/*
	 * create ui id attribute for element
	 */
	private String createUIId(Element ele, HAPUIResourceBasic resource){
		String id = this.createId();
		ele.attr(HAPConstant.UIRESOURCE_ATTRIBUTE_UIID, id);
		return id;
	}
	
	/*
	 * process java script tag and its content
	 */
	private void processJSBlock(Element ele, HAPUIResourceBasic resource){
		String content = ele.html();
		HAPJSBlock jsBlock = new HAPJSBlock(content);
		resource.addJSBlock(jsBlock);
	}
	
	/*
	 * parse string containing expression to create elements that may be string or uiResourceExpression
	 */
	private List<Object> getExpressionContentElements(String content, Map<String, HAPData> constants, HAPDataTypeManager dataTypeMan){
		List<Object> out = new ArrayList<Object>();
		while(HAPBasicUtility.isStringNotEmpty(content)){
			int index = content.indexOf(HAPConstant.UIRESOURCE_UIEXPRESSION_TOKEN_OPEN);
			if(index==-1){
				//no expression
				out.add(content);
				content = null;
			}
			else if(index!=0){
				//start with text
				out.add(content.substring(0, index));
				content = content.substring(index);
			}
			else{
				//start with expression
				int expEnd = content.indexOf(HAPConstant.UIRESOURCE_UIEXPRESSION_TOKEN_CLOSE);
				int expStart = index + HAPConstant.UIRESOURCE_UIEXPRESSION_TOKEN_OPEN.length();
				out.add(new HAPUIResourceExpression(content.substring(expStart, expEnd), HAPUIResourceUtility.buildExpressionFunctionName(this.createId()), constants, dataTypeMan));
				content = content.substring(expEnd + HAPConstant.UIRESOURCE_UIEXPRESSION_TOKEN_CLOSE.length());
			}
		}
				
		return out;
	}
	
	

	private String createId(){
		return String.valueOf(++this.m_idIndex);
	}
	
	protected HAPDataTypeManager getDataTypeManager(){ return this.m_dataTypeMan; }
	protected HAPUIResourceManager getUIResourceManager(){ return this.m_uiResourceMan;}
}
