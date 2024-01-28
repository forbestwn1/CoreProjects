//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_basicUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_uiEventData = function(uiId, eventName, eventData){
	this.uiId = uiId;
	this.eventName = eventName;
	this.eventData = eventData;
};

//extra information by domain that provided by system and consumed by ui 
var node_createSystemData = function(){
	var loc_systemData = {};

	var loc_updateSystemData = function(domain, obj){
		var domainObj = loc_systemData[domain];
		if(domainObj==undefined){
			domainObj = {};
			loc_systemData[domain] = domainObj;
		}
		domainObj = _.extend(domainObj, obj);
	};
	
	var loc_buildSystemDataContext = function(domain, context){
		if(context==undefined)  context = {};
		_.each(loc_systemData[domain], function(value, prop){
			context[node_basicUtility.buildNosliwFullName(domain+"_"+prop)] = value;
		});
		return context;
	};

	var loc_out = {
		getSystemData : function(domain){		return loc_systemData[domain];		},
		
		updateSystemData : function(domain, obj){		loc_updateSystemData(domain, obj);		},
		
		buildSystemDataContextByDomain : function(domain, context){		return loc_buildSystemDataContext(domain, context);	},	
	
		buildSystemDataContext : function(context){
			if(context==undefined)  context = {};
			//combine data from module with extra data in ui
			_.each(loc_systemData, function(data, domain){
				context = loc_buildSystemDataContext(domain, context);
			});
			return context;
		},	
	};
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("uiEventData", node_uiEventData); 
packageObj.createChildNode("createSystemData", node_createSystemData); 

})(packageObj);
