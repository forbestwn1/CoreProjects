package com.nosliw.uiresource.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import com.nosliw.common.strvalue.io.HAPStringableEntityImporterJSON;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.data.core.criteria.HAPCriteriaParser;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.uiresource.HAPDataBinding;
import com.nosliw.uiresource.HAPIdGenerator;
import com.nosliw.uiresource.context.HAPContextNodeRootAbsolute;
import com.nosliw.uiresource.context.HAPContextParser;
import com.nosliw.uiresource.definition.HAPConstantDef;
import com.nosliw.uiresource.definition.HAPElementEvent;
import com.nosliw.uiresource.definition.HAPEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.definition.HAPEmbededScriptExpressionInContent;
import com.nosliw.uiresource.definition.HAPScript;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnit;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnitResource;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnitTag;
import com.nosliw.uiresource.expression.HAPScriptExpression;
import com.nosliw.uiresource.expression.HAPScriptExpressionUtility;

/*
 * This is a utility class that process ui resource file and create ui resource object
 * the id index start with 1 every processing start so that for same ui resource, we would get same result
 */
public class HAPUIResourceParser {

	//for creating ui id
	private HAPIdGenerator m_idGenerator;
	//configuration object
	private HAPConfigure m_setting;
	//current ui resource 
	private HAPUIDefinitionUnitResource m_resource = null;
	
	private HAPValueInfoManager m_valueInfoMan;
	private HAPCriteriaParser m_criteriaParser;
	private HAPExpressionManager m_expressionManager;
	
	public HAPUIResourceParser(HAPConfigure setting, HAPExpressionManager expressionMan, HAPIdGenerator idGenerator){
		this.m_idGenerator = idGenerator;
		this.m_setting = setting;
		this.m_expressionManager = expressionMan;
		this.m_criteriaParser = HAPCriteriaParser.getInstance();
	}
	
	public HAPUIDefinitionUnitResource getUIResource(){return this.m_resource;}

	public HAPUIDefinitionUnitResource parseContent(String resourceId, String content){
		try{
			this.m_resource = new HAPUIDefinitionUnitResource(resourceId, content);
			Document doc = Jsoup.parse(content, "UTF-8");
			parseUIResourceContent(this.m_resource, doc);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return m_resource;
	}
	
	public HAPUIDefinitionUnitResource parseFile(String fileName){
		try{
			File input = new File(fileName);
			//use file name as ui resource id
			String resourceId = HAPFileUtility.getFileName(input);
			String source = HAPFileUtility.readFile(input);
			this.m_resource = new HAPUIDefinitionUnitResource(resourceId, source);
			
			Document doc = Jsoup.parse(input, "UTF-8");
			parseUIResourceContent(this.m_resource, doc);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return m_resource;
	}

	private HAPUIDefinitionUnitResource parseUIResourceContent(HAPUIDefinitionUnitResource resource, Document doc){
		try{
			Element bodyEle = doc.body();

			//parse script block
			this.parseChildScriptBlocks(bodyEle, resource);
			//parse constant block
			this.parseChildConstantBlocks(bodyEle, resource);
			//parse context block
			this.parseChildContextBlocks(bodyEle, resource);
			//parse expression block
			this.parseChildExpressionBlocks(bodyEle, resource);
			
			//parse body tag's attribute
			parseCurrentAttribute(bodyEle, resource);

			//convert all standard child tags that have data key attribute to default custom tag
			adjustDescendantTagAccordToDataBinding(bodyEle);
			
			//parse expressions within text content
			parseChildScriptExpressionInContent(bodyEle, resource);
			
			//parse all descendant tags under body element
			parseDescendantTags(bodyEle, resource);
			
			//add span structure around all pain text
			HAPUIResourceParserUtility.addSpanToText(bodyEle);

			resource.postRead();
			
			//set html content after processing
			resource.setContent(bodyEle.html());

			//get all decedant tags, for load resource purpose
			Set<HAPUIDefinitionUnitTag> tags = new HashSet<HAPUIDefinitionUnitTag>();
			HAPUIResourceParserUtility.getAllChildTags(resource, tags);
			for(HAPUIDefinitionUnitTag tag : tags)  resource.addUITagLib(tag.getTagName());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return resource;
	}
	
	private void parseChildContextBlocks(Element ele, HAPUIDefinitionUnitResource resource){
		List<Element> removes = new ArrayList<Element>();
		Elements contextEles = ele.getElementsByTag(HAPUIDefinitionUnitResource.CONTEXT);
		for(int i=0; i<contextEles.size(); i++){
			try {
				String content = contextEles.get(i).html();
				JSONObject defsJson = new JSONObject(content);
				Iterator<String> defNames = defsJson.keys();
				while(defNames.hasNext()){
					String eleName = defNames.next();
					JSONObject eleDefJson = defsJson.optJSONObject(eleName);
					HAPContextNodeRootAbsolute contextEle = HAPContextParser.parseContextRootElementInUIResource(eleDefJson);
					resource.getContext().addElement(eleName, contextEle);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			removes.add(contextEles.get(i));
		}
		//remove script ele from doc
		for(Element remove : removes)	remove.remove();
	}
	
	private void parseChildExpressionBlocks(Element ele, HAPUIDefinitionUnit resource){
		List<Element> removes = new ArrayList<Element>();

		Elements expressionEles = ele.getElementsByTag(HAPUIDefinitionUnit.EXPRESSIONS);
		for(int i=0; i<expressionEles.size(); i++){
			try {
				String content = expressionEles.get(i).html();
				JSONObject defsJson = new JSONObject(content);
				Iterator<String> defNames = defsJson.keys();
				while(defNames.hasNext()){
					String defName = defNames.next();
					
					JSONObject expDefJson = defsJson.optJSONObject(defName);
					HAPExpressionDefinition expressionDef = (HAPExpressionDefinition)HAPStringableEntityImporterJSON.parseJsonEntity(expDefJson, "data.expressiondefinition", this.m_valueInfoMan);
					resource.addExpressionDefinition(expressionDef);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			removes.add(expressionEles.get(i));
		}
		//remove script ele from doc
		for(Element remove : removes)	remove.remove();
	}
	
	/*
	 * process all constant blocks
	 */
	private void parseChildConstantBlocks(Element ele, HAPUIDefinitionUnit resource){
		List<Element> removes = new ArrayList<Element>();
		
		Elements constantEles = ele.getElementsByTag(HAPUIDefinitionUnit.CONSTANTS);
		for(int i=0; i<constantEles.size(); i++){
			try {
				String content = constantEles.get(i).text();
				JSONObject defsJson = new JSONObject(content);
				Iterator<String> defNames = defsJson.keys();
				while(defNames.hasNext()){
					String defName = defNames.next();
					resource.addConstantDef(defName, new HAPConstantDef(defsJson.get(defName)));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			removes.add(constantEles.get(i));
		}
		//remove script ele from doc
		for(Element remove : removes)	remove.remove();
	}

	/*
	 * process all script blocks
	 */
	private void parseChildScriptBlocks(Element ele, HAPUIDefinitionUnit resource){
		List<Element> removes = new ArrayList<Element>();
		Elements childEles = ele.getElementsByTag(HAPUIDefinitionUnitResource.SCRIPT);
		for(int i=0; i<childEles.size(); i++){
			Element childEle = childEles.get(i);
			String childTagName = childEle.tag().getName();
			if(HAPUIDefinitionUnit.SCRIPT.equals(childTagName)){
				HAPScript jsBlock = new HAPScript(childEle.html());
				resource.setJSBlock(jsBlock);
				removes.add(childEle);
				break;
			}
		}
		//remove script ele from doc
		for(Element remove : removes)		remove.remove();
	}

	/*
	 * convert all standard descendant tags that have data key attribute to default custom tag 
	 */
	private void adjustDescendantTagAccordToDataBinding(Element ele){
		Elements eles = ele.children();
		for(Element e : eles){
			//for each child element
			String tag = e.tagName();
			if(HAPUIResourceParserUtility.isCustomTag(e)==null){
				//for none custom tag
				Attributes attrs = e.attributes();
				boolean dataKeyAttr = false;
				for(Attribute attr : attrs){
					//check if there is any data key attribute, if so, then this tag should be costom tag
					//here we cannot just check key attribute as some regular tag may have key attribute for instance "event"
					dataKeyAttr = HAPUIResourceParserUtility.isDataKeyAttribute(attr.getKey());
					if(dataKeyAttr)	break;
				}
				if(dataKeyAttr){
					//if have data key attribute, then change the standard tag name to default custome tag name
					tag = HAPUIResourceParserUtility.makeCustomTagName(tag);
					e.tagName(tag);
				}
			}
			//do the same on child element
			adjustDescendantTagAccordToDataBinding(e);
		}
	}
	
	/*
	 * process expression in child text content within element 
	 */
	private void parseChildScriptExpressionInContent(Element ele, HAPUIDefinitionUnit resource){
		List<TextNode> textNodes = ele.textNodes();
		for(TextNode textNode : textNodes){
			String text = textNode.text();
			List<Object> segments = HAPScriptExpressionUtility.discoverUIExpressionInText(text, this.m_expressionManager);
			StringBuffer newText = new StringBuffer();
			for(Object segment : segments){
				if(segment instanceof String){
					newText.append((String)segment);
				}
				else if(segment instanceof HAPScriptExpression){
					HAPScriptExpression scriptExpression = (HAPScriptExpression)segment;
					HAPEmbededScriptExpressionInContent expressionContent = new HAPEmbededScriptExpressionInContent(this.m_idGenerator.createId(), scriptExpression, this.m_expressionManager);
					newText.append("<span "+HAPConstant.UIRESOURCE_ATTRIBUTE_UIID+"="+expressionContent.getUIId()+"></span>");
					resource.addScriptExpressionInContent(expressionContent);
				}
			}
			
			textNode.after(newText.toString());
			textNode.remove();
		}
	}
	
	/*
	 * process all the descendant tags under element
	 */
	private void parseDescendantTags(Element ele, HAPUIDefinitionUnit resource){
		List<Element> removes = new ArrayList<Element>();
		Elements eles = ele.children();
		for(Element e : eles){
			if(HAPBasicUtility.isStringEmpty(HAPUIResourceParserUtility.getUIId(e))){
				//if tag have no ui id, then create ui id for it
				String id = this.m_idGenerator.createId();
				e.attr(HAPConstant.UIRESOURCE_ATTRIBUTE_UIID, id);
			}
			
			boolean ifRemove = parseTag(e, resource);
			if(ifRemove)  removes.add(e);
		}
		
		for(Element remove : removes){
			remove.remove();
		}
	}
	
	/*
	 * process a tag element 
	 * return true : this element should be removed after processing
	 * 		  false : this element should not be removed after processiong
	 */
	private boolean parseTag(Element ele, HAPUIDefinitionUnit resource){
		String customTag = HAPUIResourceParserUtility.isCustomTag(ele);
		if(customTag!=null){
			//process custome tag
			parseCustomTag(customTag, ele, resource);
		}
		else{
			//process regular tag
			parseChildScriptExpressionInContent(ele, resource);
			//process key attribute
			parseKeyAttribute(ele, resource, false);
			//process elements's attribute that have expression value 
			parseScriptExpressionInAttribute(ele, resource, false);
			//process all descendant tags under this elment
			parseDescendantTags(ele, resource);
		}
		return false;
	}

	
	/*
	 * process customer tag
	 */
	private void parseCustomTag(String tagName, Element ele, HAPUIDefinitionUnit resource){
		String uiId = HAPUIResourceParserUtility.getUIId(ele); 
		
		HAPUIDefinitionUnitTag uiTag = new HAPUIDefinitionUnitTag(tagName, uiId);

		//process script block
		this.parseChildScriptBlocks(ele, uiTag);
		//process constant block
		this.parseChildConstantBlocks(ele, uiTag);
		//process context block
		this.parseChildContextBlocks(ele, m_resource);
		//process expression block
		this.parseChildExpressionBlocks(ele, m_resource);
		
		//process key attribute
		parseKeyAttribute(ele, resource, true);

		//process data binding attribute
		parseUITagDataBindingAttribute(ele, uiTag);
		
		//process elements's attribute that have expression value 
		parseScriptExpressionInAttribute(ele, resource, true);
		
		//process element's normal attribute
		parseCurrentAttribute(ele, uiTag);
		
		//add placeholder element to the customer tag's postion and then remove the original tag from html structure 
		ele.after("<"+HAPConstant.UIRESOURCE_TAG_PLACEHOLDER+" style=\"display:none;\" "+HAPConstant.UIRESOURCE_ATTRIBUTE_UIID+"="+ uiId +HAPConstant.UIRESOURCE_CUSTOMTAG_WRAPER_END_POSTFIX+"></"+HAPConstant.UIRESOURCE_TAG_PLACEHOLDER+">");
		ele.after("<"+HAPConstant.UIRESOURCE_TAG_PLACEHOLDER+" style=\"display:none;\" "+HAPConstant.UIRESOURCE_ATTRIBUTE_UIID+"="+ uiId +HAPConstant.UIRESOURCE_CUSTOMTAG_WRAPER_START_POSTFIX+"></"+HAPConstant.UIRESOURCE_TAG_PLACEHOLDER+">");
		ele.remove();
		
		//process contents within customer ele
		parseChildScriptExpressionInContent(ele, uiTag);
		parseDescendantTags(ele, uiTag);
		
		HAPUIResourceParserUtility.addSpanToText(ele);
		
		uiTag.postRead();
		
		uiTag.setContent(ele.html());
		
		resource.addUITag(uiTag);
	}

	/*
	 * process attribute of Element for resource(UI resource or custom tag)
	 */
	private void parseCurrentAttribute(Element ele, HAPUIDefinitionUnit resource){
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			resource.addAttribute(eleAttr.getKey(), eleAttr.getValue());
		}
	}
	
	/*
	 * process data binding attribute within customer tag
	 */
	private void parseUITagDataBindingAttribute(Element ele, HAPUIDefinitionUnitTag resource){
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			String eleAttrValue = eleAttr.getValue();
			String eleAttrName = eleAttr.getKey();
			String keyAttrName = HAPUIResourceParserUtility.isKeyAttribute(eleAttrName);
			
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
	 * process element's attribute that have script expression value
	 * only the first script expression will consider, other text or script expression will be ignored
	 */
	private void parseScriptExpressionInAttribute(Element ele, HAPUIDefinitionUnit resource, boolean isCustomerTag){
		String uiId = HAPUIResourceParserUtility.getUIId(ele); 
		
		//read attributes
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			String eleAttrKey = eleAttr.getKey();
			//replace express attribute value with; create ExpressEle object

			HAPEmbededScriptExpressionInAttribute eAttr = new HAPEmbededScriptExpressionInAttribute(eleAttrKey, uiId, eleAttr.getValue(), this.m_expressionManager);
			if(!eAttr.getScriptExpressions().isEmpty()){
				if(isCustomerTag)  resource.addScriptExpressionInTagAttribute(eAttr);
				else  resource.addScriptExpressionInAttribute(eAttr);
				ele.attr(eleAttrKey, "");
			}
			
			
//			List<Object> segments = HAPScriptExpressionUtility.discoverUIExpressionInText(eleAttr.getValue(), this.m_expressionManager);
//			HAPScriptExpression scriptExpression = null;
//			//try to find first script expression in attribute value
//			boolean expresionExists = false;
//			for(Object segment : segments){
//				if(segment instanceof HAPScriptExpression){
//					expresionExists = true;
//					break;
//				}
//			}
//			
//			if(expresionExists){
//				//handle expression attribute
//				HAPEmbededScriptExpressionInAttribute eAttr = new HAPEmbededScriptExpressionInAttribute(eleAttrKey, uiId, segments);
//				if(isCustomerTag)  resource.addScriptExpressionInTagAttribute(eAttr);
//				else  resource.addScriptExpressionInAttribute(eAttr);
//				ele.attr(eleAttrKey, "");
//			}
		}
	}
	
	/*
	 * process key attribute within element 
	 * key attribute means attribute that have predefined meaning within ui resource
	 * isCustomertag : whether this element is a customer tag
	 */
	private void parseKeyAttribute(Element ele, HAPUIDefinitionUnit resource, boolean isCustomerTag){
		String uiId = HAPUIResourceParserUtility.getUIId(ele); 
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			String eleAttrValue = eleAttr.getValue();
			String eleAttrName = eleAttr.getKey();
			String keyAttrName = HAPUIResourceParserUtility.isKeyAttribute(eleAttrName);
			
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
	
}
