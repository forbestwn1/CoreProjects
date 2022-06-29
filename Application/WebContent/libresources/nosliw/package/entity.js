//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_makeObjectWithType;
	var nod_createVariableDomain;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_EntityIdInDomain = function(parm1, parm2){
	if(typeof parm1 === 'object'){
		this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE] = parm1[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE];
		this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYID] = parm1[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYID];
		this.literateStr =  this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYID]+node_COMMONCONSTANT.SEPERATOR_LEVEL1+this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE];
	}
	else if(typeof parm1 === 'string' && parm2==undefined){
		this.literateStr = parm1;
		var index = parm1.indexOf(node_COMMONCONSTANT.SEPERATOR_LEVEL1);
		this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYID] = parm1.substring(0, index);
		this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE] = parm1.substring(index+node_COMMONCONSTANT.SEPERATOR_LEVEL1.length);
	}
	else{
		this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE] = parm1;
		this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYID] = parm2;
		this.literateStr =  this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYID]+node_COMMONCONSTANT.SEPERATOR_LEVEL1+this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE];
	}
};	

var node_createPackageRuntime = function(packageDef, configure){

	var loc_packageDef = packageDef;
	
	var loc_mainBundle = null;
	
	var loc_out = {
			
		setMainBundleRuntime : function(bundleRuntime){
			loc_mainBundle = bundleRuntime;
		}
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_PACKAGE);

	return loc_out;
};

//bundle object
var node_createCreateBundleRuntime = function(mainEntityId, bundleDef, configure){
	
	var loc_mainEntityId = mainEntityId;
	
	//bundle definition
	var loc_bundleDefinition = bundleDef;
	
	//variable domain for this bundle
	var loc_variableDomain = nod_createVariableDomain(loc_bundleDefinition.valueStructureDomain);
	
	//root component runtime
	var loc_mainComponent = null;
	
	var loc_out = {
		
		getBundleDefinition : function(){
			return loc_bundleDefinition;
		},
		
		getVariableDomain : function(){
			return loc_variableDomain;
		},
		
		setMainComponent : function(component){
			loc_mainComponent = component;
		}
	};

	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_BUNDLE);
	
	return loc_out;
	
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("package.createVariableDomain", function(){nod_createVariableDomain = this.getData();});

//Register Node by Name
packageObj.createChildNode("createPackageRuntime", node_createPackageRuntime); 
packageObj.createChildNode("createCreateBundleRuntime", node_createCreateBundleRuntime); 
packageObj.createChildNode("EntityIdInDomain", node_EntityIdInDomain); 

})(packageObj);
