{
	"definition" : [
		{
			"name" : "task1",
			"requirement" : [
				{
					"interface" : "interface1"
				},
				{
					"interface" : "interface2"
				}
			]
		}
	
	],
	"script" : {
		task1 : function(event, info, env){
		},
	
		tagEventHandler : function(event, info, env){
			console.log(JSON.stringify(arguments));
		},
	}
}
