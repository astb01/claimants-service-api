# Claimant Service API

## Overview

This project represents a simple REST API written in Java using Spring Boot.

The project uses the following dependencies: Lombok, Apache Commons, Flyway, Wiremock, RestAssured as well as other related dependencies.

## Getting Started

To run the code simply open this project up in IntelliJ.

All source code can be found under __src/main/java__ and all tests under __src/test/java__.

The code includes unit and integration tests and uses Wiremock to mock out any external services.

## Authentication

This project uses JWT as a means of securing the endpoints it produces.

## Endpoints

All endpoints to do with claimants are:

| Method | Path                        | Description                                                                                        |
| ------ | ----------------------------| ---------------------------------------------------------------------------------------------------|
| POST   | /auth/register              | Register an account. Requires a __username__ and __password__.                                     |
| POST   | /login                      | Log in to retrieve access token as a response. Requires a valid __username__ and __password__.     |
| POST   | /api/claimants              | Adds a claimant                                                                                    |
| GET    | /api/claimants              | Retrieves all claimants                                                                            |
| GET    | /api/claimants/:id          | Retrieves a claimant by it's ID                                                                    |
| GET    | /api/claimants/refno/:refNo | Retrieves a claimant by it's ID                                                                    |
| DELETE | /api/claimants/:id          | Delete a claimant by it's ID                                                                       |
| PUT    | /api/claimants/:id          | Updates a claimant by it's ID. Updates a claimant                                                  |

## Docker

This project contains a supporting __Dockerfile__ and __docker-compose.yml__.

It also contains Docker container tests (under tests/container) that are tested within the CI pipeline.