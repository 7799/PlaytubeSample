package jp.co.techfun.playtube;

// title/url JavaBeans
public class YoutBean {
	//  title
	private String youttitle;
	//  url
	private String youturl;
	
	// �R���X�g���N�^
    public YoutBean(String youttitle, String youturl) {

        // title��ݒ�
        this.youttitle = youttitle;

        // url��ݒ�
        this.youturl = youturl;
    }
	
	// title��Ԃ����\�b�h
    public String getYouttitle() {
        return youttitle;
    }
    
    // title��ݒ肷�郁�\�b�h
    public void setYouttitle(String youttitle) {
        this.youttitle = youttitle;
    }
    
    // url��Ԃ����\�b�h
    public String getYouturl() {
        return youturl;
    }
    
    // url��ݒ肷�郁�\�b�h
    public void setYouturl(String youturl) {
        this.youturl = youturl;
    }
}
