//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildComponentInterface;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_ResourceId;
	var node_resourceUtility;
	var node_componentUtility;
	var node_createServiceRequestInfoSimple;
	var node_expressionUtility;
	var node_makeObjectWithApplicationInterface;
	var node_createServiceRequestInfoSet;
	var node_createUITagOnBaseSimple;
	var node_buildUITagCoreObject;
	var node_getObjectType;
	var node_UIDataOperation;
	var node_uiDataOperationServiceUtility;
	var node_createBatchUIDataOperationRequest;
	var node_requestServiceProcessor;
	var node_createUIDataOperationRequest;
	var node_createEventObject;
	var node_createUICustomerTagTest;
	var node_createUICustomerTagViewVariable;
	var node_getEntityObjectInterface;
	var node_uiTagUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUITagDebuggerPlugin = function(){
	
	var loc_out = {

		getCreateEntityCoreRequest : function(complexEntityDef, internalValuePortContainerId, externalValuePortContainerId, bundleCore, configure, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createUITagCoreEntity"), handlers, request);
			var uiTagDefinition = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKCOMPLEXUICUSTOMERTAGDEBUGGER_TAGDEFINITION);
			out.addRequest(node_uiTagUtility.getUITagFunctionRequest(uiTagDefinition, {
				success : function(request, tagDefScriptFun){
					return loc_createUITagComponentCore(complexEntityDef, tagDefScriptFun.fun, internalValuePortContainerId, bundleCore, configure);
				}
			}));
			return out;
		},
	};

	return loc_out;
};

var loc_createUITagComponentCore = function(complexEntityDef, tagDefScriptFun, valueContextId, bundleCore, configure){
	var loc_tagDefScriptFun = tagDefScriptFun;
	var loc_complexEntityDef = complexEntityDef;
	var loc_uiTagDefinition = loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKCOMPLEXUICUSTOMERTAGDEBUGGER_TAGDEFINITION);
	var loc_valueContextId = valueContextId;
	var loc_bundleCore = bundleCore;
	var loc_envInterface = {};
	var loc_uiTagCore;

	var loc_attributes = {};
	var loc_attributeDefinition = {};

	var loc_parentUIEntity;
	
	var loc_tagEventObject = node_createEventObject();
	var loc_eventObject = node_createEventObject();

	var loc_getAttributeValue = function(name){  
		var out = loc_attributes[name];
		if(out==undefined){
			var attrDef = loc_attributeDefinition[name];
			if(attrDef!=undefined)	out = attrDef[node_COMMONATRIBUTECONSTANT.UITAGATTRIBUTEDEFINITION_DEFAULTVALUE];
		}
		return out;
	};

	var loc_getValuePortContainer = function(){
		return node_getEntityObjectInterface(loc_out).getInternalValuePortContainer();
	};

	var loc_coreEnvObj = {

		//--------------------------------- utility
		processRequest : function(requestInfo){   node_requestServiceProcessor.processRequest(requestInfo);  },

		//--------------------------------- attribute
		getAttributeValue : function(name){  return loc_getAttributeValue(name); },
		getAllAttributeNames : function(){   
			var out = [];
			_.each(loc_attributeDefinition, function(attrDef, name){
				out.push(name);
			});
			return out;
		},
		getAttributes : function(){   return loc_attributes;   },

		//--------------------------------- value structure 
		getValueContext : function(){   return loc_valueContext;    },

		//---------------------------------variable
		createVariableByName : function(variableName){
			return loc_envInterface[node_CONSTANT.INTERFACE_WITHVALUEPORT].creatVariableByName(variableName);
		},
		
		//---------------------------------operation request
		getBatchDataOperationRequest : function(operations, handlers, request){
			var requestInfo = node_createBatchUIDataOperationRequest(loc_getValuePortContainer(), handlers, request);
			_.each(operations, function(operation, i){
				requestInfo.addUIDataOperation(operation);						
			});
			return requestInfo;
		},
		executeBatchDataOperationRequest : function(operations, handlers, request){		this.processRequest(this.getBatchDataOperationRequest(operations, handlers, request));		},
		
		getDataOperationSet : function(target, path, value){  
			return new node_UIDataOperation(target, node_uiDataOperationServiceUtility.createSetOperationService(path, value)); 
		},

		getDataOperationGet : function(target, path){  
			return new node_UIDataOperation(target, node_uiDataOperationServiceUtility.createGetOperationService(path)); 
		},
		getDataOperationRequestGet : function(target, path, handler, request){	return node_createUIDataOperationRequest(loc_getValuePortContainer(), this.getDataOperationGet(target, path), handler, request);	},
		executeDataOperationRequestGet : function(target, path, handler, request){			
			return this.processRequest(this.getDataOperationRequestGet(target, path, handler, request));		
		},

		
		//--------------------------------- event
		trigueEvent : function(event, eventData, requestInfo){   loc_tagEventObject.triggerEvent(event, eventData, requestInfo);  },

		//---------------------------------ui resource view
		getCreateDefaultUIContentRequest : function(variationPoints, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createBrickAttributeRequest(node_COMMONATRIBUTECONSTANT.BLOCKCOMPLEXUICUSTOMERTAGDEBUGGER_CONTENT, variationPoints, {
				success: function(request, attrNode){
					return attrNode;
/*					
					_.each(attrNode.getChildValue().getCoreEntity().getChildrenEntity(), function(child){
						var customTag = child.getCoreEntity();
					});
*/					
				}
			}));
			return out;
		},

	};

	var loc_out = {

		setParentUIEntity : function(parentUIEntity){	loc_parentUIEntity = parentUIEntity;	},
		
		getParentUIEntity : function(){   return loc_parentUIEntity;     },
		
		updateAttributes : function(attributes){    loc_uiTagCore.updateAttributes(attributes);       },
		
		creatVariableByName : function(varName){ 
			return loc_getValuePortContainer().createVariableByName(varName) 
		},
		
		getEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			loc_attributes = loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKCOMPLEXUICUSTOMERTAGDEBUGGER_ATTRIBUTE);
			loc_attributeDefinition = loc_uiTagDefinition[node_COMMONATRIBUTECONSTANT.UITAGDEFINITION_ATTRIBUTE];
			
			var uiTagCore;
			var uiTagBase = loc_uiTagDefinition[node_COMMONATRIBUTECONSTANT.UITAGDEFINITION_BASE]; 
			if(uiTagBase=="simpleData"){
				uiTagCore = node_createUITagOnBaseSimple(loc_tagDefScriptFun, loc_coreEnvObj);
			}
			else if(uiTagBase=="arrayData"){
				uiTagCore = node_createUITagOnBaseArray(loc_tagDefScriptFun, loc_coreEnvObj);
			}
			else{
				uiTagCore = loc_tagDefScriptFun.call(loc_out, loc_coreEnvObj);
			}
			
			loc_uiTagCore = node_buildUITagCoreObject(uiTagCore); 
	
			loc_uiTagCore.created();
			
			return out;
		},
		
		getPreInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UITagInit"), handlers, request);
			
			//overriden method before view is attatched to dom
			var initObj = loc_uiTagCore.preInit(request);
			if(initObj!=undefined && node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==node_getObjectType(initObj)){
				out.addRequest(initObj);
			}
			return out;
		},

		updateView : function(view){
			var wrapperView = $("<div/>");
			var childValuePortContainer = node_getEntityObjectInterface(loc_out).getInternalValuePortContainer();
			wrapperView.append($("<br>ValuePortContainerId uitagdebugger: "+childValuePortContainer.getId()+"</br>"));
			wrapperView.append(loc_uiTagCore.initViews());

			view.append(wrapperView);
		},
		
		getPostInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UITagInit"), handlers, request);
			var initObj = loc_uiTagCore.postInit(request);
			if(initObj!=undefined && node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==node_getObjectType(initObj)){
				out.addRequest(initObj);
			}
			return out;
		},

		getUIId : function(){
			return loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.WITHUIID_UIID);
		},
		
		setEnvironmentInterface : function(envInterface){	loc_envInterface = envInterface;	},
		
		registerTagEventListener : function(eventName, handler, thisContext){	return loc_tagEventObject.registerListener(eventName, undefined, handler, thisContext);	},
		registerEventListener : function(listener, handler, thisContext){	return loc_eventObject.registerListener(undefined, listener, handler, thisContext);	},
		
	};
	
	return loc_out;	
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){node_ResourceId = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithApplicationInterface", function(){node_makeObjectWithApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("uitag.createUITagOnBaseSimple", function(){node_createUITagOnBaseSimple = this.getData();});
nosliw.registerSetNodeDataEvent("uitag.buildUITagCoreObject", function(){node_buildUITagCoreObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("variable.uidataoperation.UIDataOperation", function(){node_UIDataOperation = this.getData();});
nosliw.registerSetNodeDataEvent("variable.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("variable.uidataoperation.createBatchUIDataOperationRequest", function(){node_createBatchUIDataOperationRequest  = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("variable.uidataoperation.createUIDataOperationRequest", function(){node_createUIDataOperationRequest = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("uitag.test.createUICustomerTagTest", function(){node_createUICustomerTagTest = this.getData();	});
nosliw.registerSetNodeDataEvent("uitag.test.createUICustomerTagViewVariable", function(){node_createUICustomerTagViewVariable = this.getData();	});
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("uicontent.uiTagUtility", function(){node_uiTagUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUITagDebuggerPlugin", node_createUITagDebuggerPlugin); 

})(packageObj);