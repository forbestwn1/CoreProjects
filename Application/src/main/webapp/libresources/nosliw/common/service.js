//get/create package
var packageObj = library.getChildPackage("service");    

(function(packageObj){
	//get used node
	var node_basicUtility;
//*******************************************   Start Node Definition  ************************************** 	

/*
 * service information object 
 * this structor can be used in different senario: romote task, service request 
 * 		command: service name
 * 		data:    input data
 */
var node_ServiceInfo = function(command, parms){
	this.command = command;
	this.parms = parms;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("ServiceInfo", node_ServiceInfo); 

})(packageObj);
