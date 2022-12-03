package de.thm.mow2gamecollection.wordle.model

enum class KeyboardLayout {
    QWERTZ {
        override fun getKeys(): Array<CharArray> {
            return arrayOf(
                charArrayOf('q', 'w', 'e', 'r', 't', 'z', 'u', 'i', 'o', 'p'),
                charArrayOf('a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l'),
                charArrayOf('←', 'y', 'x', 'c', 'v', 'b', 'n', 'm', '✓')
            )
        }
    },
    QWERTY {
        override fun getKeys(): Array<CharArray> {
            return arrayOf(
                charArrayOf('q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p'),
                charArrayOf('a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l'),
                charArrayOf('←', 'z', 'x', 'c', 'v', 'b', 'n', 'm', '✓')
            )
        }
    };

    abstract fun getKeys() : Array<CharArray>
}