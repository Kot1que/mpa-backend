create table translation (
    id bigserial primary key,
    key_name varchar(255),
    entity_id bigint,
    language varchar(16),
    value varchar(255)
);

INSERT INTO translation (key_name, entity_id, language, value) VALUES ('Medicine.name', 5, 'en', 'Ventolin');
INSERT INTO translation (key_name, entity_id, language, value) VALUES ('Medicine.name', 11, 'en', 'Foster');
INSERT INTO translation (key_name, entity_id, language, value) VALUES ('Medicine.name', 18, 'en', 'Atrovent');
INSERT INTO translation (key_name, entity_id, language, value) VALUES ('Disease.name', 1, 'en', 'Arrhythmia');

