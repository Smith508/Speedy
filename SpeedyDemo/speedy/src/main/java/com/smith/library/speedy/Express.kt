package com.smith.library.speedy

/**
 * Wrapper for a function block and it's name.
 *
 * Useful when used with the 'Speedy.clock' or 'Speedy.clockFinish'
 *
 * Ex: Express("parseData") { parseData() }
 */
data class Express<R>(val blockName: String, private val block: () -> R): () -> R by block