pipeline {
    agent any

    // ─── Parameters (exposed in Jenkins UI) ─────────────────────────────────
    parameters {
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge'],
            description: 'Browser to run tests on'
        )
        choice(
            name: 'ENVIRONMENT',
            choices: ['qa', 'staging', 'prod'],
            description: 'Target test environment'
        )
        choice(
            name: 'SUITE',
            choices: ['testng.xml', 'testng-smoke.xml', 'testng-parallel.xml'],
            description: 'TestNG suite file to execute'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run browser in headless mode'
        )
    }

    // ─── Environment variables ───────────────────────────────────────────────
    environment {
        JAVA_HOME       = tool name: 'JDK-11', type: 'jdk'
        MAVEN_HOME      = tool name: 'Maven-3', type: 'maven'
        PATH            = "${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${env.PATH}"
        TIMESTAMP       = sh(script: "date +%Y%m%d_%H%M%S", returnStdout: true).trim()
        REPORT_DIR      = "reports"
        SCREENSHOT_DIR  = "screenshots"
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
        timeout(time: 60, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    // ─── Stages ─────────────────────────────────────────────────────────────
    stages {

        stage('Checkout') {
            steps {
                echo "Checking out source code..."
                checkout scm
                echo "Branch: ${env.GIT_BRANCH} | Commit: ${env.GIT_COMMIT}"
            }
        }

        stage('Environment Info') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
                echo "Browser: ${params.BROWSER} | Env: ${params.ENVIRONMENT} | Suite: ${params.SUITE} | Headless: ${params.HEADLESS}"
            }
        }

        stage('Compile') {
            steps {
                echo "Compiling source code..."
                sh 'mvn clean compile test-compile -q'
            }
        }

        stage('Run Tests') {
            steps {
                echo "Running tests..."
                sh """
                    mvn test \
                        -Dbrowser=${params.BROWSER} \
                        -Denv=${params.ENVIRONMENT} \
                        -Dsuite=${params.SUITE} \
                        -Dheadless=${params.HEADLESS} \
                        -Dmaven.test.failure.ignore=true \
                        -q
                """
            }
        }

        stage('Publish Reports') {
            steps {
                echo "Publishing test reports..."

                // Publish TestNG results
                publishHTML(target: [
                    allowMissing         : true,
                    alwaysLinkToLastBuild: true,
                    keepAll              : true,
                    reportDir            : 'reports',
                    reportFiles          : 'ExtentReport.html',
                    reportName           : 'Extent Report',
                    reportTitles         : 'Selenium Test Execution Report'
                ])

                // Publish Cucumber report
                publishHTML(target: [
                    allowMissing         : true,
                    alwaysLinkToLastBuild: true,
                    keepAll              : true,
                    reportDir            : 'reports',
                    reportFiles          : 'cucumber-report.html',
                    reportName           : 'Cucumber HTML Report',
                    reportTitles         : 'BDD Cucumber Report'
                ])
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'reports/**', allowEmptyArchive: true
                archiveArtifacts artifacts: 'screenshots/**', allowEmptyArchive: true
                archiveArtifacts artifacts: 'logs/**', allowEmptyArchive: true
            }
        }
    }

    // ─── Post actions ────────────────────────────────────────────────────────
    post {

        always {
            echo "Pipeline completed — Build: ${env.BUILD_NUMBER}"

            // TestNG JUnit results for Jenkins test graph
            junit testResults: 'target/surefire-reports/*.xml',
                  allowEmptyResults: true

            // Clean workspace to free disk space
            cleanWs()
        }

        success {
            echo "BUILD SUCCEEDED ✔ — All tests passed"
            emailext(
                subject: "✔ PASSED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    <h2 style='color:green'>Build Passed</h2>
                    <p><b>Job:</b> ${env.JOB_NAME}</p>
                    <p><b>Build #:</b> ${env.BUILD_NUMBER}</p>
                    <p><b>Browser:</b> ${params.BROWSER} | <b>Env:</b> ${params.ENVIRONMENT}</p>
                    <p><b>Suite:</b> ${params.SUITE}</p>
                    <p><a href='${env.BUILD_URL}'>View Build</a></p>
                    <p><a href='${env.BUILD_URL}Extent_Report/'>View Extent Report</a></p>
                """,
                to: 'qa-team@company.com',
                mimeType: 'text/html'
            )
        }

        failure {
            echo "BUILD FAILED ✘"
            emailext(
                subject: "✘ FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    <h2 style='color:red'>Build Failed</h2>
                    <p><b>Job:</b> ${env.JOB_NAME}</p>
                    <p><b>Build #:</b> ${env.BUILD_NUMBER}</p>
                    <p><b>Browser:</b> ${params.BROWSER} | <b>Env:</b> ${params.ENVIRONMENT}</p>
                    <p><a href='${env.BUILD_URL}console'>View Console Output</a></p>
                    <p><a href='${env.BUILD_URL}Extent_Report/'>View Extent Report</a></p>
                """,
                to: 'qa-team@company.com',
                mimeType: 'text/html'
            )
        }

        unstable {
            echo "BUILD UNSTABLE — Some tests failed"
        }
    }
}
