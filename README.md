# MyLuncher

Note: some classes used in this project come from the community, and as such are under other open source licenses.
please see the links in the headers of those java files for more information about the source & the open source license used.


## The license
Copyright 2015 jikuja

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## Usage
1. compile with gradle `./gradlew --daemon shadowrun`
2. run e.g. `java -jar build/libs/MyLuncher-0.1.jar --user <your email/username> -a FTBLite2 --mc-version "1.6.4" --log-mc`
or `java -jar build/libs/MyLuncher-0.1.jar --user <your email/username> -a FTBLite3`
3. How to use credentials from Vanilla launcher
   * e.g. `java -jar  build/libs/MyLuncher-0.0.1.jar --vanilla-credentials --user <your email/username> -a FTBLite3`

## Missing features
* No legacy pack support
* MC version must be given manually with --mc-version if running other than 1.7.10
* <add more things>
