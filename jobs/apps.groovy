String getSafeName(String job) {
  return job.replaceAll('/', '-').replaceAll('\\.', '-').replaceAll('--', '-')
}

def apps = [
    ['job_name': 'jenkins-elder-shared-library', 'repo_name': 'https://github.com/egazizov-r7/jenkins-elder-shared-library.git'],
    ['job_name': 'go-cve-dictionary-elder', 'repo_name': 'https://github.com/egazizov-r7/go-cve-dictionary-elder.git', 'jenkinsfile_path': 'Jenkinsfile/goCveDictElder'],
]

apps.each { app ->
    multibranchPipelineJob(getSafeName(app.job_name)) {
        branchSources {
            branchSource {
                source {
                    github {
                        id("ID-" + app.job_name)
                        repoOwner('egazizov-r7')
                        repository(app.repo_name)
                        traits {
                            gitHubBranchDiscovery {
                                strategyId(1)
                            }
                            gitHubPullRequestDiscovery {
                                strategyId(1)
                            }
                        }
                    }

                strategy {
                    defaultBranchPropertyStrategy {
                        props {
                            triggerPRCommentBranchProperty {
                                commentBody(jenkinsReleaseTrigger)
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
}
