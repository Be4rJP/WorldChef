# WorldChef

起動時にワールドのzipファイルを解凍して読み込むプラグイン (API機能付き)


## How to use

* サーバーのpluginsフォルダにWorldChef.jarを入れる
* サーバーを起動する
* WorldChefフォルダとその中にconfig.ymlが生成されているので任意で書き換える
```yaml
#このプラグインの設定を読み込んで自動的にzipファイルの解凍をするかどうかの設定
#API機能のみを使いたい場合は false 推奨
stand-alone: true

#zipファイルが入っているフォルダと解凍したワールドを入れるためのフォルダ
#指定したパスの前にはサーバーを実行しているフォルダのパスが指定される
#world-folderは通常では空欄を指定
folder-path:
  zip-files: 'maps'
  world-folder: ''

#ログを出力するかどうかの設定
show-logs: true

#エラーログを出力するかどうかの設定
show-errors: true

#ZIP解凍を実行するときのバッファサイズ
buffer-size: 1024

#ワールドを解凍した後に読み込みを実行するかどうかの設定
load-worlds: true
```

## License
https://github.com/Be4rJP/WorldChef/blob/master/LICENSE

## Releases
https://github.com/Be4rJP/WorldChef/releases