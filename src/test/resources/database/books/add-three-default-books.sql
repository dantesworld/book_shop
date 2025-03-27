DELETE FROM books;
INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES
    (1, 'The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 12.99, 'A story of wealth, love, and the American Dream in the 1920s.', 'https://example.com/image1.jpg', false),
    (2, 'To Kill a Mockingbird', 'Harper Lee', '9780061120084', 10.50, 'A powerful story of racial injustice and moral growth in the American South.', 'https://example.com/image2.jpg', false),
    (3, '1984', 'George Orwell', '9780451524935', 9.99, 'A dystopian novel about totalitarianism and surveillance society.', 'https://example.com/image3.jpg', false);