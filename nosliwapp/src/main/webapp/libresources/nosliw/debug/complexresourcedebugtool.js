//get/create package
var packageObj = library.getChildPackage();    


(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_getComponentLifecycleInterface;
	var node_getComponentInterface;
	var node_getEntityTreeNodeInterface;
	var node_createServiceRequestInfoSimple;
	var node_requestServiceProcessor;
	var node_getComponentManagementInterface;
	var node_componentUtility;
	var node_getStateMachineDefinition;
	var node_getComplexEntityObjectInterface;
	var node_complexEntityUtility;
	var node_createConfigures;
	var node_createServiceRequestInfoRemote;
    var node_ServiceInfo;
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_createComponentResourceGroupByType = function(){
	var loc_vueComponent = {
		data : function(){
			return {};
		},
		watch: {
		},
		methods : {
			
			getEntityType : function(){	return this.data[node_COMMONATRIBUTECONSTANT.CONTAINERCOMPLEXENTITY_ENTITYTYPE];    },

			getResources : function(){	return this.data[node_COMMONATRIBUTECONSTANT.CONTAINERCOMPLEXENTITY_COMPLEXENTITYINFO];    },
			
			onSelectResource : function(resourceInfo){
				this.$emit("selectResource", resourceInfo);
			},

		},
		props : ['data'],
		template : `
			<div class="treeview-item">
				<div class="treeview-item-root">
			        <span class="treeview-toggle"></span>
				    <a class="treeview-item-selectable">
					      <div class="treeview-item-content">
								<i class="icon f7-icons">doc</i>
								<div class="treeview-item-label">{{getEntityType()}}</div>
					      </div>
				    </a>
				</div>

			    <div class="treeview-item-children" v-for="(resource, index) in getResources()" v-bind:key="index">
				      <div class="treeview-item-content">
							<complexresource-resource
						  		v-bind:data="resource"
								v-on:selectResource="onSelectResource"
							/>
				      </div>
			    </div>
		    </div>
		`
	};
	return loc_vueComponent;
};

var loc_createComponentComplexResource = function(){
	var loc_vueComponent = {
		data : function(){
			return {};
		},
		watch: {
		},
		methods : {
			
			getName : function(){   return this.data[node_COMMONATRIBUTECONSTANT.INFOCOMPLEXENTITY_RESOURCENAME];    },
			
			onSelectResource : function(){	this.$emit("selectResource", this.data);		},
		},
		props : ['data'],
		template : `
		    <a v-on:click.prevent="onSelectResource">{{getName()}}</a>
		`
	};
	return loc_vueComponent;
};

var node_createComplexResourceDebugView = function(containerViewId){
	
	var loc_CONFIGURE_NONE = "none";
	
	var loc_configureName = "browsecomplexentity";
	
	var loc_componentData = {
		configures : [],
		complexResources : [],
		selectedConfigure : loc_CONFIGURE_NONE,
		CONFIGURE_NONE : loc_CONFIGURE_NONE,
		currentApplication : null
	};
	
	var loc_vue;
	
	var loc_init = function(){
		
		nosliw.registerSetNodeDataEvent("runtime", function(){
			//register remote task configure
			var configure = node_createConfigures({
				url : loc_configureName,
			});
			
			nosliw.runtime.getRemoteService().registerSyncTaskConfigure(loc_configureName, configure);
		});


		Vue.component('complexresource-group', loc_createComponentResourceGroupByType());
		Vue.component('complexresource-resource', loc_createComponentComplexResource());

		loc_vue = new Vue({
			el : "#"+containerViewId,
			data: loc_componentData,
			methods : {
				onSelectConfigure : function(configure){
					this.selectedConfigure = this.$el.querySelector("#configures").value;
				},
				
				onSelectResource : function(resourceInfo){
					var loc_resourceIdObj = resourceInfo[node_COMMONATRIBUTECONSTANT.INFOCOMPLEXENTITY_RESOURCEID];
					var loc_resourceType = loc_resourceIdObj[node_COMMONATRIBUTECONSTANT.RESOURCEID_RESOURCETYPE];
					var loc_resourceId = loc_resourceIdObj[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID];
					var loc_configure = this.selectedConfigure==loc_CONFIGURE_NONE?"":this.selectedConfigure;

					var appWin = window.open ("http://localhost:8080/nosliw/application.html?resourceType="+loc_resourceType+"&resourceId="+loc_resourceId+"&configure="+loc_configure,"_blank");
					var that  = this;
					appWin.addEventListener("nosliwApplicationActive", function(event) {
						nosliw = appWin.nosliw;
						that.currentApplication = event.detail;
						updateApplication(that.currentApplication);
				    });
				}
			},
			watch: {
			},
			computed : {
			},
			template : `
				<div>
					<div>
						<select name="configures" id="configures" v-on:change="onSelectConfigure">
						  <option :value="CONFIGURE_NONE">{{CONFIGURE_NONE}}</option>
						  <option :value="configure" v-for="(configure, index) in this.configures" v-bind:key="index">{{configure}}</option>
						</select>
						Select : {{selectedConfigure}}
					</div>						

					<div>
						<complexresource-group
							v-for="(resourceGroup, index) in this.complexResources"
							v-bind:key="index"
					  		v-bind:data="resourceGroup"
							v-on:selectResource="onSelectResource"
						/>
		
					</div>						

				</div>
			`
		});

		loc_loadAllConfigures();
		
		loc_loadAllResources();

	};

	var loc_loadAllConfigures = function(){
		var remoteRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(node_COMMONATRIBUTECONSTANT.BROWSECOMPLEXENTITYSERVLET_COMMAND_CONFIGURES, {}), undefined, {
			success : function(request, configures){
				loc_componentData.configures = configures;
			}
		});
		node_requestServiceProcessor.processRequest(remoteRequest);
	}

	var loc_loadAllResources = function(handlers, requestInfo){
		var remoteRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(node_COMMONATRIBUTECONSTANT.BROWSECOMPLEXENTITYSERVLET_COMMAND_RESOURCES, {}), undefined, {
			success : function(request, complexResources){
				loc_componentData.complexResources = complexResources;
			}
		});
		node_requestServiceProcessor.processRequest(remoteRequest);
	};

	var loc_out = {
		
	};
	
	loc_init();
	return loc_out;
};
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentManagementInterface", function(){node_getComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.getStateMachineDefinition", function(){node_getStateMachineDefinition = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getComplexEntityObjectInterface", function(){node_getComplexEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){  node_complexEntityUtility = this.getData();});

nosliw.registerSetNodeDataEvent("variable.context.createContextVariablesGroup", function(){  node_createContextVariablesGroup = this.getData();});
nosliw.registerSetNodeDataEvent("variable.context.createContextVariableInfo", function(){  node_createContextVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("variable.data.utility", function(){  node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.setting.createConfigures", function(){node_createConfigures = this.getData();});


nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoRemote", function(){node_createServiceRequestInfoRemote = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComplexResourceDebugView", node_createComplexResourceDebugView); 

})(packageObj);
