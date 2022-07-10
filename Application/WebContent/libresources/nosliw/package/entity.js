//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_makeObjectWithType;
	
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

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});

//Register Node by Name
packageObj.createChildNode("EntityIdInDomain", node_EntityIdInDomain); 

})(packageObj);
