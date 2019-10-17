//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createSecurityService = function(){

	var loc_token;

	var loc_ownerType;
	var loc_ownerId;
	
	var loc_out = {
	
		getToken : function(){		return loc_token;	},
		
		setToken : function(token){		loc_token = token;		},
		
		getOwnerInfo : function(){
			var out = {};
			out[node_COMMONATRIBUTECONSTANT.OWNERINFO_USERID] = loc_token || "testUser";
			out[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTTYPE] = loc_ownerType || "app";
			out[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTID] = loc_ownerId || "testApp";
			return out;
		},
		
		createOwnerInfo : function(ownerType, ownerId){
			var out = {};
			out[node_COMMONATRIBUTECONSTANT.OWNERINFO_USERID] = loc_token;
			out[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTTYPE] = ownerType;
			out[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTID] = ownerId;
			return out;
		},
		
		setOwnerType : function(ownerType){ 	loc_ownerType = ownerType;	},
		
		setOwnerId : function(ownerId){  loc_ownerId = ownerId;   }
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
