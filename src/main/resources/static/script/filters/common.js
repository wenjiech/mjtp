var commonFilters = angular.module("myapp.common.filters", []);

commonFilters.filter('amountFilter', function(){
	return function(input){
		if (input == null)
			return "-";
		return input;
	}
	
});
