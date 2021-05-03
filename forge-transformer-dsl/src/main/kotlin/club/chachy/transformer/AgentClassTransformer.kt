package club.chachy.transformer

import club.chachy.transformer.dsl.TransformerBuilder
import club.chachy.transformer.handler.WriterHandler
import com.google.common.collect.ArrayListMultimap
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.lang.instrument.ClassFileTransformer
import java.security.ProtectionDomain

open class AgentClassTransformer : ClassFileTransformer {
    // ArrayListMultiMap to hold transformer data
    private val transformers: ArrayListMultimap<String, TransformerBuilder.() -> Unit> = ArrayListMultimap.create()
    private val writerHandlers = mutableListOf<(WriterHandler) -> Unit>()

    override fun transform(
        loader: ClassLoader?,
        className: String?,
        classBeingRedefined: Class<*>?,
        protectionDomain: ProtectionDomain?,
        classfileBuffer: ByteArray?
    ): ByteArray? {
        if (className == null) return classfileBuffer

        val clazzTransformers = transformers[className] ?: return classfileBuffer
        if (clazzTransformers.isEmpty()) return classfileBuffer

        val node = ClassNode()

        ClassReader(classfileBuffer).also {
            it.accept(node, ClassReader.EXPAND_FRAMES)
        }

        clazzTransformers.forEach {
            it(TransformerBuilder(node, className))
        }

        val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES).also {
            node.accept(it)
        }

        writerHandlers.forEach { it.invoke(WriterHandler(className, writer)) }
        return writer.toByteArray()
    }

    /**
     * Load transformer block into a list mapped to the class name.
     *
     * @param name Name of the class to be transformed
     * @param transformer The transformer block where your transformations are.
     */
    fun transform(vararg name: String, transformer: TransformerBuilder.() -> Unit) {
        name.forEach {
            transformers.put(it.replace(".", "/"), transformer)
        }
    }

    /**
     * Called after the class writing happens, this can be used like below to write .class files to disk
     */
    fun writerHandler(block: (WriterHandler) -> Unit) = writerHandlers.add(block)

    init {
        writerHandler {
            val shouldOutputBytecode = System.getProperty("debugBytecode", "false")?.toBoolean() ?: false

            if (shouldOutputBytecode) {
                val bytecodeDirectory = File("bytecode")
                val transformedClassName = "${it.transformedName.replace('.', '/')}.class"
                val bytecodeOutput = File(bytecodeDirectory, transformedClassName)

                try {
                    bytecodeOutput.parentFile.mkdirs()
                    bytecodeOutput.writeBytes(it.writer.toByteArray())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}