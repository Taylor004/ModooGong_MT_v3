package kr.co.bravecompany.api.android.stdapp.api.data;

/**
 * Created by BraveCompany on 2017. 4. 17..
 */

public class BkCodeVO {
    private String CD;
    private String NM;
    private String TCH_CD;

    public BkCodeVO(String CD, String NM, String TCH_CD) {
        this.CD = CD;
        this.NM = NM;
        this.TCH_CD = TCH_CD;
    }

    public String getCD() {
        return CD;
    }

    public String getNM() {
        return NM;
    }

    public String getTCH_CD() {
        return TCH_CD;
    }
}
