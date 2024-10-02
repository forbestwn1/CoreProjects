//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;

//*******************************************   Start Node Definition  **************************************
	
//interface for ui tag core object
var node_buildUITagCoreObject = function(rawEntity){
	var loc_rawEntity = rawEntity;
	
	var loc_out = {
		created : function(){    return loc_rawEntity.created==undefined?undefined:loc_rawEntity.created();     },
		preInit : function(request){    return loc_rawEntity.preInit==undefined?undefined:loc_rawEntity.preInit(request);     },
		initViews : function(request){    return loc_rawEntity.initViews==undefined?undefined:loc_rawEntity.initViews(request);     },
		postInit : function(request){    return loc_rawEntity.postInit==undefined?undefined:loc_rawEntity.postInit(request);     },
		destroy : function(request){    return loc_rawEntity.destroy==undefined?undefined:loc_rawEntity.destroy(request);     },
		updateAttributes : function(attributes, request){return loc_rawEntity.updateAttributes==undefined?undefined:loc_rawEntity.updateAttributes(attributes, request);     },

		findFunctionDown : function(name){    return loc_rawEntity.findFunctionDown==undefined?undefined:loc_rawEntity.findFunctionDown(name);     },	
		getChildUIViews : function(){    return loc_rawEntity.getChildUIViews==undefined?undefined:loc_rawEntity.getChildUIViews();     },
		getValidateDataRequest : function(handlers, request){    return loc_rawEntity.getValidateDataRequest==undefined?undefined:loc_rawEntity.getValidateDataRequest(handlers, request);     },
		createContextForDemo : function(id, parentContext){    return loc_rawEntity.createContextForDemo==undefined?undefined:loc_rawEntity.createContextForDemo(handlers, request);     },
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("buildUITagCoreObject", node_buildUITagCoreObject); 

})(packageObj);
