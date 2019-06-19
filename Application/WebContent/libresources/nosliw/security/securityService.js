//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createSecurityService = function(){

	var loc_token;

	var loc_ownerInfo;
	
	var loc_out = {
	
		getToken : function(){
			return loc_token;
		},
		
		setToken : function(token){
			loc_token = token;
		},
		
		getOwnerInfo : function(){
			if(loc_ownerInfo==undefined){
				loc_ownerInfo = {};
				loc_ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_USERID] = "testUser";
				loc_ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTID] = "testApp";
				loc_ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTTYPE] = "app";
			}
			return loc_ownerInfo;
		},
		
		setOwnerInfo : function(ownerInfo){
			loc_ownerInfo = ownerInfo;
		}
	};

	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createSecurityService", node_createSecurityService); 

})(packageObj);
