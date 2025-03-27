DELETE FROM books_categories WHERE book_id = (SELECT id FROM books WHERE title = 'Dune');
DELETE FROM books WHERE title = 'Dune';