//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createConfigure;
	var node_createErrorData;
	var node_componentUtility;
	var node_requestServiceProcessor;
	var node_createDataAssociation;
	var node_createIODataSet;
	var node_makeObjectWithApplicationInterface;
	var node_getObjectType;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createScriptTaskGroupEntityPlugin = function(){
	
	var loc_out = {

		getCreateComplexEntityCoreRequest : function(complexEntityDef, valueContextId, bundleCore, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createScriptTaskGroup(complexEntityDef, valueContextId, bundleCore, configure);
			}, handlers, request);
		},

	};

	return loc_out;
};


var loc_createScriptTaskGroup = function(scriptTaskGroupDef, valueContextId, bundleCore, configure){
	
	var loc_scriptTaskGroupDef;
	
	var loc_tasksDefByName = {};
	
	var loc_scriptObj;
	
	var loc_configure;
	
	var loc_init = function(scriptTaskGroupDef, configure){
		loc_scriptTaskGroupDef = scriptTaskGroupDef;
		loc_configure = configure;
		
		_.each(loc_scriptTaskGroupDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYSCRIPTTASKGROUP_DEFINITION), function(taskDef, i){
			loc_tasksDefByName[taskDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME]] = taskDef;
		});
		
		var scriptAttrValue = loc_scriptTaskGroupDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYSCRIPTTASKGROUP_SCRIPT);
		if (typeof scriptAttrValue === 'function') {
			loc_scriptObj = scriptAttrValue();
		}
		else  loc_scriptObj = scriptAttrValue; 
	};
	
	var loc_facadeTaskContainer = node_createTaskContainerInterface({
		
		getAllItemIds : function(){
			var out = [];
			_.each(loc_tasksDefByName, function(taskDef, name){
				out.push(name);
			});
			return out;
		},
		
		getItemVariableInfos : function(itemId){
			
		},
		
		getItemRequirement : function(itemId){
			return loc_tasksDefByName[itemId][node_COMMONATRIBUTECONSTANT.DEFINITIONTASKSCRIPT_REQUIREMENT];
		},
		
		getExecuteItemRequest : function(itemId, taskInput, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var result = loc_scriptObj[itemId](taskInput, handlers, request);
			if(node_getObjectType(result)==node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST){
				out.addRequest(result);				
			}
			else{
				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
					return result;
				}));
			}
			return out;
		},
	});
	
	
	var loc_out = {
		
		getComplexEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			return out;
		},
		
		getRequirement : function(){
			
		}
	};

	loc_init(scriptTaskGroupDef, configure);

	loc_out = node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASKCONTAINER, loc_facadeTaskContainer);

	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("error.entity.createErrorData", function(){node_createErrorData = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithApplicationInterface", function(){node_makeObjectWithApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});

//Register Node by Name
packageObj.createChildNode("createScriptTaskGroupEntityPlugin", node_createScriptTaskGroupEntityPlugin); 

})(packageObj);
