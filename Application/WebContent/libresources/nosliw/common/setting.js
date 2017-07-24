//get/create package
var packageObj = library.getChildPackage("setting");    

(function(packageObj){
	//get used node
	var node_basicUtility;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_CONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

/*
 * store setting for sync task, (service, command)
 */
var node_createConfiguresBase = function(baseConfigures){
	
	var loc_baseConfigures = node_createConfigures(baseConfigures);
	
	var loc_out = {
			createConfigures : function(configures){
				return loc_baseConfigures.mergeWith(node_createConfigures(configures));
			},
			
			getBaseConfigures : function(){
				return loc_baseConfigures;
			},
	};
	
	return loc_out;
};

/*
 * configure entity
 * input: 
 * 		string : name1:value1;name2:value2;name3:value3
 * 		object : configure object
 */
var node_createConfigures = function(configures){
	var loc_configures = {};
	if(_.isString(configures)){
		//literal
		var pairs = configures.split(node_NOSLIWCOMMONCONSTANT.CONS_SEPERATOR_ELEMENT);
		for(var i in pairs){
			var pair = pairs[i];
			var segs = pair.split(NOSLIWCOMMONCONSTANT.CONS_SEPERATOR_PART); 
			loc_configures[seg[0]] = sets[1];
		}
	}
	else if(_.isObject(configures)){
		if(node_getObjectType(configures)===node_CONSTANT.TYPEDOBJECT_TYPE_CONFIGURES){
			return configures;
		}
		else{
			loc_configures = configures;
		}
	}
	
	var loc_out = {
		prv_getConfiguresObject : function(){
			return loc_configures;
		},
			
		getConfigure : function(name){
			return loc_configures[name];
		},
		
		mergeWith : function(configures){
			var configuresObj = node_basicUtility.mergeObjects(loc_configures, configures.prv_getConfiguresObject());
			return node_createConfigures(configuresObj);
		},
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_CONFIGURES);
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createConfiguresBase", node_createConfiguresBase);  
packageObj.createChildNode("createConfigures", node_createConfigures); 
	
})(packageObj);
