/*
 * store setting for sync task, (service, command)
 */
var createConfiguresBase = function(baseConfigures){
	
	var loc_baseConfigures = createConfigures(baseConfigures);
	
	var loc_out = {
			createConfigures : function(configures){
				return loc_baseConfigures.mergeWith(createConfigures(configures));
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
var createConfigures = function(configures){
	var loc_configures = {};
	if(_.isString(configures)){
		//literal
		var pairs = configures.split(NOSLIWCOMMONCONSTANT.CONS_SEPERATOR_ELEMENT);
		for(var i in pairs){
			var pair = pairs[i];
			var segs = pair.split(NOSLIWCOMMONCONSTANT.CONS_SEPERATOR_PART); 
			loc_configures[seg[0]] = sets[1];
		}
	}
	else if(_.isObject(configures)){
		if(nosliwTypedObjectUtility.getObjectType(configures)===NOSLIWCONSTANT.TYPEDOBJECT_TYPE_CONFIGURES){
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
			var configuresObj = nosliwCommonUtility.mergeObjects(loc_configures, configures.prv_getConfiguresObject());
			return createConfigures(configuresObj);
		},
	};
	
	loc_out = nosliwTypedObjectUtility.makeTypedObject(loc_out, NOSLIWCONSTANT.TYPEDOBJECT_TYPE_CONFIGURES);
	
	return loc_out;
};

