package jp.co.techfun.playtube;

import java.util.List;
import java.util.ListIterator;

import jp.co.techfun.englishwordbook.CheckWordActivity;
import jp.co.techfun.englishwordbook.R;
import jp.co.techfun.englishwordbook.WordBean;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// YouTube���挟�����Activity
public class PlaytubeSampleActivity extends ListActivity {
	// �P��i�[�p���X�g�C���X�^���X
    private List<YoutBean> list;
    private FavoriteDbUtil dbUtil;
 // ListIterator�C���X�^���X
    private ListIterator<YoutBean> itr;
	
	
    // �C���e���g�̃f�[�^�󂯓n���L�[��`
    // YouTube����URL
    static enum IntentKey {
        MEDIA_URL, MEDIA_TITLE
    };

    // ��ԕێ��p�L�[��`
    private static enum BundleKey {
        SEARCH_KEYWORD
    };

    // onCreate���\�b�h(��ʏ����\���C�x���g)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ���C�A�E�g�ݒ�t�@�C���w��
        setContentView(R.layout.playtube_main);

        // �����L�[���[�h���͗pEditText�Ƀ��X�i�[�ݒ�
        EditText etKeyword = (EditText) findViewById(R.id.et_keyword);
        etKeyword.setOnFocusChangeListener(keywordInputOnFocusChangeListener);

        // �����{�^���Ƀ��X�i�[�ݒ�
        ImageButton ibtnSearch = (ImageButton) findViewById(R.id.ibtn_search);
        ibtnSearch.setOnClickListener(searchBtnOnClickListener);

        // �O�y�[�W( < )�{�^���Ƀ��X�i�[�ݒ�
        ImageButton ibtnPrev = (ImageButton) findViewById(R.id.ibtn_prev);
        ibtnPrev.setOnClickListener(prevBtnOnClickListener);

        // ���y�[�W( > )�{�^���Ƀ��X�i�[�ݒ�
        ImageButton ibtnNext = (ImageButton) findViewById(R.id.ibtn_next);
        ibtnNext.setOnClickListener(nextBtnOnClickListener);
        
        
     // �f�[�^�x�[�X����Youtube���C�ɓ���擾
        list = dbUtil.getFavorite();
        
        // �擾�����P���TextView�ɐݒ�
        itr = list.listIterator();
        if (itr.hasNext()) {
            tvYoutTitle = (TextView) findViewById(R.id.tv_title);
            tvYoutUrl = (TextView) findViewById(R.id.tv_url);
            YoutBean fav = itr.next();
            tvEnglishword.setText(fav.getYouttitle());
            tvJapaneseword.setText(fav.getYouturl());
            // �o�^���ꂽ�P�ꂪ�Ȃ��ꍇ�A���b�Z�[�W���g�[�X�g�\��
        } else {
            Toast.makeText(PlaytubeSampleActivity.this, "�킩��Ȃ�",
                Toast.LENGTH_SHORT).show();
        }
    }

    // onSaveInstanceState���\�b�h�i��ԕێ�����)
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // �����L�[���[�hEditText�擾
        EditText etKeyword = (EditText) findViewById(R.id.et_keyword);

        // �����L�[���[�h��ۑ�
        outState.putString(BundleKey.SEARCH_KEYWORD.name(), etKeyword.getText()
            .toString());
    }

    // onRestoreInstanceState���\�b�h�i��ԕ��A�����j
    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        // �����L�[���[�h���擾
        String keyword = state.getString(BundleKey.SEARCH_KEYWORD.name());

        // �����L�[���[�h��EditText�ɐݒ�
        EditText etKeyword = (EditText) findViewById(R.id.et_keyword);
        etKeyword.setText(keyword);

        // �������ʂ��擾
        List<YouTubeVideoItem> resultList =
            YouTubeDataUtil.getInstance().getLastResutList();

        // ���惊�X�g��\��
        setSearchResult(resultList);
    }

    // setSearchResult���\�b�h�i�������ʂ̃��X�g�ݒ菈��)
    private void setSearchResult(List<YouTubeVideoItem> items) {

        // �A�_�v�^�N���X�̃C���X�^���X����
        ListAdapter adapter =
            new VideoListAdapter(this, R.layout.playtube_main, items);

        // �A�_�v�^�ݒ�
        setListAdapter(adapter);

        // ���惊�X�g�փt�H�[�J�X
        getListView().requestFocus();
    }

    // onListItemClick���\�b�h(���惊�X�g���1���I������)
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        // �N���b�N��������̃A�C�e�����擾
        YouTubeVideoItem item =
            (YouTubeVideoItem) l.getItemAtPosition(position);

        // YouTube����Đ�Activity���N��
        Intent intent = new Intent(this, VideoPlayerActivity.class);

        // �C���e���g�p�����[�^�ɍĐ��Ώۓ����URL��ݒ�
        intent.putExtra(IntentKey.MEDIA_URL.name(), item.getMpeg4spURL());

        // �C���e���g�p�����[�^�ɓ���^�C�g����ݒ�
        intent.putExtra(IntentKey.MEDIA_TITLE.name(), item.getTitle());

        // Activity��\��
        startActivity(intent);
    }

    // �����L�[���[�h�t�H�[�J�X�ύX�����X�i�[��`
    private OnFocusChangeListener keywordInputOnFocusChangeListener =
        new OnFocusChangeListener() {
            // onFocusChange���\�b�h(�t�H�[�J�X�ύX���C�x���g)
            @Override
            public void onFocusChange(View v, boolean isFocused) {
                // �O�y�[�W( < )�{�^���Ǝ��y�[�W( > )�{�^���I�u�W�F�N�g�擾
                ImageButton ibtnPrev =
                    (ImageButton) findViewById(R.id.ibtn_prev);
                ImageButton ibtnNext =
                    (ImageButton) findViewById(R.id.ibtn_next);
                // �����L�[���[�h�Ƀt�H�[�J�X���ꂽ�ꍇ
                if (isFocused) {
                    // Prev�ENext�{�^�����\��
                    ibtnPrev.setVisibility(View.GONE);
                    ibtnNext.setVisibility(View.GONE);
                } else {
                    // Prev�ENext�{�^����\��
                    ibtnPrev.setVisibility(View.VISIBLE);
                    ibtnNext.setVisibility(View.VISIBLE);
                }
            }
        };

    // �����{�^���N���b�N���X�i�[��`
    private OnClickListener searchBtnOnClickListener = new OnClickListener() {
        // onClick���\�b�h(�{�^���N���b�N���C�x���g)
        @Override
        public void onClick(View v) {
            // �����L�[���[�h���擾
            EditText etKeyword = (EditText) findViewById(R.id.et_keyword);

            // �������ʂ��擾
            List<YouTubeVideoItem> items =
                YouTubeDataUtil.getInstance().getSearchResult(
                    etKeyword.getText().toString());

            // �������ʂ�\��
            setSearchResult(items);
        }
    };

    // �O�y�[�W( < )�{�^���{�^���N���b�N���X�i�[��`
    private OnClickListener prevBtnOnClickListener = new OnClickListener() {
        // onClick���\�b�h(�{�^���N���b�N���C�x���g)
        @Override
        public void onClick(View v) {

            // �������ʂ̑O�̃y�[�W���擾
            List<YouTubeVideoItem> items =
                YouTubeDataUtil.getInstance().getPrevPage();

            // �������ʂ�\��
            setSearchResult(items);
        }
    };

    // ���y�[�W( > )�{�^���{�^���N���b�N���X�i�[��`
    private OnClickListener nextBtnOnClickListener = new OnClickListener() {
        // onClick���\�b�h(�{�^���N���b�N���C�x���g)
        @Override
        public void onClick(View v) {

            // �������ʂ̎��̃y�[�W���擾
            List<YouTubeVideoItem> items =
                YouTubeDataUtil.getInstance().getNextPage();

            // �������ʂ�\��
            setSearchResult(items);
        }
    };
}
