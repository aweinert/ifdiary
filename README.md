# IFDiary

This is a simple android app that sends the user a daily notification at 6 AM and queries them whether they kept to intermittent fasting yesterday.
Depending on the response, it shows one of two images.
Independently of the response, it shows statistics over fasting for the past 7 days and for the past 30 days.
This app is only intended for my personal use, hence its target platform is rather well defined: It is supposed to run on a Samsung Galaxy S7 running Android 8.0.0.
If any part of this app is functional on any other platform, this is more of a happy coincidence than anything else.

## Building and Running

If you would like to build this app yourself, you need to add three drawables called "panda", "panda_happy", and "panda_sad" into the folder res/drawable.
The drawable "panda" will be used as the icon of the app, while "panda_happy" and "panda_sad" are the images shown on positive and negative answering of the query, respectively.
The images I use privately are not included in this directory due to restrictions on redistribution.
