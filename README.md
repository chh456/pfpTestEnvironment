# pfpTestEnvironment

With these classes you can simulate parts of the lejos framework used in the model factory. It's also possible to simulate sensors and engines used in the productive environment. The code is heavily commented.

You can import this directly with git or via zip file. If you use git please be aware that there will be merge conflicts when you change the classes and try to pull updates. The easiest way should be using own classes for your own code and add them to a local .gitignore file.

The code is still in development and there are still concurrency issues in the sensor class unless you register your devices via the SensorListener interface.
