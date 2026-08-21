# FSWEB-s19-Challenge — Twitter API

Spring Boot 3.2.5 · Java 17 · PostgreSQL · Spring Security (JWT) · React (frontend/ klasöründe, port 3200)

## Açılış (IntelliJ)
1. IntelliJ → File → Open → BU klasörü seç (pom.xml kökte).
2. "Trust Project" de; Maven senkronizasyonunun bitmesini bekle (alt çubuk).
3. SDK olarak JDK 17 seçili olmalı (File → Project Structure → Project).

## Veritabanı (Postgres.app)
1. postgresapp.com → indir → Initialize.
2. Uygulama içinden `twitterdb` adında veritabanı oluştur
   (veya psql'de: CREATE DATABASE twitterdb;).
3. application.yml varsayılanları Postgres.app'e göredir: kullanıcı = Mac
   kullanıcı adın, şifre boş. Farklıysa DB_USERNAME / DB_PASSWORD ortam
   değişkenlerini Run Configuration'a ekle.

## Çalıştırma
- TwitterApiApplication → yeşil ok. Tablolar ilk açılışta otomatik kurulur (ddl-auto: update).
- İlk test:
  curl -X POST http://localhost:3000/register -H "Content-Type: application/json" \
    -d '{"firstName":"Hira","lastName":"Surcan","username":"hira","email":"hira@test.com","password":"gizli123"}'

## Frontend
cd frontend && npm install && npm run dev  (http://localhost:3200)

## Endpointler
POST /register, /login (açık) · POST/GET/PUT/DELETE /tweet... · POST /comment,
PUT/DELETE /comment/{id} · POST /like, /dislike · POST /retweet — ayrıntı ve
kurallar kod içi yorumlarda.
