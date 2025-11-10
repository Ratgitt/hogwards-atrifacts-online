--liquibase formatted sql

--changeset ratmir:1
ALTER TABLE public.users
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
/* liquibase rollback
ALTER TABLE public.users
DROP COLUMN IF EXISTS created_at;
*/

--changeset ratmir:2
ALTER TABLE public.users
ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP;
/* liquibase rollback
ALTER TABLE public.users
DROP COLUMN IF EXISTS modified_at;
*/

--changeset ratmir:3
ALTER TABLE public.users
RENAME COLUMN modified_at TO last_modified_at;
/* liquibase rollback
ALTER TABLE public.users
RENAME COLUMN last_modified_at TO modified_at;
*/

--changeset ratmir:4
ALTER TABLE public.users
ADD COLUMN IF NOT EXISTS created_by VARCHAR(32);
/* liquibase rollback
ALTER TABLE public.users
DROP COLUMN IF EXISTS created_by;
*/

--changeset ratmir:5
ALTER TABLE public.users
ADD COLUMN IF NOT EXISTS last_modified_by VARCHAR(32);
/* liquibase rollback
ALTER TABLE public.users
DROP COLUMN IF EXISTS last_modified_by;
*/