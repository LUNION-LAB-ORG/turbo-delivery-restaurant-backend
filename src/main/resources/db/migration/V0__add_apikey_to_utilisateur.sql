-- Ajout de la colonne apikey si elle n'existe pas
ALTER TABLE utilisateur
ADD COLUMN IF NOT EXISTS apikey VARCHAR(255);

-- Ajout de la contrainte unique si elle n'existe pas
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uk_utilisateur_apikey'
    ) THEN
        ALTER TABLE utilisateur
        ADD CONSTRAINT uk_utilisateur_apikey UNIQUE (apikey);
    END IF;
END$$;

-- Pré-remplissage de la colonne pour éviter NOT NULL error
UPDATE utilisateur
SET apikey = CONCAT('TEMP-', gen_random_uuid())
WHERE apikey IS NULL;

-- Rendre la colonne NOT NULL
ALTER TABLE utilisateur
ALTER COLUMN apikey SET NOT NULL;
