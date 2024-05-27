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
	var node_getEntityTreeNodeInterface;
	var node_getApplicationInterface;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createTaskPlugin = function(){
	
	var loc_out = {

		getCreateEntityRequest : function(entityDef, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createTaskCore(entityDef, configure);
			}, handlers, request);
		},
	};

	return loc_out;
};


var loc_createTaskCore = function(taskDef, configure){

	var loc_taskDef = taskDef;
	
	var loc_envInterface;

	var loc_indexId = 0;
	
	var loc_createTaskId = function(){
		loc_indexId++;
		return loc_indexId + "";
	};

	var loc_facadeTask = {
		getItemVariableInfos : function(){
		},

		getRequirement : function(){
			
		},

		getExecuteRequest : function(adForTaskName, handlers, request){
			return loc_taskUtility.getTaskAttributeExecuteRequest(loc_out, node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYTASK_IMPLEMENTATION, extraInfo, handlers, request);
		},
	};
		
	var loc_out = {
		
		getExecuteTaskRequest : function(adForTaskName, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

			//create task entity runtime
			var childId = loc_createTaskId();
			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createChildByAttributeRequest(childId, node_COMMONATRIBUTECONSTANT.BLOCKTASKWRAPPER_TASK, undefined, {
				success : function(request){
					var childNodeObj = loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].getChild(childId);
					var adapter = childNodeObj.getAdapters()[adForTaskName];
					return adapter.getExecuteRequest();
				}
			}));
			
			//init entity runtime
			
			
			
			//execute da for task
			
			
			//destroy task entity runtime

			return out;			
		},
		
		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},
		
	};
	
	return node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK, loc_facadeTask);
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
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getApplicationInterface", function(){node_getApplicationInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("createTaskPlugin", node_createTaskPlugin); 

})(packageObj);
