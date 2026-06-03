# ToDoListApp

A Android To-Do List application created as a university class project (Projekt na zaliczenie PUM). 

This app allows users to efficiently manage their daily tasks, set reminders, and keep track of completed activities. It features a clean, Material Design-inspired user interface and utilizes a local SQLite database for data persistence.

## 🚀 Features

* **Task Management:** Create, read, update, and delete (CRUD) your tasks with ease.
* **Date & Time Picker:** Set  deadlines for your tasks using the built-in calendar and time picker dialogs.
* **Reminders & Notifications:** Never miss a deadline! The app schedules local alarms and sends push notifications to remind you of your upcoming tasks.
* **Task Status:** Mark tasks as complete or incomplete, with visual indicators (colors and icons) to easily distinguish them on the list.
* **Built-in Flashlight:** A handy toggle button to quickly turn on your device's flashlight directly from the main screen.
* **Local Storage:** All tasks are securely saved on your device using an SQLite database.
* **Clean UI:** Styled with Google's Material Components and custom drawables for a modern look.

## 🛠️ Technologies Used

* **Language:** Java
* **Platform:** Android (Min SDK 21, Target SDK 29)
* **Database:** SQLite (Custom `DbHandler`)
* **UI/UX:** XML Layouts, Material Components for Android, Custom Drawables
* **View Binding:** [ButterKnife](https://github.com/JakeWharton/butterknife) for clean view injection
* **Other Tools:** `AlarmManager` for scheduling, `NotificationManager` for alerts, `CameraManager` for the flashlight feature.

## 🎓 About
This project was developed for a university course (PUM) to demonstrate proficiency in Android app development, specifically focusing on UI design, local databases (SQLite), and system services (Notifications, Alarms, Camera).
