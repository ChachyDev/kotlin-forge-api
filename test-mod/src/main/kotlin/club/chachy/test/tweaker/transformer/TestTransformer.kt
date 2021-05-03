package club.chachy.test.tweaker.transformer

import club.chachy.transformer.ClassTransformer
import codes.som.anthony.koffee.insns.jvm.getstatic
import codes.som.anthony.koffee.insns.jvm.invokevirtual
import codes.som.anthony.koffee.insns.jvm.ldc
import java.io.PrintStream

class TestTransformer : ClassTransformer() {
    init {
        transform("club.chachy.test.TestMod") {
            method("init")?.let {
                insertReturn(it) {
                    getstatic(System::class, "out", PrintStream::class)
                    ldc("This was injected via forge-transformer-dsl!")
                    invokevirtual(PrintStream::class, "println", void, String::class)
                }
            }
        }
    }
}