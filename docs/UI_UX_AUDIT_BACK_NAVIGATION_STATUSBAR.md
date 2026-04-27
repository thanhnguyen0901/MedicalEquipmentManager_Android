# BÁO CÁO KIỂM TRA UI/UX (Hệ thống Điều hướng & Hiển thị Status Bar)

## 1. Danh sách màn hình đã review
1.  **WelcomeActivity**: Màn hình chào mừng (Entry point).
2.  **HomeActivity**: Trang chủ (Dashboard).
3.  **CategoryActivity**: Quản lý danh mục (Consolidated).
4.  **EquipmentListActivity**: Danh sách thiết bị y tế.
5.  **EquipmentFormActivity**: Thêm/Sửa thiết bị.
6.  **EquipmentDetailActivity**: Chi tiết thiết bị.
7.  **ReportActivity**: Báo cáo và Thống kê.
8.  **FilterActivity**: Bộ lọc (Hiện đang trùng lặp chức năng với Report).

---

## 2. Vấn đề tìm thấy và Phân tích kỹ thuật

### A. Vấn đề Điều hướng (Back Navigation)
- **Thiếu nút Back vật lý trong Header**:
    - Hầu hết các màn hình con (`CategoryActivity`, `EquipmentListActivity`, `ReportActivity`) đều sử dụng Header tùy biến nhưng thiếu nút quay lại (Back/Arrow icon) ở góc trên bên trái. Người dùng phải phụ thuộc hoàn toàn vào phím Back của hệ thống.
    - **Tệp liên quan**: `activity_category.xml`, `activity_equipment_list.xml`, `activity_report.xml`.
- **Nút Back không đồng nhất**:
    - `EquipmentDetailActivity` có nút "Quay lại" nhưng đặt ở dưới cùng của màn hình. Đây không phải là vị trí chuẩn cho navigation back (thường đặt ở top bar).
    - **Tệp liên quan**: `activity_equipment_detail.xml`, `EquipmentDetailActivity.java`.
- **Trùng lặp Activity**:
    - Hiện đang có cả `CategoryActivity` và `CategoryListActivity`. Trong `HomeActivity`, `cardCategories` dẫn đến `CategoryActivity`. Tuy nhiên `AndroidManifest.xml` vẫn khai báo cả hai.
    - **Tệp liên quan**: `AndroidManifest.xml`, `HomeActivity.java`.

### B. Vấn đề Hiển thị (Status Bar & Layout)
- **Thiếu `fitsSystemWindows="true"`**:
    - Một số màn hình không có thuộc tính này khiến Header bị đẩy lên sát hoặc bị che bởi Status Bar trên các dòng máy có Notch/Dynamic Island.
    - **Lỗi nặng nhất ở**: `activity_category.xml` và `activity_equipment_detail.xml`.
- **Header Padding không thống nhất**:
    - `activity_equipment_detail.xml` sử dụng `paddingTop="48dp"` để "né" status bar một cách thủ công.
    - Các màn hình khác (`activity_home.xml`, `activity_report.xml`) sử dụng `paddingTop="24dp"` kết hợp `fitsSystemWindows`.
    - Điều này gây ra hiện tượng giật/nhảy vị trí Header khi chuyển giữa các màn hình.
- **Màu sắc và Font size không nhất quán**:
    - Header `CategoryActivity` dùng màu `#3F51B5`, trong khi các màn hình khác dùng `#1A237E`.
    - Font size tiêu đề Header nhảy từ `24sp` đến `32sp` không rõ quy luật.

---

## 3. Nguyên nhân kỹ thuật có khả năng gây lỗi
1.  **Theme**: Sử dụng `Theme.Material3.DayNight.NoActionBar` nên hệ thống không tự động cung cấp Up Button. Cần phải tự implement trong layout XML và Java code.
2.  **Layout Root**: Thiếu sự thống nhất trong việc sử dụng `fitsSystemWindows` ở thẻ root của XML.
3.  **Hardcoded Padding**: Sử dụng giá trị padding cứng (ví dụ `48dp`) để tránh status bar thay vì dùng window insets, dẫn đến không tương thích trên các kích thước màn hình khác nhau.

---

## 4. Đề xuất hướng sửa cụ thể

### Bước 1: Thống nhất Header và Nút Back
- Chỉnh sửa toàn bộ layout XML của màn hình con để thêm một `ImageButton` (icon `ic_arrow_back`) vào Header.
- Trong Java code, sử dụng `onBackPressedDispatcher` hoặc đơn giản là `finish()` khi nhấn nút Back này.
- Di chuyển nút Back trong `EquipmentDetailActivity` lên Header.

### Bước 2: Chuẩn hóa Layout Status Bar
- Thêm `android:fitsSystemWindows="true"` vào tất cả thẻ root của Activity Layout.
- Quy định một chuẩn `paddingTop` cho tất cả Header (ví dụ: `24dp` khi đã có `fitsSystemWindows`).

### Bước 3: Đồng bộ Design System
- Thống nhất màu Primary là `#1A237E` cho tất cả Header.
- Thống nhất TextSize cho Title là `28sp` và Subtitle là `16sp`.
- Cập nhật `activity_category.xml` để khớp với style của các màn hình khác.

### Bước 4: Dọn dẹp Code
- Loại bỏ hoặc hợp nhất `CategoryListActivity` vào `CategoryActivity` nếu không còn dùng đến.
- Kiểm tra lại luồng `finish()` trong `WelcomeActivity` để đảm bảo không quay lại được màn hình chào mừng sau khi đã vào Home.

---

## 5. Thứ tự ưu tiên sửa
1.  **Cao (High)**: Thêm nút Back vào Header cho các màn hình con (UX quan trọng nhất).
2.  **Cao (High)**: Sửa lỗi Header bị che bởi Status Bar (`fitsSystemWindows`).
3.  **Trung bình (Medium)**: Thống nhất màu sắc và khoảng cách Header (Consistency).
4.  **Thấp (Low)**: Xóa bỏ các Activity/Layout dư thừa.
