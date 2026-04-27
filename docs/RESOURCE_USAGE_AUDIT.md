# Resource Usage Audit - Medical Manager App

**Tình trạng: Review toàn diện Resource (Strings, Colors, Dimens)**
**Người thực hiện: Senior Android Developer**

## A. Tổng quan hiện trạng
Project đã bắt đầu chuẩn hóa Resource nhưng vẫn còn tồn tại nhiều điểm hardcode rải rác trong các file Layout và Java.
- **Common Resources**: Đã có `strings.xml` tiếng Việt tương đối đầy đủ và `colors.xml` định nghĩa theo Brand.
- **Hardcoding**: 
    - Layout vẫn còn hardcode màu sắc (`#1A237E`, `#F8F9FD`, `#F5F5F5`).
    - Các giá trị kích thước (margin, padding, corner radius) hardcode hoàn toàn (16dp, 20dp, 12dp).
    - Code Java (`EquipmentAdapter`) vẫn dùng `Color.parseColor` và hardcode chuỗi so sánh trạng thái.

## B. Danh sách hardcoded text
| File | Text hiện tại | Đề xuất Key | Giá trị tiếng Việt |
|:---|:---|:---|:---|
| activity_welcome.xml | android:text="@string/..." | (Đã dùng string resource) | - |
| activity_category.xml | "Category Name" (hint) | `label_category_name` | Tên loại thiết bị |
| activity_category.xml | "Add Category" (text) | `btn_add_category` | Thêm loại thiết bị |
| activity_category.xml | "Existing Categories" | `label_existing_categories` | Danh sách loại thiết bị |
| EquipmentDetailActivity.java | "Error: Equipment not found" | `err_not_found` | Không tìm thấy dữ liệu thiết bị |

## C. Danh sách hardcoded color
| File | Color hiện tại | Mục đích | Đề xuất Key | Ghi chú |
|:---|:---|:---|:---|:---|
| activity_home.xml | `#1A237E` | Header BG | `@color/brand_primary` | Reuse |
| activity_home.xml | `#F8F9FD` | Root BG | `@color/app_bg` | Reuse |
| activity_welcome.xml | `#F0F4FF` | Decorative BG | `@color/bg_decorative` | Tạo mới |
| activity_equipment_form.xml | `#F5F5F5` | Spinner BG | `@color/bg_field` | Tạo mới |
| EquipmentAdapter.java | `#2E7D32` | Status Active | `@color/status_active` | Reuse |
| EquipmentAdapter.java | `Color.RED` | Status Broken | `@color/status_broken` | Reuse |

## D. Danh sách hardcoded spacing/size (Lặp lại nhiều)
| File | Giá trị | Mục đích | Đề xuất Dimen Key |
|:---|:---|:---|:---|
| Multiple layouts | `16dp` | Standard Margin | `spacing_medium` |
| Multiple layouts | `20dp` | Standard Padding | `spacing_large` |
| Multiple layouts | `12dp` | Corner Radius | `radius_medium` |
| Multiple layouts | `16dp` | Corner Radius | `radius_large` |
| activity_home.xml | `24dp` | Header Padding | `spacing_xlarge` |

## E. Danh sách resource đang define nhưng không dùng
| Resource Name | File khai báo | Có nên xóa? | Lý do |
|:---|:---|:---|:---|
| `status_array` | strings.xml | Nên xóa | Đã được thay thế bởi `status_array_ui` và `status_array_db` |
| `brand_primary_light` | colors.xml | Giữ lại | Có thể dùng cho hiệu ứng nhấn (ripple/state) |

## F. Đề xuất Cleanup Plan

### Phase 1: Hoàn thiện Resource Base
1. Tạo file `res/values/dimens.xml` để định nghĩa spacing và radius chuẩn.
2. Cập nhật `colors.xml` với các màu nền bổ trợ (`bg_field`, `bg_decorative`).
3. Dọn dẹp `strings.xml` (xóa `status_array` cũ).

### Phase 2: Refactor Layouts
1. Thay thế toàn bộ mã màu `#...` bằng `@color/...`.
2. Thay thế kích thước `dp` bằng `@dimen/...`.
3. Đảm bảo toàn bộ `android:text` và `android:hint` dùng `@string/...`.

### Phase 3: Refactor Java Code
1. Cập nhật `EquipmentAdapter.java` dùng `ContextCompat.getColor(context, R.color.status_...)` thay cho `Color.parseColor`.
2. Đảm bảo các Toast/Dialog dùng `context.getString(R.string....)`.

### Phase 4: Final Cleanup & Testing
1. Chạy "Analyze -> Inspect Code" để tìm resource thực sự không sử dụng.
2. Regression test toàn bộ luồng app để đảm bảo không sai lệch UI.

---
**Senior Android Developer**
