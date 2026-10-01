# ChatTranslate

Minecraftサーバー(Spigot/Paper)用のチャット翻訳プラグインです。
プレイヤーごとに設定した言語にチャットを自動翻訳します。

## 対応バージョン
1.14 - 1.21.x

## 特徴
- DeepL API と Azure Translator API に対応
- 同じ言語の人への翻訳リクエストをまとめることでAPI消費を節約
- 設定したAPIキーを自動判別して使用します

## インストール
1. `plugins`フォルダに `ChatTranslate.jar` を入れる
2. サーバーを起動する
3. `plugins/ChatTranslate/config.yml` にAPIキーを入力する
4. `/chat reload` を実行して適用する

## コマンド
- `/chat lang <EN|JA>` : 翻訳先の言語を設定
- `/chat on` : 翻訳をオンにする
- `/chat off` : 翻訳をオフにする
- `/chat reload` : configを再読み込み (要OP)

## Config例
```yaml
translator-mode: "AUTO"
deepl-api-key: ""
azure-api-key: ""
azure-region: "global"
default-language: "EN"
```
