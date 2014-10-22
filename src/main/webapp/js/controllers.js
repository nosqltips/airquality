'use strict';

/* Controllers
 * created in: March, 2014
 * author: Zepeng Zhao
 * */
angular.module('airQualityApp.controllers', []).
    controller('TweetCtrl', function($scope, $log, ngTableParams, usSpinnerService, searchFactory, countFactory, mltFactory, deleteFactory, facetFactory) {
        // create a map in the "map" div, set the view to a given place and zoom
        var map = L.map('map',{center:[39.5015, -111.555], zoom:2, zoomControl:true});
        // add an OpenStreetMap tile layer
        L.tileLayer('http://otile1.mqcdn.com/tiles/1.0.0/map/{z}/{x}/{y}.jpg', {
            attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        }).addTo(map);

        $scope.error = null;

        // Table parameters
        if (! $scope.tableParams) {
            $scope.data = [];
            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 25
            }, {
                // Bug: total doesn't seem to update after first setting
                total: 0,
                getData: function($defer, params) {
                    params.total($scope.data.length);
                    $defer.resolve($scope.data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                }
            });                     
        }
        
        // Line chart
        $scope.tweetsByHourOptions = {
          axes: {
            x: {key: 'time', 
                labelFunction: function(value) {return moment(value).fromNow()}, 
                type: 'linear'},
            y: {type: 'linear'}
          },
          series: [
            {y: 'count', 
                color: 'steelblue', 
                thickness: '2px', 
                type: 'area', 
                striped: true,
                label: 'Tweets per hour'}
          ],
          lineMode: 'cardinal',
          tension: 0.9,
          tooltip: {mode: 'scrubber', formatter: function(x, y, series) {return moment(x).fromNow() + ' : ' + y;}},
          drawLegend: true,
          drawDots: true,
          columnsHGap: 10
        }

        $scope.mo = function(geo) {
            if ($scope.circle !== undefined) {
                map.removeLayer($scope.circle);
            }
            map.setView(geo, 15);
            var Icon = L.icon({
                iconUrl: 'images/leaf-green.png',
                iconSize:     [38, 95], // size of the icon
                iconAnchor:   [22, 94], // point of the icon which will correspond to marker's location
                popupAnchor:  [-3, -76] // point from which the popup should open relative to the iconAnchor
            });
            $scope.circle = L.marker(geo,{icon:Icon});
            map.addLayer($scope.circle);
        };

        $scope.countResult = countFactory.count();
        $scope.searchOption = ["Tweet", "Tweeter"];
        $scope.duration = ["Day", "Week", "Month", "Year"];
        $scope.states = ["Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", 
            "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", 
            "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", 
            "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia",
            "Wisconsin", "Wyoming", "American Samoa", "District of Columbia", "Guam", "Puerto Rico", "Virgin Islands"];
        
        $scope.removeDoc = function(docId) {
            deleteFactory.delete(docId).get().$promise.then(function(result) {
                $scope.updateData(result);
            });
        };

        $scope.getMlt = function(id) {
            usSpinnerService.spin('mainSpinner');
            $scope.docid = id;
            mltFactory.searchMlt(id).get().$promise.then(function(result) {
                $scope.updateData(result);
                usSpinnerService.stop('mainSpinner');
            });
        };

        $scope.searchUser = function(screenName) {
            $scope.terms = "@" + screenName;
            $scope.search($scope.terms, null, null, null);
        }
        $scope.searchOther = function(value) {
            $scope.terms = value;
            $scope.search($scope.terms, null, null, null);
        }
        $scope.searchPlace = function(place) {
            $scope.place = place;
            $scope.terms = place;
            $scope.search(null, $scope.place, null, null);
        }
        $scope.search = function(terms, place, startDate, endDate) {
            // This is to get around a curious Angular request function
            if (terms !== undefined && terms !== null) {
                terms = terms.replace("@", "#");
            }
//            $log.log("terms " + terms);
//            $log.log("place " + place);
            usSpinnerService.spin('mainSpinner');
            searchFactory.search(terms, place, startDate, endDate, $scope.sentiment).get().$promise.then(function(result) {
                $scope.updateData(result);
                usSpinnerService.stop('mainSpinner');
            });
        };        
        
        // ***************************
        // Related functions
        // ***************************
        $scope.updateData = function(result) {
            $scope.query = result["query"];
            var data = result["results"];
            // If no results, then use the previous results
            if (data) {
                $scope.data = data;
                $scope.error = null;
            } else {
                $scope.error = "No results available. Showing previous results."
            }
            $scope.facetRequests = result["requests"];
            $scope.updateFacets();
            $scope.updateTable();
            $scope.updateMap();
        }
        
        $scope.updateTable = function() {
            $scope.tableParams.data = $scope.data;
            $scope.tableParams.reload();
        }
        
        $scope.updateFacets = function() {
            for (var i=0; i < $scope.facetRequests.length; i++) {
                if ($scope.facetRequests[i].field === "TWEETSBYHOUR") {
                    $scope.tweetsByHour = $scope.facetRequests[i].selectables;
                }
            }
        }

        $scope.resetMap = function() {
            map.setView([39.5015, -111.555], 2);
            if ($scope.circle !== undefined) {
                map.removeLayer($scope.circle);
            }
            if ($scope.layerGroup !== undefined) {
                map.removeLayer($scope.layerGroup);
            }
        }
        
        $scope.updateMap = function() {
            $scope.resetMap();
            var markerList =[];
            $scope.layerGroup = new L.MarkerClusterGroup({maxClusterRadius:30});
            for (var j = 0;j < $scope.data.length; j++) {
                var temp = $scope.data[j]["location"]["geo"];
                if (temp !== null) {
                    var m = new L.Marker(temp);
                    m.data = $scope.data[j];
                    markerList.push(m);
                }
            }
            $scope.layerGroup.addLayers(markerList);
            $scope.layerGroup.on('clusterclick', function (e) {
                $scope.clusterTweets(e.layer.getAllChildMarkers());
            }, $scope.layerGroup);
            $scope.layerGroup.on('click',function(e) {
                $scope.data=[e.layer.data];
                $scope.updateTable();
            });
            map.addLayer($scope.layerGroup);
        }
        
        $scope.clusterTweets = function(children) {
            var contents = [];
            for (var i = 0; i < children.length; i++) {
                contents.push(children[i].data);
            }
            $scope.data = contents;
            $scope.tableParams.data = $scope.data;
            $scope.tableParams.reload();
        };

        // Date functions
        $scope.today = function () {
            $scope.startDate = new Date();
            $scope.endDate = new Date();
        };
        $scope.minDate = "10-01-2014";
        $scope.maxDate = new Date();

        $scope.toggleMin = function () {
            $scope.minDate = $scope.minDate ? null : new Date();
        };
        $scope.toggleMin();

        // Start Date
        $scope.sClear = function () {
            $scope.startDate = null;
        };
        $scope.sOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.sOpened = true;
        };

        // End Date
        $scope.eClear = function () {
            $scope.endDate = null;
        };
        $scope.eOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.eOpened = true;
        };

        // Date Options
        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };

        $scope.formats = ['MM-dd-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
        $scope.format = $scope.formats[0];

        // Sentiment
        $scope.sentiment = false;
        $scope.sentimentText = "Sentiment OFF";
        $scope.toggleSentiment = function(terms, opt, place, startDate, endDate) {
            if ($scope.sentiment) {
                $scope.sentiment = false;
                $scope.sentimentText = "Sentiment OFF";
            } else {
                $scope.sentiment = true;
                $scope.sentimentText = "Sentiment ON";
            }
            
            $scope.search(terms, opt, place, startDate, endDate, $scope.sentiment);
        }
        
        // Our starting point
        window.setTimeout(
            function() {
                $scope.search();
            }, 100
        );
    }).

    factory('searchFactory', function($resource) {
        return {
            search: function(terms, place, startDate, endDate, sentiment) {
                var result = $resource('api/:verb',
                        {verb: 'search', terms: terms, place: place, startDate: startDate, endDate: endDate, sentiment: sentiment, pageSize: 500},
                        {action: {method: 'get'}});
                return result;
            }
        };
    }).
    factory('mltFactory', function($resource) {
        return {
            searchMlt: function(id) {
                var result = $resource('api/:verb',
                        {verb: 'mlt', docId: id},
                        {action: {method: 'get'}});
                return result;
            }
        };
    }).
    factory('countFactory', function($resource) {
        return {
            count: function() {
                var result = $resource('api/:verb',
                        {verb: 'count'},
                        {action: {method: 'get'}});
                return result.get();
            }
        };
    }).
    factory('facetFactory', function($resource) {
        return {
            facet: function(term) {
                var result = $resource('api/facet/:verb',
                        {verb: term},
                        {action: {method: 'get', isArray: true}});
                return result;
            }
        };
    }).
    factory('deleteFactory', function($resource) {
        return {
            delete: function(id) {
                var result = $resource('api/:verb',
                        {verb: 'delete', docId: id},
                        {action: {method: 'get'}});
                return result;
            }
        };
    }).
    directive('onEnter', function() {
        return function(scope, element, attrs) {
            element.bind("keydown keypress", function(event) {
                if (event.which === 13) {
                    scope.$apply(function() {
                        scope.$eval(attrs.onEnter);
                    });
                    event.preventDefault();
                }
            });
        };
    });
        