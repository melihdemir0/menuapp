# MenuApp

MenuApp, restoranlar için dijital menü ve yönetim sistemidir.  
Yönetici paneli üzerinden menü öğeleri eklenebilir, düzenlenebilir ve silinebilir.  
Müşteriler ise menüyü mobil uyumlu bir arayüz ile görüntüleyebilir.

---

## Özellikler
- Admin paneli ile CRUD işlemleri (kategori ve ürün yönetimi)  
- Spring Security ile kullanıcı giriş sistemi  
- PostgreSQL ve Flyway ile veritabanı yönetimi  
- Mobil uyumlu müşteri arayüzü (React)  
- Docker ile kolay dağıtım  

---

## Kullanılan Teknolojiler
- Backend: Spring Boot, Spring Security, JPA/Hibernate, Flyway  
- Database: PostgreSQL  
- Frontend: React    

---

## Kurulum

### 1. Backend
Projeyi klonla, ardından aşağıdaki adımları uygula:  
- `cd menuapp`  
- `mvn clean install`  
- `mvn spring-boot:run`  

### 2. Database
PostgreSQL üzerinde veritabanını oluştur:  
- `CREATE DATABASE menuapp_db;`  

Flyway migration dosyaları otomatik çalışacaktır.

### 3. Frontend
Frontend klasörüne geçerek çalıştır:  
- `cd menuapp-frontend`  
- `npm install`  
- `npm start`  

---

## Yol Haritası
- Çoklu dil desteği  
- Görsel yükleme  
- Menü kategorileri için sıralama özelliği  
- SaaS yapısına uygun çoklu restoran desteği  

---

## Geliştirici
- [Melih Demir](https://github.com/melihdemir0) 
