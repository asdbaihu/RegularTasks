angular.module('infocomApp').factory('BashService', ['$http', '$q', function($http, $q){

var REST_SERVICE_URI = 'http://localhost:8090/infocom/bashes/';
var CHANGE_EMAIL_SETTINGS = 'http://localhost:8090/infocom/emailsettings/'

var factory = {
    showAllBash: showAllBash,
    createBash: createBash,
    updateBash: updateBash,
    deleteBash: deleteBash,
    changeApi: changeApi,
    makeEmailChanges: makeEmailChanges,
    showEmailSettings: showEmailSettings
};

return factory;

    function showEmailSettings(){
     var deferred = $q.defer();
        $http.get(CHANGE_EMAIL_SETTINGS)
            .then(
            function(response){
                deferred.resolve(response.data);
                },
            function(errResponse){
                alert('Error finding Email Settings');
                deferred.reject(errResponse);
            }
            );
        return deferred.promise;
    }

        function makeEmailChanges(emailsettings){
        var deferred = $q.defer();
        $http.post(CHANGE_EMAIL_SETTINGS, emailsettings)
        .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while update EmailSettings');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
        }

    function showAllBash(page, size){
    var deferred = $q.defer();
    $http.get(REST_SERVICE_URI, {params: {page: page,
                                          size: size}
                                          })
        .then(
        function(response){
            deferred.resolve(response.data);
            },
        function(errResponse){
            alert('Error finding Bash-scripts');
            deferred.reject(errResponse);
        }
        );
    return deferred.promise;
    }

    function changeApi(task){
    if (task == 0) {
        REST_SERVICE_URI = 'http://localhost:8090/infocom/bashes/';
    } else if (task == 1) {
        REST_SERVICE_URI = 'http://localhost:8090/infocom/exel/';
    } else {
        REST_SERVICE_URI = 'http://localhost:8090/infocom/email/';
        }
    }

    function createBash(bashscript) {
        var deferred = $q.defer();
        $http.post(REST_SERVICE_URI, bashscript)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while creating Bash-script');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }

   function updateBash(bash, id) {
        var deferred = $q.defer();
        $http.put(REST_SERVICE_URI+id, bash)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while Bash-script');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }

     function deleteBash(id) {
            var deferred = $q.defer();
            $http.delete(REST_SERVICE_URI+id)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while deleting Bash-script');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }



}]);