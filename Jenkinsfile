pipeline {
    agent any

    parameters {
        booleanParam(name: 'RUN_STABILITY_SCAN', defaultValue: true, description: 'Run Code Stability Scan')
        booleanParam(name: 'RUN_QUALITY_SCAN', defaultValue: true, description: 'Run Code Quality Analysis')
        booleanParam(name: 'RUN_COVERAGE_SCAN', defaultValue: true, description: 'Run Code Coverage Analysis')
    }

    environment {
        SONARQUBE_ENV = 'SonarQubeServer'  // Change if different
        EMAIL_RECIPIENTS = 'sahilt537@gmail.com'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/OT-MICROSERVICES/salary-api.git', branch: 'main'
            }
        }

        stage('Parallel Scans') {
            parallel {
                stage('Code Stability') {
                    when {
                        expression { params.RUN_STABILITY_SCAN }
                    }
                    steps {
                        echo "Running unit tests..."
                        sh './gradlew test' // Or use mvn test if using Maven
                    }
                }

                stage('Code Quality Analysis') {
                    when {
                        expression { params.RUN_QUALITY_SCAN }
                    }
                    steps {
                        echo "Running SonarQube analysis..."
                        withSonarQubeEnv("${SONARQUBE_ENV}") {
                            sh './gradlew sonarqube'
                        }
                    }
                }

                stage('Code Coverage Analysis') {
                    when {
                        expression { params.RUN_COVERAGE_SCAN }
                    }
                    steps {
                        echo "Generating code coverage report..."
                        sh './gradlew jacocoTestReport'
                    }
                }
            }
        }

        stage('Approval Before Publish') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    input message: 'Approve to publish the build?', ok: 'Approve'
                }
            }
        }

        stage('Publish Artifacts') {
            steps {
                echo "Publishing artifacts..."
                sh './gradlew build'
            }
        }
    }

    post {
        success {
            echo '✅ Build succeeded.'
            emailext(
                subject: "✅ Jenkins Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """<p>Build completed successfully.</p>
                         <p><b>Project:</b> ${env.JOB_NAME}</p>
                         <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                         <p><b>Link:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>""",
                mimeType: 'text/html',
                to: "${EMAIL_RECIPIENTS}"
            )
        }

        failure {
            echo '❌ Build failed.'
            emailext(
                subject: "❌ Jenkins Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """<p>Build failed.</p>
                         <p><b>Project:</b> ${env.JOB_NAME}</p>
                         <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                         <p><b>Link:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>""",
                mimeType: 'text/html',
                to: "${EMAIL_RECIPIENTS}"
            )
        }
    }
}
