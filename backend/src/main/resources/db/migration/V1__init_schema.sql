-- === Users (Admin için) ===
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- === Category (sadece ID tutar, isimler translation tablosunda) ===
CREATE TABLE category (
    id SERIAL PRIMARY KEY
);

-- === Category Translation ===
CREATE TABLE category_translation (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL REFERENCES category(id) ON DELETE CASCADE,
    language_code VARCHAR(5) NOT NULL,
    name VARCHAR(255) NOT NULL,
    UNIQUE (category_id, language_code)
);

-- === Menu Item ===
CREATE TABLE menu_item (
    id SERIAL PRIMARY KEY,
    image_path VARCHAR(255),
    price NUMERIC(10,2) NOT NULL,
    category_id INT REFERENCES category(id) ON DELETE SET NULL
);

-- === Menu Item Translation ===
CREATE TABLE menu_item_translation (
    id BIGSERIAL PRIMARY KEY,
    menu_item_id BIGINT NOT NULL REFERENCES menu_item(id) ON DELETE CASCADE,
    language_code VARCHAR(5) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    UNIQUE (menu_item_id, language_code)
);
