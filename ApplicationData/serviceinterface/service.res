[{
	"static": {
		"id": "schoolService",
		"name": "TestTemplateServiceSchool",
		"displayName": "Element school ranking list",
		"description" : "all element school rank in ontario",
		"tag" : ["demo", "pubic"],
		"interface" : {
			"parm" : [
				{
					"name" : "schoolTypeInService",
					"displayName" : "School Type",
					"description" : "The type of school, public, private, ...",
					"dataInfo" : {
						"criteria": "test.string;1.0.0",
						"rule" : [
							{
								"ruleType" : "enum",
								"enumCode" : "schooltype"
							}
						]
					},
					"defaultValue" :{
						"dataTypeId": "test.string;1.0.0",
						"value": "Public"
					}
				},
				{
					"name" : "schoolRatingInService",
					"displayName" : "School Rating",
					"description" : "The rating of school, 0--10",
					"dataInfo" : {
						"criteria": "test.float;1.0.0",
						"rule" : [
							{
								"ruleType" : "mandatory",
								"description" : "Cannot be blank!!!"
							},
							{
								"ruleType" : "enum",
								"dataSet" : [
									{
										"dataTypeId": "test.float;1.0.0",
										"value": 8
									},
									{
										"dataTypeId": "test.float;1.0.0",
										"value": 8.5
									},
									{
										"dataTypeId": "test.float;1.0.0",
										"value": 9
									},
									{
										"dataTypeId": "test.float;1.0.0",
										"value": 10
									}
								]
							}
						]
					},
					"defaultValue" :{
						"dataTypeId": "test.float;1.0.0",
						"value": 9
					}
				}
			],
			"result" : {
				"success" : {
					"output" : [
						{
							"name" : "outputInService",
							"displayName": "All Schools",
							"dataInfo" : {
								"criteria" : "test.array;1.0.0%%||element:test.map;1.0.0%%||schoolName:test.string;1.0.0,schoolRating:test.float;1.0.0,geo:test.geo;1.0.0||%%||%%"
							}
						}
					]
				}
			}
		},
		"display" : {
			"displayName" : "Element school ranking list English",
			"description" : "all element school rank in ontario",
			"interface" : {
				"parm" : {
					"schoolTypeInService" : {
						"displayName" : "School Type"
					},
					"schoolRatingInService" : {
						"displayName" : "School Rating"
					}
				},
				"result" : {
					"success" : {
						"output": {
							"outputInService" : {
								"displayName" : "a list of element schools",
								"children" : {
									"element" : {
										"displayName": "School Info",
										"children":	{
											"schoolName" : {
												"displayName": "School Name"
											},
											"schoolRate" : {
												"displayName": "School Rate"
											}
										}											
									}
								}
							}
						}
					}
				}
			}
		}
	},
	"runtime" : {
	    "implementation" : "com.nosliw.service.test.template.school.HAPServiceImp"
	}
}]
