//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createStateBackupService = function(componentType, id, version, storeService){
	
	var loc_componentType = componentType;
	var loc_id = id;
	var loc_version = version;
	var loc_storeService = storeService;
	
	var loc_out = {
			
		getBackupData : function(){  
			var storeData = loc_storeService.retrieveData(loc_componentType, loc_id);
			if(storeData==undefined)   return;
			loc_out.clearBackupData();  //clear backup data after retrieve
			if(storeData.version!=loc_version)		return;
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
	
var node_createState = function(){
	var loc_state = {};
	
	var loc_out = {
		
		getAllState : function(){   return loc_state;   },
		setAllState : function(state){  loc_state = state; },
		
		getState : function(component){
			var out = loc_state[component];
			if(out==undefined){
				out = {};
				loc_state[component] = out;
			}
			return out;
		},
		
		getStateValue : function(component, name){
			return loc_out.getState(component)[name];
		},
		
		setStateValue : function(component, name, value){
			loc_out.getState(component)[name] = value;
		},
		
		clear : function(){
			loc_state = {};
		}
	};
	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createState", node_createState); 
packageObj.createChildNode("createStateBackupService", node_createStateBackupService); 

})(packageObj);
