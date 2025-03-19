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
	var node_createDataAssociation;
	var node_taskUtility;
	var node_getApplicationInterface;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createDataAssociationForTaskAdapterPlugin = function(){
	
	var loc_out = {

		getNewAdapterRequest : function(adapterDefinition, baseEntityCore, handlers, request){
			return node_createServiceRequestInfoSimple({}, function(request){
				return loc_createDataAssociationForTaskAdapter(adapterDefinition, baseEntityCore);
			}, handlers, request);
		},
	};

	return loc_out;
};


var loc_createDataAssociationForTaskAdapter = function(dataAssociationTask, baseEntityCore){
	
	var loc_baseEntityCore;
	
	var loc_dataAssociationForTask;
	var loc_dataAssociationIn;
	var loc_dataAssociationOut;

	var loc_init = function(dataAssociationTask, baseEntityCore){
		loc_baseEntityCore = baseEntityCore;
		
		loc_dataAssociationForTask = dataAssociationTask.getAttributeValue(node_COMMONATRIBUTECONSTANT.ADAPTERDATAASSOCIATIONFORTASK_DATAASSOCIATION);
		loc_dataAssociationIn = node_createDataAssociation(loc_dataAssociationForTask[node_COMMONATRIBUTECONSTANT.DATAASSOCIATIONFORTASK_IN], loc_baseEntityCore);
	
		loc_dataAssociationOut = {};
		_.each(loc_dataAssociationForTask[node_COMMONATRIBUTECONSTANT.DATAASSOCIATIONFORTASK_OUT], function(da, name){
			loc_dataAssociationOut[name] = node_createDataAssociation(loc_dataAssociationForTask[node_COMMONATRIBUTECONSTANT.DATAASSOCIATIONFORTASK_OUT][name], loc_baseEntityCore);
		});
	};
	
	var loc_out = {
		
		getExecuteTaskRequest : function(taskContext, handlers, request){
			
			var onInitTaskRequest = function(handlers, request){
				return loc_dataAssociationIn.getExecuteRequest(handlers, request);
			};
			
			var onFinishTaskRequest = function(task, handlers, request){
				return loc_dataAssociationOut[task.getTaskResult().resultName].getExecuteRequest(handlers, request);
			};
			
			return node_taskUtility.getExecuteEntityTaskRequest(loc_baseEntityCore, onInitTaskRequest, taskContext, onFinishTaskRequest, handlers, request)
		},
		
	};
	
	loc_init(dataAssociationTask, baseEntityCore);
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
nosliw.registerSetNodeDataEvent("iovalue.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("task.taskUtility", function(){node_taskUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.getApplicationInterface", function(){node_getApplicationInterface = this.getData();});


//Register Node by Name
packageObj.createChildNode("createDataAssociationForTaskAdapterPlugin", node_createDataAssociationForTaskAdapterPlugin); 

})(packageObj);
