-- === Kategoriler ===
INSERT INTO category DEFAULT VALUES; -- id=1
INSERT INTO category DEFAULT VALUES; -- id=2
INSERT INTO category DEFAULT VALUES; -- id=3
INSERT INTO category DEFAULT VALUES; -- id=4
INSERT INTO category DEFAULT VALUES; -- id=5
INSERT INTO category DEFAULT VALUES; -- id=6
INSERT INTO category DEFAULT VALUES; -- id=7
INSERT INTO category DEFAULT VALUES; -- id=8
INSERT INTO category DEFAULT VALUES; -- id=9

-- === Kategori Çevirileri ===
INSERT INTO category_translation (category_id, language_code, name) VALUES
-- 1: Çorbalar
(1, 'tr', 'Çorbalar'),
(1, 'en', 'Soups'),
(1, 'de', 'Suppen'),
(1, 'ru', 'Супы'),
(1, 'ar', 'الشوربات'),

-- 2: Izgara Balıklar
(2, 'tr', 'Izgara Balıklar'),
(2, 'en', 'Grilled Fish'),
(2, 'de', 'Gegrillter Fisch'),
(2, 'ru', 'Жареная рыба'),
(2, 'ar', 'الأسماك المشوية'),

-- 3: Salatalar
(3, 'tr', 'Salatalar'),
(3, 'en', 'Salads'),
(3, 'de', 'Salate'),
(3, 'ru', 'Салаты'),
(3, 'ar', 'السلطات'),

-- 4: Başlangıçlar
(4, 'tr', 'Başlangıçlar'),
(4, 'en', 'Starters'),
(4, 'de', 'Vorspeisen'),
(4, 'ru', 'Закуски'),
(4, 'ar', 'المقبلات'),

-- 5: Deniz Ürünleri
(5, 'tr', 'Deniz Ürünleri'),
(5, 'en', 'Seafood'),
(5, 'de', 'Meeresfrüchte'),
(5, 'ru', 'Морепродукты'),
(5, 'ar', 'المأكولات البحرية'),

-- 6: Ana Yemekler
(6, 'tr', 'Ana Yemekler'),
(6, 'en', 'Main Dishes'),
(6, 'de', 'Hauptgerichte'),
(6, 'ru', 'Основные блюда'),
(6, 'ar', 'الأطباق الرئيسية'),

-- 7: Tatlılar
(7, 'tr', 'Tatlılar'),
(7, 'en', 'Desserts'),
(7, 'de', 'Nachspeisen'),
(7, 'ru', 'Десерты'),
(7, 'ar', 'الحلويات'),

-- 8: İçecekler
(8, 'tr', 'İçecekler'),
(8, 'en', 'Drinks'),
(8, 'de', 'Getränke'),
(8, 'ru', 'Напитки'),
(8, 'ar', 'المشروبات'),

-- 9: Şaraplar
(9, 'tr', 'Şaraplar'),
(9, 'en', 'Wines'),
(9, 'de', 'Weine'),
(9, 'ru', 'Вина'),
(9, 'ar', 'النبيذ');

-- === Menü Ürünleri ===
-- 1: Balık Çorbası (Çorbalar)
INSERT INTO menu_item (image_path, price, category_id)
VALUES ('/images/fish_soup.jpg', 70, 1);

-- 2: Izgara Somon (Izgara Balıklar)
INSERT INTO menu_item (image_path, price, category_id)
VALUES ('/images/grilled_salmon.jpg', 180, 2);

-- 3: Tiramisu (Tatlılar)
INSERT INTO menu_item (image_path, price, category_id)
VALUES ('/images/tiramisu.jpg', 90, 7);

-- === Menü Ürün Çevirileri ===

-- 1: Balık Çorbası
INSERT INTO menu_item_translation (menu_item_id, language_code, name, description) VALUES
(1, 'tr', 'Balık Çorbası', 'Taze balıklardan hazırlanmış nefis çorba'),
(1, 'en', 'Fish Soup', 'Delicious soup prepared with fresh fish'),
(1, 'de', 'Fischsuppe', 'Leckere Suppe aus frischem Fisch'),
(1, 'ru', 'Рыбный суп', 'Вкусный суп из свежей рыбы'),
(1, 'ar', 'شوربة السمك', 'شوربة لذيذة محضرة من سمك طازج');

-- 2: Izgara Somon
INSERT INTO menu_item_translation (menu_item_id, language_code, name, description) VALUES
(2, 'tr', 'Izgara Somon', 'Izgara edilmiş somon fileto'),
(2, 'en', 'Grilled Salmon', 'Fresh salmon fillet grilled to perfection'),
(2, 'de', 'Gegrillter Lachs', 'Frisches Lachsfilet, perfekt gegrillt'),
(2, 'ru', 'Жареный лосось', 'Филе свежего лосося, приготовленное на гриле'),
(2, 'ar', 'سلمون مشوي', 'فيليه سلمون طازج مشوي بعناية');

-- 3: Tiramisu
INSERT INTO menu_item_translation (menu_item_id, language_code, name, description) VALUES
(3, 'tr', 'Tiramisu', 'Klasik İtalyan tatlısı, kahveli ve kremalı'),
(3, 'en', 'Tiramisu', 'Classic Italian dessert with coffee and cream'),
(3, 'de', 'Tiramisu', 'Klassisches italienisches Dessert mit Kaffee und Sahne'),
(3, 'ru', 'Тирамису', 'Классический итальянский десерт с кофе и кремом'),
(3, 'ar', 'تيراميسو', 'حلوى إيطالية كلاسيكية بالقهوة والكريمة');
