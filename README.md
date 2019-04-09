Android Recyclerview Example
=====================================================

This is sample App. App is preapared to demonstrate recyclerview.

Prerequisites
--------------

- Android SDK v28
- Latest Android Build Tools
- Android Support Repository

Getting started
---------------

This sample uses the Gradle build system.

1. Download the samples by cloning this repository(See the options at the top of the page.)
1. In Android Studio, create a new project and choose the "Import non-Android Studio project" or
  "Import Project" option.

1. If prompted for a gradle configuration, accept the default settings.
  Alternatively use the "gradlew build" command to build the project directly.

App overview
--------------
- App uses https://www.themoviedb.org/ APIs to show list of movies.

Libraries
-----------

- Recyclerview with Gridlayout manager used to show movie list.
- okHttp is networking lib for API calls.
- Picasso is image loading lib.
- Gson is JSON parsing lib.
- Room database for offline caching of network APIs.
- For Movie Detail screen android palette lib is source to create gradiant, statusbar color and font colors based on bitmap of movie poster.

