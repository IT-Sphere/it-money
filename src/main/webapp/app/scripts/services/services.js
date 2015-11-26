/**
 * Этот скрипт содержит кастомные сервисы
 *
 * http://it-channel.ru/
 *
 * @author Budnikov Aleksandr
 */
var services = angular.module('ITMoney.services', ['ngResource', 'ITMoney.configs']);

services.factory('User', function ($http, Config, ErrorHandler) {
    return {
        get: function (id, successCallback) {
            return $http.post(Config.appName, {
                controller: "userController",
                action: 'getById',
                params: {id: id}
            }).success(function (response) {
                ErrorHandler(response, successCallback);
            });
        },

        delete: function (id, successCallback) {
            return $http.post(Config.appName, {
                controller: "userController",
                action: 'deleteById',
                params: {id: id}
            }).success(function (response) {
                ErrorHandler(response, successCallback);
            });
        },

        findUsersByQuery: function (query, successCallback) {
            return $http.post(Config.appName, {
                controller: "userController",
                action: 'findUsersByQuery',
                params: {query: query}
            }).success(function (response) {
                ErrorHandler(response, successCallback);
            });
        },

        save: function (user, successCallback) {
            return $http.post(Config.appName, {
                controller: "userController",
                action: 'save',
                params: user
            }).success(function (response) {
                ErrorHandler(response, successCallback);
            });
        },

        getAll: function (successCallback) {
            return $http.post(Config.appName, {controller: "userController", action: 'getAll'}).success(function (response) {
                ErrorHandler(response, successCallback);
            });
        },

        getCount: function (successCallback) {
            return $http.post(Config.appName, {
                controller: "userController",
                action: 'getCount'
            }).success(function (response) {
                ErrorHandler(response, successCallback);
            });
        }
    }
});

services.factory('ErrorHandler', function ($log) {
    return function (response, process) {
        if (response.status === "ok") {
            if (process) {
                process(response.result);
            }
            $log.debug('ResponseReceiver received: ' + response.result);
        } else if (response.status === "fail") {
            $log.error(response.error);
            alert(response.error);
        }
    };
});