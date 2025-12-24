# API Documentation

This document outlines the API endpoints for the Issue Tracker application.

## 1. Authentication

### Register

**Endpoint:** `POST /api/auth/register`
**Description:** Registers a new user and returns a JWT token.

**Request Body:**

```json
{
  "username": "jdoe",
  "email": "jdoe@example.com",
  "password": "securePassword123",
  "firstName": "John",
  "lastName": "Doe"
}
```

**Response Body:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": 1
}
```

### Login

**Endpoint:** `POST /api/auth/login`
**Description:** Authenticates a user and returns a JWT token.

**Request Body:**

```json
{
  "username": "jdoe",
  "password": "securePassword123"
}
```

**Response Body:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": 1
}
```

---

## 2. Projects

### Create Project

**Endpoint:** `POST /api/projects/create`
**Description:** Creates a new project. Requires authentication.

**Request Body:**

```json
{
  "key": "PROJ",
  "name": "Project Alpha",
  "description": "Main project for development"
}
```

**Response Body:**

```json
{
  "id": 1,
  "key": "PROJ",
  "name": "Project Alpha",
  "description": "Main project for development",
  "ownerId": 1,
  "archived": false,
  "createdAt": "2023-10-27T10:00:00",
  "updatedAt": "2023-10-27T10:00:00"
}
```

### Get All Projects

**Endpoint:** `GET /api/projects/getProjects`
**Description:** Retrieves a list of all projects.

**Response Body:**

```json
[
  {
    "id": 1,
    "key": "PROJ",
    "name": "Project Alpha",
    "description": "Main project for development",
    "ownerId": 1,
    "archived": false,
    "createdAt": "2023-10-27T10:00:00",
    "updatedAt": "2023-10-27T10:00:00"
  }
]
```

### Get Project by Key

**Endpoint:** `GET /api/projects/{key}/get`
**Description:** Retrieves details of a specific project.

**Response Body:**

```json
{
  "id": 1,
  "key": "PROJ",
  "name": "Project Alpha",
  "description": "Main project for development",
  "ownerId": 1,
  "archived": false,
  "createdAt": "2023-10-27T10:00:00",
  "updatedAt": "2023-10-27T10:00:00"
}
```

### Update Project

**Endpoint:** `PUT /api/projects/{key}/update`
**Description:** Updates project details.

**Request Body:**

```json
{
  "name": "Project Alpha Updated",
  "description": "Updated description",
  "archived": false
}
```

**Response Body:** (Same as Project Response)

### Archive Project

**Endpoint:** `PATCH /api/projects/{key}/archive`
**Description:** Archives a project.

**Response Body:** (Same as Project Response with `archived: true`)

### Delete Project

**Endpoint:** `DELETE /api/projects/{key}/delete`
**Description:** Deletes a project.

**Response Body:** `204 No Content`

---

## 3. Issues

### Create Issue

**Endpoint:** `POST /api/issues`
**Description:** Creates a new issue in a project.

**Request Body:**

```json
{
  "projectKey": "PROJ",
  "summary": "Fix login bug",
  "description": "Login fails when password contains special chars.",
  "typeId": 1,
  "priorityId": 2,
  "reporterId": 1,
  "assigneeId": 2,
  "dueDate": "2023-12-31" // Optional
}
```

_(Note: `typeId` maps to Issue Type e.g., Bug, Task. `priorityId` maps to Priority e.g., High, Medium)_

**Response Body:**

```json
{
  "id": 101,
  "key": "PROJ-1",
  "projectKey": "PROJ",
  "summary": "Fix login bug",
  "description": "Login fails when password contains special chars.",
  "type": "Bug",
  "priority": "High",
  "status": "OPEN",
  "reporterId": 1,
  "assigneeId": 2,
  "dueDate": "2023-12-31T00:00:00",
  "createdAt": "2023-10-27T11:00:00",
  "updatedAt": "2023-10-27T11:00:00"
}
```

### Get Issue

**Endpoint:** `GET /api/issues/{key}`
**Description:** Retrieves an issue by its key (e.g., PROJ-1).

**Response Body:** (Same as Issue Response)

### Get Issues by Project

**Endpoint:** `GET /api/issues/project/{projectKey}`
**Description:** Retrieves all issues for a given project.

**Response Body:** List of Issue Responses.

### Update Issue

**Endpoint:** `PUT /api/issues/{key}`
**Description:** Updates an issue.

**Request Body:**

```json
{
  "summary": "Updated summary",
  "description": "Updated description",
  "priorityId": 1,
  "statusId": 2,
  "assigneeId": 3,
  "dueDate": "2024-01-01"
}
```

**Response Body:** (Same as Issue Response)

### Delete Issue

**Endpoint:** `DELETE /api/issues/{key}`
**Description:** Deletes an issue.

**Response Body:** `204 No Content`

---

## 4. Teams

### Create Team

**Endpoint:** `POST /api/team/create_team`
**Description:** Creates a new team.

**Request Body:**

```json
{
  "name": "Backend Team",
  "description": "Handles server-side logic"
}
```

**Response Body:**

```json
{
  "id": 1,
  "name": "Backend Team",
  "description": "Handles server-side logic"
}
```

### Assign Member to Team

**Endpoint:** `POST /api/team/assignMember_team`
**Description:** Adds a user to a team.

**Request Body:**

```json
{
  "teamId": 1,
  "userId": 2,
  "role": "DEVELOPER"
}
```

**Response Body:**

```json
{
  "id": 1,
  "team": { ... },
  "user": { ... },
  "role": "DEVELOPER"
}
```

### Assign Project to Team

**Endpoint:** `POST /api/team/assign_project`
**Description:** Assigns a team to a project.

**Request Body:**

```json
{
  "teamId": 1,
  "projectKey": "PROJ"
}
```

**Response Body:**

```json
{
  "id": 1,
  "team": { ... },
  "project": { ... }
}
```

---

## 5. Sprints

### Create Sprint

**Endpoint:** `POST /api/sprints`
**Description:** Creates a new sprint.

**Request Body:**

```json
{
  "projectKey": "PROJ",
  "name": "Sprint 1",
  "startDate": "2023-11-01",
  "endDate": "2023-11-14",
  "goal": "Implement Auth"
}
```

**Response Body:**

```json
{
  "id": 5,
  "name": "Sprint 1",
  "projectKey": "PROJ",
  "status": "PLANNED",
  "goal": "Implement Auth",
  "startDate": "2023-11-01",
  "endDate": "2023-11-14"
}
```

### Get Project Sprints

**Endpoint:** `GET /api/sprints/project/{projectKey}`
**Description:** Retrieves all sprints for a project.

**Response Body:** List of Sprint Responses.

### Change Sprint Status

**Endpoint:** `PATCH /api/sprints/{id}/status?status=ACTIVE`
**Description:** Updates the status of a sprint (e.g., PLANNED, ACTIVE, COMPLETED).

**Response Body:** (Same as Sprint Response)

### Get Sprint Issues

**Endpoint:** `GET /api/sprints/{id}/issues`
**Description:** Retrieves all issues assigned to a sprint.

**Response Body:** List of Issue Responses.

### Add Issue to Sprint

**Endpoint:** `POST /api/sprints/{id}/issues/{issueKey}`
**Description:** Assigns an issue to a sprint.

**Response Body:** (Same as Issue Response)

### Remove Issue from Sprint

**Endpoint:** `DELETE /api/sprints/{id}/issues/{issueKey}`
**Description:** Removes an issue from a sprint (unassigns it).

**Response Body:** (Same as Issue Response)

---

## 6. Comments

### Add Comment

**Endpoint:** `POST /api/comments`
**Description:** Adds a comment to an issue.

**Request Body:**

```json
{
  "issueKey": "PROJ-1",
  "authorId": 1,
  "body": "This needs more investigation."
}
```

**Response Body:**

```json
{
  "id": 20,
  "issueKey": "PROJ-1",
  "authorId": 1,
  "authorUsername": "jdoe",
  "body": "This needs more investigation.",
  "createdAt": "2023-10-27T12:00:00",
  "updatedAt": "2023-10-27T12:00:00"
}
```

### Get Comments

**Endpoint:** `GET /api/comments/issue/{issueKey}`
**Description:** Retrieves all comments for an issue.

**Response Body:** List of Comment Responses.

### Edit Comment

**Endpoint:** `PUT /api/comments/{id}`
**Description:** Edits a comment.

**Request Body:** (Same as Create Comment, mostly `body` is used)

### Delete Comment

**Endpoint:** `DELETE /api/comments/{id}?authorId=1`
**Description:** Deletes a comment.

---

## 7. Board

### Get Project Board

**Endpoint:** `GET /api/boards/project/{projectKey}`
**Description:** Retrieves the Kanban board for a project.

**Response Body:**

```json
{
  "scope": "PROJECT",
  "projectKey": "PROJ",
  "sprintId": null,
  "columns": [
    {
      "status": "OPEN",
      "statusDisplayName": "OPEN",
      "issues": [ ... ] // List of Issue objects
    },
    {
      "status": "IN_PROGRESS",
      "statusDisplayName": "IN PROGRESS",
      "issues": [ ... ]
    },
    {
      "status": "DONE",
      "statusDisplayName": "DONE",
      "issues": [ ... ]
    }
  ]
}
```

### Get Sprint Board

**Endpoint:** `GET /api/boards/sprint/{sprintId}`
**Description:** Retrieves the Kanban board for a specific sprint.

**Response Body:** Similar structure to Project Board, but grouped by Sprint context.
