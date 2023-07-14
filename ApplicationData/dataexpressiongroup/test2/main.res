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
						"baseVarNormal": {
							"definition":{
								"criteria": "test.string;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "default value of baseVarNormal"
							}
						}
					}
				}
			]
		},
		"element": [
			{
				"name" : "reference",
				"status": "disabled1",
				"expression" : "<(dataexpressionsingle|#local1)>.with(localBase:?(baseVarNormal)?)"
			}
		]
	}
}
