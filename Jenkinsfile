pipeline {
    agent any

    parameters {
        booleanParam(name: 'RUN_STABILITY_SCAN', defaultValue: true, description: 'Run Code Stability Scan')
        booleanParam(name: 'RUN_QUALITY_SCAN', defaultValue: true, description: 'Run Code Quality Analysis')
        booleanParam(name: 'RUN_COVERAGE_SCAN', defaultValue: true, description: 'Run Code Coverage Analysis')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'chmod +x gradlew'
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
            mail to: 'sahilt537@gmail.com',
                 subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "The build completed successfully.\n\nCheck details here: ${env.BUILD_URL}"
        }
        failure {
            mail to: 'sahilt537@gmail.com',
                 subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "The build failed.\n\nCheck details here: ${env.BUILD_URL}"
        }
    }
}
