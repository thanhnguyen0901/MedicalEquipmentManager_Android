# Regression Checklist - Medical Manager App

**Tình trạng: Review sau khi sửa UI & Việt hóa**
**Người thực hiện: Senior QA + Developer**

## 1. Luồng Khởi động & Điều hướng (Navigation Flow)
| STT | Kịch bản kiểm tra | Kết quả mong đợi | Trạng thái |
|:---|:---|:---|:---|
| 1.1 | Mở app lần đầu | Màn hình Welcome hiển thị đúng text tiếng Việt, nút "BẮT ĐẦU" hoạt động. | [ ] |
| 1.2 | Welcome -> Trang chủ | Chuyển trang mượt mà, không giật lag. | [ ] |
| 1.3 | Trang chủ -> Loại thiết bị -> Back | Vào đúng màn hình Quản lý loại thiết bị, nút Back quay về Trang chủ. | [ ] |
| 1.4 | Trang chủ -> Thiết bị -> Back | Vào đúng danh sách thiết bị, nút Back quay về Trang chủ. | [ ] |
| 1.5 | Thiết bị -> Thêm thiết bị -> Back | Nút Back trên header hoạt động đúng. | [ ] |
| 1.6 | Thiết bị -> Chi tiết thiết bị -> Back | Nút Back trên header hoạt động đúng. | [ ] |
| 1.7 | Trang chủ -> Báo cáo -> Back | Nút Back trên header hoạt động đúng. | [ ] |

## 2. Kiểm tra giao diện & UX (UI/UX Consistency)
| STT | Điểm cần soát | Tiêu chuẩn | Trạng thái |
|:---|:---|:---|:---|
| 2.1 | Header vs Status Bar | Header không được đè lên status bar (đã fix fitsSystemWindows). | [ ] |
| 2.2 | Màu sắc chủ đạo | Toàn bộ header dùng màu Indigo (#1A237E), nền app màu xám nhạt (#F8F9FD). | [ ] |
| 2.3 | Font chữ & Kích thước | Tiêu đề trang con 24sp, tiêu đề trang chủ 28sp. | [ ] |
| 2.4 | Padding & Spacing | Các CardView có khoảng cách đều (16dp-20dp), không bị dính sát mép màn hình. | [ ] |
| 2.5 | Navigation Bar (System) | Nội dung cuối danh sách không bị che bởi thanh điều hướng hệ thống. | [ ] |

## 3. Ngôn ngữ & Việt hóa (Localization)
| STT | Vị trí kiểm tra | Yêu cầu | Trạng thái |
|:---|:---|:---|:---|
| 3.1 | Màn hình Loại thiết bị | Không còn "Manage Categories", "Add Category", "ID: CAT...". | [ ] |
| 3.2 | Tình trạng thiết bị | Hiển thị: "Hoạt động", "Bảo trì", "Hỏng" (Không phải Active/Maintenance/Broken). | [ ] |
| 3.3 | Thông báo (Toast) | Thông báo lưu, xóa, lỗi phải hoàn toàn bằng tiếng Việt. | [ ] |
| 3.4 | Hộp thoại (Dialog) | Dialog xác nhận xóa phải hiển thị tiếng Việt rõ ràng. | [ ] |
| 3.5 | Lỗi nhập liệu (Validation) | Các câu báo lỗi bỏ trống hoặc sai định dạng năm phải là tiếng Việt. | [ ] |

## 4. Nghiệp vụ & Dữ liệu (Business Logic)
| STT | Chức năng | Hành động kiểm tra | Trạng thái |
|:---|:---|:---|:---|
| 4.1 | CRUD Loại thiết bị | Thêm mới, Sửa tên, Xóa loại thiết bị (có ràng buộc nếu đã có thiết bị). | [ ] |
| 4.2 | CRUD Thiết bị | Thêm mới (chọn Loại & Tình trạng VN), Sửa, Xóa. | [ ] |
| 4.3 | Mapping Status | Kiểm tra khi sửa thiết bị, Spinner phải hiển thị đúng trạng thái cũ bằng tiếng Việt. | [ ] |
| 4.4 | Báo cáo lọc theo loại | Chọn một loại, kết quả trả về đúng các thiết bị thuộc loại đó. | [ ] |
| 4.5 | Báo cáo đặc biệt | Lọc Năm > 2020 & Tình trạng "Hoạt động" phải trả về kết quả chính xác từ DB. | [ ] |

## 5. Các màn hình rủi ro cao (High-Risk Areas)
1. **Màn hình Báo cáo (ReportActivity)**: Do thay đổi cách hiển thị Status từ Anh sang Việt, cần kiểm tra kỹ query SQLite có bị ảnh hưởng không (SQL vẫn dùng "Active").
2. **Màn hình Sửa thiết bị (EquipmentFormActivity)**: Kiểm tra việc map ngược từ text tiếng Việt trên Spinner về giá trị Enum tiếng Anh để lưu xuống DB.
3. **Màn hình Loại thiết bị (CategoryActivity)**: Màn hình này vừa được refactor lại layout và ID nút Back.

## 6. Ghi chú lỗi phát hiện (Bug Log)
*(Để trống nếu chưa phát hiện)*
- ...

---
**Ký xác nhận**
Senior Android Developer
