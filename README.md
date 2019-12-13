# pfpTestEnvironment

With these classes you can simulate parts of the lejos framework used in the model factory. It's also possible to simulate sensors and engines used in the productive environment. The code is heavily commented.

## Installation

You can install the test environment either by downloading the code as a zip-file and import it in Eclipse or by pulling the repository via git.

### Installation in Eclipse via git

* Right click empty space in Package explorer on the left
* Import -> Git -> Projects from Git -> Next -> CloneURI
* URI: https://github.com/chh456/pfpTestEnvironment.git
* Eclipse should automatically fill in the values into the forms
Host: github.com
Repository path: /chh456/pfpTestEnvironment.git
Connection: https
Port, User, Password: blank
* On the next page "master" branch should be selected -> Next
* Chose a directory, Initial branch: master, Remote name: origin -> Finish

### Installation in Eclipse via zip-file

* On this page click on "Clone or download"
* Download ZIP
* Save it locally
* In Eclipse: right click empty space in Package explorer on the left
* Import -> General -> *Existing Projects into Workspace* -> Next
* Select archvie file -> Browse for the downloaded zip
* Under Projects you should find MechTestEnvironment
* Select it and copy projects into workspace