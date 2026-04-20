package app.revanced.patches.youtube.announcements

import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.patch.resourcePatch
import app.revanced.patcher.fingerprint
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

private val mainActivityOnCreateFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC)
    returns("V")
    parameters("Landroid/os/Bundle;")
    strings("MainActivity", "WatchWhileActivity", "HomeActivity")
    custom { method, _ ->
        method.name == "onCreate"
    }
}

private val announcementResourcePatch = resourcePatch {
    execute {
        document("res/values/strings.xml").use { document ->
            val resources = document.documentElement

            fun addString(name: String, value: String) {
                val element = document.createElement("string")
                element.setAttribute("name", name)
                element.textContent = value
                resources.appendChild(element)
            }

            addString("rhyan57_patch_title", "patch By rhyan57")
            addString("rhyan57_discord_button", "Discord")
            addString("rhyan57_dont_show_button", "Não mostrar mais")
            addString("rhyan57_close_button", "Fechar")
            addString("rhyan57_discord_url", "https://discord.gg/rhyan57")
            addString("rhyan57_patch_prefs", "rhyan57_patch_prefs")
            addString("rhyan57_shown_key", "rhyan57_announcement_shown")
        }
    }
}

val rhyan57AnnouncementPatch = bytecodePatch(
    name = "Rhyan57 announcement",
    description = "Exibe uma tela de apresentação 'patch By rhyan57' com link para o Discord ao iniciar o aplicativo.",
) {
    compatibleWith(
        "com.google.android.youtube"("19.05.36", "19.16.39", "19.43.41", "19.47.53", "20.05.46", "20.14.43")
    )

    dependsOn(announcementResourcePatch)

    execute {
        val onCreateMatch = mainActivityOnCreateFingerprint.methodOrNull ?: return@execute

        val impl = onCreateMatch.implementation!!
        val superCallIndex = impl.instructions.indexOfFirst { instruction ->
            instruction.opcode == Opcode.INVOKE_SUPER
        }.takeIf { it >= 0 } ?: 0

        addInstruction(
            onCreateMatch,
            superCallIndex + 1,
            """
            invoke-static {p0}, Lapp/revanced/extension/youtube/announcements/Rhyan57AnnouncementHelper;->showAnnouncementIfNeeded(Landroid/app/Activity;)V
            """.trimIndent()
        )
    }
}
