# CLI-Task-Management-System

Entities:

  1-User (with subclasses Employee, Manager, Supervisor)
  2-Task
  3-Comment
  4-TaskHistory
Relationships:

  Employee (1) - (n) Task
  Manager (1) - (n) Task
  Supervisor (1) - (n) Task
  Task (1) - (n) Comment
  Comment (n) - (1) User (createdBy)
  Comment (n) - (1) Task
  Task (1) - (n) TaskHistory
  User (1) - (n) TaskHistory

The ERD Diagram is attecahed.
![ERD Diagram](https://github.com/hk5ahi/CLI-Task-Management-System/assets/75085428/a55845ce-50d5-4204-8a8b-4501b73c56b8)
