package net.rolodophone.ludumdare46

data class LevelFactory(val state: StateGame, val title: String, val objective: String, val lvlNum: Int, val gauges: FloatArray, val gaugeSpeeds: FloatArray, val clearCondition: () ->
Boolean) {
    fun create() = Level(state, title, objective, lvlNum, gauges.clone(), gaugeSpeeds.clone(), clearCondition)
}