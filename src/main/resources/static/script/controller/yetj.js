
'use strict';

(function(myApp){
    //应用渠道管理
	myApp.controller('yetjListCtrl',['$scope', '$state',function($scope, $state){
		console.log("1111111111yetjListCtrl");
		$scope.title1="mjtp1111";
		$scope.querytitle="querytitle11111";
		
		$scope.option_years=[2015,2016,2017,2018,2019];
		$scope.option_months=[{"show":"一月","value":"1"},
		                      {"show":"二月","value":"2"},
		                      {"show":"三月","value":"3"},
		                      {"show":"四月","value":"4"},
		                      {"show":"五月","value":"5"},
		                      {"show":"六月","value":"6"},
		                      {"show":"七月","value":"7"},
		                      {"show":"八月","value":"8"},
		                      {"show":"九月","value":"9"},
		                      {"show":"十月","value":"10"},
		                      {"show":"十一月","value":"11"},
		                      {"show":"十二月","value":"12"}];
	}]);

}(myApp));
