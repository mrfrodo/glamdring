-- System Design Catalog seed: The Fan-Out Architecture
INSERT INTO system_design (name, classic_example, core_blueprint)
SELECT 'The Fan-Out Architecture',
       'News Feed / Social Media Timeline',
       'Handles asynchronous distribution of content from a single producer to millions of followers using push (write-time) or pull (read-time) models.'
WHERE NOT EXISTS (SELECT 1 FROM system_design WHERE name = 'The Fan-Out Architecture');

INSERT INTO system_design_keycomponent (id, position, name, role)
SELECT d.id, c.position, c.name, c.role
FROM system_design d
CROSS JOIN (
    SELECT 0 AS position, 'Message Queue' AS name, 'Decouples post creation from feed generation.' AS role
    UNION ALL SELECT 1, 'Fan-Out Workers', 'Background workers that duplicate posts into individual user timeline caches.'
    UNION ALL SELECT 2, 'Graph / NoSQL Database', 'Stores user relationships and timeline pointers.'
) c
WHERE d.name = 'The Fan-Out Architecture'
  AND NOT EXISTS (SELECT 1 FROM system_design_keycomponent k WHERE k.id = d.id AND k.position = c.position);

INSERT INTO system_design_technology (id, component_position, position, technology)
SELECT d.id, t.component_position, t.position, t.technology
FROM system_design d
CROSS JOIN (
    SELECT 0 AS component_position, 0 AS position, 'Kafka' AS technology
    UNION ALL SELECT 0, 1, 'RabbitMQ'
) t
WHERE d.name = 'The Fan-Out Architecture'
  AND NOT EXISTS (SELECT 1 FROM system_design_technology x
                  WHERE x.id = d.id AND x.component_position = t.component_position AND x.position = t.position);

INSERT INTO system_design_reuse (id, position, target)
SELECT d.id, r.position, r.target
FROM system_design d
CROSS JOIN (
    SELECT 0 AS position, 'Activity streams' AS target
    UNION ALL SELECT 1, 'Notification feeds'
    UNION ALL SELECT 2, 'Live sports and stock tickers'
    UNION ALL SELECT 3, 'Follower-based chat broadcasts'
) r
WHERE d.name = 'The Fan-Out Architecture'
  AND NOT EXISTS (SELECT 1 FROM system_design_reuse x WHERE x.id = d.id AND x.position = r.position);
