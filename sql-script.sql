CREATE DATABASE jobportal;
USE jobportal;

CREATE TABLE users_type(
	user_type_id INT NOT NULL AUTO_INCREMENT,
    user_type_name VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY(user_type_id)
);
INSERT INTO users_type VALUES
(1,"Recruiter"),(2,"Job Seeker");

CREATE TABLE users(
	id INT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) DEFAULT NULL,
    password VARCHAR(255) DEFAULT NULL,
    is_active bit(1) DEFAULT NULL,
    registration_date DATETIME(6) DEFAULT NULL,
    user_type_id INT DEFAULT NULL,
    PRIMARY KEY(id),
    UNIQUE KEY(email),
    FOREIGN KEY(user_type_id) REFERENCES users_type(user_type_id)
);

CREATE TABLE recruiter_profile(
	user_account_id INT NOT NULL,
    first_name VARCHAR(255) DEFAULT NULL,
    last_name VARCHAR(255) DEFAULT NULL,
    country VARCHAR(255) DEFAULT NULL,
    city VARCHAR(255) DEFAULT NULL,
    state VARCHAR(255) DEFAULT NULL,
    company VARCHAR(255) DEFAULT NULL,
    profile_photo VARCHAR(64) DEFAULT NULL,
    PRIMARY KEY(user_account_id),
    FOREIGN KEY(user_account_id) REFERENCES users(id)
);

CREATE TABLE job_company(
	id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) DEFAULT NULL,
    logo VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE job_location(
	id INT NOT NULL AUTO_INCREMENT,
    country VARCHAR(255) DEFAULT NULL,
    city VARCHAR(255) DEFAULT NULL,
    state VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE job_post_activity(
	job_post_id INT NOT NULL AUTO_INCREMENT,
    job_title VARCHAR(255) DEFAULT NULL,
    job_type VARCHAR(255) DEFAULT NULL,
    remote VARCHAR(255) DEFAULT NULL,
    salary VARCHAR(255) DEFAULT NULL,
    description_of_job VARCHAR(10000) DEFAULT NULL,
    posted_date DATETIME(6) DEFAULT NULL,
    job_company_id INT DEFAULT NULL,
    job_location_id INT DEFAULT NULL,
    posted_by_id INT DEFAULT NULL,
    PRIMARY KEY(job_post_id),
    FOREIGN KEY(job_company_id) REFERENCES job_company(id),
    FOREIGN KEY(job_location_id) REFERENCES job_location(id),
    FOREIGN KEY(posted_by_id) REFERENCES users(id)
);

CREATE TABLE job_seeker_profile(
	user_account_id INT NOT NULL,
    first_name VARCHAR(255) DEFAULT NULL,
    last_name VARCHAR(255) DEFAULT NULL,
    country VARCHAR(255) DEFAULT NULL,
    city VARCHAR(255) DEFAULT NULL,
    state VARCHAR(255) DEFAULT NULL,
    employment_type VARCHAR(255) DEFAULT NULL,
    work_authorization VARCHAR(255) DEFAULT NULL,
    profile_photo VARCHAR(255) DEFAULT NULL,
    resume VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY(user_account_id),
    FOREIGN KEY(user_account_id) REFERENCES users(id)
);

CREATE TABLE skills(
	id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) DEFAULT NULL,
    experience_level VARCHAR(255) DEFAULT NULL,
    years_of_experience VARCHAR(255) DEFAULT NULL,
    job_seeker_profile INT DEFAULT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(job_seeker_profile)REFERENCES job_seeker_profile(user_account_id)
);

CREATE TABLE job_seeker_apply(
	id INT NOT NULL AUTO_INCREMENT,
    apply_date VARCHAR(255) DEFAULT NULL,
    cover_letter VARCHAR(255) DEFAULT NULL,
    job INT DEFAULT NULL,
    user_id INT DEFAULT NULL,
    PRIMARY KEY(id),
    UNIQUE KEY(user_id,job),
    FOREIGN KEY(job)REFERENCES job_post_activity(job_post_id),
    FOREIGN KEY(user_id) REFERENCES job_seeker_profile(user_account_id)
);

CREATE TABLE job_seeker_save(
	id INT NOT NULL AUTO_INCREMENT,
    job INT DEFAULT NULL,
    user_id INT DEFAULT NULL,
    PRIMARY KEY(id),
    UNIQUE KEY(user_id,job),
    FOREIGN KEY(job) REFERENCES job_post_activity(job_post_id),
    FOREIGN KEY(user_id) REFERENCES job_seeker_profile(user_account_id)
);
