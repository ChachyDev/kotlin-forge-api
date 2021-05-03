package club.chachy.transformer.dsl

import codes.som.anthony.koffee.BlockAssembly
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper
import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.*

/**
 * Builder which contains utilities.
 *
 * @param classNode ClassNode instance of the class being transformed.
 * @param name Transformed name of the class being currently transformed.
 *
 * @author ChachyDev
 * @since 1.0
 */
class TransformerBuilder(val classNode: ClassNode, val name: String?) {
    // Get a method from the transforming class' ClassNode
    fun method(deobfName: String, seargeName: String, descriptor: String? = null) =
        method(classNode, deobfName, seargeName, descriptor)

    fun method(name: String, descriptor: String? = null) =
        method(classNode, name, name, descriptor)

    // Get a method from a specified class node with its name
    fun method(classNode: ClassNode, deobfName: String, seargeName: String, descriptor: String? = null) =
        classNode.methods.firstOrNull {
            mapMethodDesc(it).run { this == descriptor || descriptor == null } && mapMethodName(
                classNode,
                it
            ).run { this == deobfName || this == seargeName }
        }

    // Get a method from a boolean check from the transforming class' ClassNode
    fun method(node: (MethodNode) -> Boolean) = method(classNode, node)

    // Get a method from a specified class node with a boolean check
    fun method(classNode: ClassNode, node: (MethodNode) -> Boolean) = classNode.methods.firstOrNull(node)

    // Get field from a the transforming class' ClassNode
    fun field(deobfName: String, seargeName: String) = field(classNode, deobfName, seargeName)

    // Get a field from a specified ClassNode with a name.
    fun field(classNode: ClassNode, deobfName: String, seargeName: String) =
        classNode.fields.firstOrNull { mapFieldName(classNode, it).run { this == deobfName || this == seargeName } }

    // Create a ClassNode instance from a class name.
    fun clazz(name: String): ClassNode {
        return ClassNode().also {
            ClassReader(name).also { reader ->
                reader.accept(it, ClassReader.EXPAND_FRAMES)
            }
        }
    }

    /**
     * Find an instruction located in a method with a [predicate].
     */
    inline fun <reified T : AbstractInsnNode> MethodNode.findInstruction(predicate: (T) -> Boolean): T? {
        for (insn in instructions) {
            if (insn is T && predicate(insn)) return insn
        }

        return null
    }

    fun insertBefore(methodNode: MethodNode, insnNode: AbstractInsnNode, block: BlockAssembly.() -> Unit) =
        methodNode.instructions.insertBefore(insnNode, BlockAssembly(InsnList(), ArrayList()).apply(block).instructions)

    fun insert(methodNode: MethodNode, block: BlockAssembly.() -> Unit) =
        methodNode.instructions.insert(BlockAssembly(InsnList(), ArrayList()).apply(block).instructions)

    fun insert(deobfName: String, seargeName: String, block: BlockAssembly.() -> Unit) =
        method(deobfName, seargeName)?.instructions?.insert(
            BlockAssembly(
                InsnList(),
                ArrayList()
            ).apply(block).instructions
        )

    fun insert(methodNode: MethodNode, insnNode: AbstractInsnNode, block: BlockAssembly.() -> Unit) =
        methodNode.instructions.insert(insnNode, BlockAssembly(InsnList(), ArrayList()).apply(block).instructions)

    fun insertReturn(methodNode: MethodNode, block: BlockAssembly.() -> Unit) {
        methodNode.instructions.insertBefore(
            methodNode.instructions.last.previous,
            BlockAssembly(InsnList(), ArrayList())
                .apply(block)
                .instructions
        )
    }

    fun insertBefore(methodNode: MethodNode, insnNode: AbstractInsnNode, insnList: InsnList) =
        methodNode.instructions.insertBefore(insnNode, insnList)

    fun insert(methodNode: MethodNode, insnList: InsnList) =
        methodNode.instructions.insert(insnList)

    fun insert(deobfName: String, seargeName: String, insnList: InsnList) =
        method(deobfName, seargeName)?.instructions?.insert(insnList)

    fun insert(methodNode: MethodNode, insnNode: AbstractInsnNode, insnList: InsnList) =
        methodNode.instructions.insert(insnNode, insnList)

    fun insertReturn(methodNode: MethodNode, insnList: InsnList) {
        methodNode.instructions.insertBefore(
            methodNode.instructions.last.previous,
            insnList
        )
    }

    fun mapMethodName(classNode: ClassNode, methodNode: MethodNode): String =
        FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, methodNode.name, methodNode.desc)

    fun mapFieldName(classNode: ClassNode, fieldNode: FieldNode): String =
        FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(classNode.name, fieldNode.name, fieldNode.desc)

    fun mapMethodDesc(methodNode: MethodNode): String =
        FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(methodNode.desc)

    fun mapFieldNameFromNode(fieldInsnNode: FieldInsnNode): String =
        FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(fieldInsnNode.owner, fieldInsnNode.name, fieldInsnNode.desc)

    fun clearInstructions(methodNode: MethodNode) {
        methodNode.instructions.clear()
        methodNode.localVariables?.clear()
        methodNode.tryCatchBlocks?.clear()
    }
}