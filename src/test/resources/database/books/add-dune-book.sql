DELETE
FROM books
WHERE title = 'Dune';
INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (4, 'Dune', 'Frank Herbert', '9780441172719', 49.49,
        'A book about a young heir leads a rebellion on a spice-rich desert planet.',
        'https://example.com/dune-cover.jpg', false);