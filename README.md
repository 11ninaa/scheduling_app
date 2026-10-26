

\# Schedura



Schedura is a full-stack scheduling application designed to simplify session management and booking. It provides a user-friendly interface for viewing sessions, managing bookings, and tracking scheduling information.



\## Features



\- View and manage scheduled sessions

\- Book sessions

\- Track session and booking statuses

\- View scheduling statistics

\- AI-powered functionality using the Gemini API



\## Tech Stack



\*\*Frontend\*\*

\- React

\- TypeScript

\- Vite



\*\*Backend\*\*

\- Java

\- Spring Boot

\- Maven

\- Spring Data JPA



\*\*Database\*\*

\- PostgreSQL



\*\*AI Integration\*\*

\- Google Gemini API



\## Project Structure



```text

teamlift\_scheduler/

├── frontend/

└── backend/

```



\## Prerequisites



Make sure you have the following installed:



\- Node.js and npm

\- Java JDK

\- Maven (or use the included Maven Wrapper)

\- PostgreSQL



\## Getting Started



\### 1. Clone the repository



```bash

git clone https://github.com/11ninaa/teamlift\_challenge.git

cd teamlift\_challenge

```



\### 2. Configure the database



Create a PostgreSQL database named `teamlift\_db`.



Configure the database connection in the backend's local configuration. Set the following environment variables according to your local PostgreSQL setup:



```text

DB\_URL=jdbc:postgresql://localhost:5433/teamlift\_db

DB\_USERNAME=your\_postgres\_username

DB\_PASSWORD=your\_postgres\_password

GEMINI\_API\_KEY=your\_gemini\_api\_key

```



Use your own database credentials and Gemini API key. Never commit passwords or API keys to the repository.



\### 3. Run the backend



Open a terminal in the `backend/` directory and run:



```bash

.\\mvnw.cmd spring-boot:run

```



On macOS or Linux, run:



```bash

./mvnw spring-boot:run

```



The backend uses the database configuration provided through the environment variables.



\### 4. Run the frontend



Open another terminal and navigate to the `frontend/` directory:



```bash

npm install

npm run dev

```



Open the local URL displayed by Vite in your browser.



\## Configuration



Keep local configuration files, environment files, passwords, and API keys out of version control. Configure the required environment variables before starting the application.



\## License



This project was developed as a full-stack application.



