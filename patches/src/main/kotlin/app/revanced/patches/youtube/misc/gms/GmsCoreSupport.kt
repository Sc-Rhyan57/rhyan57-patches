package app.revanced.patches.youtube.misc.gms

import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.patch.resourcePatch
import app.revanced.patcher.patch.option.PatchOption.PatchExtensions.stringPatchOption
import app.revanced.patcher.fingerprint

val gmsCoreVendorGroupId by stringPatchOption(
    key = "gmsCoreVendorGroupId",
    default = "app.revanced",
    values = mapOf(
        "ReVanced" to "app.revanced",
        "inotia00 (RVX)" to "app.rvx",
        "Anddea" to "app.revanced",
    ),
    title = "GmsCore vendor group ID",
    description = "The group ID of the GmsCore vendor to use for authentication.",
    required = true,
)

private val gmsCoreSupportResourcePatch = resourcePatch {
    dependsOn(
        gmsCoreVendorGroupId
    )

    execute {
        val vendorGroupId = gmsCoreVendorGroupId!!

        document("AndroidManifest.xml").use { document ->
            val manifest = document.documentElement

            val queries = document.createElement("queries")
            val packageElement = document.createElement("package")
            packageElement.setAttribute("android:name", "$vendorGroupId.android.gms")
            queries.appendChild(packageElement)
            manifest.appendChild(queries)

            val application = manifest.getElementsByTagName("application").item(0)

            val metaData = document.createElement("meta-data")
            metaData.setAttribute("android:name", "app.revanced.GMSCORE_VENDOR_GROUP_ID")
            metaData.setAttribute("android:value", vendorGroupId)
            application.appendChild(metaData)
        }
    }
}

private val checkGmsCoreFingerprintMain = fingerprint {
    strings("com.google.android.gms")
    custom { method, _ ->
        method.name == "checkGmsCore" || method.parameters.isEmpty()
    }
}

val gmsCoreSupport = bytecodePatch(
    name = "GmsCore support",
    description = "Allows patched YouTube to run without root under a different package name using GmsCore (MicroG) instead of Google Play Services. Required for Google account login on non-rooted devices.",
) {
    compatibleWith("com.google.android.youtube")

    dependsOn(gmsCoreSupportResourcePatch)

    val gmsPackageNameReferences = listOf(
        "com.google.android.gms",
        "com.google.android.gsf",
    )

    execute {
        val vendorGroupId = gmsCoreVendorGroupId!!

        classes.forEach { classDef ->
            classDef.methods.forEach { method ->
                val implementation = method.implementation ?: return@forEach

                val stringInstructions = implementation.instructions.filterIsInstance<
                        com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c>()

                stringInstructions.forEach { instruction ->
                    if (instruction.referenceType ==
                        com.android.tools.smali.dexlib2.ReferenceType.STRING) {
                        val reference = instruction.reference.toString()
                        if (gmsPackageNameReferences.any { reference == it }) {
                            val newReference = reference.replace(
                                "com.google.android",
                                "$vendorGroupId.android"
                            )
                            val mutableMethod = proxy(classDef).mutableClass
                                .methods.first { it.name == method.name }
                            val idx = mutableMethod.implementation!!.instructions.indexOf(instruction)
                            replaceInstruction(
                                mutableMethod,
                                idx,
                                "const-string v${instruction.registerA}, \"$newReference\""
                            )
                        }
                    }
                }
            }
        }
    }
}
