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

1. Download the samples by cloning this repository or downloading an archived
  snapshot. (See the options at the top of the page.)
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
(1)Movie List Screen.
- Recyclerview with gridlayout manager used to show movie list.
- okHttp is networking lib for API calls.
- picasso is image loading lib.
- gson is JSON parsing lib.

(2) Movie Deatil Screen.
- For Movie Detail screen android palette lib is source to create gradiant, statusbar color and font colors based on bitmap of movie poster.


Screenshots
-----------
![Screenshot_20190323-133733](https://user-images.githubusercontent.com/48313933/54864888-6f027f80-4d83-11e9-9dfd-acb571a17298.jpg)

![Screenshot_20190323-133749](https://user-images.githubusercontent.com/48313933/54864886-6ad66200-4d83-11e9-82aa-f4695bd598bd.jpg)



Google Play Store Link
----------------------
- https://play.google.com/store/apps/details?id=com.leanagriassignment






