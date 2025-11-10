--liquibase formatted sql

--changeset ratmir:move_schema_01
CREATE SCHEMA IF NOT EXISTS spring_course;
--rollback DROP SCHEMA IF EXISTS spring_course CASCADE;

--changeset ratmir:move_schema_02
SET session_replication_role = replica;
--rollback SET session_replication_role = DEFAULT;

--changeset ratmir:move_schema_03
ALTER TABLE public.company SET SCHEMA spring_course;
--rollback ALTER TABLE spring_course.company SET SCHEMA public;

--changeset ratmir:move_schema_04
ALTER TABLE public.company_locales SET SCHEMA spring_course;
--rollback ALTER TABLE spring_course.company_locales SET SCHEMA public;

--changeset ratmir:move_schema_05
ALTER TABLE public.users SET SCHEMA spring_course;
--rollback ALTER TABLE spring_course.users SET SCHEMA public;

--changeset ratmir:move_schema_06
ALTER TABLE public.payment SET SCHEMA spring_course;
--rollback ALTER TABLE spring_course.payment SET SCHEMA public;

--changeset ratmir:move_schema_07
ALTER TABLE public.chat SET SCHEMA spring_course;
--rollback ALTER TABLE spring_course.chat SET SCHEMA public;

--changeset ratmir:move_schema_08
ALTER TABLE public.users_chat SET SCHEMA spring_course;
--rollback ALTER TABLE spring_course.users_chat SET SCHEMA public;

--changeset ratmir:move_schema_09
SET session_replication_role = DEFAULT;
--rollback SET session_replication_role = replica;