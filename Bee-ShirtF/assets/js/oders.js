angular
  .module("orderApp", [])
  .controller("OrderController", ["$scope", "$timeout", function ($scope, $timeout) {
    // Variables for control and messages
    $scope.showCreateForm = false;
    $scope.orders = [];
    $scope.errorMessage = "";
    $scope.successMessage = "";

    // Date range filter variables
    $scope.startDate = null;
    $scope.endDate = null;

    // Format dates to Vietnamese locale
    $scope.formatDate = function (dateString) {
      const options = { year: "numeric", month: "2-digit", day: "2-digit" };
      const date = new Date(dateString);
      return date.toLocaleDateString("vi-VN", options);
    };

    // Load orders data from the server
    $scope.loadDataOrderToTable = function () {
      const token = sessionStorage.getItem("jwtToken");
      if (!token) {
        alert("Bạn chưa đăng nhập. Vui lòng đăng nhập lại.");
        window.location.href = "http://127.0.0.1:5500/assets/account/login.html#!/login";
        return;
      }

      // Fetch data using AJAX
      $.ajax({
        type: "GET",
        url: "http://localhost:8080/bills/history",
        headers: { Authorization: "Bearer " + token },
        success: function (response) {
          if (Array.isArray(response.result)) {
            $scope.orders = response.result.map((order, index) => ({
              stt: index + 1,
              codeBill: order.codeBill,
              desiredDate: $scope.formatDate(order.desiredDate),
              totalMoney: order.totalMoney.toLocaleString("vi-VN", {
                style: "currency",
                currency: "VND",
              }),
              statusBill: order.statusBill === 1 ? "Đã thanh toán" : "Chưa thanh toán",
            }));
            $scope.successMessage = "Tải dữ liệu thành công!";
            $timeout($scope.initializeDataTable, 0); // Initialize DataTable with orders
          } else {
            console.error("Dữ liệu không phải là một mảng:", response.result);
            $scope.errorMessage = "Dữ liệu không đúng định dạng.";
          }
          $scope.$apply();
        },
        error: function (error) {
          console.error("Lỗi khi lấy dữ liệu:", error);
          $scope.errorMessage = "Không thể lấy dữ liệu.";
          $scope.$apply();
        },
      });
    };

    // Initialize DataTable with order data and date filter
    $scope.initializeDataTable = function () {
      if ($.fn.DataTable.isDataTable("#order-table")) {
        $("#order-table").DataTable().destroy(); // Destroy old DataTable if exists
      }

      const table = $("#order-table").DataTable({
        paging: true,
        searching: true,
        ordering: true,
        pageLength: 5,
        data: $scope.orders.map((order) => [
          order.stt,
          order.codeBill,
          order.desiredDate,
          order.totalMoney,
          order.statusBill,
        ]),
        columns: [
          { title: "STT" },
          { title: "Mã Hóa Đơn" },
          { title: "Ngày Thanh Toán", className: "text-end" },
          { title: "Tổng Tiền", className: "text-end" },
          { title: "Trạng Thái", className: "text-end" },
        ],
        language: {
          search: "Tìm kiếm:",
          lengthMenu: "Hiện _MENU_ mục",
          info: "Hiển thị _START_ đến _END_ trong tổng số _TOTAL_ mục",
          paginate: {
            first: "Đầu",
            last: "Cuối",
            next: "Tiếp",
            previous: "Trước",
          },
        },
      });

      // Custom date range filter
      $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
        const desiredDate = new Date(data[2]); // Date in column 2
        const startDate = $scope.startDate ? new Date($scope.startDate) : null;
        const endDate = $scope.endDate ? new Date($scope.endDate) : null;

        if (
          (!startDate || desiredDate >= startDate) &&
          (!endDate || desiredDate <= endDate)
        ) {
          return true;
        }
        return false;
      });
    };

    // Function to apply the date filter
    $scope.applyDateFilter = function () {
      $("#order-table").DataTable().draw();
    };

    // Initialize data and DataTable on page load
    angular.element(document).ready(function () {
      $scope.loadDataOrderToTable();
    });
  }]);
