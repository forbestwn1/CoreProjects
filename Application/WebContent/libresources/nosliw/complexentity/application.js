//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_makeObjectWithType;
	var node_createPackageDebugView;
	var node_createConfigure;
	var node_basicUtility;
	var node_componentUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_PACKAGE_NAME = "package";	

//application is main object for application
//it may multiple package (one is main package, each decoration is package as well)
var node_createApplication = function(resourceId, configure){

	var loc_resourceId = resourceId;
	
	var loc_configure = configure;
	var loc_configureValue = node_createConfigure(configure).getConfigureValue();
	
	var loc_parentView;

	var loc_envInterface;
	
	var loc_createPackageRuntime = function(request){
		var packageRuntime = nosliw.runtime.getComplexEntityService().createPackageRuntime(loc_resourceId, loc_configure, request);
		loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].addChild(loc_PACKAGE_NAME, packageRuntime);
		return packageRuntime;
	};
	
	var loc_getPackageRuntime = function(){
		return loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].getChild(loc_PACKAGE_NAME).getChildValue();
	};
	
	var loc_out = {

		getDataType: function(){    return  "application";   },

		getPackageRuntime : function(){   return loc_getPackageRuntime();   },
		
		getMainEntityRuntime : function(){ return this.getPackageRuntime().getCoreEntity().getMainBundleRuntime().getCoreEntity().getMainEntity();  },
		
		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},

		getPreInitRequest : function(handlers, request){   
			loc_createPackageRuntime(request);
		},
		
		updateView : function(view){    
			loc_parentView = view;
			loc_getPackageRuntime().updateView(view);     
		},
		
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_APPLICATION);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("component.debug.createPackageDebugView", function(){node_createPackageDebugView = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createApplication", node_createApplication); 

})(packageObj);
