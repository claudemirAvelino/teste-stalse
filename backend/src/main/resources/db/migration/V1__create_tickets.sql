CREATE TABLE tickets (
    id            UUID         PRIMARY KEY,
    customer_name VARCHAR(120) NOT NULL,
    channel       VARCHAR(20)  NOT NULL,
    subject       VARCHAR(200) NOT NULL,
    description   TEXT,
    category      VARCHAR(80)  NOT NULL,
    status        VARCHAR(20)  NOT NULL,
    priority      VARCHAR(10)  NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_tickets_status     ON tickets (status);
CREATE INDEX idx_tickets_priority   ON tickets (priority);
CREATE INDEX idx_tickets_category   ON tickets (category);
CREATE INDEX idx_tickets_created_at ON tickets (created_at DESC);
