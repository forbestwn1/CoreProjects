//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createAppDecoration;
	var node_createConfigure;
	
//*******************************************   Start Node Definition  ************************************** 	

	
var node_createAppConfigure = function(settingName, parms){
	var appConfigure = node_createConfigure(loc_appSetting, loc_globalConfig, parms).getChildConfigure(undefined, settingName);
	return appConfigure;
};

var loc_globalConfig = {
	__appDataService : function(parms){
		return parms.dataService;
	},
	__storeService : function(parms){
		return parms.storeService;
	},
	__ownerConfigure : function(parms){
		var ownerConfigure = parms.ownerConfigure;
		if(ownerConfigure==undefined){
			ownerConfigure = {
				defaultOwnerType : "app",
				ownerIdByType : {
					"app" : "1234567890"
				}
			};
		}
		return ownerConfigure;
	}
};

var loc_appSetting = {
	share : {
	},
	parts : {
		application : {
			appDecoration : {
				parts : [
					{
						id : "application",
						resource : function(parms){
							return node_createAppDecoration;
						},
						modules : {
							"parts" : {
								application : {
									"root" : function(parms){
										return parms.mainModuleRoot;
									},
									uiDecoration : 
									{
										parts : [
											{
												id: 'Decoration_application_header_framework7'
											}
										] 
									},
									moduleDecoration : 
									{
										parts : [
											{
												id: 'base',
											},
											{
												id: 'application_framework7_mobile',
												app : function(parms){
													return parms.framework7App;
												},
												uiResource : {
													container : {
														id : "Decoration_application_container_framework7"
													}
												}
											}
										],
									},
								},
								"setting" : {
									"root" : function(parms){
										return parms.settingModuleRoot;
									},
									uiDecoration : 
									{
										parts : [
											{
												id: 'Decoration_setting_framework7'
											}
										] 
									},
									"moduleDecoration" : 
									{
										parts : [
											{
												id: 'base',
											},
											{
												id: 'setting_framework7_mobile',
											},
										]
									},
								},
							}
						},
						
					}
				]
			},
		}
	}
};
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createAppDecoration", function(){node_createAppDecoration = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});

//Register Node by Name
packageObj.createChildNode("createAppConfigure", node_createAppConfigure); 

})(packageObj);
