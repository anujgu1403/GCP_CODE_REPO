## Search Service

### Setting up your dev environment

1.  Install docker
    * [Mac users](https://docs.docker.com/docker-for-mac/)
    * [Windows users](https://docs.docker.com/engine/installation/windows/)
    * [Linux users](https://docs.docker.com/engine/installation/linux/ubuntulinux/)

2.  Use this [link](https://docs.docker.com/compose/install/) to install docker compose

3. Please note that setup scripts are compiled for OSX. For Windows change the extension to .bat and tweak the commands/paths necessary for Windows.

### Running the application

1. Use the scripts in docker directory to setup and run the application
    * To setup data execute the script `initial.sh`
    * To run the application execute the script `start.sh`
    * To stop the application execute the script `stop.sh`


### Running Tests

1. To run unit tests run

    `./gradlew clean test`
