# Online Bookstore

## Environment variables

Create a `.env` file based on `.env.template` and fill in the required values.

⚠️ Do not commit the `.env` file to the repository (it contains sensitive data).

---

## Run with Docker

Build the project:

```bash
mvn clean package
```

Run the application with Docker:

```bash
docker compose up --build
```

---

## Notes

* On the first run, the application may fail to connect to the database because MySQL is still starting.
* If that happens, simply run:

```bash
docker compose down
docker compose up
```

