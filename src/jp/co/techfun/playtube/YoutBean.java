package jp.co.techfun.playtube;

// title/url JavaBeans
public class YoutBean {
	//  title
	private String youttitle;
	//  url
	private String youturl;
	
	// コンストラクタ
    public YoutBean(String youttitle, String youturl) {

        // titleを設定
        this.youttitle = youttitle;

        // urlを設定
        this.youturl = youturl;
    }
	
	// titleを返すメソッド
    public String getYouttitle() {
        return youttitle;
    }
    
    // titleを設定するメソッド
    public void setYouttitle(String youttitle) {
        this.youttitle = youttitle;
    }
    
    // urlを返すメソッド
    public String getYouturl() {
        return youturl;
    }
    
    // urlを設定するメソッド
    public void setYouturl(String youturl) {
        this.youturl = youturl;
    }
}
