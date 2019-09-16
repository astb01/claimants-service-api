CREATE TABLE IF NOT EXISTS benefits (
  id BIGINT NOT NULL PRIMARY key auto_increment,
  name VARCHAR(50) NOT NULL,
  unique(name)
);

CREATE UNIQUE INDEX idx_benefits
  ON benefits(name);

CREATE TABLE IF NOT EXISTS claimants (
  id BIGINT NOT NULL PRIMARY key auto_increment,
  title VARCHAR(6) NOT NULL,
  first_name VARCHAR(80) NOT NULL,
  last_name VARCHAR(80) NOT NULL,
  gender VARCHAR(1) NOT NULL,
  dob DATE NOT NULL,
  street VARCHAR(80) NOT NULL,
  city VARCHAR(80) NOT NULL,
  post_code VARCHAR(6) NOT NULL,
  refno VARCHAR(9) NOT NULL,
  driving_licence_no VARCHAR(80) NULL,

  unique(dob, first_name, last_name, refno, driving_licence_no)
);

CREATE UNIQUE INDEX idx_claimants
  ON claimants(dob, first_name, last_name, refno, driving_licence_no);

CREATE TABLE IF NOT EXISTS claimants_benefits (
  id BIGINT NOT NULL PRIMARY key auto_increment,
  claimants_id BIGINT NULL REFERENCES claimants(id) on DELETE CASCADE,
  benefits_id BIGINT NOT NULL REFERENCES benefits(id) on DELETE CASCADE,
  UNIQUE (claimants_id, benefits_id)
);

CREATE UNIQUE INDEX idx_claimants_benefits
  ON claimants_benefits(claimants_id, benefits_id);

/* Authentication Purposes Only */
CREATE TABLE IF NOT EXISTS auth_users (
  id        BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  username  VARCHAR(255) NOT NULL,
  password  VARCHAR(255) NOT NULL,
  UNIQUE (username)
);