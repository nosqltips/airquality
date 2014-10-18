'use strict';


// Declare app level module which depends on filters, and services
angular.module('airQualityApp', [
    'ngRoute',
    'airQualityApp.filters',
    'airQualityApp.services',
    'airQualityApp.directives',
    'airQualityApp.controllers',
    'ngResource',
    'ngSanitize',
    'ngTable',
    'n3-line-chart',
    'angularSpinner',
    'ui.bootstrap'
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/main', {templateUrl: 'partials/main.html', controller: 'TweetCtrl'});
  $routeProvider.otherwise({redirectTo: '/main'});
}]);
