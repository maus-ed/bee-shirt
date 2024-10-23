angular.module("loginApp", ["ngRoute"]).config(function ($routeProvider) {
  $routeProvider
    .when("/admin/home", {
      templateUrl: "/page/admin/home.html",
      controller: "AdminController",
    })
    .when("/staff/home", {
      templateUrl: "/page/staff/home.html",
      controller: "StaffController",
    })
    .when("/user/home", {
      templateUrl: "/page/user/home.html",
      controller: "UserController",
    })
    .when("/login", {
      templateUrl: "/page/auth/login.html",
      controller: "LoginController",
    })
    .otherwise({
      redirectTo: "/login",
    });
});
