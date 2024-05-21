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
	var node_dataIOUtility;
	var node_taskUtility;
	var node_ioDataFactory;
	var node_getBasicEntityObjectInterface;
	var node_IOTaskInfo;
	var node_getApplicationInterface;
	var node_complexEntityUtility;
	
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
		
		getExecuteRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			out.addRequest(loc_dataAssociationIn.getExecuteRequest({
				success : function(request, intputValue){
					
				}
			}));
			
			
			
			var inIO = node_createIODataSet();
			
			var outIOs = {};
			var taskInfo = new node_IOTaskInfo(function(handlers, request){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				out.addRequest(inIO.getDataValueRequest(undefined, {
					success : function(request, taskInputValue){
						var taskInterface = node_getApplicationInterface(node_complexEntityUtility.getCoreEntity(childRuntime), node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK);
						var taskInput = extraInfo==undefined?undefined:extraInfo.taskInput;
						var requirement = extraInfo==undefined?undefined:extraInfo.requirement;
						return taskInterface.getExecuteRequest(taskInputValue.default, requirement, {
							success : function(request, taskResult){
								var resultName = taskResult.resultName;
								outIOs[resultName] = node_createIODataSet();
								outIOs[resultName].setData(undefined, {default:taskResult.resultValue});
								return resultName;
							}
						});
					}
				}));
				return out;
			}, "interactive", inIO, outIOs);
			
			var parentIODataSet = node_createIODataSet(node_ioDataFactory.createIODataByComplexEntity(parentCore));
			out.addRequest(node_taskUtility.getExecuteWrappedTaskRequest(parentIODataSet, loc_dataAssociationTask, taskInfo));
			return out;
		}
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
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("error.entity.createErrorData", function(){node_createErrorData = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.dataIOUtility", function(){node_dataIOUtility = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.taskUtility", function(){node_taskUtility = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.ioDataFactory", function(){node_ioDataFactory = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getBasicEntityObjectInterface", function(){node_getBasicEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.IOTaskInfo", function(){node_IOTaskInfo = this.getData();});
nosliw.registerSetNodeDataEvent("component.getApplicationInterface", function(){node_getApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createDataAssociationForTaskAdapterPlugin", node_createDataAssociationForTaskAdapterPlugin); 

})(packageObj);
