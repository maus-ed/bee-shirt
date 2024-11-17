var app = angular.module('CategoryApp', []);
app.service('categoryService', ['$http', function($http) {
    this.getCategories = function(page) {
        return $http.get(`http://localhost:8080/api/categories/list?page=${page}`);
    };

    this.getCategoryDetail = function(codeCategory) {
        return $http.get(`http://localhost:8080/api/categories/detail/${codeCategory}`);
    };

    this.addCategory = function(category) {
        return $http.post('http://localhost:8080/api/categories/add', category);
    };

    this.updateCategory = function(codeCategory, category) {
        return $http.put(`http://localhost:8080/api/categories/update/${codeCategory}`, category);
    };

    this.deleteCategory = function(codeCategory) {
        return $http.put(`http://localhost:8080/api/categories/delete/${codeCategory}`);
    };
}]);

app.controller('categoryController', ['$scope', 'categoryService', function($scope, categoryService) {
    $scope.categories = [];
    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.pages = [];
    $scope.category = {};
    $scope.newCategory = {};
    $scope.confirmDelete = false;
    $scope.categoryToDelete = null;

    $scope.getCategories = function(page) {
        categoryService.getCategories(page).then(function(response) {
            $scope.categories = response.data.content.map(function(item) {
                return {
                    codeCategory: item[0],
                    nameCategory: item[1],
                    statusCategory: item[2]
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
            $scope.getCategories($scope.currentPage);
        }
    };

    $scope.viewCategoryDetail = function(codeCategory) {
        categoryService.getCategoryDetail(codeCategory).then(function(response) {
            $scope.category = response.data;
            $('#viewCategoryDetailModal').modal('show');
        });
    };

    // Close modal function
    $scope.closeModal = function(modalId) {
        $(`#${modalId}`).modal('hide');
    };

    $scope.openAddCategoryModal = function() {
        $scope.newCategory = {};
    };

    $scope.saveNewCategory = function() {
        categoryService.addCategory($scope.newCategory).then(function(response) {
            $scope.getCategories($scope.currentPage);
            $('#addCategoryModal').modal('hide');
            location.reload(); 
        });
    };

    $scope.editCategory = function(category) {
        $scope.category = angular.copy(category);
        $('#editCategoryModal').modal('show');
    };

    $scope.saveEditCategory = function() {
        categoryService.updateCategory($scope.category.codeCategory, $scope.category).then(function(response) {
            $scope.getCategories($scope.currentPage);
            $('#editCategoryModal').modal('hide');
        });
    };

    $scope.deleteCategory = function(codeCategory) {
        $scope.categoryToDelete = codeCategory;
        $('#confirmDeleteModal').modal('show');
    };

    $scope.confirmDeleteCategory = function() {
        categoryService.deleteCategory($scope.categoryToDelete).then(function(response) {
            $scope.getCategories($scope.currentPage);
            $('#confirmDeleteModal').modal('hide');
        });
    };

    $scope.getCategories($scope.currentPage);
}]);
