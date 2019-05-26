//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_objectOperationUtility;
	var node_IOTaskResult;
	var node_NormalActivityOutput;
	var node_EndActivityOutput;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_contextUtility;
	var node_createAppRuntimeRequest;
	var node_resourceUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_appDataService = function(){
	var loc_data = [
		{
			id : "id1",
			version : "version1",
			data : {
				schoolTypeInData : {
					"dataTypeId": "test.options;1.0.0",
					"value": {
						"value" : "Public",
						"optionsId" : "schoolType"
					}
				},
				schoolRatingInData : {
					"dataTypeId": "test.float;1.0.0",
					"value": 9.0
				}
			}
		},
	];
	
	var loc_out = {
			
		getGetAppDataInfoRequest : function(dataName, handlers, requester_parent){
			return node_createServiceRequestInfoSimple(undefined, function(requestInfo){
				var out = [];
				_.each(loc_data, function(dataEle, i){
					out.push(new node_ApplicationDataInfo(dataName, dataEle.id, dataEle.version));
				});
				return out;
			}, handlers, requester_parent);
		},	

		getGetAppDataByIdRequest : function(dataName, id, handlers, requester_parent){
			return node_createServiceRequestInfoSimple(undefined, function(requestInfo){
				var out;
				_.each(loc_data, function(data, index){
					if(data.id==id){
						out = data;
					}
				});
				return out;
			}, handlers, requester_parent);
		},	

		getAddAppDataRequest : function(dataName, data, version, handlers, requester_parent){
			return node_createServiceRequestInfoSimple(undefined, function(requestInfo){
				var appData = {
					id : nosliw.generateId(),
					version : version,
					data : data,
				};
				loc_data.push(appData);
				return appData;
			}, handlers, requester_parent);
		},	
			
		getDeleteAppDataRequest : function(dataName, id, handlers, requester_parent){
			return node_createServiceRequestInfoSimple(undefined, function(requestInfo){
				for(var i in loc_data){
					if(loc_data[i].id==id){
						loc_data.splice(i, 1);
						return;
					}
				}
			}, handlers, requester_parent);
		},	

		getUpdateAppDataRequest : function(dataName, appData, handlers, requester_parent){
			return node_createServiceRequestInfoSimple(undefined, function(requestInfo){
				for(var i in loc_data){
					if(loc_data[i].id==appData.id){
						loc_data[i] = appData;
						return data;
					}
				}
			}, handlers, requester_parent);
		},	
	};

	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityResult", function(){node_IOTaskResult = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityOutput", function(){node_NormalActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.EndActivityOutput", function(){node_EndActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createAppRuntimeRequest", function(){node_createAppRuntimeRequest = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ApplicationDataInfo", function(){node_ApplicationDataInfo = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("appDataService", node_appDataService); 

})(packageObj);
