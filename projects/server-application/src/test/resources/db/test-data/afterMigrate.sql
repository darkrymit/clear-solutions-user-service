-- Clear all previous data if exists
TRUNCATE TABLE users CASCADE;

-- Reset sequence
ALTER SEQUENCE users_id_seq RESTART WITH 1;

-- Add regular data
INSERT INTO users (email, first_name, last_name, birth_date, address, phone_number, created_at,
                   last_modified_at)
VALUES ('johndoe@gmail.com', 'John', 'Doe', '2002-02-22', 'John Doe address', '+1234567892',
        '2024-03-24T22:22:09.266615Z', '2024-03-24T22:28:19.266615Z'),
       ('john.doe@example.com', 'John', 'Doe', '1980-01-01', '123 Main St', '+1234567890',
        '2024-03-25T22:22:09.266615Z', '2024-03-25T22:28:19.266615Z'),
       ('jane.doe@example.com', 'Jane', 'Doe', '1985-02-02', '456 Maple Ave', '+380987654321',
        '2024-03-26T22:22:09.266615Z', '2024-03-26T22:28:19.266615Z'),
       ('mike.smith@example.com', 'Mike', 'Smith', '1990-03-03', '789 Oak St', '+4567890123',
        '2024-03-27T22:22:09.266615Z', '2024-03-27T22:28:19.266615Z'),
       ('linda.johnson@example.com', 'Linda', 'Johnson', '1995-04-04', '321 Pine Ave',
        '+7890123456', '2024-03-28T22:22:09.266615Z', '2024-03-28T22:28:19.266615Z');

-- Add data with NULL values in optional fields
INSERT INTO users (email, first_name, last_name, birth_date, address, phone_number, created_at,
                   last_modified_at)
VALUES ('null.user@example.com', 'Null', 'Nullable', '2000-01-01', NULL, NULL,
        '2024-03-29T22:22:09.266615Z', '2024-03-29T22:28:19.266615Z');

-- Add data with cyrillic characters
INSERT INTO users (email, first_name, last_name, birth_date, address, phone_number, created_at,
                   last_modified_at)
VALUES ('ivan.ivanov@example.com', 'Іван', 'Іванов', '1980-01-01', '123 Main St', '+1234567890',
        '2024-03-30T22:22:09.266615Z', '2024-03-30T22:28:19.266615Z'),
       ('mariya.petrivna@example.com', 'Марія', 'Петрівна', '1985-02-02', '456 Maple Ave',
        '+380987654321', '2024-03-31T22:22:09.266615Z', '2024-03-31T22:28:19.266615Z'),
       ('alexey.pan@example.com', 'Олексій', 'Панчер', '1990-03-03', '789 Oak St',
        '+4567890123', '2024-04-01T22:22:09.266615Z', '2024-04-01T22:28:19.266615Z'),
       ('dar.gol@gmail.com', 'Дарина', 'Голова', '1995-04-04', '321 Pine Ave',
        '+7890123456', '2024-04-02T22:22:09.266615Z', '2024-04-02T22:28:19.266615Z');

-- Add data with a dot in names or two-word names
INSERT INTO users (email, first_name, last_name, birth_date, address, phone_number, created_at,
                   last_modified_at)
VALUES ('john.doe.jr@example.com', 'John.Doe', 'Jr', '2000-01-01', '123 Main St', '+1234567890',
        '2024-04-03T22:22:09.266615Z', '2024-04-03T22:28:19.266615Z'),
       ('jane.doe.smith@example.com', 'Jane Doe', 'Smith', '1985-02-02', '456 Maple Ave',
        '+380987654321', '2024-04-04T22:22:09.266615Z', '2024-04-04T22:28:19.266615Z');