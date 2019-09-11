//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

//state backup service is used by component to save data when component paused and retrieve data when component resume
//it is majorly for better user experience, user can continue from where he left
//so, data lost is not a big issue, the worst case is user start from beginning
//the data is stored in UI side
//state data has version information that store with state data
var node_createStateBackupService1 = function(componentType, id, version, storeService){
	
	var loc_componentType = componentType;  
	var loc_id = id;
	var loc_version = version;   //component version
	var loc_storeService = storeService;
	
	var loc_out = {
			
		getBackupData : function(){  
			var storeData = loc_storeService.retrieveData(loc_componentType, loc_id);
			if(storeData==undefined)   return;
			loc_out.clearBackupData();  //clear backup data after retrieve
			if(storeData.version!=loc_version)		return;   //when component version change, the data stored by previous component would not work
			return storeData.data;
		},
		
		saveBackupData : function(stateData){
			var storeData = {
				version : loc_version,
				data : stateData
			};
			loc_storeService.saveData(loc_componentType, loc_id, storeData);  
		},
		
		clearBackupData : function(){  
			loc_storeService.clearData(loc_componentType, loc_id);  
		}
	};
	return loc_out;
};	


var node_createStateBackupService = function(componentType, id, version, storeService){
	var loc_componentType = componentType;  
	var loc_id = id;
	var loc_version = version;   //component version
	var loc_storeService = storeService;

	var loc_state = node_createState();
	
	var loc_requesters = {};
	
	var loc_eventObject;

	//read backup data from store to state
	//return whether store data exists
	var loc_retrieveStateData = function(){  
		loc_state.clear();
		var storeData = loc_storeService.retrieveData(loc_componentType, loc_id);
		loc_clearStateData();  //clear backup data after retrieve
		if(storeData!=undefined){
			if(storeData.version!=loc_version)		storeData = undefined;   //when component version change, the data stored by previous component would not work
			if(storeData!=undefined)	loc_state.setStateValue(storeData.data, request);  //update state data
		}
		return storeData!=undefined;
	};
	
	//save state vlue to store
	var loc_saveStateData = function(){
		var storeData = {
			version : loc_version,
			data : loc_state.getStateValue(),
		};
		loc_storeService.saveData(loc_componentType, loc_id, storeData);  
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
			var rootRequest = request.getRootRequest();
			var requestId = rootRequest.getId();
			if(loc_requesters[requestId]==undefined){
				loc_state.setValue(path, value, request);
				
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
		clear : function(request){		loc_state.clear();		},
		
		//create child state by path
		createChildState : function(path){	return loc_state.createChildState(path); },
	};
	
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
		setValue : function(path, value, request){	node_objectOperationUtility.operateObject(loc_stateValue, path, command==node_CONSTANT.WRAPPER_OPERATION_SET, value);	},
		
		//clear state
		clear : function(request){		loc_out.setStateValue(undefined, request);		},
		
		//create child state by path
		createChildState : function(path){	return loc_createChildState(loc_out, path); },
	};
	
	return loc_out;
};

var loc_createChildState = function(parent, path){
	var loc_parent = parent;
	var loc_path = path;
	
	var loc_out = {
		//state as value
		getStateValue : function(request){	return node_objectOperationUtility.getObjectAttributeByPath(loc_parent.getStateValue(request), loc_path);	},
		//set state
		setStateValue : function(stateValue, request){ loc_parent.setValue(loc_path, value, request);	},
		
		//get state value by name
		getValue : function(path, request){	return node_objectOperationUtility.getObjectAttributeByPath(loc_out.getStateValue(request), loc_path);	},
		//set state value by name
		setValue : function(path, value, request){	loc_parent.setValue(loc_path+"."+path, value, request);	},

		//clear state
		clear : function(request){		loc_out.setStateValue(undefined, request);		},

		//create child state by path
		createChildState : function(path){		return node_createChildState(loc_out, path);	},
	};
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createStateBackupService", node_createStateBackupService); 

})(packageObj);
