{
	"name": "internal1",
	"description": "test1",
	"context": {
		"element": {
			"bbb":{
				definition : {
					"criteria": "test.string;1.0.0",
				},
				"defaultValue": {
					"dataTypeId": "test.string;1.0.0",
					"value": "This is my world!"
				}
			},
		}
	},
	"attachment": {
	},	
	"element": [{
			"id": "expressionLocal",
			"name": "test1",
			"description": "standard",
			"element" : [
				{
					"expression" : "?(bbb)?",
				}
			]
		},
	]
}
