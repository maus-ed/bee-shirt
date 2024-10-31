// Định nghĩa module và cấu hình router
angular
  .module("loginApp", ["ngRoute"]) // Thêm ngRoute vào module
  .config(function ($routeProvider) {
    $routeProvider
      .when("/admin/admin", {
        templateUrl: "admin/admin.html",
        controller: "AdminController",
      })
      .when("/staff/staff", {
        templateUrl: "staff/staff.html",
        controller: "StaffController",
      })
      .when("/user/home", {
        templateUrl: "user/home.html",
        controller: "UserController",
      })
      .otherwise({
        redirectTo: "/login", // Redirect về trang đăng nhập nếu không khớp
      });
  })
  .controller("loginController", function ($scope, $http) {
    $scope.user = {
      username: "",
      password: "",
    };
    $scope.rememberMe = false; // Mặc định không chọn "Remember Me"
    $scope.errorMessage = "";

    // Hàm đăng nhập
    $scope.login = function () {
      $http
        .post("http://localhost:8080/auth/login", $scope.user)
        .then(function (response) {
          console.log("Response nhận được từ server:", response);

          // Kiểm tra response.data có trường 'token' không
          const token = response.data.result.token;
          console.log("Token nhận được:", token);

          if (!token || token.split(".").length !== 3) {
            $scope.errorMessage = "Token không hợp lệ.";
            return;
          }

          // Giải mã JWT để lấy payload
          const payload = JSON.parse(atob(token.split(".")[1]));
          console.log("Payload giải mã:", payload);

          // Lưu token vào localStorage hoặc sessionStorage dựa trên "Remember Me"
          if ($scope.rememberMe) {
            localStorage.setItem("jwtToken", token); // Lưu vào localStorage nếu nhớ
            console.log(
              "Token đã được lưu vào localStorage:",
              localStorage.getItem("jwtToken")
            ); // Kiểm tra xem token đã lưu chưa
          } else {
            sessionStorage.setItem("jwtToken", token); // Lưu vào sessionStorage nếu không nhớ
            console.log(
              "Token đã được lưu vào sessionStorage:",
              sessionStorage.getItem("jwtToken")
            ); // Kiểm tra xem token đã lưu chưa
          }

          // Lấy quyền cao nhất từ token
          const highestRole = getHighestRole(payload.scope);
          redirectToPage(highestRole);
        })
        .catch(function (error) {
          console.log("Lỗi:", error); // Xem chi tiết lỗi
          if (error.data && error.data.message) {
            $scope.errorMessage = error.data.message; // Hiển thị thông báo lỗi từ server
          } else {
            $scope.errorMessage = "Đăng nhập thất bại.";
          }
        });
    };

    // Hàm lấy quyền cao nhất
    function getHighestRole(scopes) {
      const roles = scopes ? scopes.split(" ") : [];
      const rolePriority = {
        ROLE_ADMIN: 1,
        ROLE_STAFF: 2,
        ROLE_USER: 3,
      };

      const validRoles = roles.filter((role) => rolePriority[role]);
      validRoles.sort((a, b) => rolePriority[a] - rolePriority[b]);

      return validRoles[0] || null;
    }

    // Điều hướng đến trang tương ứng với quyền cao nhất
    function redirectToPage(highestRole) {
      if (highestRole === "ROLE_ADMIN") {
        window.location.href = "/assets/page/staff/Staff.html";
      } else if (highestRole === "ROLE_STAFF") {
        window.location.href = "/assets/page/staff/BanHang.html";
      } else {
        window.location.href = "/assets/page/user/home.html";
      }
    }
  });
