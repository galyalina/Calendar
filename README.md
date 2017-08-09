# MyDays

Demo android application organized accourding to MVP(Model View Presenter) pattern.
The MVP pattern allows separate the presentation layer from the logic and makes views independent from data source. Additional advantage of MVP pattern that it allows to test most of the logic without using instrumentation tests.

Features

Main view Calendar
Circular seek bar and centered button for addding date to the list of selected dates.
Device's month dates are presented.

List view
List of picked dates

Pre-requisites

Android SDK 25

Android Build Tools v25.0.2

Android Support Repository

Tech

Uses a number of open source projects to work properly:

Picasso - A powerful image downloading and caching library for Android
Used for simple loading of User's avatar

Retrofit - Type-safe HTTP client for Android and Java by Square, Inc.
Used for Get requests

Apache Commons Lang provides a host of helper utilities for the java.lang API, notably String manipulation methods, basic numerical methods, object reflection, concurrency, creation and serialization and System properties.
Usefull when overriding methods, such as hashCode, toString and equals.

Todos

Tablet two panel view support
Add more tests
Add analytics/bug reports SDKs(Splunk mint etc)
Add ProGuard

License

Copyright 2013 Square, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

<div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
