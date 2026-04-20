package app.revanced.patches.youtube.player.download

import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.patch.resourcePatch
import app.revanced.patcher.fingerprint
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

private val videoDownloadButtonFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.FINAL)
    returns("V")
    strings("offline_video_endpoint", "download_button")
}

private val downloadButtonVisibilityFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC)
    returns("V")
    opcodes(
        Opcode.IGET_BOOLEAN,
        Opcode.IF_EQZ,
        Opcode.IGET_OBJECT,
        Opcode.INVOKE_VIRTUAL,
    )
    strings("is_offline_available")
}

private val overlayButtonInitFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR)
    strings("overlay_download_button_container")
}

private val downloadVideoResourcePatch = resourcePatch {
    execute {
        document("res/values/strings.xml").use { document ->
            val resources = document.documentElement

            fun addString(name: String, value: String) {
                val element = document.createElement("string")
                element.setAttribute("name", name)
                element.textContent = value
                resources.appendChild(element)
            }

            addString("rhyan57_download_video", "Baixar Vídeo")
            addString("rhyan57_download_audio", "Baixar Áudio")
            addString("rhyan57_download_title", "Opções de Download")
            addString("rhyan57_download_not_available", "Download não disponível para este vídeo")
            addString("rhyan57_download_started", "Download iniciado")
        }

        document("res/layout/video_player_overlay_layout.xml").use { _ -> }
    }
}

val videoDownloadPatch = bytecodePatch(
    name = "Video download",
    description = "Adiciona botão para baixar vídeos diretamente no player, semelhante ao YouTube Go. Usa o sistema de download nativo do YouTube desbloqueado.",
) {
    compatibleWith(
        "com.google.android.youtube"("19.05.36", "19.16.39", "19.43.41", "19.47.53", "20.05.46", "20.14.43")
    )

    dependsOn(downloadVideoResourcePatch)

    execute {
        val downloadButtonMatch = videoDownloadButtonFingerprint.match(
            videoDownloadButtonFingerprint.methodOrNull ?: return@execute
        )

        downloadButtonMatch?.let { match ->
            val method = match.method
            val instructions = method.implementation!!.instructions

            val insertIndex = instructions.indexOfFirst { instruction ->
                instruction.opcode == Opcode.RETURN_VOID
            }.takeIf { it >= 0 } ?: return@let

            addInstruction(
                method,
                insertIndex,
                """
                invoke-static {}, Lapp/revanced/extension/youtube/player/download/VideoDownloadHelper;->enableDownloadButton()V
                """.trimIndent()
            )
        }

        val visibilityMatch = downloadButtonVisibilityFingerprint.methodOrNull
        visibilityMatch?.let { method ->
            val impl = method.implementation!!
            val constIndex = impl.instructions.indexOfFirst { instruction ->
                instruction.opcode == Opcode.CONST_4
            }.takeIf { it >= 0 } ?: return@let

            addInstruction(
                method,
                constIndex + 1,
                """
                invoke-static {}, Lapp/revanced/extension/youtube/player/download/VideoDownloadHelper;->setDownloadButtonVisible(Z)V
                """.trimIndent()
            )
        }
    }
}
