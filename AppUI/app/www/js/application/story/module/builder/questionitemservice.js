/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoCommon;
	var node_requestServiceProcessor;
	var node_storyChangeUtility;
	var node_storyUtility;
	var node_designUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentQuestionItemService = function(availableService){

	var loc_loadServices = function(that){
		var request = node_createServiceRequestInfoSequence(undefined, {
			success : function(request, serviceDefs){
				var services = [];
				_.each(serviceDefs, function(serviceDef, i){
					var serviceStatic = serviceDef[node_COMMONATRIBUTECONSTANT.DEFINITIONSERVICE_STATIC];
					var displayResource = serviceStatic[node_COMMONATRIBUTECONSTANT.INFOSERVICEINTERFACE_DISPLAY];
					var displayName = displayResource!=undefined?displayResource[node_COMMONATRIBUTECONSTANT.ENTITYINFO_DISPLAYNAME]:undefined;
					if(displayName==undefined)  displayName = serviceStatic[node_COMMONATRIBUTECONSTANT.ENTITYINFO_DISPLAYNAME];   
					var service = {
						id : serviceStatic[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID], 
						name : serviceStatic[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME], 
						displayName : displayName, 
						description : serviceStatic[node_COMMONATRIBUTECONSTANT.ENTITYINFO_DESCRIPTION], 
						sinterface :  serviceStatic[node_COMMONATRIBUTECONSTANT.INFOSERVICESTATIC_INTERFACE],
					};
					services.push(service);
				});
				that.services = services;
				
				var service = loc_getCurrentServiceById(that, that.currentServiceId);
				if(service!=undefined)    that.currentService = service;
				else that.currentService = {};
			}
		});

		var gatewayId = node_COMMONATRIBUTECONSTANT.RUNTIME_GATEWAY_SERVICE;
		var command = node_COMMONATRIBUTECONSTANT.GATEWAYSERVICE_COMMAND_SEARCHDEFINITION;
		var parms = {};
		var query = {};
		query[node_COMMONATRIBUTECONSTANT.QUERYSERVICEDEFINITION_KEYWORDS] = ['demo'];
		parms[node_COMMONATRIBUTECONSTANT.GATEWAYSERVICE_COMMAND_SEARCHDEFINITION_QUERY] = query;
		var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms);
		request.addRequest(gatewayRequest);
		
		node_requestServiceProcessor.processRequest(request);
	};
	
	var loc_getCurrentServiceById = function(that, serviceId){
		for(var i in that.services){
			var service = that.services[i];
			if(service[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]==serviceId){
				return service;
			}
		}
	};
	
	var loc_vueComponent = {
		data : function(){
			return {
				services : [],
				currentServiceId : "",
				currentService : {}
			};
			return loc_services;
		},
		props : ['question', 'story'],
		components : {
		},
		methods : {
		},
		created : function(){
			loc_loadServices(this);
			var element = node_storyUtility.getQuestionTargetElement(this.story, this.question);
			this.currentServiceId = element[node_COMMONATRIBUTECONSTANT.STORYNODESERVICE_REFERENCEID];
		},
		computed: {
			selectServiceId : {
				get : function(){
					return this.currentServiceId;
				},
				
				set : function(id){
					this.currentServiceId = id;
					var service = loc_getCurrentServiceById(this, this.currentServiceId);
					if(service!=undefined){
						this.currentService = service;
						node_designUtility.applyPatchFromQuestion(this.story, this.question, node_COMMONATRIBUTECONSTANT.STORYNODESERVICE_REFERENCEID, this.currentService.id, this.question.answer);
						node_designUtility.applyPatchFromQuestion(this.story, this.question, node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME, this.currentService.name, this.question.answer);
						node_designUtility.applyPatchFromQuestion(this.story, this.question, node_COMMONATRIBUTECONSTANT.ENTITYINFO_DISPLAYNAME, this.currentService.displayName, this.question.answer);
						node_designUtility.applyPatchFromQuestion(this.story, this.question, node_COMMONATRIBUTECONSTANT.ENTITYINFO_DESCRIPTION, this.currentService.discription, this.question.answer);
						this.$emit("answerChange", this.currentService);
					}
					else{
						this.currentService = {};
					}
				}
			}
		},
		watch : {
		},

		template : `
			<div>
				{{question.question}}:<select style="display:inline;" v-model="selectServiceId" placeholder="Select service...">
				  <option v-for="service in services" v-bind:value="service.id">
				    {{ service.displayName }}
				  </option>
				</select>	

				<br>
				<br>
				<br>
				DataSource : {{currentService.displayName}}
				<br>
				Description : {{currentService.description}}
				<br>
				Inputs: 
				<br>
				<div v-if="currentService.sinterface!=undefined" style="margin-left:25px">
					<div v-for="parm in currentService.sinterface.request">
						name : {{parm.name}}
						<br>
						dataType : {{parm.dataInfo.criteria}}
						<br>
						defaultValue : {{parm.defaultValue}}
						<br>
						rules : 
						<div v-for="(rule, index) in parm.dataInfo.rule"  style="margin-left:25px">
							{{index+1}}: {{rule.ruleType}}
								<div style="margin-left:25px">
									<div v-if="rule.ruleType=='mandatory'"></div>
									<div v-if="rule.ruleType=='enum'"></div>
									<div v-if="rule.ruleType=='jsscript'">script: {{rule.script}}</div>
									<div v-if="rule.ruleType=='expression'">expression:{{rule.expression}}</div>
								</div>
						</div>
						<br>
					</div>
					
					
				</div>

				<br>
				<br>
				Outputs: 
				<br>
				<div v-if="currentService.sinterface!=undefined" style="margin-left:25px">
					<div v-for="parm in currentService.sinterface.result.success.output">
						name : {{parm.name}}
						<br>
						dataType : {{parm.dataInfo.criteria}}
						<br>
					</div>
				</div>

			</div>
		`
	};
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){	node_createServiceRequestInfoCommon = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyChangeUtility", function(){node_storyChangeUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.designUtility", function(){node_designUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentQuestionItemService", node_createComponentQuestionItemService); 

})(packageObj);
