//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_DesignStep = function(changes, question, info){
	this.changes = changes;
	this.question = question;
	this.info = info;
	return this;
};

var node_ElementId = function(categary, id){
	this[node_COMMONATRIBUTECONSTANT.IDELEMENT_CATEGARY] = categary;
	this[node_COMMONATRIBUTECONSTANT.IDELEMENT_ID] = id;
};
	
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("DesignStep", node_DesignStep); 
packageObj.createChildNode("ElementId", node_ElementId); 

})(packageObj);
