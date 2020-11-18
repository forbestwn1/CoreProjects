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

	var loc_availableService = availableService;
	
	loc_availableService = [
		{
			id : 'TestTemplateService1',
			name : 'TestTemplateService1'
		},
		{
			id : 'TestTemplateService2',
			name : 'TestTemplateService2'
		},
		{
			id : 'TestTemplateServiceSchool',
			name : 'TestTemplateServiceSchool'
		},
		{
			id : 'schoolService',
			name : 'schoolService'
		}
	];
	
	var loc_services = {
		data : [],
	};
	
	var loc_updateServices = function(){
		var request = node_createServiceRequestInfoSequence(undefined, {
			success : function(request, serviceDefs){
				var services = [];
				_.each(serviceDefs, function(serviceDef, i){
					var service = {
						id : serviceDef[node_COMMONATRIBUTECONSTANT.DEFINITIONSERVICE_STATIC][node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID], 
						name : serviceDef[node_COMMONATRIBUTECONSTANT.DEFINITIONSERVICE_STATIC][node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME], 
						description : serviceDef[node_COMMONATRIBUTECONSTANT.DEFINITIONSERVICE_STATIC][node_COMMONATRIBUTECONSTANT.ENTITYINFO_DESCRIPTION], 
					};
					services.push(service);
				});
				loc_services.data = services;
			}
		});

		var gatewayId = node_COMMONATRIBUTECONSTANT.RUNTIME_GATEWAY_SERVICE;
		var command = node_COMMONATRIBUTECONSTANT.GATEWAYSERVICE_COMMAND_SEARCHDEFINITION;
		var parms = {};
		var query = {};
		query[node_COMMONATRIBUTECONSTANT.QUERYSERVICEDEFINITION_KEYWORDS] = ['public'];
		parms[node_COMMONATRIBUTECONSTANT.GATEWAYSERVICE_COMMAND_SEARCHDEFINITION_QUERY] = query;
		var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms);
		request.addRequest(gatewayRequest);
		
		node_requestServiceProcessor.processRequest(request);
	};
	
	var loc_vueComponent = {
		data : function(){
			return loc_services;
		},
		props : ['question', 'story'],
		components : {
		},
		methods : {
		},
		mounted : function(){
			loc_updateServices();
		},
		computed: {
			currentService : {
				get : function(){
					var element = node_storyUtility.getQuestionTargetElement(this.story, this.question);
					var out = {
						id : element[node_COMMONATRIBUTECONSTANT.STORYNODESERVICE_REFERENCEID],
						name : element[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME],
						description : element[node_COMMONATRIBUTECONSTANT.ENTITYINFO_DESCRIPTION],
					};
					if(out.id==undefined)   out.name = undefined;
					return out;
				},
				
				set : function(service){
					node_designUtility.applyPatchFromQuestion(this.story, this.question, node_COMMONATRIBUTECONSTANT.STORYNODESERVICE_REFERENCEID, service.id, this.question.answer);
					node_designUtility.applyPatchFromQuestion(this.story, this.question, node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME, service.name, this.question.answer);
					node_designUtility.applyPatchFromQuestion(this.story, this.question, node_COMMONATRIBUTECONSTANT.ENTITYINFO_DESCRIPTION, service.description, this.question.answer);
					this.$emit("answerChange", service);
				}
			}
		},
		template : `
			<div>
				{{currentService.name}}
				<br>
				{{currentService.description}}
				<br>
				
				<select style="display:inline;" v-model="currentService" placeholder="Select service...">
				  <option v-for="service in data" v-bind:value="service">
				    {{ service.name }}
				  </option>
				</select>	
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
