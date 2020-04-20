package net.rolodophone.ludumdare46

data class LevelFactory(val state: StateGame, val title: String, val hints: List<String>, val lvlNum: Int, val gauges: FloatArray, val gaugeSpeeds: FloatArray, val clearCondition: () ->
Boolean) {
    var failedAttempts = 0
    fun create() = Level(state, title, hints, lvlNum, gauges.clone(), gaugeSpeeds.clone(), clearCondition)
}