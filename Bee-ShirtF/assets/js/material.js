 
var app = angular.module('MaterialApp', []);
app.service('materialService', ['$http', function($http) {
    this.getMaterials = function(page) {
        return $http.get(`http://localhost:8080/api/materials/list?page=${page}`);
    };

    this.getMaterialDetail = function(codeMaterial) {
        return $http.get(`http://localhost:8080/api/materials/detail/${codeMaterial}`);
    };

    this.addMaterial = function(material) {
        return $http.post('http://localhost:8080/api/materials/add', material);
    };

    this.updateMaterial = function(codeMaterial, material) {
        return $http.put(`http://localhost:8080/api/materials/update/${codeMaterial}`, material);
    };

    this.deleteMaterial = function(codeMaterial) {
        return $http.put(`http://localhost:8080/api/materials/delete/${codeMaterial}`);
    };
}]);

app.controller('materialController', ['$scope', 'materialService', function($scope, materialService) {
    $scope.materials = [];
    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.pages = [];
    $scope.material = {};
    $scope.newMaterial = {};
    $scope.confirmDelete = false;
    $scope.materialToDelete = null;

    $scope.getMaterials = function(page) {
        materialService.getMaterials(page).then(function(response) {
            $scope.materials = response.data.content.map(function(item) {
                return {
                    codeMaterial: item[0],
                    nameMaterial: item[1],
                    statusMaterial: item[2]
                };
            });
            $scope.totalPages = response.data.totalPages;
            $scope.pages = new Array($scope.totalPages);
        });
    };
    
    // Go to the next/previous page
    $scope.goToPage = function(page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.currentPage = page;
            $scope.getMaterials($scope.currentPage);
        }
    };

    $scope.viewMaterialDetail = function(codeMaterial) {
        materialService.getMaterialDetail(codeMaterial).then(function(response) {
            $scope.material = response.data;
            $('#viewMaterialDetailModal').modal('show');
        });
    };

    // Close modal function
    $scope.closeModal = function(modalId) {
        $(`#${modalId}`).modal('hide');
    };

    $scope.openAddMaterialModal = function() {
        $scope.newMaterial = {};
    };

    $scope.saveNewMaterial = function() {
        materialService.addMaterial($scope.newMaterial).then(function(response) {
            $scope.getMaterials($scope.currentPage);
            $('#addMaterialModal').modal('hide');
            location.reload(); 
        });
    };

    $scope.editMaterial = function(material) {
        $scope.material = angular.copy(material);
        $('#editMaterialModal').modal('show');
    };

    $scope.saveEditMaterial = function() {
        materialService.updateMaterial($scope.material.codeMaterial, $scope.material).then(function(response) {
            $scope.getMaterials($scope.currentPage);
            $('#editMaterialModal').modal('hide');
        });
    };

    $scope.deleteMaterial = function(codeMaterial) {
        $scope.materialToDelete = codeMaterial;
        $('#confirmDeleteModal').modal('show');
    };

    $scope.confirmDeleteMaterial = function() {
        materialService.deleteMaterial($scope.materialToDelete).then(function(response) {
            $scope.getMaterials($scope.currentPage);
            $('#confirmDeleteModal').modal('hide');
        });
    };

    $scope.getMaterials($scope.currentPage);
}]);