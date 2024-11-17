var app = angular.module('SizeApp', []);
        app.service('sizeService', ['$http', function($http) {
            this.getSizes = function(page) {
                return $http.get(`http://localhost:8080/api/sizes/list?page=${page}`);
            };

            this.getSizeDetail = function(codeSize) {
                return $http.get(`http://localhost:8080/api/sizes/detail/${codeSize}`);
            };

            this.addSize = function(size) {
                return $http.post('http://localhost:8080/api/sizes/add', size);
            };

            this.updateSize = function(codeSize, size) {
                return $http.put(`http://localhost:8080/api/sizes/update/${codeSize}`, size);
            };

            this.deleteSize = function(codeSize) {
                return $http.put(`http://localhost:8080/api/sizes/delete/${codeSize}`);
            };
        }]);

        app.controller('sizeController', ['$scope', 'sizeService', function($scope, sizeService) {
            $scope.sizes = [];
            $scope.currentPage = 0;
            $scope.totalPages = 0;
            $scope.pages = [];
            $scope.size = {};
            $scope.newSize = {};
            $scope.confirmDelete = false;
            $scope.sizeToDelete = null;

            $scope.getSizes = function(page) {
                sizeService.getSizes(page).then(function(response) {
                    $scope.sizes = response.data.content.map(function(item) {
                        return {
                            codeSize: item[0],
                            namesize: item[1],
                            statussize: item[2]
                        };
                    });
                    $scope.totalPages = response.data.totalPages;
                    $scope.pages = new Array($scope.totalPages);
                });
            };
             // Chuyển đến trang mới
            $scope.goToPage = function(page) {
                if (page >= 0 && page < $scope.totalPages) {
                    $scope.currentPage = page;
                    $scope.getSizes($scope.currentPage);
                }
            };

            $scope.viewSizeDetail = function(codeSize) {
                sizeService.getSizeDetail(codeSize).then(function(response) {
                    $scope.size = response.data;
                    $('#viewSizeDetailModal').modal('show');
                });
            };

            $scope.openAddSizeModal = function() {
                $scope.newSize = {};
            };

            $scope.saveNewSize = function() {
                sizeService.addSize($scope.newSize).then(function(response) {
                    $scope.getSizes($scope.currentPage);
                    $('#addSizeModal').modal('hide');
                });
            };

            $scope.editSize = function(size) {
                $scope.size = angular.copy(size);
                $('#editSizeModal').modal('show');
            };

            $scope.saveEditSize = function() {
                sizeService.updateSize($scope.size.codeSize, $scope.size).then(function(response) {
                    $scope.getSizes($scope.currentPage);
                    $('#editSizeModal').modal('hide');
                });
            };

            $scope.deleteSize = function(codeSize) {
                $scope.sizeToDelete = codeSize;
                $('#confirmDeleteModal').modal('show');
            };

            $scope.confirmDeleteSize = function() {
                sizeService.deleteSize($scope.sizeToDelete).then(function(response) {
                    $scope.getSizes($scope.currentPage);
                    $('#confirmDeleteModal').modal('hide');
                });
            };

            $scope.getSizes($scope.currentPage);
        }]);