package jp.co.techfun.playtube;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// データベース関連処理クラス
public class FavoriteDbUtil {

    // ヘルパーインスタンス
    private SQLiteOpenHelper helper;

    // DBインスタンス
    private SQLiteDatabase db;

    // DB名
    private static final String DB_NAME = "YoutubeDB";

    // テーブル名
    private static final String TABLE_NAME = "FavYoutube";

    // カラム名(通番)
    private static final String C_ID = "_id";
    // カラム名(英単語)
    private static final String C_YOUTUBE_TITLE = "youttitle";
    // カラム名(日本語訳)
    private static final String C_YOUTUBE_URL = "youturl";

    // コンストラクタ
    public FavoriteDbUtil(Context con) {
        // DB作成
        helper = new SQLiteOpenHelper(con, DB_NAME, null, 1) {
            @Override
            public void onUpgrade(SQLiteDatabase database, int oldVersion,
                int newVersion) {
                // 処理なし
            }

            @Override
            public void onCreate(SQLiteDatabase database) {
                // 処理なし
            }
        };

        // テーブル作成
        try {
            // DBインスタンス取得
            db = helper.getWritableDatabase();

            // SQL生成
            StringBuilder sql = new StringBuilder();
            sql.append("create table " + TABLE_NAME + "(");
            // 通番(自動採番)
            sql.append(C_ID + " integer primary key autoincrement,");
            // 英単語
            sql.append(C_YOUTUBE_TITLE + " text not null,");
            // 日本語訳
            sql.append(C_YOUTUBE_URL + " text not null");
            sql.append(")");

            // SQL実行
            db.execSQL(sql.toString());
        } catch (Throwable th) {
            Log.w(getClass().getSimpleName(), "テーブルの作成に失敗しました。", th);
        } finally {
            db.close();
        }
    }

    // addWordメソッド（単語情報登録処理）
    public void addFavotite(YoutBean fav) {
        try {
            // DBインスタンス取得
            db = helper.getWritableDatabase();

            // SQL生成
            StringBuilder sql = new StringBuilder();
            sql.append("insert into " + TABLE_NAME + " values (");
            // 通番(自動採番)
            sql.append("null,");
            // 緯度
            sql.append("'" + fav.getYouttitle() + "',");
            // コメント
            sql.append("'" + fav.getYouturl() + "'");
            sql.append(")");

            // SQL実行
            db.execSQL(sql.toString());
        } catch (Throwable th) {
            Log.w(getClass().getSimpleName(), "テーブルへのデータ登録に失敗しました。", th);
        } finally {
            db.close();
        }
    }

    // getWordListメソッド（単語情報取得処理）
    public List<YoutBean> getFavorite() {
        // 取得した単語格納用リスト
        List<YoutBean> youtList = new ArrayList<YoutBean>();

        // データ取得用カーソル
        Cursor cursor = null;
        try {
            // DBインスタンス取得
            db = helper.getWritableDatabase();

            // テーブルから取得する列名を定義
            String[] columns = { C_YOUTUBE_TITLE, C_YOUTUBE_URL };

            // データ取得
            cursor =
                db.query(TABLE_NAME, columns, null, null, null, null,
                		C_YOUTUBE_TITLE);

            // 取得したデータをリストに格納
            while (cursor.moveToNext()) {
            	YoutBean fav =
                    new YoutBean(cursor.getString(0), cursor.getString(1));
            	youtList.add(fav);
            }
        } catch (Throwable th) {
            Log.w(getClass().getSimpleName(), "テーブルデータの取得に失敗しました。", th);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        // 単語リストを返す
        return youtList;
    }
}
