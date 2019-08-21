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
var node_createStateBackupService = function(componentType, id, version, storeService){
	
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

//component's state data
//state data --- part --- name
var node_createState = function(){
	var loc_state = {};
	
	var loc_out = {
		getAllState : function(){   return loc_state;   },
		setAllState : function(state){  loc_state = state; },
		
		getState : function(part){
			var out = loc_state[part];
			if(out==undefined){
				out = {};
				loc_state[part] = out;
			}
			return out;
		},
		
		getStateValue : function(part, name){		return loc_out.getState(part)[name];	},
		
		setStateValue : function(part, name, value){	loc_out.getState(part)[name] = value;	},
		
		clear : function(){		loc_state = {};		}
	};
	return loc_out;
};
	
var node_getStoreService = function(){
	var loc_out = {
		saveData : function(categary, id, data){
			localStorage.setItem(categary+"_"+id, JSON.stringify(data));
		},
		
		retrieveData : function(categary, id){
			return JSON.parse(localStorage.getItem(categary+"_"+id));
		},
		
		clearData : function(categary, id){
			return localStorage.removeItem(categary+"_"+id);
		}
	}
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createState", node_createState); 
packageObj.createChildNode("createStateBackupService", node_createStateBackupService); 
packageObj.createChildNode("getStoreService", node_getStoreService); 

})(packageObj);
