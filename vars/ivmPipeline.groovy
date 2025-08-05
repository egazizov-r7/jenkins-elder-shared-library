def call(Map pipelineParams) {
    pipeline {
        agent any

        parameters {
            string(name: 'POD_IDLE_MINUTES', defaultValue: '0', description: 'Number of minutes pod will stay idle post build')
        }

        options {
            ansiColor('xterm')
            timestamps()
        }

        stages {
            stage('Start Regression testing') {
                steps {
                    sh "echo Starting regression testing with parameters: ${pipelineParams}"
                }
            }
        } // End of Stages
    } // End of pipeline
}
