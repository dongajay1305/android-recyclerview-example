Leanagri Assignment
=====================================================

This is sample App. App is preapared as an assignment for Leanagri interview process.

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
- App uses  https://www.themoviedb.org/ APIs to show list of movies and clicking on any movie redirects to movie details screen.
- There is also option to sort movies based on release date, rating, votes, popularity etc.

Libraries
-----------

- Recyclerview with Gridlayout manager used to show movie list.
- okHttp is networking lib for API calls.
- Picasso is image loading lib.
- Gson is JSON parsing lib.
- Room database for offline caching of network APIs.
- For Movie Detail screen android palette lib is source to create gradiant, statusbar color and font colors based on bitmap of movie poster.


Google Play Store Link
----------------------
- https://play.google.com/store/apps/details?id=com.leanagriassignment


Screenshots
-----------
<a href="url"><img src="https://user-images.githubusercontent.com/48313933/54881965-68efca00-4e7b-11e9-9f81-2e4e2f72049e.jpg" align="left" height="640" width="360"></a>

<a href="url"><img src="https://user-images.githubusercontent.com/48313933/54881967-6beaba80-4e7b-11e9-9065-b75104f28363.jpg" align="left" height="640" width="360"></a>
