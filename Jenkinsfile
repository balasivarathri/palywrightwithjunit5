pipeline {
    agent any

    tools {
        maven 'maven' // Make sure this name matches your Jenkins Maven installation name
    }

    stages {
        stage('Dev') {
            steps {
                echo("Deploy to SIT")
            }
        }

        stage('SIT') {
            steps {
                echo("Deploy to UAT")
            }
        }

        stage('UAT') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    git 'https://github.com/balasivarathri/palywrightwithjunit5.git'
                    bat "mvn clean verify"
                }
            }
        }

        stage('Generate Cucumber HTML Report') {
            steps {
                bat """
                    mvn net.masterthought:maven-cucumber-reporting:5.8.1:generate ^
                      -Dcucumber.jsonFiles=target/cucumber-reports/cucumber.json ^
                      -Dcucumber.outputDirectory=target/cucumber-html-reports
                """
            }
        }

        stage('Publish HTML Report') {
            steps {
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/cucumber-html-reports',
                    reportFiles: 'overview-features.html',
                    reportName: 'Customized Cucumber Report'
                ])
            }
        }
    }
}
