package kr.co.bravecompany.api.android.stdapp.api.data;

/**
 * Created by BraveCompany on 2016. 11. 15..
 */

public class ChrCodeVO {
    private String CD;
    private String NM;
    private String TCH_CD;
    private String salCate;

    public ChrCodeVO(String CD, String NM, String TCH_CD, String salCate) {
        this.CD = CD;
        this.NM = NM;
        this.TCH_CD = TCH_CD;
        this.salCate = salCate;
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

    public String getSalCate() {
        return salCate;
    }
}
