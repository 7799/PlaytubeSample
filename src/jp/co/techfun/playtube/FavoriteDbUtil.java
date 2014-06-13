package jp.co.techfun.playtube;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// �f�[�^�x�[�X�֘A�����N���X
public class FavoriteDbUtil {

    // �w���p�[�C���X�^���X
    private SQLiteOpenHelper helper;

    // DB�C���X�^���X
    private SQLiteDatabase db;

    // DB��
    private static final String DB_NAME = "YoutubeDB";

    // �e�[�u����
    private static final String TABLE_NAME = "FavYoutube";

    // �J������(�ʔ�)
    private static final String C_ID = "_id";
    // �J������(�p�P��)
    private static final String C_YOUTUBE_TITLE = "youttitle";
    // �J������(���{���)
    private static final String C_YOUTUBE_URL = "youturl";

    // �R���X�g���N�^
    public FavoriteDbUtil(Context con) {
        // DB�쐬
        helper = new SQLiteOpenHelper(con, DB_NAME, null, 1) {
            @Override
            public void onUpgrade(SQLiteDatabase database, int oldVersion,
                int newVersion) {
                // �����Ȃ�
            }

            @Override
            public void onCreate(SQLiteDatabase database) {
                // �����Ȃ�
            }
        };

        // �e�[�u���쐬
        try {
            // DB�C���X�^���X�擾
            db = helper.getWritableDatabase();

            // SQL����
            StringBuilder sql = new StringBuilder();
            sql.append("create table " + TABLE_NAME + "(");
            // �ʔ�(�����̔�)
            sql.append(C_ID + " integer primary key autoincrement,");
            // �p�P��
            sql.append(C_YOUTUBE_TITLE + " text not null,");
            // ���{���
            sql.append(C_YOUTUBE_URL + " text not null");
            sql.append(")");

            // SQL���s
            db.execSQL(sql.toString());
        } catch (Throwable th) {
            Log.w(getClass().getSimpleName(), "�e�[�u���̍쐬�Ɏ��s���܂����B", th);
        } finally {
            db.close();
        }
    }

    // addWord���\�b�h�i�P����o�^�����j
    public void addFavotite(YoutBean fav) {
        try {
            // DB�C���X�^���X�擾
            db = helper.getWritableDatabase();

            // SQL����
            StringBuilder sql = new StringBuilder();
            sql.append("insert into " + TABLE_NAME + " values (");
            // �ʔ�(�����̔�)
            sql.append("null,");
            // �ܓx
            sql.append("'" + fav.getYouttitle() + "',");
            // �R�����g
            sql.append("'" + fav.getYouturl() + "'");
            sql.append(")");

            // SQL���s
            db.execSQL(sql.toString());
        } catch (Throwable th) {
            Log.w(getClass().getSimpleName(), "�e�[�u���ւ̃f�[�^�o�^�Ɏ��s���܂����B", th);
        } finally {
            db.close();
        }
    }

    // getWordList���\�b�h�i�P����擾�����j
    public List<YoutBean> getFavorite() {
        // �擾�����P��i�[�p���X�g
        List<YoutBean> youtList = new ArrayList<YoutBean>();

        // �f�[�^�擾�p�J�[�\��
        Cursor cursor = null;
        try {
            // DB�C���X�^���X�擾
            db = helper.getWritableDatabase();

            // �e�[�u������擾����񖼂��`
            String[] columns = { C_YOUTUBE_TITLE, C_YOUTUBE_URL };

            // �f�[�^�擾
            cursor =
                db.query(TABLE_NAME, columns, null, null, null, null,
                		C_YOUTUBE_TITLE);

            // �擾�����f�[�^�����X�g�Ɋi�[
            while (cursor.moveToNext()) {
            	YoutBean fav =
                    new YoutBean(cursor.getString(0), cursor.getString(1));
            	youtList.add(fav);
            }
        } catch (Throwable th) {
            Log.w(getClass().getSimpleName(), "�e�[�u���f�[�^�̎擾�Ɏ��s���܂����B", th);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        // �P�ꃊ�X�g��Ԃ�
        return youtList;
    }
}
