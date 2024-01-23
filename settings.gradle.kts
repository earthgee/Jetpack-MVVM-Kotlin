include(":app")
include(":architecture")
rootProject.name = "Jetpack-MVVM-Kotlin"

gradle.afterProject {
    println("setting: after(project==${this}")
}

gradle.beforeProject {
    println("setting: before(project==${this}")
}

gradle.addBuildListener(object: BuildListener {

    override fun beforeSettings(settings: Settings) {
        println("setting: beforeSettings==${settings}")
    }

    override fun settingsEvaluated(settings: Settings) {
        println("setting: settingsEvaluated==${settings}")
    }

    override fun projectsLoaded(gradle: Gradle) {
        println("setting: projectsLoaded==${gradle}")
    }

    override fun projectsEvaluated(gradle: Gradle) {
        println("setting: projectsEvaluated==${gradle}")
    }

    override fun buildFinished(result: BuildResult) {
        println("setting: buildFinished==${result}")
    }

})

gradle.addListener(TaskExecutionGraphListener {
    println("setting: graphPopulated==${it}")
})

gradle.addListener(object: ProjectEvaluationListener {

    override fun beforeEvaluate(project: Project) {
        println("setting: beforeEvaluate==${project}")
    }

    override fun afterEvaluate(project: Project, state: ProjectState) {
        println("setting: afterEvaluate==${project}")
    }

})

gradle.addListener(object: TaskExecutionListener {

    override fun beforeExecute(task: Task) {
        println("setting: task(beforeExecute==${task}")
    }

    override fun afterExecute(task: Task, state: TaskState) {
        println("setting: task(afterExecute==${task}")
    }

})

gradle.addListener(object: TaskActionListener {

    override fun beforeActions(task: Task) {
        println("setting: beforeActions==${task}")
    }

    override fun afterActions(task: Task) {
        println("setting: afterActions==${task}")
    }

})

gradle.taskGraph.whenReady {
    println("setting: taskGraph(whenReady)")
}



























