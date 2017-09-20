package com.nosliw.uiresource;

import java.io.File;
import java.io.InputStream;
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
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.strvalue.io.HAPStringableEntityImporterJSON;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.data.core.criteria.HAPCriteriaParser;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionManager;

/*
 * This is a utility class that process ui resource file and create ui resource object
 * the id index start with 1 every processing start so that for same ui resource, we would get same result
 */
public class HAPUIResourceParser {

	//for creating ui id
	private HAPUIResourceIdGenerator m_idGenerator;
	//configuration object
	private HAPConfigure m_setting;
	//current ui resource 
	private HAPUIResource m_resource = null;
	
	private HAPValueInfoManager m_valueInfoMan;
	private HAPCriteriaParser m_criteriaParser;
	private HAPExpressionManager m_expressionManager;
	
	public HAPUIResourceParser(HAPConfigure setting, HAPExpressionManager expressionMan, HAPUIResourceIdGenerator idGenerator){
		this.m_idGenerator = idGenerator;
		this.m_setting = setting;
		this.m_expressionManager = expressionMan;
		this.m_criteriaParser = HAPCriteriaParser.getInstance();
	}
	
	public HAPUIResource getUIResource(){return this.m_resource;}
	
	public HAPUIResource parseFile(String fileName){
		try{
			File input = new File(fileName);
			//use file name as ui resource name
			String resourceName = HAPFileUtility.getFileName(input);
			
			Document doc = Jsoup.parse(input, "UTF-8");
			this.m_resource = parseUIResourceContent(resourceName, doc);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return m_resource;
	}

	private HAPUIResource parseUIResourceContent(String resourceName, Document doc){
		HAPUIResource resource = null;
		try{
			resource = new HAPUIResource(resourceName);

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
			Set<HAPUITag> tags = new HashSet<HAPUITag>();
			HAPUIResourceParserUtility.getAllChildTags(resource, tags);
			for(HAPUITag tag : tags)  resource.addUITagLib(tag.getTagName());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return resource;
	}
	
	
	
	private void parseChildContextBlocks(Element ele, HAPUIResourceBasic resource){
		List<Element> removes = new ArrayList<Element>();
		Elements contextEles = ele.getElementsByTag(HAPUIResourceBasic.CONTEXT);
		for(int i=0; i<contextEles.size(); i++){
			try {
				String content = contextEles.get(i).html();
				JSONObject defsJson = new JSONObject(content);
				Iterator<String> defNames = defsJson.keys();
				while(defNames.hasNext()){
					String eleName = defNames.next();
					JSONObject eleDefJson = defsJson.optJSONObject(eleName);
					HAPContextElement contextEle = new HAPContextElement(eleName);

					Object d = eleDefJson.opt(HAPContextElement.DEFAULT);
					if(d!=null)		contextEle.setDefault(d.toString());

					String criteria = eleDefJson.optString(HAPContextElement.CRITERIA);
					if(!HAPBasicUtility.isStringEmpty(criteria)){
						contextEle.setCriteria(this.m_criteriaParser.parseCriteria(criteria));
					}
					else{
						Object criteriasObj = eleDefJson.opt(HAPContextElement.CHILDREN);
						Map<String, HAPDataTypeCriteria> criterias = new LinkedHashMap<String, HAPDataTypeCriteria>();
						this.parseContextElementChildren("", criteriasObj, criterias);
						for(String path : criterias.keySet()){
							contextEle.addChild(path, criterias.get(path));
						}
					}
					resource.addContextElement(contextEle);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			removes.add(contextEles.get(i));
		}
		//remove script ele from doc
		for(Element remove : removes)	remove.remove();
	}
	
	private void parseContextElementChildren(String path, Object criteriasObj, Map<String, HAPDataTypeCriteria> criterias){
		if(criteriasObj instanceof String){
			criterias.put(path, this.m_criteriaParser.parseCriteria((String)criteriasObj));
		}
		else if(criteriasObj instanceof JSONObject){
			JSONObject criteriasJsonObj = (JSONObject)criteriasObj;
			Iterator<String> names = criteriasJsonObj.keys();
			while(names.hasNext()){
				String name = names.next();
				parseContextElementChildren(HAPNamingConversionUtility.cascadePath(path, name), criteriasJsonObj.opt(name), criterias);
			}
		}
	}
	
	private void parseChildExpressionBlocks(Element ele, HAPUIResourceBasic resource){
		List<Element> removes = new ArrayList<Element>();

		Elements expressionEles = ele.getElementsByTag(HAPUIResourceBasic.EXPRESSIONS);
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
	private void parseChildConstantBlocks(Element ele, HAPUIResourceBasic resource){
		List<Element> removes = new ArrayList<Element>();
		
		Elements constantEles = ele.getElementsByTag(HAPUIResourceBasic.CONSTANTS);
		for(int i=0; i<constantEles.size(); i++){
			try {
				String content = constantEles.get(i).text();
				JSONObject defsJson = new JSONObject(content);
				Iterator<String> defNames = defsJson.keys();
				while(defNames.hasNext()){
					String defName = defNames.next();
					
					if(resource==null || defsJson==null){
						int kkkk = 5555;
						kkkk++;
					}
					
					resource.addConstant(defName, new HAPConstantDef(defsJson.get(defName)));
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
	private void parseChildScriptBlocks(Element ele, HAPUIResourceBasic resource){
		List<Element> removes = new ArrayList<Element>();
		
		Elements childEles = ele.children();
		for(int i=0; i<childEles.size(); i++){
			Element childEle = childEles.get(i);
			String childTagName = childEle.tag().getName();
			if(HAPUIResourceBasic.SCRIPTS.equals(childTagName)){
				HAPScript jsBlock = new HAPScript(ele.html());
				resource.addJSBlock(jsBlock);
				removes.add(childEle);
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
	private void parseChildScriptExpressionInContent(Element ele, HAPUIResourceBasic resource){
		List<TextNode> textNodes = ele.textNodes();
		for(TextNode textNode : textNodes){
			String text = textNode.text();
			List<Object> segments = HAPUIResourceParserUtility.parseUIExpression(text, this.m_idGenerator, this.m_expressionManager);
			StringBuffer newText = new StringBuffer();
			for(Object segment : segments){
				if(segment instanceof String){
					newText.append((String)segment);
				}
				else if(segment instanceof HAPScriptExpression){
					HAPScriptExpression scriptExpression = (HAPScriptExpression)segment;
					newText.append("<span "+HAPConstant.UIRESOURCE_ATTRIBUTE_UIID+"="+scriptExpression.getId()+"></span>");
					HAPScriptExpressionInContent expressionContent = new HAPScriptExpressionInContent(scriptExpression);
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
	private void parseDescendantTags(Element ele, HAPUIResourceBasic resource){
		List<Element> removes = new ArrayList<Element>();
		Elements eles = ele.children();
		for(Element e : eles){
			if(HAPBasicUtility.isStringEmpty(HAPUIResourceParserUtility.getUIId(e))){
				//if tag have no ui id, then create ui id for it
				createUIId(e, resource);
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
	private boolean parseTag(Element ele, HAPUIResourceBasic resource){
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
	private void parseCustomTag(String tagName, Element ele, HAPUIResourceBasic resource){
		String uiId = HAPUIResourceParserUtility.getUIId(ele); 
		
		HAPUITag uiTag = new HAPUITag(tagName, uiId);

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
	private void parseCurrentAttribute(Element ele, HAPUIResourceBasic resource){
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			resource.addAttribute(eleAttr.getKey(), eleAttr.getValue());
		}
	}
	
	/*
	 * process data binding attribute within customer tag
	 */
	private void parseUITagDataBindingAttribute(Element ele, HAPUITag resource){
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
	 * only the first script expression will consider
	 */
	private void parseScriptExpressionInAttribute(Element ele, HAPUIResourceBasic resource, boolean isCustomerTag){
		String uiId = HAPUIResourceParserUtility.getUIId(ele); 
		
		//read attributes
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			String eleAttrKey = eleAttr.getKey();
			//replace express attribute value with; create ExpressEle object
			
			List<Object> segments = HAPUIResourceParserUtility.parseUIExpression(eleAttr.getValue(), this.m_idGenerator, this.m_expressionManager);
			HAPScriptExpression scriptExpression = null;
			//try to find first script expression in attribute value
			for(Object segment : segments){
				if(segment instanceof HAPScriptExpression){
					scriptExpression = (HAPScriptExpression)segment;
					break;
				}
			}
			
			if(scriptExpression!=null){
				//handle expression attribute
				HAPScriptExpressionInAttribute eAttr = new HAPScriptExpressionInAttribute(eleAttrKey, uiId, scriptExpression);
				if(isCustomerTag)  resource.addScriptExpressionInTagAttribute(eAttr);
				else  resource.addScriptExpressionInAttribute(eAttr);
				ele.attr(eleAttrKey, "");
			}
		}
	}
	
	/*
	 * process key attribute within element 
	 * key attribute means attribute that have predefined meaning within ui resource
	 * isCustomertag : whether this element is a customer tag
	 */
	private void parseKeyAttribute(Element ele, HAPUIResourceBasic resource, boolean isCustomerTag){
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
	
	/*
	 * create ui id attribute for element
	 */
	private String createUIId(Element ele, HAPUIResourceBasic resource){
		String id = this.m_idGenerator.createId();
		ele.attr(HAPConstant.UIRESOURCE_ATTRIBUTE_UIID, id);
		return id;
	}
	
}
