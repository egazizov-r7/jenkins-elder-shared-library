def call(Map pipelineParams) {
    pipeline {
        agent any

        stages {
            stage('Start Regression testing') {
                steps {
                    sh "echo Starting regression testing with parameters: ${pipelineParams}"
                }
            }
        } // End of Stages
    } // End of pipeline
}
