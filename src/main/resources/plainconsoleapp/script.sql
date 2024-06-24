insert into groups(group_name)
values ('XK-55'),
       ('DM-78'),
       ('QO-87'),
       ('BI-01'),
       ('BE-30'),
       ('HU-80'),
       ('XJ-38'),
       ('QY-30'),
       ('RR-29'),
       ('FU-44');

insert into students (group_id, first_name, last_name)
select floor(random() * 10) + 1 as group_id,
       first_names.first_name,
       last_names.last_name
from (select 'David' as first_name
      union all
      select 'Henry'
      union all
      select 'Alice'
      union all
      select 'Ivy'
      union all
      select 'Grace'
      union all
      select 'Frank'
      union all
      select 'Bob'
      union all
      select 'Jack'
      union all
      select 'Emma'
      union all
      select 'Charlie') as first_names
         cross join
     (select 'Garcia' as last_name
      union all
      select 'Fisher'
      union all
      select 'Clark'
      union all
      select 'Irwin'
      union all
      select 'Jones'
      union all
      select 'Davis'
      union all
      select 'Hall'
      union all
      select 'Brown'
      union all
      select 'Adams'
      union all
      select 'Evans'
      union all
      select 'Martinez'
      union all
      select 'Moore'
      union all
      select 'Nelson'
      union all
      select 'Parker'
      union all
      select 'Reed'
      union all
      select 'Rogers'
      union all
      select 'Smith'
      union all
      select 'Taylor'
      union all
      select 'Walker'
      union all
      select 'Young') as last_names
limit 200;

insert into courses (course_name, course_description)
values ('Art', 'Study of visual arts and creative expression'),
       ('Biology', 'Study of living organisms and their interactions'),
       ('Chemistry', 'Study of matter, its properties, composition, and reactions'),
       ('Computer Science', 'Study of algorithms, data structures, and computational systems'),
       ('Geography',
        'Study of Earth''s landscapes, environments, and the relationships between people and their environments'),
       ('History', 'Study of past events, societies, and civilizations'),
       ('Literature', 'Study of written works, including novels, poems, and plays'),
       ('Math', 'Study of numbers, quantities, shapes, and patterns'),
       ('Music', 'Study of sound, rhythm, melody, and composition'),
       ('Physics', 'Study of matter, energy, motion, and the fundamental forces of nature');

insert into course_students (student_id, course_id)
select (FLOOR(RANDOM() * 200) + 1) as student_id,
       (FLOOR(RANDOM() * 10) + 1)  as course_id
from generate_series(1, 450) s
group by student_id, course_id
limit 450;

