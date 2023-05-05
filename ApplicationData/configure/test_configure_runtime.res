{
	"nosliw_decoration1" :{
		"parts" : [
			{
				"parts" : {
					"nosliw_info" : {
						"name" : "firstDec",
						"type" : "test.decoration.1",
						"id": "decoration1",
						"description": "this is for testing decoration"
					},
					"nosliw_core" : {
						"nosliw_debug" : "true",
						"configureDec1" : "configureDec1Value",
						"configureDec2" : ["configureDec21Value", "configureDec22Value", "configureDec23Value"]
					}
				}
			}
		]
	},
	"nosliw_core" : {
		"a-none-test_complex_script" : {
			"nosliw_core" : {
				"a_configure" : {
					"aa" : "aa value",
					"ab" : "ab value"
				}
			}
		},
		"b-none-test_complex_1" : {
			"nosliw_core" : {
				"ba_none_test_complex_script" : {
					"nosliw_core" : {
						"baa" : "baa value",
						"bab" : "bab value"
					}
				}
			}
		},
		"nosliw_debug_package" : "true",
		"nosliw_debug" : "true",
		"configure2" : ["configure21Value", "configure22Value", "configure23Value"],
		"test_none_dataexpressiongroup" : {
			"nosliw_debug_package" : "true",
			"nosliw_debug" : "true",
			"nosliw_decoration" : {
				
			
			}
		}
	}
}
