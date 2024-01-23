# Setting up PostgreSQL using Docker

This guide explains how to set up a PostgreSQL using Docker containers. Follow the steps below to get started.

> Make sure you have the Docker installed on your machine. If not, download and install Docker from
> the [Official Docker Website](https://www.docker.com/get-started).

## Steps

### 1. Pull the PostgreSQL Docker Image

If you haven't already, pull the PostgreSQL image from Docker Hub by executing the following command in your terminal or
command prompt:

```bash
docker pull postgres
```

### 2. Run a PostgreSQL Container

Run a Docker container using the PostgreSQL image with your preferred credentials (username, password, and database
name).

Replace `POSTGRES_USER`, `POSTGRES_PASSWORD`, `POSTGRES_DB`, and ports with your desired values.

```bash
docker run -d --name postgres-container -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=school_db -p 5432:5432 postgres
```

### 3. Exit and Stop the Container (Optional)

After finishing your tasks, exit the PostgreSQL client (psql) by typing `\q` and stop the Docker container:

```bash
docker stop postgres-container
```

This command stops the PostgreSQL container while preserving the data. You can start the container again later using
docker start postgres-container.

