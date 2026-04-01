CREATE SCHEMA `ecommerce_portfolio` ;
USE `ecommerce_portfolio`;

CREATE TABLE customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    email VARCHAR(200) NOT NULL UNIQUE,
    passwd VARCHAR(200) NOT NULL,
	user_role ENUM('ADMIN', 'USER') NOT NULL
);

CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    order_date DATE NOT NULL,
    total_price DECIMAL(9,2) NOT NULL CHECK (total_price >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    category_description TEXT
);

CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    product_description TEXT,
    unit_price DECIMAL(9,2) NOT NULL CHECK (unit_price > 0),
    SKU INT NOT NULL UNIQUE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

CREATE TABLE order_details (
    order_details_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(9,2) NOT NULL CHECK (unit_price > 0),
    address VARCHAR(300),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE inventory (
    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL UNIQUE,
    quantity INT NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL UNIQUE,
    amount DECIMAL(9,2) NOT NULL CHECK (amount > 0),
    method VARCHAR(20) NOT NULL CHECK (method IN ('debit','credit','paypal')),
    payment_status VARCHAR(20) NOT NULL CHECK (payment_status IN ('pending', 'completed', 'failed', 'refunded')),
    paid_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

CREATE INDEX idx_customer ON customers (customer_id);
CREATE INDEX idx_order ON order_details (product_id, quantity);
CREATE INDEX idx_product_info ON products (product_id, unit_price, product_name);
CREATE INDEX idx_inventory_amount ON inventory (quantity, last_update);
CREATE INDEX idx_category ON categories (category_id, category_name);

INSERT INTO customers (customer_name, email, passwd, user_role)
VALUES ('admin', 'admin@valgames.com', '$2a$10$VP3KSE7dH2e4ylZtSXr1e.C4AipPG7NfrbEWsihRyP/lp2bkpgvpO', 'admin');


INSERT INTO categories (category_name, category_description) VALUES
('N64 Controllers', 'Original and third-party Nintendo 64 controllers'),
('N64 Games', 'Nintendo 64 cartridge games'),
('GBA Games', 'Game Boy Advance cartridge games');

INSERT INTO products (category_id, product_name, product_description, unit_price, SKU) VALUES
(1, 'N64 OEM Controller - Gray', 'Original hardware. Minor cosmetic wear on shell.', 15000.00, 1001),
(1, 'N64 OEM Controller - Yellow', 'Original hardware. Excellent condition.', 15000.00, 1002),
(1, 'N64 HoriPAD Controller - Red', 'Enthusiast-grade hardware. Ergonomic grip. Near-mint, tight thumbstick.', 45000.00, 1003),
(1, 'N64 HoriPAD Controller - Green', 'Enthusiast-grade hardware. Ergonomic grip. Tight stick, light surface scratches.', 35000.00, 1004),
(2, 'Conker Bad Fur Day N64', 'Rare-developed adventure. Mature themes. Authentic cartridge, clean label.', 40000.00, 2001),
(2, 'Zelda Ocarina of Time N64', 'Action-adventure RPG. Gold cartridge, minor label wear.', 40000.00, 2002),
(2, 'Mischief Makers N64', '2D side-scrolling platformer. Cartridge only, tested working.', 40000.00, 2003),
(3, 'Pokemon LeafGreen GBA', 'GBA remake of Kanto region. Original internal battery tested. Label intact.', 40000.00, 3001),
(2, 'Harvest Moon 64 N64', 'Agricultural simulation RPG. High-collectible. Clean contacts, saves functional.', 40000.00, 2004),
(2, 'Diddy Kong Racing 64 N64', 'Multi-vehicle racing title. Adventure Mode supported. Cartridge shows shelf wear.', 40000.00, 2005),
(3, 'Pokemon Sapphire GBA', 'Third-generation entry. Hoenn region. Original translucent shell, intact label.', 40000.00, 3002),
(3, 'Pokemon FireRed GBA', 'Gen 1 remake on GBA. Updated visuals. Authentic Nintendo PCB, verified working.', 40000.00, 3003),
(2, 'Banjo-Tooie N64', 'Banjo-Kazooie sequel. Expanded world maps. Excellent cartridge and label condition.', 40000.00, 2006);

INSERT INTO inventory (product_id, quantity, last_update) VALUES
(1, 68, DEFAULT),
(2, 30, DEFAULT),
(3, 52, DEFAULT),
(4, 25, DEFAULT),
(5, 31, DEFAULT),
(6, 64, DEFAULT),
(7, 56, DEFAULT),
(8, 47, DEFAULT),
(9, 42, DEFAULT),
(10, 59, DEFAULT),
(11, 23, DEFAULT),
(12, 35, DEFAULT),
(13, 0, DEFAULT);
