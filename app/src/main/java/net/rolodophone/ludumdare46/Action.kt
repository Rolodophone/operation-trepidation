package net.rolodophone.ludumdare46

class Action(val text: String, val duration: Float, val effect: () -> Unit = {}, val condition: () -> Boolean = {true}) {
    var progress = 0f
}