# ChatTranslate

Minecraft (Spigot/Paper) サーバー向けの、リアルタイムチャット翻訳プラグインです。
プレイヤーごとに設定した言語へ、サーバー内のチャットを自動で翻訳して表示します。

## 🌟 特徴

- **DeepL & Azure API 対応**: 高精度なDeepL翻訳、またはAzure Translator APIを使用可能。
- **スマートなAUTOモード**: `config.yml` に設定したAPIキーを自動で判別。片方だけでも動作し、両方設定した場合はDeepLを優先してAzureを予備として使います。
- **API通信の最適化 (N+1問題対策済み)**: 同じ言語に翻訳するプレイヤーが複数いる場合でも、APIの呼び出しは各言語につき1回のみ！無料枠のクオータを無駄に消費しません。
- **幅広い互換性**: Java 11ベースでビルドされており、Minecraft **1.14 〜 1.21.x** の幅広いバージョンで動作します。

## 📥 導入方法

1. リリースページ（またはビルドした） `ChatTranslate-1.0-SNAPSHOT.jar` をサーバーの `plugins` フォルダに入れます。
2. サーバーを起動（または再起動）すると、`plugins/ChatTranslate/config.yml` が生成されます。
3. `config.yml` を開き、DeepLまたはAzureのAPIキーを入力します。
4. サーバー内で `/chat reload` コマンドを実行するか、サーバーを再起動して設定を反映させます。

## 💬 コマンド

| コマンド | 説明 | 権限 |
| :--- | :--- | :--- |
| `/chat lang <EN|JA>` | 翻訳先のターゲット言語を設定します | 誰でも |
| `/chat on` | 自分のチャット翻訳を有効化します | 誰でも |
| `/chat off` | 自分のチャット翻訳を無効化します | 誰でも |
| `/chat reload` | `config.yml` を再読み込みし、API設定を適用します | OP または `chattranslate.reload` |

> 💡 **Tip:** `/chat` コマンドは便利な **Tab補完** に対応しています！入力途中でTabキーを押すと候補が表示されます。

## ⚙️ config.yml の設定例

```yaml
# Translator Mode (AUTO を推奨します)
# AUTO: 設定されているAPIキーを自動判別します。両方ある場合はHYBRID（DeepL優先）になります。
translator-mode: "AUTO"

# DeepL API Key (https://www.deepl.com/pro-api)
# 使わない場合は空欄にしてください
deepl-api-key: "YOUR_DEEPL_API_KEY"

# Azure Translator API Key
# 使わない場合は空欄にしてください
azure-api-key: ""
azure-region: "global"

# デフォルトのターゲット言語 (ISO 639-1)
default-language: "EN"
```
