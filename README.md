# CLI Task Management System

This repository contains a Command Line Interface (CLI) Task Management System, where users can manage tasks, comments, and task history. The system supports different user roles: Employee, Manager, and Supervisor.

## Entities

1. **User**: Represents different user roles - Employee, Manager, and Supervisor.
2. **Task**: Represents tasks with various attributes such as title, description, status, etc.
3. **Comment**: Represents comments associated with tasks.
4. **TaskHistory**: Captures the history of task status changes.

## Relationships

- Employee (1) - (n) Task
- Manager (1) - (n) Task
- Supervisor (1) - (n) Task
- Task (1) - (n) Comment
- Comment (n) - (1) User (createdBy)
- Comment (n) - (1) Task
- Task (1) - (n) TaskHistory
- User (1) - (n) TaskHistory

## ERD Diagram

An Entity-Relationship Diagram (ERD) visualizing the relationships between entities is provided below:

![ERD Diagram](https://github.com/hk5ahi/CLI-Task-Management-System/assets/75085428/a55845ce-50d5-4204-8a8b-4501b73c56b8)

## Getting Started

Follow these steps to set up and run the CLI Task Management System:

1. Clone this repository to your local machine.
2. Navigate to the project directory.
3. Open a terminal and run the CLI application using appropriate commands.

