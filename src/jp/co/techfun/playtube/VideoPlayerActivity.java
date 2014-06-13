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

// ����Đ����Activity
public class VideoPlayerActivity extends Activity {
	// YOUTUBE_DB�C���X�^���X
    private FavoriteDbUtil dbUtil;
	String urlString = null;
	String title = null;
    // onCreate���\�b�h(��ʏ����\���C�x���g)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ���C�A�E�g�ݒ�t�@�C���w��
        setContentView(R.layout.video_player);

        // ���ȉ��̂ǂ��炩���R�����g�A�E�g���Ďg�p���Ă��������B
        // ***************************************
        // MediaPlayer�N���X���g�p��������Đ�
        // useMediaPlayer();
        // ***************************************
        // VideoPlayer�N���X���g�p��������Đ�
        useVideoPlayer();
        // ***************************************
        
        
     // �u���C�ɓ���v�{�^���ɃN���b�N���X�i�[�ݒ�
     		Button btnFav = (Button) findViewById(R.id.button_fav);
     		btnFav.setOnClickListener(btnFavClickListener);
     		
     	// DBUtil�C���X�^���X����
            dbUtil = new FavoriteDbUtil(this);
    }
    
    
 // ���C�ɓ���{�^���N���b�N���X�i�[��`
 	private OnClickListener btnFavClickListener = new OnClickListener() {
 		@Override
 		public void onClick(View v) {

 			
			// �A���[�����I���_�C�A���O�\��
// 			Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
// 			startActivityForResult(intent, REQUEST_CODE_RINGTONE_PICKER);
 		// youtBean�C���X�^���X����
            YoutBean fav =
                new YoutBean(title,urlString);

            // DB��title url��o�^
            dbUtil.addFavotite(fav);
 			Toast.makeText(VideoPlayerActivity.this,
 					title+urlString, Toast.LENGTH_SHORT)
                    .show();
 		}
 	};

    // useVideoPlayer���\�b�h�iVideoPlayer�N���X���g�p��������Đ�����)
    private void useVideoPlayer() {
        VideoView vvPlayer = (VideoView) findViewById(R.id.vv_player);

        // �����URI���擾
//        String urlString =
        urlString =
            getIntent().getStringExtra(
                PlaytubeSampleActivity.IntentKey.MEDIA_URL.name());

        // �����URI��ݒ�
        vvPlayer.setVideoURI(Uri.parse(urlString));

        // ����^�C�g�����擾
//        String title =
        title =
            getIntent().getStringExtra(
                PlaytubeSampleActivity.IntentKey.MEDIA_TITLE.name());

        // �^�C�g���o�[�ɓ���^�C�g����ݒ�
        setTitle(title);

        // ���f�B�A�R���g���[����ݒ�
        vvPlayer.setMediaController(new MediaController(this));

        // ������Đ�
        vvPlayer.start();
    }

    // useMediaPlayer���\�b�h�iMediaPlayer�N���X���g�p��������Đ������j
    private void useMediaPlayer() {
        // ���f�B�A�v���[���[�C���^���X����
        final MediaPlayer mp = new MediaPlayer();

        // �����URL���擾
        final String urlString =
            getIntent().getStringExtra(
                PlaytubeSampleActivity.IntentKey.MEDIA_URL.name());

        // �T�[�t�F�C�X�r���[�擾
        final SurfaceView sv = (SurfaceView) findViewById(R.id.vv_player);

        // �T�[�t�F�C�X�z���_�擾
        final SurfaceHolder sh = sv.getHolder();
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // ���f�B�A�v���[���[�ɃT�[�t�F�C�X�z���_�ݒ�
        mp.setDisplay(sh);

        sh.addCallback(new Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // �����~
                mp.stop();
                mp.release();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {

                    // �����URL��ݒ�
                    mp.setDataSource(urlString);

                    // ����Đ�����
                    mp.prepare();

                    // ���������16:9�ɕϊ�
                    int w = getWindowManager().getDefaultDisplay().getWidth();
                    int h = w / 16 * 9;
                    sv.layout(0, 0, w, h);

                    // ����Đ�
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
