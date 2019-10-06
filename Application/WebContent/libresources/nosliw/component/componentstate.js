//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithType;
	var node_createEventObject;
	var node_basicUtility;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;

//*******************************************   Start Node Definition  ************************************** 	

//object to manage component state
var node_createComponentState = function(state, getGetStateDataRequest, getRestoreStateDataRequest){
	//callback request for collecting component state data
	var loc_getGetStateDataRequest = getGetStateDataRequest;
	//callback request for restoring component state data
	var loc_getRestoreStateDataRequest = getRestoreStateDataRequest;
	
	//state object
	var loc_state = state;
	//store state data for lifecycle rollback 
	var loc_stateDataForRollBack = [];

	var loc_STATEDATA_NAME = "STATEDATA_NAME";
	
	var loc_out = {
		setState : function(state){		loc_state = state;	},
		
		//backup component to state
		getBackupStateRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("component backup state"), handlers, request);
			out.addRequest(loc_getGetStateDataRequest({
				success : function(request, stateData){
					loc_state.setValue(loc_STATEDATA_NAME, stateData, request);
				}
			}));
			return out;
		},
		
		//restore state to component
		getRestoreStateRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("component store state"), handlers, request);
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				var stateData = loc_state.getValue(loc_STATEDATA_NAME, request);
				return loc_getRestoreStateDataRequest(stateData);
			}));
			return out;
		},
		
		//save state data for roll back in the future purpose
		getSaveStateDataForRollBackRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_getGetStateDataRequest({
				success : function(request, stateData){
					//save state data first
					loc_stateDataForRollBack.push(node_basicUtility.clone(stateData));
				}
			}));
			return out;
		},
		
		//restore state data to component for rollback
		getRestoreStateDataForRollBackRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				var stateData = loc_stateDataForRollBack.pop();
				return loc_getRestoreStateDataRequest(stateData);
			}));
			return out;
		},
		
		initDataForRollBack : function(){  loc_stateDataForRollBack = [];  },
		clearDataFroRollBack : function(){    loc_stateDataForRollBack = [];  },
	};
	return loc_out;
};	
	
	
//state backup service is used by component to save data when component paused and retrieve data when component resume
//it is majorly for better user experience, user can continue from where he left
//so, data lost is not a big issue, the worst case is user start from beginning
//the data is stored in UI side
//state data has version information that store with state data
//version: version of the component
var node_createStateBackupService = function(componentType, id, version, storeService){
	var loc_componentType = componentType;  
	var loc_id = id;
	var loc_version = version;   //component version
	var loc_storeService = storeService;

	var loc_state = node_createState();
	
	var loc_requesters = {};
	
	var loc_eventObject = node_createEventObject();

	//read backup data from store to state
	//return whether store data exists
	var loc_retrieveStateData = function(){  
		loc_state.clear();
		var storeData = loc_storeService.retrieveData(loc_componentType, loc_id);
		loc_clearStateData();  //clear backup data after retrieve
		if(storeData!=undefined){
			if(storeData.version!=loc_version)		storeData = undefined;   //when component version change, the data stored by previous component would not work
			if(storeData!=undefined)	loc_state.setStateValue(storeData.data);  //update state data
		}
		return storeData!=undefined;
	};
	
	//save state vlue to store
	var loc_saveStateData = function(){
		var stateData = loc_state.getStateValue();
		if(stateData==undefined){
			loc_clearStateData();
		}
		else{
			var storeData = {
				version : loc_version,
				data : stateData,
			};
			loc_storeService.saveData(loc_componentType, loc_id, storeData);  
		}
	};
	
	var loc_clearStateData = function(){  
		loc_storeService.clearData(loc_componentType, loc_id);  
	};
	
	//retrieve store to state if need
	var loc_retrieveState = function(request){
		var rootRequest = request.getRootRequest();
		var requestId = rootRequest.getId();
		if(loc_requesters[requestId]==undefined){
			//first time for this request, retrieve state from store
			loc_retrieveStateData();
			
			loc_requesters[requestId] = rootRequest;
			rootRequest.registerEventListener(loc_eventObject, function(e, data, req){
				if(e==node_CONSTANT.REQUEST_EVENT_ALMOSTDONE){
				}
				else if(e==node_CONSTANT.REQUEST_EVENT_DONE){
					//remove request
					if(loc_requesters[requestId]!=undefined){
						delete loc_requesters[requestId];
						rootRequest.unregisterEventListener(loc_eventObject);
					}
				}
			});
		}
	};
	
	var loc_out = {
		//state as value
		getStateValue : function(request){
			loc_retrieveState(request);
			return loc_state.getStateValue(request);
		},
		
		//set state
		setStateValue : function(stateValue, request){  
			loc_out.setValue(undefined, stateValue, request);
		},
		
		//get state value by name
		getValue : function(path, request){		
			loc_retrieveState(request);
			return loc_state.getValue(path, request);
		},

		//set state value by path
		setValue : function(path, value, request){
			if(node_basicUtility.isStringEmpty(path))  loc_state.setStateValue(value, request);
			else	loc_state.setValue(path, value, request);
			var rootRequest = request.getRootRequest();
			var requestId = rootRequest.getId();
			if(loc_requesters[requestId]==undefined){
				loc_requesters[requestId] = rootRequest;
				rootRequest.registerEventListener(loc_eventObject, function(e, data, req){
					if(e==node_CONSTANT.REQUEST_EVENT_ALMOSTDONE){
					}
					else if(e==node_CONSTANT.REQUEST_EVENT_DONE){
						//when all request done, then save to store
						loc_saveStateData();
					}
				});
			}
		},
		
		//clear state
		clear : function(request){		loc_out.setStateValue(undefined, request);		},
		
		//create child state by path
		createChildState : function(path){	return loc_createChildState(loc_out, path); },
		
		setVersion(version){  loc_version = version;  }
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_BACKUPSERVICE);
	return loc_out;
};


var node_createState = function(){
	var loc_stateValue = undefined;
	
	var loc_out = {
		//state as value
		getStateValue : function(request){   return loc_stateValue;   },
		//set state
		setStateValue : function(stateValue, request){  loc_stateValue = stateValue; },
		
		//get state value by name
		getValue : function(path, request){		return node_objectOperationUtility.getObjectAttributeByPath(loc_stateValue, path);	},
		//set state value by name
		setValue : function(path, value, request){
			if(loc_stateValue==undefined)  loc_stateValue = {};
			node_objectOperationUtility.operateObject(loc_stateValue, path, node_CONSTANT.WRAPPER_OPERATION_SET, value);	
		},
		
		//clear state
		clear : function(request){		loc_out.setStateValue(undefined, request);		},
		
		//create child state by path
		createChildState : function(path){	return loc_createChildState(loc_out, path); },
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_STATE);
	return loc_out;
};

var loc_createChildState = function(parent, path){
	var loc_parent = parent;
	var loc_path = path;
	
	var loc_out = {
		//state as value
		getStateValue : function(request){	return node_objectOperationUtility.getObjectAttributeByPath(loc_parent.getStateValue(request), loc_path);	},
		//set state
		setStateValue : function(stateValue, request){ loc_parent.setValue(loc_path, stateValue, request);	},
		
		//get state value by name
		getValue : function(path, request){	return node_objectOperationUtility.getObjectAttributeByPath(loc_out.getStateValue(request), path);	},
		//set state value by name
		setValue : function(path, value, request){	loc_parent.setValue(loc_path+"."+path, value, request);	},

		//clear state
		clear : function(request){		loc_out.setStateValue(undefined, request);		},

		//create child state by path
		createChildState : function(path){		return node_createChildState(loc_out, path);	},
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_STATE);
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});


//Register Node by Name
packageObj.createChildNode("createStateBackupService", node_createStateBackupService); 
packageObj.createChildNode("createComponentState", node_createComponentState); 

})(packageObj);
