//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createSecurityService = function(){

	var loc_token;
	
	var loc_out = {
	
		getToken : function(){
			return loc_token;
		},
		
		setToken : function(token){
			loc_token = token;
		}
	};

	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createSecurityService", node_createSecurityService); 

})(packageObj);
