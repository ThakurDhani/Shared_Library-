pipeline {
    agent any

    tools {
        jdk 'jdk21'
        gradle 'gradle9'
    }

    parameters {
        booleanParam(name: 'RUN_STABILITY_SCAN', defaultValue: true)
        booleanParam(name: 'RUN_QUALITY_SCAN', defaultValue: true)
        booleanParam(name: 'RUN_COVERAGE_SCAN', defaultValue: true)
    }

    environment {
        EMAIL = 'sahilt537@gmail.com'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/OT-MICROSERVICES/salary-api.git'
            }
        }

        stage('Parallel Checks') {
            parallel {
                stage('Code Stability') {
                    when { expression { params.RUN_STABILITY_SCAN } }
                    steps {
                        sh './gradlew clean build'
                    }
                }
                stage('Code Quality') {
                    when { expression { params.RUN_QUALITY_SCAN } }
                    steps {
                        echo "Code quality check (static code analysis)..."
                        // Add static code analysis tool later like SpotBugs
                    }
                }
                stage('Code Coverage') {
                    when { expression { params.RUN_COVERAGE_SCAN } }
                    steps {
                        sh './gradlew jacocoTestReport'
                    }
                    post {
                        always {
                            jacoco execPattern: '**/build/jacoco/*.exec', classPattern: '**/build/classes/java/main', sourcePattern: '**/src/main/java', inclusionPattern: '**/*.class', exclusionPattern: ''
                        }
                    }
                }
            }
        }

        stage('Test Report') {
            steps {
                junit '**/build/test-results/test/*.xml'
            }
        }

        stage('Approval for Publish') {
            steps {
                input message: 'Approve to publish artifacts?', ok: 'Proceed'
            }
        }

        stage('Publish Artifacts') {
            steps {
                archiveArtifacts artifacts: 'build/libs/*.jar', onlyIfSuccessful: true
            }
        }
    }

    post {
        success {
            mail to: "${EMAIL}",
                 subject: "SUCCESS: Build #${BUILD_NUMBER}",
                 body: "The Jenkins build #${BUILD_NUMBER} succeeded.\nCheck console output at ${BUILD_URL}"
        }
        failure {
            mail to: "${EMAIL}",
                 subject: "FAILURE: Build #${BUILD_NUMBER}",
                 body: "The Jenkins build #${BUILD_NUMBER} failed.\nCheck console output at ${BUILD_URL}"
        }
    }
}
