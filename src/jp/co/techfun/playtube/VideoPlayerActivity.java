package jp.co.techfun.playtube;

import java.io.IOException;






//import jp.co.techfun.englishwordbook.EnglishWordBookDbUtil;
//import jp.co.techfun.englishwordbook.WordBean;
//import jp.co.techfun.englishwordbook.CheckPronounceActivity;
//import jp.co.techfun.englishwordbook.R;
//import jp.co.techfun.alarmclock.R;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

// 動画再生画面Activity
public class VideoPlayerActivity extends Activity {
	// YOUTUBE_DBインスタンス
    private FavoriteDbUtil dbUtil;
	String urlString = null;
	String title = null;
    // onCreateメソッド(画面初期表示イベント)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // レイアウト設定ファイル指定
        setContentView(R.layout.video_player);

        // ※以下のどちらかをコメントアウトして使用してください。
        // ***************************************
        // MediaPlayerクラスを使用した動画再生
        // useMediaPlayer();
        // ***************************************
        // VideoPlayerクラスを使用した動画再生
        useVideoPlayer();
        // ***************************************
        
        
     // 「お気に入り」ボタンにクリックリスナー設定
     		Button btnFav = (Button) findViewById(R.id.button_fav);
     		btnFav.setOnClickListener(btnFavClickListener);
     		
     	// DBUtilインスタンス生成
            dbUtil = new FavoriteDbUtil(this);
    }
    
    
 // お気に入りボタンクリックリスナー定義
 	private OnClickListener btnFavClickListener = new OnClickListener() {
 		@Override
 		public void onClick(View v) {

 			
			// アラーム音選択ダイアログ表示
// 			Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
// 			startActivityForResult(intent, REQUEST_CODE_RINGTONE_PICKER);
 		// youtBeanインスタンス生成
            YoutBean fav =
                new YoutBean(title,urlString);

            // DBへtitle urlを登録
            dbUtil.addFavotite(fav);
 			Toast.makeText(VideoPlayerActivity.this,
 					title+urlString, Toast.LENGTH_SHORT)
                    .show();
 		}
 	};

    // useVideoPlayerメソッド（VideoPlayerクラスを使用した動画再生処理)
    private void useVideoPlayer() {
        VideoView vvPlayer = (VideoView) findViewById(R.id.vv_player);

        // 動画のURIを取得
//        String urlString =
        urlString =
            getIntent().getStringExtra(
                PlaytubeSampleActivity.IntentKey.MEDIA_URL.name());

        // 動画のURIを設定
        vvPlayer.setVideoURI(Uri.parse(urlString));

        // 動画タイトルを取得
//        String title =
        title =
            getIntent().getStringExtra(
                PlaytubeSampleActivity.IntentKey.MEDIA_TITLE.name());

        // タイトルバーに動画タイトルを設定
        setTitle(title);

        // メディアコントローラを設定
        vvPlayer.setMediaController(new MediaController(this));

        // 動画を再生
        vvPlayer.start();
    }

    // useMediaPlayerメソッド（MediaPlayerクラスを使用した動画再生処理）
    private void useMediaPlayer() {
        // メディアプレーヤーインタンス生成
        final MediaPlayer mp = new MediaPlayer();

        // 動画のURLを取得
        final String urlString =
            getIntent().getStringExtra(
                PlaytubeSampleActivity.IntentKey.MEDIA_URL.name());

        // サーフェイスビュー取得
        final SurfaceView sv = (SurfaceView) findViewById(R.id.vv_player);

        // サーフェイスホルダ取得
        final SurfaceHolder sh = sv.getHolder();
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // メディアプレーヤーにサーフェイスホルダ設定
        mp.setDisplay(sh);

        sh.addCallback(new Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // 動画停止
                mp.stop();
                mp.release();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {

                    // 動画のURLを設定
                    mp.setDataSource(urlString);

                    // 動画再生準備
                    mp.prepare();

                    // 横幅を基準に16:9に変換
                    int w = getWindowManager().getDefaultDisplay().getWidth();
                    int h = w / 16 * 9;
                    sv.layout(0, 0, w, h);

                    // 動画再生
                    mp.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                int width, int height) {
            }
        });
    }
}
