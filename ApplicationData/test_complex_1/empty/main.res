{
	"info": {
		"name": "test data association",
		"description": ""
	},
	"entity": [
		{
			"info": {
				"name" : "attachment", 
				"status": "disabled1"
			},
			"entity": {
				"value" : [
					{
						"info": {
							"name" : "rootconstant", 
							"status": "disabled1"
						},
						"entity": "HelloConstant"
					}
				]
			}
		},
		{
			"info": {
				"name": "valueContext",
				"status": "disabled1"
			},
			"entity": [
				{
					"groupType" : "public",
					"valueStructure" : {
						"<%=&(rootconstant)&%>": {
							"definition":{
								"criteria": "test.string;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "default value of parent_public"
							}
						}
					}
				}
			]
		}
	]
}
