package com.nosliw.core.data.criteria;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.data.HAPDataTypeId;
import com.nosliw.data.imp.criteria.parser.generated.HAPCriteriaParserGenerated;
import com.nosliw.data.imp.criteria.parser.generated.HAPCriteriaParserGeneratedConstants;
import com.nosliw.data.imp.criteria.parser.generated.SimpleNode;

public class HAPParserCriteria implements HAPCriteriaParserGeneratedConstants{

	List<String> m_tokens;
	
	private HAPParserCriteria(){
		//build tokens
		this.m_tokens = new ArrayList<String>();
		for(String token : HAPCriteriaParserGeneratedConstants.tokenImage){
			String newToken = token;
			if(newToken.startsWith("\""))   newToken = newToken.substring(1);
			if(newToken.endsWith("\""))  newToken = newToken.substring(0, newToken.length()-1);
			this.m_tokens.add(newToken);
		}
	}
	private static HAPParserCriteria m_instance;
	
	public static HAPParserCriteria getInstance(){
		if(m_instance==null)   m_instance = new HAPParserCriteria();
		return m_instance;
	}
	
	public String getToken(int type){
		return this.m_tokens.get(type);
	}
	
	  public HAPDataTypeCriteria parseCriteria(String criteria){
		  if(HAPUtilityBasic.isStringEmpty(criteria))  return HAPDataTypeCriteriaAny.getCriteria();
		  SimpleNode root = null;
		  try{
			  InputStream is = new ByteArrayInputStream(criteria.getBytes());
			  HAPCriteriaParserGenerated parser = new HAPCriteriaParserGenerated( is ) ;
	          root = parser.Criteria(0);
		  }
		  catch(Exception e){
			  e.printStackTrace();
			  return null;
		  }
          return processCriteriaNode(root);
	  }

	  private HAPDataTypeCriteria processCriteriaNode(SimpleNode criteriaNode){
		  HAPDataTypeCriteria out = null;
		  
		  SimpleNode childNode = (SimpleNode)criteriaNode.jjtGetChild(0);
		  switch(childNode.getId()){
		  case HAPCriteriaParserGenerated.JJTIDCRITERIA:
			  out = this.processCriteriaNodeId(childNode);
			  break;
		  case HAPCriteriaParserGenerated.JJTIDSCRITERIA:
			  out = this.processCriteriaNodeIds(childNode);
			  break;
		  case HAPCriteriaParserGenerated.JJTRANGECRITERIA:
			  out = this.processCriteriaNodeRange(childNode);
			  break;
		  case HAPCriteriaParserGenerated.JJTORCRITERIA:
			  out = this.processCriteriaNodeOr(childNode);
			  break;
		  case HAPCriteriaParserGenerated.JJTANDCRITERIA:
			  out = this.processCriteriaNodeAnd(childNode);
			  break;
		  case HAPCriteriaParserGenerated.JJTREFERENCECRITERIA:
			  out = this.processCriteriaNodeReference(childNode);
			  break;
		  case HAPCriteriaParserGenerated.JJTANYCRITERIA:
			  out = this.processCriteriaNodeAny(childNode);
			  break;
		  case HAPCriteriaParserGenerated.JJTEXPRESSIONCRITERIA:
			  out = this.processCriteriaNodeExpression(childNode);
			  break;
		  }		  
		  
		  return out;
	  }

	  private HAPDataTypeCriteriaAny processCriteriaNodeAny(SimpleNode node){
		  return HAPDataTypeCriteriaAny.getCriteria();
	  }

	  private HAPDataTypeCriteriaExpression processCriteriaNodeExpression(SimpleNode expressionNode){
		  String expression = (String)expressionNode.jjtGetValue();
		  return new HAPDataTypeCriteriaExpression(expression);
	  }

	  private HAPDataTypeCriteriaReference processCriteriaNodeReference(SimpleNode referenceNode){
		  String reference = (String)referenceNode.jjtGetValue();
		  return new HAPDataTypeCriteriaReference(reference);
	  }
	  
	  private HAPDataTypeCriteriaAnd processCriteriaNodeAnd(SimpleNode andNode){
		  List<HAPDataTypeCriteria> children = new ArrayList<HAPDataTypeCriteria>();
		  List<SimpleNode> childrenNode = this.getChildrenNodeByType(andNode, HAPCriteriaParserGenerated.JJTCRITERIA);
		  for(SimpleNode childNode : childrenNode){
			  children.add(this.processCriteriaNode(childNode));
		  }
		  return new HAPDataTypeCriteriaAnd(children);
	  }

	  private HAPDataTypeCriteriaOr processCriteriaNodeOr(SimpleNode orNode){
		  List<HAPDataTypeCriteria> children = new ArrayList<HAPDataTypeCriteria>();
		  List<SimpleNode> childrenNode = this.getChildrenNodeByType(orNode, HAPCriteriaParserGenerated.JJTCRITERIA);
		  for(SimpleNode childNode : childrenNode){
			  children.add(this.processCriteriaNode(childNode));
		  }
		  return new HAPDataTypeCriteriaOr(children);
	  }
	  
	  private HAPDataTypeCriteriaRange processCriteriaNodeRange(SimpleNode idsNode){
		  String[] allIds = (String[])idsNode.jjtGetValue();
		  HAPDataTypeId from = allIds[0]==null?null:new HAPDataTypeId(allIds[0]);
		  HAPDataTypeId to = allIds[1]==null?null:new HAPDataTypeId(allIds[1]);
		  SimpleNode subCriteriaGroupNode = this.getChildNodeByType(idsNode, HAPCriteriaParserGenerated.JJTSUBCRITERIAGROUP);
		  HAPDataTypeSubCriteriaGroup subCriteriaGroup = null;
		  if(subCriteriaGroupNode!=null){
			  subCriteriaGroup = this.processSubCriteriaGroupNode(subCriteriaGroupNode);
		  }
		  return new HAPDataTypeCriteriaRange(from, to, subCriteriaGroup);
	  }
	  
	  private HAPDataTypeCriteriaIds processCriteriaNodeIds(SimpleNode idsNode){
		  List<SimpleNode> idsCriteriaNode = this.getChildrenNodeByType(idsNode, HAPCriteriaParserGenerated.JJTIDCRITERIA);
		  Set<HAPDataTypeCriteriaId> childrenCriteria = new HashSet<HAPDataTypeCriteriaId>();
		  for(SimpleNode idCriteriaNode : idsCriteriaNode){
			  childrenCriteria.add(this.processCriteriaNodeId(idCriteriaNode));
		  }
		  return new HAPDataTypeCriteriaIds(childrenCriteria);
	  }
	  
	  private HAPDataTypeCriteriaId processCriteriaNodeId(SimpleNode idNode){
		  String dataTypeIdStr = (String)idNode.jjtGetValue();
		  SimpleNode subCriteriaGroupNode = getChildNodeByType(idNode, HAPCriteriaParserGenerated.JJTSUBCRITERIAGROUP);
		  HAPDataTypeSubCriteriaGroup subCriteriaGroup = null;
		  if(subCriteriaGroupNode!=null){
			  subCriteriaGroup = this.processSubCriteriaGroupNode(subCriteriaGroupNode);
		  }
		  return new HAPDataTypeCriteriaId(new HAPDataTypeId(dataTypeIdStr), subCriteriaGroup);
	  }
	
	  private HAPDataTypeSubCriteriaGroup processSubCriteriaGroupNode(SimpleNode subCriteriaGroupNode){
		  boolean isOpen = (Boolean)subCriteriaGroupNode.jjtGetValue();
		  
		  List<SimpleNode> subCriteriaNodes = this.getChildrenNodeByType(subCriteriaGroupNode, HAPCriteriaParserGenerated.JJTSUBCRITERIA);
		  Map<String, HAPDataTypeCriteria> criterias = new LinkedHashMap<String, HAPDataTypeCriteria>();
		  for(SimpleNode subCriteriaNode : subCriteriaNodes){
			  String name = (String)subCriteriaNode.jjtGetValue();
			  SimpleNode criteriaNode = this.getChildNodeByType(subCriteriaNode, HAPCriteriaParserGenerated.JJTCRITERIA);
			  HAPDataTypeCriteria criteria = this.processCriteriaNode(criteriaNode);
			  criterias.put(name, criteria);
		  }
		  HAPDataTypeSubCriteriaGroupImp out = new HAPDataTypeSubCriteriaGroupImp(isOpen, criterias);
		  
//		  Map<String, HAPDataTypeCriteria> out = new LinkedHashMap<String, HAPDataTypeCriteria>();
//		  for(int i=0; i<subCriteriaGroupNode.jjtGetNumChildren(); i++){
//			  SimpleNode eleNode = (SimpleNode)subCriteriaGroupNode.jjtGetChild(i);
//			  String name = (String)eleNode.jjtGetValue();
//			  SimpleNode criteriaNode = this.getChildNodeByType(eleNode, HAPCriteriaParserGenerated.JJTCRITERIA);
//			  HAPDataTypeCriteria criteria = this.processCriteriaNode(criteriaNode);
//			  out.put(name, criteria);
//		  }
		  return out;
	  }
	  
	  
	  private SimpleNode getChildNodeByType(SimpleNode parentNode, int type){
		  for(int i=0; i<parentNode.jjtGetNumChildren(); i++){
			  SimpleNode childNode = (SimpleNode)parentNode.jjtGetChild(i);
			  if(childNode.getId()==type)  return childNode;
		  }
		  return null;
	  }
	  
	  private List<SimpleNode> getChildrenNodeByType(SimpleNode parentNode, int type){
		  List<SimpleNode> out = new ArrayList<SimpleNode>();
		  for(int i=0; i<parentNode.jjtGetNumChildren(); i++){
			  SimpleNode childNode = (SimpleNode)parentNode.jjtGetChild(i);
			  if(childNode.getId()==type)  out.add(childNode);
		  }
		  return out;
	  }
}
