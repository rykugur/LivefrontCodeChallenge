# LivefrontCodeChallenge

Code challenge for Livefront.

Consumes NASA's Astronomy Picture of the Day (APOD) API to display stunning visuals of our universe.

NASA's API documentation can be found [here](https://api.nasa.gov/) and [here](https://github.com/nasa/apod-api).

Currently the list view will refresh every time it is re-created. Ideally we would have all results cached and only make an API call after X hours (i.e. 24) has passed, as by its very nature this API will only return a different result set once a day.

## MVVM

The app makes use of MVVM to separate UI and business logic.

## Libraries used

* Compose
* Coroutines
* Room
* Hilt
* Moshi
* Retrofit

## Known issues

* Video thumbnails are currently disabled and considered a stretch goal. I believe this is because the endpoint returns urls from different video providers, each of which requiring possibly different handling to get a still frame for the thumbnail.
* When the device has no network access, sometimes the pull refresh indicator can get stuck. This may be a bug with the compose framework.
* Landscape display needs some sprucing up.