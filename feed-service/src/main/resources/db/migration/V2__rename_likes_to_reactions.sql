-- Renombrar columna likes_count a reactions_count
ALTER TABLE posts CHANGE COLUMN likes_count reactions_count INT NOT NULL DEFAULT 0;

-- Actualizar el contador para reflejar todas las reacciones existentes
UPDATE posts p
SET p.reactions_count = (
    SELECT COUNT(*)
    FROM reactions r
    WHERE r.post_id = p.id
);
