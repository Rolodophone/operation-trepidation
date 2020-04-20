package net.rolodophone.ludumdare46

data class LevelFactory(val state: StateGame, val title: String, val lvlNum: Int, val gauges: FloatArray, val gaugeSpeeds: FloatArray, val clearCondition: () -> Boolean) {

    fun create() = Level(state, title, lvlNum, gauges.clone(), gaugeSpeeds.clone(), clearCondition)
}