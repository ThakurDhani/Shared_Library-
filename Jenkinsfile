pipeline {
    agent any

    parameters {
        booleanParam(name: 'RUN_STABILITY_SCAN', defaultValue: true)
        booleanParam(name: 'RUN_QUALITY_SCAN', defaultValue: true)
        booleanParam(name: 'RUN_COVERAGE_SCAN', defaultValue: true)
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Parallel Scans') {
            parallel {
                stage('Code Stability') {
                    when { expression { return params.RUN_STABILITY_SCAN } }
                    steps {
                        sh './gradlew test'
                    }
                }
                stage('Code Quality') {
                    when { expression { return params.RUN_QUALITY_SCAN } }
                    steps {
                        sh './gradlew check'
                    }
                }
                stage('Code Coverage') {
                    when { expression { return params.RUN_COVERAGE_SCAN } }
                    steps {
                        sh './gradlew jacocoTestReport'
                    }
                }
            }
        }

        stage('Generate Reports') {
            steps {
                archiveArtifacts artifacts: '**/build/reports/**', allowEmptyArchive: true
            }
        }

        stage('Approval Before Publish') {
            steps {
                input message: 'Approve publishing?', ok: 'Publish Now'
            }
        }

        stage('Publish Artifacts') {
            steps {
                sh './gradlew publish'
            }
        }
    }

    post {
        success {
            slackSend(channel: '#ci-notifications', message: "Build Successful: ${env.JOB_NAME} #${env.BUILD_NUMBER}")
            mail to: 'team@example.com',
                 subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "Build Success. View: ${env.BUILD_URL}"
        }
        failure {
            slackSend(channel: '#ci-notifications', message: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}")
            mail to: 'team@example.com',
                 subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "Build Failed. View: ${env.BUILD_URL}"
        }
    }
}
