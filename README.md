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

#zip解凍を実行するときのバッファサイズ
buffer-size: 1024

#ワールドを解凍した後に自動的に読み込みを実行するかどうかの設定
#false 推奨
load-worlds: false
```

##### Example of API usage:
```java
public class ExamplePlugin extends JavaPlugin {

	private static ExamplePlugin instance;
	private WorldChefAPI worldChefAPI;

	@Override
	public void onEnable() {
		instance = this;
		worldChefAPI = new GlowingAPI(instance);

		String zipFolderPath = "maps";
		String worldFolderPath = "";
		boolean showLogs = true;
		boolean showErrors = true;
		worldChefAPI = new WorldChefAPI(this, zipFolderPath, worldFolderPath, showLogs, showErrors);
		worldChefAPI.loadZips();
		worldChefAPI.unZip(1024);
	}

	public static ExamplePlugin getInstance() {
		return instance;
	}

	public WorldChefAPI getWorldChefAPI() {
		return worldChefAPI;
	}

}
```

## License
https://github.com/Be4rJP/WorldChef/blob/master/LICENSE

## Releases
https://github.com/Be4rJP/WorldChef/releases