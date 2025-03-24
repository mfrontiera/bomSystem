-- 1. Wstawienie słownika "Occupation"
INSERT INTO dictionary (id, name, version)
VALUES (1, 'OCCUPATION', 1);

-- 2. Wstawienie wersji słownika dla "Occupation"
-- (opcjonalnie możesz dodać kolumnę z datą obowiązywania, np. effective_from)
INSERT INTO dictionary_version (id, dictionary_id, version)
VALUES (1, 1, 1);

-- 3. Wstawienie pozycji słownikowych (OccupationItem)
INSERT INTO dictionary_item (id, dictionary_version_id, symbol, description, version)
VALUES (1, 1, 'INSURANCE_CLERK', 'Insurance Clerk', 1);

INSERT INTO dictionary_item (id, dictionary_version_id, symbol, description, version)
VALUES (2, 1, 'MORTARMAN', 'Mortarman', 1);

INSERT INTO dictionary_item (id, dictionary_version_id, symbol, description, version)
VALUES (3, 1, 'BEER_COIL_CLEANER', 'Beer Coil Cleaner', 1);

INSERT INTO dictionary_item (id, dictionary_version_id, symbol, description, version)
VALUES (4, 1, 'SCALE_ATTENDANT', 'Scale Attendant', 1);
