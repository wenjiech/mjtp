var commonDirectives = angular.module("myapp.common.directive", []);

commonDirectives.directive('breadcrumb',function(){
	 return {
		 restrict: 'E',
		 templateUrl:'views/directives/breadcrumb.html'
	 }
});
	
