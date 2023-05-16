{
	"info": {
		"name": "test data association",
		"description": ""
	},
	"entity": [
		{
			"embeded": {
				"info": {
					"name" : "adapter-none-serviceprovider",
					"status": "disabled1"
				},
				"entity" : {
					"serviceKey" : {
						"id" : "TestBasicService"
					}
				}
			},
			"adapter":[
				{
					"name" : "parent.child.data",
					"status": "disabled1",
					"entityType" : "dataAssociationInteractive",
					"entity" : {
						"in" : {
							"serviceParm1" : {
								"definition" : {
									"mapping" : {
										"elementPath": "parm1"
									}
								}
							}
						},
						"out" : {
							"success" : {
								"output1" : {
									"definition" : {
										"mapping" : {
											"elementPath": "outputInService1"
										}
									}
								}
							}
						}
					}
				}
			]
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
						"parm1": {
							"definition":{
								"criteria": "test.string;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "default value of parm1"
							}
						},
						"parm2": {
							"definition":{
								"criteria": "test.string;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "default value of parm2"
							}
						},
						"output1": {
							"definition":{
								"criteria": "test.string;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "default value of output1"
							}
						},
						"output2": {
							"definition":{
								"criteria": "test.string;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "default value of output2"
							}
						}
					}
				}
			]
		}
	]
}
