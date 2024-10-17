CREATE TABLE courses (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         capacity INT NOT NULL,
                         current_enrollment INT DEFAULT 0,
                         start_date TIMESTAMP WITH TIME ZONE NOT NULL,
                         end_date TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE students (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          surname VARCHAR(255) NOT NULL
);

CREATE TABLE enrollment (
                            id SERIAL PRIMARY KEY,
                            student_id INT REFERENCES students(id),
                            course_id INT REFERENCES courses(id)
);