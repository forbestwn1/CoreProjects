{
	"info": {
		"name": "test.expression",
		"description": "test expression group entity"
	},
	"entity": {
		"valueContext" : {
			"entity": [
				{
					"groupType" : "public",
					"valueStructure" : {
						"localBase": {
							"definition":{
								"criteria": "test.string;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "default value of localBase"
							}
						}
					}
				}
			]
		},
		"expression" : "?(localBase)?"
	}
}
