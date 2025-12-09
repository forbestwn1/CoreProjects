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
	var node_ValueInVarOperation;
	var node_valueInVarOperationServiceUtility;
	var node_createBatchValueInVarOperationRequest;
	var node_requestServiceProcessor;
	var node_createValueInVarOperationRequest;
	var node_createEventObject;
	var node_createUICustomerTagTest;
	var node_createUICustomerTagViewVariable;
	var node_getEntityObjectInterface;
	var node_uiTagUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUITagPlugin = function(){
	
	var loc_out = {

		getCreateEntityCoreRequest : function(complexEntityDef, internalValuePortContainerId, externalValuePortContainerId, bundleCore, configure, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createUITagCoreEntity"), handlers, request);
			var uiTagDefinition = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKCOMPLEXUICUSTOMERTAG_TAGDEFINITION);
			out.addRequest(node_uiTagUtility.getUITagFunctionRequest(uiTagDefinition, {
				success : function(request, tagDefScriptFun){
					return loc_createUITagComponentCore(complexEntityDef, tagDefScriptFun.fun, internalValuePortContainerId, bundleCore, configure);
				}
			}));
			return out;
		}

	};

	return loc_out;
};

var loc_createUITagComponentCore = function(complexEntityDef, tagDefScriptFun, valueContextId, bundleCore, configure){
	var loc_tagDefScriptFun = tagDefScriptFun;
	var loc_complexEntityDef = complexEntityDef;
	var loc_valueContextId = valueContextId;
	var loc_bundleCore = bundleCore;
	var loc_envInterface = {};
	var loc_uiTagCore;

	var loc_attributes = {};
	var loc_attributeDefinition = {};

	var loc_parentUIEntity;
	
	var loc_tagEventObject = node_createEventObject();
	var loc_eventObject = node_createEventObject();

	var loc_trigueEvent = function(event, eventData, requestInfo){
		var eventHandlerDef = loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKCOMPLEXUICUSTOMERTAG_EVENT)[event];
		var eventHandlerInfo = eventHandlerDef[node_COMMONATRIBUTECONSTANT.UIHANDLEREVENT_HANDLERINFO];
		var handlerType = eventHandlerInfo[node_COMMONATRIBUTECONSTANT.UIINFOEVENTHANDLER_TYPE];
		
		if(handlerType==node_COMMONCONSTANT.UICONTENT_EVENTHANDLERTYPE_TASK){
			var taskBrickId = eventHandlerInfo[node_COMMONATRIBUTECONSTANT.UIINFOEVENTHANDLER_TASKBRICKID];
			
			var relativePath = trigguerInfo[node_COMMONATRIBUTECONSTANT.INFOTRIGGUERTASK_HANDLERID][node_COMMONATRIBUTECONSTANT.IDBRICKINBUNDLE_RELATIVEPATH];
			var handlerEntityCoreWrapper = node_complexEntityUtility.getBrickCoreByRelativePath(loc_out, relativePath);

			var taskSetup = node_createTaskSetup(
				node_taskUtility.createTaskFunctionWithSettingValuePortValues(
					node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_EVENT, 
					node_COMMONCONSTANT.VALUEPORT_TYPE_EVENT, 
					eventData)
			);
					
			var taskExeRequest = node_taskExecuteUtility.getExecuteWrapperedTaskWithAdapterRequest(handlerEntityCoreWrapper, undefined, taskSetup, {
				success : function(request, taskResult){
					eventResultView.val(JSON.stringify(taskResult));
				}
			});
			node_requestServiceProcessor.processRequest(taskExeRequest);
		}
	};

	var loc_initAttributes = function(){
		_.each(loc_attributeDefinition, function(attrDef, name){
			var defaultValue = attrDef[node_COMMONATRIBUTECONSTANT.UITAGDEFINITIONATTRIBUTE_DEFAULTVALUE];
			loc_attributes[name] = defaultValue;
		});
		
		loc_attributes = _.extend(loc_attributes, loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKCOMPLEXUICUSTOMERTAG_ATTRIBUTE));
	};

	var loc_getAttributeValue = function(name){
		return loc_attributes[name];
	};

	var loc_getValuePortContainer = function(){
   		return loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].getInternalValuePortContainer();
	};

	var loc_coreEnvObj = {

		//--------------------------------- debug
		getEntityEnvInterface : function(){   return loc_envInterface;   },
      

		//--------------------------------- utility
		processRequest : function(requestInfo){   node_requestServiceProcessor.processRequest(requestInfo);  },

		//--------------------------------- attribute
		getAttributeDefinition : function(name){  return loc_attributeDefinition[name]; },
		getAttributeValue : function(name){  return loc_getAttributeValue(name); },
		getAttributeValues : function(){   return loc_attributes;   },
		getAllAttributeNames : function(){   
			var out = [];
			_.each(loc_attributeDefinition, function(attrDef, name){
				out.push(name);
			});
			return out;
		},

		//---------------------------------variable
		createVariableByName : function(variableName){
			return loc_envInterface[node_CONSTANT.INTERFACE_WITHVALUEPORT].createVariableByName(variableName);
		},
		
		//---------------------------------operation request
		getBatchDataOperationRequest : function(operations, handlers, request){
			var requestInfo = node_createBatchValueInVarOperationRequest(loc_getValuePortContainer(), handlers, request);
			_.each(operations, function(operation, i){
				requestInfo.addValueInVarOperation(operation);						
			});
			return requestInfo;
		},
		executeBatchDataOperationRequest : function(operations, handlers, request){		this.processRequest(this.getBatchDataOperationRequest(operations, handlers, request));		},
		
		getDataOperationSet : function(target, path, value){  
			return new node_ValueInVarOperation(target, node_valueInVarOperationServiceUtility.createSetOperationService(path, value)); 
		},

		getDataOperationGet : function(target, path){  
			return new node_ValueInVarOperation(target, node_valueInVarOperationServiceUtility.createGetOperationService(path)); 
		},
		getDataOperationRequestGet : function(target, path, handler, request){	return node_createValueInVarOperationRequest(loc_getValuePortContainer(), this.getDataOperationGet(target, path), handler, request);	},
		executeDataOperationRequestGet : function(target, path, handler, request){			
			return this.processRequest(this.getDataOperationRequestGet(target, path, handler, request));		
		},

		//--------------------------------- query tag
		queryCustomTagInstance : function(query){          },
		
		//--------------------------------- event
		trigueEvent : function(event, eventData, requestInfo){   loc_trigueEvent(event, eventData, requestInfo);  },

		//---------------------------------ui resource view
		getCreateDefaultUIContentRequest : function(variationPoints, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createBrickAttributeRequest(node_COMMONATRIBUTECONSTANT.WITHUICONTENT_UICONTENT, variationPoints, {
				success : function(request, childNode){
					childNode.getChildValue().getCoreEntity().setParentUIEntity(loc_out);
					return childNode;
				}
			}));
			return out;
		},
		getCreateDefaultUIContentWithInitRequest : function(variationPoints, view, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createBrickAttributeWithInitRequest(node_COMMONATRIBUTECONSTANT.WITHUICONTENT_UICONTENT, variationPoints, view, {
				success : function(request, childNode){
					childNode.getChildValue().getCoreEntity().setParentUIEntity(loc_out);
					return childNode;
				}
			}));
			return out;
		},

	};

	var loc_out = {

		setParentUIEntity : function(parentUIEntity){	loc_parentUIEntity = parentUIEntity;	},
		
		getParentUIEntity : function(){   return loc_parentUIEntity;     },
		
		updateAttributes : function(attributes){
			loc_attributes = _.extend(loc_attributes, attributes);    
			loc_uiTagCore.updateAttributes(attributes);       
		},
		
		createVariableByName1 : function(varName){ 
			return loc_getValuePortContainer().createVariableByName(varName) 
		},
		
		getEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			loc_attributeDefinition = loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKCOMPLEXUICUSTOMERTAG_ATTRIBUTEDEFINITION);
			
			loc_initAttributes();
			
			var uiTagCore;
			var uiTagBase = loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKCOMPLEXUICUSTOMERTAG_BASE); 
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
			view.append(loc_uiTagCore.initViews());
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
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.ValueInVarOperation", function(){node_ValueInVarOperation = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.valueInVarOperationServiceUtility", function(){node_valueInVarOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.createBatchValueInVarOperationRequest", function(){node_createBatchValueInVarOperationRequest  = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.createValueInVarOperationRequest", function(){node_createValueInVarOperationRequest = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("uitag.test.createUICustomerTagTest", function(){node_createUICustomerTagTest = this.getData();	});
nosliw.registerSetNodeDataEvent("uitag.test.createUICustomerTagViewVariable", function(){node_createUICustomerTagViewVariable = this.getData();	});
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("uicontent.uiTagUtility", function(){node_uiTagUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUITagPlugin", node_createUITagPlugin); 

})(packageObj);
