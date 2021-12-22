CREATE TABLE purchase (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER NOT NULL,
    nfe VARCHAR(250),
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE purchase_book(
	purchase_id INTEGER NOT NULL,
	book_id INTEGER NOT NULL,
    FOREIGN KEY (purchase_id) REFERENCES purchase(id),
    FOREIGN KEY (book_id) REFERENCES books(id),
    PRIMARY KEY (purchase_id, book_id)
);