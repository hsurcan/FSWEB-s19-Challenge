# 🐦 Twitter API Backend & Integration (FSWEB Sprint 19 Challenge)

Bu proje, **Workintech FSWEB Sprint 19 Challenge** kapsamında geliştirilmiş, Twitter (X) platformunun temel işlevlerini simüle eden robust bir **Spring Boot RESTful API** backend servisi ve ona bağlı **React** frontend entegrasyonudur.

Sistem; Katmanlı Mimari (*Controller/Service/Repository/Entity*), Spring Security tabanlı yetkilendirme, Global Hata Yönetimi (*Global Exception Handling*), Bean Validation ve PostgreSQL veritabanı ilişkileri göz önünde bulundurularak inşa edilmiştir.

---

## 🛠️ Teknolojiler ve Araçlar

* **Java 17+**
* **Spring Boot 3.x**
  * Spring Data JPA
  * Spring Security
  * Spring Validation
* **PostgreSQL** (Veritabanı)
* **React** (Frontend - Port `3200`)
* **JUnit 5 & Mockito** (Unit Testler)
* **Maven / Gradle**
* **Lombok**

---

## 🏗️ Mimari Yapı ve Tasarım

Proje, sürdürülebilirlik ve test edilebilirlik ilkelerine uygun olarak katmanlı mimari (*Layered Architecture*) prensiplerine dayanır:

```text
src/main/java/com/workintech/twitter/
├── config/          # Spring Security, CORS ve Global Bean Yapılandırmaları
├── controller/      # HTTP Requestlerini karşılayan Endpoint Katmanı
├── dto/             # Data Transfer Objects (Request/Response şablonları)
├── entity/          # PostgreSQL Veritabanı Varlıkları (User, Tweet, Comment, Like, Retweet)
├── exception/       # Custom Exception Sınıfları ve @ControllerAdvice Global Exception Handler
├── repository/      # Spring Data JPA Repository Arayüzleri
└── service/         # İş Mantığı (Business Logic) Katmanı
