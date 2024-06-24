create table groups
(
    group_id   serial primary key,
    group_name varchar(50) not null
);

create table students
(
    student_id serial primary key,
    group_id   int references groups (group_id),
    first_name varchar(30) not null,
    last_name  varchar(30) not null
);

create table courses
(
    course_id          serial primary key,
    course_name        varchar(50) not null,
    course_description text
);

create table student_courses
(
    student_id int references students (student_id),
    course_id  int references courses (course_id),
    primary key (student_id, course_id)
);
