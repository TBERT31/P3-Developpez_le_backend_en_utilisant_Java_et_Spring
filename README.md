[![forthebadge](https://forthebadge.com/images/badges/cc-0.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-javascript.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/uses-css.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)

# Estate

## Start the frontend

The frontend of this project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.

1. **Git clone:**

    ```sh
    git clone https://github.com/TBERT31/P3-Developpez_le_backend_en_utilisant_Java_et_Spring/tree/main
    ```

2. **Go inside folder:**

    ```sh
    cd backend
    ```

3. **Install dependencies:**

    ```sh
    npm install
    ```

4. **Development server:**

> Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

5. **Build**

> Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Start the backend

The backend of this project is built with Java version 17 and Spring Boot version 3.2.5.

### Prerequisites

- [Java JDK 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) or later
- [Maven](https://maven.apache.org/download.cgi)
- [MySQL](https://dev.mysql.com/downloads/installer/) or [XAMPP](https://www.apachefriends.org/index.html)

### MySQL Setup

1. **Install MySQL or XAMPP:**

    - **MySQL:** Download and install from [here](https://dev.mysql.com/downloads/installer/).
    - **XAMPP:** Download and install from [here](https://www.apachefriends.org/index.html).

2. **Create the database:**

    - If using MySQL, open MySQL Workbench and create a new schema.
    - If using XAMPP, start the Apache and MySQL modules from the XAMPP Control Panel and use phpMyAdmin to create a new database.

3. **Run the SQL script:**

    The SQL script to create the schema is available at `ressources/sql/script.sql`.

    ```sh
    mysql -u your_username -p chatop < ressources/sql/script.sql
    ```

### Running the backend

1. **(If you haven't already done so) Clone the repository:**

    ```sh
    git clone https://github.com/TBERT31/P3-Developpez_le_backend_en_utilisant_Java_et_Spring/tree/main
    ```

2. **Go inside the backend folder:**

    ```sh
    cd backend
    ```

3. **Configure the application properties:**

    Open `src/main/resources/application.properties` and configure the following properties with your MySQL database details:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/chatop
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    # Uncomment and adjust the dialect if needed
    # spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect

    server.port=3001
    ```

    If you are using a different MySQL dialect, you may need to configure the Hibernate dialect:

    ```properties
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
    ```

4. **Install dependencies and run the application:**

    ```sh
    mvn clean install
    mvn spring-boot:run
    ```

    The backend server will start on `http://localhost:3001`.

## Resources

### Mockoon env

Download Mockoon here: [Mockoon Download](https://mockoon.com/download/)

After installing you could load the environment:

> ressources/mockoon/rental-oc.json

directly inside Mockoon:

> File > Open environment

For launching the Mockoon server click on play button.

Mockoon documentation: [Mockoon Docs](https://mockoon.com/docs/latest/about/)

### Postman collection

For Postman import the collection:

> ressources/postman/rental.postman_collection.json

by following the documentation:

[Importing Data into Postman](https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman)

## Authors

Main developer: Thomas Berteau
