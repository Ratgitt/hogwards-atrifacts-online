-- Очистка всех таблиц и сброс счетчиков IDENTITY
TRUNCATE TABLE
    spring_course.users_chat,
    spring_course.chat,
    spring_course.payment,
    spring_course.users,
    spring_course.company_locales,
    spring_course.company
RESTART IDENTITY CASCADE;