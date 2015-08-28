var app = angular.module('shortLink', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'googlechart'
]);

app.config(function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'views/createShort.html',
        controller: 'CreateShort'
    }).when('/urlStatistics/:param1', {
        templateUrl: 'views/urlStatistics.html',
        controller: 'UrlStatisticsController'
    }).when('/topSites', {
        templateUrl: 'views/topSites.html',
        controller: 'TopSitesController'
    }).otherwise({
        templateUrl: 'views/long.html',
        controller: 'longUrl'
    })
});

app.controller('CreateShort', function ($scope, $http, $location, $cookieStore) {
    $scope.createUrl = function () {
            if(checkLongUrl($scope.URL.longURL)) {
                if (!$scope.URL.customURL) {
                        $http.post('/api/v1/short', $scope.URL.longURL).success(function (data) {
                            var short = data['shortURL'];
                            short = short.replace(/\"/g, "");
                            short = 'http://' + short;
                            $scope.URL.short = short;
                            $scope.URL.clicks = data['click'];
                            document.getElementById("buttonStatistics").style.visibility='visible';
                            document.getElementById("url-card").style.visibility='visible';
                            $scope.requestStatistics = '/#/urlStatistics/' + $scope.URL.longURL;
                            //$cookieStore.put("longUrl", data['longURL']);
                        })
                } else {
                    if(isABadWord($scope.URL.customURL)) {
                        Materialize.toast('Custom url not allowed due to bad words, please check it.', 5000);
                    } else {
                        $http.post('/api/v1/shortCustom', $scope.URL).success(function (data) {
                            if (data == null) {
                                Materialize.toast('Word not available, try again', 5000);
                            } else {
                                var short = $scope.URL.customURL;
                                short = 'http://localhost:8080/#/' + short;
                                $scope.URL.short = short;
                                $scope.URL.clicks = data['click'];
                                document.getElementById("buttonStatistics").style.visibility='visible';
                                document.getElementById("url-card").style.visibility='visible';
                                //$cookies.put('longUrl', data['longURL']);
                                $scope.requestStatistics = '/#/urlStatistics/' + $scope.URL.longURL;
                            }
                        })
                }
            }
        } else {
                Materialize.toast('Url not allowed, check it please.', 5000);
        }
    }

});

app.controller('UrlStatisticsController', function ($scope, $http, $routeParams, $location) {
 var param1 = $routeParams.param1;
    var path = $location.path();

    $http.get('/api/v1/url_statistics/',{params:{"param1": param1}}).success(function(data) {
        $scope.total_clicks = data['click'];
        $scope.shortURL = data['shortURL'];
        $scope.longURL = data['longURL'];
        console.log($scope.longURL);
        $scope.geoChart = geoChart(data['statistichePaesi']);
        if(data['click'] == 0) {
            $scope.browserChart = barChart(null,'Browser', '');
            $scope.platformChart = barChart(null,'Platform','');
        } else {
            $scope.browserChart = barChart(data['statisticheBrowser'],'Browser', 'Clicks');
            $scope.platformChart = barChart(data['statisticheOS'],'Platform','Clicks');
        }
    });


});

app.controller('TopSitesController', function($scope, $http) {

    $http.post('/api/v1/top_sites','').success(function(data) {
        console.log(data);
    });

});


app.controller('longUrl', function ($scope, $http, $location) {

    $http.post('/api/v1/risultato', $location.absUrl()).success(function (data) {
        data = data.replace(/\"/g, "");
        if(data.toString().indexOf("http://") > -1 || data.toString().indexOf("https://") > -1) {
            location.href =  data;
        } else {
            location.href = 'http://' + data;
        }
    })

});
