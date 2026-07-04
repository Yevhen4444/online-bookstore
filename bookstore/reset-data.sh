#!/bin/bash
# Скидає всі дані в БД, окрім юзера-адміна (email передається першим аргументом)
# Використання: ./reset-data.sh yevhen.test@example.com

ADMIN_EMAIL="${1:-yevhen.test@example.com}"
MYSQL_CONTAINER="bookstore-mysql"
MYSQL_ROOT_PASS="bookstore_pass"
DB_NAME="bookstore"

echo "Скидаю дані, залишаю адміна: $ADMIN_EMAIL"

docker exec -i "$MYSQL_CONTAINER" mysql -u root -p"$MYSQL_ROOT_PASS" "$DB_NAME" <<SQL
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM cart_item;
DELETE FROM shopping_cart;
DELETE FROM book_categories;
DELETE FROM books;
DELETE FROM categories;

-- видаляє всіх юзерів, окрім адміна (і їх ролі)
DELETE FROM user_roles WHERE user_id IN (
    SELECT id FROM (SELECT id FROM users WHERE email != '$ADMIN_EMAIL') AS tmp
);
DELETE FROM users WHERE email != '$ADMIN_EMAIL';

-- гарантує, що в адміна завжди є кошик після скидання
INSERT INTO shopping_cart (user_id)
SELECT id FROM users WHERE email = '$ADMIN_EMAIL';

SET FOREIGN_KEY_CHECKS = 1;
SQL

echo "Готово. Залишився тільки: $ADMIN_EMAIL (з кошиком)"
