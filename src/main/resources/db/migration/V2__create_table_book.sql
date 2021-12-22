CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(255) NOT NULL,
    customer_id INTEGER NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);