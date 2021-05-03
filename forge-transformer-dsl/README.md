# forge-transformer-dsl

forge-transformer-dsl is a simple project that allows for an easy start to using transformers with Forge with
instruction building powered by Koffee.

### Example usage of forge-transformer-dsl with Forge:

```kotlin
class MyModTransformer : ClassTransformer() {
    init {
        transform("net.minecraft.client.Minecraft") {
            val startGame = method("startGame", "func_71384_a") ?: return@transform

            insert(startGame) {
                getstatic(System::class, "out", PrintStream::class)
                ldc("Hello, World!")
                invokevirtual(PrintStream::class, "println", void, String::class)
            }
        }
    }
}
```

### Debugging

If you want to have forge-transformer-dsl output the .class files with the bytecode modifications applied:

- Add "-DdebugBytecode=true" to your JVM Arguments
- The modified .class files should be outputted to a folder called "bytecode" in your run directory
