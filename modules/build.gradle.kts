tasks.matching {
    it.group == "publishing"
}.forEach {
    it.enabled = false
}