-- Creating user:

CREATE USER "schoolAdmin" IDENTIFIED BY "school";

GRANT ALL PRIVILEGES ON *.* TO "schoolAdmin";


-- Creating database:

CREATE DATABASE school_admin_db;

USE school_admin_db;


-- Creting tables:

CREATE TABLE IF NOT EXISTS student(
	id INT AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    
    CONSTRAINT student_pk PRIMARY KEY(id),
    CONSTRAINT student_first_name_is_valid CHECK(LENGTH(first_name) > 0),
    CONSTRAINT student_last_name_is_valid CHECK(LENGTH(last_name) > 0)
);

CREATE TABLE IF NOT EXISTS teacher(
	id INT AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    
    CONSTRAINT teacher_pk PRIMARY KEY(id),
    CONSTRAINT teacher_first_name_is_valid CHECK(LENGTH(first_name) > 0),
    CONSTRAINT teacher_last_name_is_valid CHECK(LENGTH(last_name) > 0)
);

CREATE TABLE IF NOT EXISTS class(
	id INT AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    teacher_id INT,
    
    CONSTRAINT class_pk PRIMARY KEY(id),
    CONSTRAINT class_name_is_valid CHECK(LENGTH(name) > 0),
    CONSTRAINT class_teacher_fk FOREIGN KEY(teacher_id) REFERENCES teacher(id) ON DELETE SET NULL
);

-- Relationship between student and class
CREATE TABLE IF NOT EXISTS student_class_rel(
	id INT AUTO_INCREMENT,
    student_id INT NOT NULL,
    class_id INT NOT NULL,
    
    CONSTRAINT student_class_rel_pk PRIMARY KEY(id),
    CONSTRAINT student_class_rel_student_fk FOREIGN KEY(student_id) REFERENCES student(id) ON DELETE CASCADE,
    CONSTRAINT student_class_rel_class_fk FOREIGN KEY(class_id) REFERENCES class(id) ON DELETE CASCADE
);


-- Set auto increments

ALTER TABLE student AUTO_INCREMENT = 1;

ALTER TABLE teacher AUTO_INCREMENT = 1;

ALTER TABLE class AUTO_INCREMENT = 1;

ALTER TABLE student_class_rel AUTO_INCREMENT = 1;


-- Test area: