package kr.co.bravecompany.api.android.stdapp.api.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by BraveCompany on 2016. 11. 15..
 */

public class BoardCateResult {

    private BoardCate boardCates;
    private Cate cates;
    private Tch tchs;
    private Chr chrs;
    private Bk bks;

    public BoardCate getBoardCates() {
        return boardCates;
    }

    public Cate getCates() {
        return cates;
    }

    public Tch getTchs() {
        return tchs;
    }

    public Chr getChrs() {
        return chrs;
    }

    public Bk getBks() {
        return bks;
    }

    public class BoardCate{
        private Board BOARD_CATE;

        public Board getBOARD_CATE() {
            return BOARD_CATE;
        }
    }

    public class Board {
        @SerializedName("03")
        private ArrayList<BoardCateVO> boardCatesStudyQA;
        @SerializedName("05")
        private ArrayList<BoardCateVO> boardCatesOneToOneQA;

        public ArrayList<BoardCateVO> getBoardCatesStudyQA() {
            return boardCatesStudyQA;
        }

        public ArrayList<BoardCateVO> getBoardCatesOneToOneQA() {
            return boardCatesOneToOneQA;
        }
    }

    public class Cate{
        private ArrayList<SalCateVO> SAL_CATE;

        public ArrayList<SalCateVO> getSAL_CATE() {
            return SAL_CATE;
        }
    }

    public class Tch{
        private ArrayList<TchCodeVO> TCH_CD;

        public ArrayList<TchCodeVO> getTCH_CD() {
            return TCH_CD;
        }
    }

    public class Chr{
        private ArrayList<ChrCodeVO> CHR_CD;

        public ArrayList<ChrCodeVO> getCHR_CD() {
            return CHR_CD;
        }
    }

    public class Bk{
        private ArrayList<BkCodeVO> BK_CD;

        public ArrayList<BkCodeVO> getBK_CD() {
            return BK_CD;
        }
    }

}
