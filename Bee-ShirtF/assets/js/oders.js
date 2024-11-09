angular
  .module("orderApp", [])
  .controller("OrderController", [
    "$scope",
    function ($scope) {
      // Biến để kiểm soát hiển thị form
      $scope.showCreateForm = false;

      // Dữ liệu
      $scope.orders = [];
      $scope.errorMessage = ""; // Để lưu thông báo lỗi
      $scope.successMessage = ""; // Để lưu thông báo thành công

      // Hàm định dạng ngày
      $scope.formatDate = function (dateString) {
        const options = { year: "numeric", month: "2-digit", day: "2-digit" };
        const date = new Date(dateString);
        return date.toLocaleDateString("vi-VN", options);
      };

      // Hàm tải danh sách đơn hàng từ server bằng AJAX
      $scope.loadDataOrderToTable = function () {
        const token = sessionStorage.getItem("jwtToken");
        // Kiểm tra xem mã thông báo có tồn tại không
        if (!token) {
          alert("Bạn chưa đăng nhập. Vui lòng đăng nhập lại.");
          window.location.href = "http://127.0.0.1:5500/assets/account/login.html#!/login"; // Redirect to the login page
          return;
      }
      
        // Sử dụng jQuery AJAX
        $.ajax({
          type: "GET",
          url: "http://localhost:8080/bills/history",
          headers: {
            Authorization: "Bearer " + token,
          },
          success: function (response) {
            if (Array.isArray(response.result)) {
              $scope.orders = response.result.map((order) => ({
                codeBill: order.codeBill,
                desiredDate: $scope.formatDate(order.desiredDate),
                totalMoney: order.totalMoney.toLocaleString("vi-VN", {
                  style: "currency",
                  currency: "VND",
                }),
                statusBill: order.statusBill === 1 ? "Đã thanh toán" : "Chưa thanh toán",
              }));
              $scope.successMessage = "Tải dữ liệu thành công!";
              $scope.initializeDataTable(); // Khởi tạo DataTable
            } else {
              console.error("Dữ liệu không phải là một mảng:", response.result);
              $scope.errorMessage = "Dữ liệu không đúng định dạng.";
            }
            $scope.$apply(); // Áp dụng thay đổi cho Angular
          },
          error: function (error) {
            console.error("Lỗi khi lấy dữ liệu:", error);
            if (error.responseJSON) {
              console.error("Chi tiết lỗi:", error.responseJSON);
            }
            $scope.errorMessage = "Không thể lấy dữ liệu.";
            $scope.$apply(); // Áp dụng thay đổi cho Angular
          },
        });
      };

      // Hàm khởi tạo DataTable
      $scope.initializeDataTable = function () {
        // Khởi tạo DataTable và thêm dữ liệu từ orders
        const table = $('#order-table').DataTable({
          paging: true,
          searching: true,
          ordering: true,
          pageLength: 5,
          data: $scope.orders.map((order, index) => ([
            index + 1, // STT
            order.codeBill, // Mã Hóa Đơn
            order.desiredDate, // Ngày Thanh Toán
            order.totalMoney, // Tổng Tiền
            order.statusBill // Trạng Thái
          ])),
          columns: [
            { title: "STT" },
            { title: "Mã Hóa Đơn" },
            { title: "Ngày Thanh Toán", className: "text-end" },
            { title: "Tổng Tiền", className: "text-end" },
            { title: "Trạng Thái", className: "text-end" }
          ],
          language: {
            search: "Tìm kiếm:",
            lengthMenu: "Hiện _MENU_ mục",
            info: "Hiển thị _START_ đến _END_ trong tổng số _TOTAL_ mục",
            paginate: {
              first: "Đầu",
              last: "Cuối",
              next: "Tiếp",
              previous: "Trước"
            }
          }
        });
      };

      // Gọi hàm này để tải dữ liệu khi tài liệu sẵn sàng
      angular.element(document).ready(function () {
        $scope.loadDataOrderToTable(); // Tải dữ liệu vào bảng
      });
    },
  ]);
