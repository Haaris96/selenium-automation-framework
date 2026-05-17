pipeline {
    agent any

    parameters {
        choice(name: 'BROWSER',     choices: ['chrome', 'firefox', 'edge'],                           description: 'Browser')
        choice(name: 'ENVIRONMENT', choices: ['qa', 'staging', 'prod'],                               description: 'Environment')
        choice(name: 'SUITE',       choices: ['testng.xml', 'testng-smoke.xml', 'testng-parallel.xml'], description: 'TestNG Suite')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Headless mode')
    }

    environment {
        JAVA_HOME  = tool name: 'JDK-21',  type: 'jdk'
        MAVEN_HOME = tool name: 'Maven-3', type: 'maven'
        PATH       = "${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${env.PATH}"
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
        timeout(time: 60, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    stages {

        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/Haaris96/selenium-automation-framework.git',
                        credentialsId: 'github-pat'
                    ]]
                ])
                echo "Checked out branch: ${env.GIT_BRANCH} | Commit: ${env.GIT_COMMIT?.take(8)}"
            }
        }

        stage('Environment Info') {
            steps {
                bat 'java -version'
                bat 'mvn -version'
                echo "Browser: ${params.BROWSER} | Env: ${params.ENVIRONMENT} | Suite: ${params.SUITE} | Headless: ${params.HEADLESS}"
            }
        }

        stage('Compile') {
            steps {
                bat 'mvn clean compile test-compile -q'
            }
        }

        stage('Run Tests') {
            steps {
                bat """
                    mvn test ^
                        -Dbrowser=${params.BROWSER} ^
                        -Denv=${params.ENVIRONMENT} ^
                        -Dsuite=${params.SUITE} ^
                        -Dheadless=${params.HEADLESS} ^
                        -Dmaven.test.failure.ignore=true
                """
            }
        }

        stage('Publish Extent Report') {
            steps {
                publishHTML(target: [
                    allowMissing: true,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'reports',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Report',
                    reportTitles: 'Test Execution Report'
                ])
            }
        }

        stage('Publish Cucumber Report') {
            steps {
                publishHTML(target: [
                    allowMissing: true,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'reports',
                    reportFiles: 'cucumber-report.html',
                    reportName: 'Cucumber Report',
                    reportTitles: 'BDD Cucumber Report'
                ])
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'reports/**,screenshots/**,logs/**', allowEmptyArchive: true
            }
        }
    }

    post {
        always {
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
            cleanWs()
        }
        success { echo 'BUILD SUCCEEDED - All tests passed' }
        failure { echo 'BUILD FAILED - Check report for details' }
        unstable { echo 'BUILD UNSTABLE - Some tests failed' }
    }
}