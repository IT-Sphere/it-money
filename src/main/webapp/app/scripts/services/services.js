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
                controller: "USER",
                action: 'GET_BY_ID',
                params: {id: id}
            }).success(function (response) {
                ErrorHandler(response, successCallback);
            });
        },

        delete: function (id, successCallback) {
            return $http.post(Config.appName, {
                controller: "USER",
                action: 'DELETE_BY_ID',
                params: {id: id}
            }).success(function (response) {
                ErrorHandler(response, successCallback);
            });
        },

        save: function (user, successCallback) {
            return $http.post(Config.appName, {
                controller: "USER",
                action: 'SAVE',
                params: user
            }).success(function (response) {
                ErrorHandler(response, successCallback);
            });
        },

        getAll: function (successCallback) {
            return $http.post(Config.appName, {controller: "USER", action: 'GET_ALL'}).success(function (response) {
                ErrorHandler(response, successCallback);
            });
        },

        getCount: function (successCallback) {
            return $http.post(Config.appName, {
                controller: "USER",
                action: 'GET_COUNT'
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