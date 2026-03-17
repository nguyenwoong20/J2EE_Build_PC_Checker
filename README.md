# 🖥️ BuildPC Checker - Java Spring Boot & MySQL

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-brightgreen?style=for-the-badge&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Authentication-blueviolet?style=for-the-badge)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203.0-green?style=for-the-badge&logo=swagger&logoColor=black)

> Một hệ thống Backend mạnh mẽ được xây dựng bằng Java Spring Boot, hỗ trợ xây dựng cấu hình máy tính cá nhân, kiểm tra độ tương thích phần cứng 6 lớp và phân tích hiệu năng chuyên sâu.

---

## 🚀 Tính Năng Chính

*   **🔍 Kiểm tra tương thích (6-Layer Compatibility Check):**
    *   **Socket:** Kiểm tra sự đồng bộ giữa CPU và Mainboard.
    *   **RAM:** Kiểm tra chuẩn RAM (DDR4/DDR5) và số lượng khe cắm khả dụng trên Mainboard.
    *   **Kích thước (Dimensions):** Kiểm tra Mainboard Form Factor so với Case, chiều cao tản nhiệt và độ dài VGA.
    *   **Lưu trữ:** Kiểm tra số lượng cổng M.2, SATA và khay ổ cứng (SSD/HDD).
    *   **Năng lượng:** Tính toán TDP linh kiện và đưa ra cảnh báo nếu PSU không đủ công suất.
*   **⚡ Gợi ý công suất nguồn (PSU Recommendation):** Tự động tính toán tổng công suất tiêu thụ của hệ thống và đề xuất mức công suất nguồn (Wattage) an toàn, tối ưu nhất.
*   **⚠️ Cảnh báo thông minh (Smart Warnings):**
    *   Cảnh báo Dual Channel nếu hệ thống chưa tối ưu RAM.
    *   Cảnh báo lệch chuẩn PCIe giữa Mainboard và VGA.
    *   Cảnh báo thiếu hụt cổng kết nối fan/RGB (nếu có dữ liệu).
*   **💾 Quản lý cấu hình (Build Management):** Cho phép người dùng tạo, lưu trữ và quản lý danh sách các bộ PC đã xây dựng (yêu cầu đăng nhập).
*   **⚖️ Kiểm tra nghẽn cổ chai (Bottleneck Checker):** 
    *   Phân tích sự cân bằng giữa CPU và GPU dựa trên điểm Benchmark thực tế.
    *   Tính toán tỉ lệ Bottleneck cho 3 kịch bản độ phân giải phổ biến: **1080p, 1440p (2K), 4K**.
    *   Đưa ra các lời khuyên nâng cấp hoặc thay đổi linh kiện để đạt hiệu năng cao nhất.
*   **📚 Tài liệu API trực quan:** Tích hợp Swagger UI giúp việc thử nghiệm và tích hợp Frontend trở nên dễ dàng và nhanh chóng.

---

## 🛠️ Công Nghệ Sử Dụng

| Thành phần | Công nghệ |
| :--- | :--- |
| **Framework** | Spring Boot 3.5.9 (Java 17) |
| **Security** | Spring Security & OAuth2 Resource Server |
| **Authentication** | JWT (JSON Web Token) with Refresh Token |
| **Database** | MySQL 8.0 & Spring Data JPA (Hibernate) |
| **Documentation** | SpringDoc OpenAPI (Swagger UI) |
| **Mapper** | MapStruct (High efficiency Object Mapping) |
| **Utilities** | Project Lombok, Validation, Java Mail Sender |

---

## ⚙️ Cấu Hình Ban Đầu

Trước khi chạy dự án, bạn cần cấu hình các thông số môi trường trong file `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/identity_service
    username: YOUR_DB_USERNAME
    password: YOUR_DB_PASSWORD
  mail:
    username: YOUR_EMAIL@gmail.com
    password: YOUR_APP_PASSWORD # Mã ứng dụng Gmail
jwt:
  signerKey: "your_64_character_hex_secret_key_here"
```

---

## 🏃 Cách Chạy Dự Án

1.  **Cài đặt & Kết nối Database:**
    *   Đảm bảo bạn đã cài đặt MySQL Server.
    *   Tạo database với tên: `identity_service`.

2.  **Khởi động ứng dụng bằng Maven Wrapper:**
    ```bash
    ./mvnw spring-boot:run
    ```
    *Hoặc chạy trực tiếp file `BuildPcCheckerApplication.java` từ IDE (IntelliJ IDEA/Eclipse).*

3.  **Dữ liệu mặc định (Admin Seed):** Khi chạy lần đầu, hệ thống sẽ tự động khởi tạo tài khoản Admin:
    *   **Email:** `haoaboutme@gmail.com`
    *   **Password:** `admin`

---

## 📖 Tài Liệu API

Sau khi server khởi động thành công (mặc định tại port `8080` với context path `/identity`), bạn có thể truy cập tài liệu API tại:

🔗 **Swagger UI:** [http://localhost:8080/identity/swagger-ui.html](http://localhost:8080/identity/swagger-ui.html)

---

## 📁 Cấu Trúc Thư Mục

```text
src/main/java/com/j2ee/buildpcchecker/
├── compatibility/     # Logic kiểm tra sự tương thích (6 lớp validation)
├── configuration/     # Cấu hình Security, Swagger, CORS, JWT Decoder
├── controller/        # Các REST API Endpoints
├── dto/               # Data Transfer Objects (Request/Response)
├── entity/            # JPA Entities (Lớp mapping Database)
├── mapper/            # Ánh xạ dữ liệu (Entity <-> DTO)
├── repository/        # Lớp giao tiếp Database (Spring Data JPA)
├── service/           # Xử lý logic nghiệp vụ chính
└── BuildPcCheckerApplication.java  # Main application entry point
```

---

## 📄 License & Contact

Dự án được phát triển nhằm mục đích học tập và hỗ trợ cộng đồng yêu công nghệ. 
📧 Liên hệ: `haoaboutme@gmail.com` (hoặc `nguyenlehoanhao2004@email.com`)

---
*Cảm ơn bạn đã quan tâm đến dự án!* 🌟
