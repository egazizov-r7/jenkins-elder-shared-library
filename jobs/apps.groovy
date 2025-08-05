String getSafeName(String job) {
  return job.replaceAll('/', '-').replaceAll('\\.', '-').replaceAll('--', '-')
}

// Define the trigger comment for PR releases
// def jenkinsReleaseTrigger = "jenkins: release"

def apps = [
    // ['job_name': 'jenkins-elder-shared-library', 'repo_name': 'jenkins-elder-shared-library'],
    ['job_name': 'go-cve-dictionary-elder', 'repo_name': 'go-cve-dictionary-elder', 'jenkinsfile_path': 'Jenkinsfile/goCveDictElder'],
]

apps.each { app ->
    multibranchPipelineJob(getSafeName(app.job_name)) {
        branchSources {
            branchSource {
                source {
                    github {
                        id("ID-" + app.job_name)
                        credentialsId('github-credentials') // Add this line
                        repoOwner('egazizov-r7')
                        repository(app.repo_name)
                        repositoryUrl(null)
                        configuredByUrl(false)
                        traits {
                            gitHubBranchDiscovery {
                                strategyId(1)
                            }
                            gitHubPullRequestDiscovery {
                                strategyId(1)
                            }
                        }
                    }
                }

                strategy {
                    defaultBranchPropertyStrategy {
                        props {
                            triggerPRCommentBranchProperty {
                                allowUntrusted(true)
                                addReaction(false)
                            }
                        }
                    }
                }
            }
        }
        factory {
            workflowBranchProjectFactory {
              scriptPath(app.jenkinsfile_path == null ? "Jenkinsfile" : app.jenkinsfile_path)
            }
        }
        orphanedItemStrategy {
          discardOldItems {
            numToKeep(5)
            daysToKeep(7)
          }
        }
    }
}
