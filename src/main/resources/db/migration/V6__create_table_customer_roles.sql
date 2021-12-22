CREATE TABLE customer_roles (
    customer_id INTEGER NOT NULL,
    role VARCHAR(50) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);