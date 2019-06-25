# cuHacking: Android App
The app that will be used by attendees of cuHacking 2020 as well as by organizers to help manage attendees.

## Getting Started
To build this project you will need Android Studio 3.4 and the Android SDK for Android Q (29).

You will need to put a `google-services.json` file in the `/app` folder in order for Firebase to work. See the [Firebase docs](https://firebase.google.com/docs/android/setup#add-config-file) on adding the Firebase configuration file.

In your `local.properties` file (in the project root directory) you will need to include the following properties:
```properties
mapbox.key=your mapbox key here
api.endpoint=your "/"-terminated root api endpoint here
```

## Built With
A lot of things. See the app [`build.gradle.kts`](/app/build.gradle.kts) file for the full list of dependencies.