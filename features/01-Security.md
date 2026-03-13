# Security

## Multi user application

The application will be used by multiple users and will need to be secured.
This includes implementing authentication and authorization mechanisms to ensure that only authorized users can access the data and perform certain actions.

## Access control

There will be two types of users: administrators and regular users.
Regular users will be able to create, edit, and delete their own golf data. 
Administrators will have the same privileges as regular users, plus the ability to manage users (listing all users, creating a new user, or editing details of a user).
There are two types of data: golf data and user details.
Regarding the golf data, all users will have access only to golf data they have created! 
Administrators will have access to their golf data and all user details (administrators will be able to see/change details of all users).  

## Login and logout

The application will serve nothing to an unauthenticated user and will redirect to the login page if not authenticated.
Users will log in using their email address and password.
Only active users can log in.
After logging in, or when coming back, the application will redirect the user to the home page.
Users can log out of the application at any time.
Users stay logged in for 15 days, then require re-login.
The application will on each request check whether the current user is still active and log the user out if he/she is not active anymore.

## User model

For each user the application will register the following information:

- full name
- email address (will be used as a login)
- password (at least 8 characters)
- role (administrator or regular user)
- active status

The details of the user will be stored in a database. The application will use a secure password hashing algorithm to store user passwords securely in the database.

All users can edit their own information, but they can change only the full name and the password.

## Password reset

Users can't request a password reset. They have to ask the administrator to do it.

## Initial admin user

When starting, the application will check whether there is an administrator user in the database. 
If not, the application will create an administrator user with a default password. 
The administrator user will be able to manage users, including creating new users and editing user details.

The details of the default administrator user will be configurable in the application properties and when deployed will be set to the value of the environment variables 

- `ADMIN_USER_EMAIL`
- `ADMIN_USER_PASSWORD`,
- `ADMIN_USER_FULL_NAME`.

The default administrator user will be created with status active.

## Deployment

The application will use a secure connection (HTTPS) to ensure that sensitive data is transmitted securely. This will be handled by the reverse proxy and Let's Encrypt certificates.
All the traffic will be redirected to the HTTPS by the reverse proxy. 
