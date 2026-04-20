# rhyan57-patches

Patches personalizados para ReVanced, desenvolvidos por **rhyan57**.

## ✨ Patches incluídos

### 🔐 GmsCore Support
Permite que o YouTube ReVanced funcione sem root usando MicroG (GmsCore).
- Redireciona chamadas `com.google.android.gms` para o GmsCore instalado
- Compatível com ReVanced GmsCore, mMicroG e outros forks
- Configurável via opção `gmsCoreVendorGroupId`

### 📥 Video Download
Adiciona download de vídeos diretamente no player, resgatando o YouTube Go.
- Botão de download no player
- Compatível com NewPipe e LibreTube
- Opções: Vídeo (MP4), Áudio (MP3), Copiar link
- Fallback via Android Share Sheet

### 🎨 Rhyan57 Announcement
Tela de apresentação com cantos arredondados ao abrir o app.
- Design escuro `#1A1A2E` com destaque `#E94560`
- Botão Discord
- Toggle "Não mostrar mais"
- Botão Fechar

---

## 📋 Versões suportadas

`19.05.36` · `19.16.39` · `19.43.41` · `19.47.53` · `20.05.46` · `20.14.43`

---

## 🛠️ Como usar

### Requisitos
- [ReVanced Manager](https://github.com/ReVanced/revanced-manager/releases)
- [ReVanced GmsCore](https://github.com/ReVanced/GmsCore/releases) (para login Google)
- APK do YouTube compatível ([APKMirror](https://www.apkmirror.com/apk/google-inc/youtube/))

### Adicionando ao ReVanced Manager

1. Abra o **ReVanced Manager**
2. Vá em **Configurações → Fontes**
3. Toque em **+** e adicione:
   - **Patches:** `https://github.com/rhyan57/rhyan57-patches/releases/latest/download/patches.rvp`
   - **Integrations:** `https://github.com/rhyan57/rhyan57-patches/releases/latest/download/extensions.rve`
4. Selecione o APK do YouTube e aplique os patches

### Compilando manualmente

```bash
git clone https://github.com/rhyan57/rhyan57-patches
cd rhyan57-patches
./gradlew build
```

Gera `patches/build/libs/*.rvp`

---

## 🔑 Login Google (MicroG)

1. Instale [ReVanced GmsCore](https://github.com/ReVanced/GmsCore/releases)
2. Abra GmsCore → Self-Check → ative todas as permissões
3. Ative "Battery optimization ignored"
4. Aplique o patch **GmsCore Support** com `gmsCoreVendorGroupId = app.revanced`
5. Instale e faça login

---

## 📁 Estrutura

```
rhyan57-patches/
├── patches/src/main/kotlin/app/revanced/patches/youtube/
│   ├── misc/gms/               ← GmsCore Support
│   ├── player/download/        ← Video Download
│   └── announcements/          ← Rhyan57 Announcement
├── extensions/src/main/java/app/revanced/extension/youtube/
│   ├── player/download/        ← VideoDownloadHelper.java
│   └── announcements/          ← Rhyan57AnnouncementHelper.java
├── settings.gradle.kts
├── gradle/libs.versions.toml
└── .github/workflows/release.yml
```

---

## 📜 Licença

GPL-3.0 — veja [LICENSE](LICENSE)

---

## 💬 Contato

Discord: [discord.gg/rhyan57](https://dsc.gg/BetterStar)
