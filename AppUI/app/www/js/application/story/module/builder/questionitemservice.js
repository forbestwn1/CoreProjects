/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
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
	
	var loc_vueComponent = {
		data : function(){
			return {
				allService : loc_availableService
			};
		},
		props : ['question', 'story'],
		components : {
		},
		methods : {
		},
		computed: {
			serviceId : {
				get : function(){
					var element = node_storyUtility.getQuestionTargetElement(this.story, this.question);
					return element[node_COMMONATRIBUTECONSTANT.STORYNODESERVICE_REFERENCEID];
				},
				
				set : function(serviceId){
					node_designUtility.applyPatchFromQuestion(this.story, this.question, node_COMMONATRIBUTECONSTANT.STORYNODESERVICE_REFERENCEID, serviceId, this.question.answer);
					node_designUtility.applyPatchFromQuestion(this.story, this.question, node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME, serviceId, this.question.answer);
					this.$emit("answerChange", serviceId);
				}
			}
		},
		template : `
			<select style="display:inline;" v-model="serviceId" placeholder="Select service...">
			  <option v-for="service in allService" v-bind:value="service.id">
			    {{ service.name }}
			  </option>
			</select>	
		`
	};
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyChangeUtility", function(){node_storyChangeUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.designUtility", function(){node_designUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentQuestionItemService", node_createComponentQuestionItemService); 

})(packageObj);
