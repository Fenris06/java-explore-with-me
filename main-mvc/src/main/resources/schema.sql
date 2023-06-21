CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  email VARCHAR(254) NOT NULL,
  name VARCHAR(250) NOT NULL,
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS locations (
 id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 lat VARCHAR(100) NOT NULL,
 lon VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
 id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 category BIGINT NOT NULL,
 confirmed_requests BIGINT,
 created_on TIMESTAMP NOT NULL,
 description VARCHAR(1000) NOT NULL,
 event_date TIMESTAMP NOT NULL,
 initiator BIGINT NOT NULL,
 location BIGINT NOT NULL,
 paid BOOLEAN NOT NULL,
 participant_limit BIGINT NOT NULL,
 request_moderation BOOLEAN NOT NULL,
 CONSTRAINT fk_events_to_users FOREIGN KEY(initiator) REFERENCES users(id) ON DELETE CASCADE,
 CONSTRAINT fk_events_to_categories FOREIGN KEY(category) REFERENCES categories(id) ON DELETE CASCADE,
 CONSTRAINT fk_events_to_locations FOREIGN KEY(location) REFERENCES locations(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests (
 id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 created TIMESTAMP NOT NULL,
 event BIGINT NOT NULL,
 requester BIGINT NOT NULL,
 status VARCHAR(60) NOT NULL,
 CONSTRAINT fk_requests_to_events FOREIGN KEY(event) REFERENCES events(id) ON DELETE CASCADE,
 CONSTRAINT fk_requests_to_users FOREIGN KEY(requester) REFERENCES users(id) ON DELETE CASCADE
);

