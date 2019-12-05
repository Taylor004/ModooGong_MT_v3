package kr.co.bravecompany.api.android.stdapp.api.data;

import java.util.ArrayList;

/**
 * Created by BraveCompany on 2017. 10. 31..
 */

public class StudyFileDetailResult {
    private ViewInfoVO view_info;
    private ArrayList<FileInfoVO> file_info;

    public ViewInfoVO getView_info() {
        return view_info;
    }

    public ArrayList<FileInfoVO> getFile_info() {
        return file_info;
    }

    public class ViewInfoVO {
        private int no;
        private String title;
        private String content;
        private String wdate;
        private String chr_nm;

        public int getNo() {
            return no;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getWdate() {
            return wdate;
        }

        public String getChr_nm() {
            return chr_nm;
        }
    }

    public class FileInfoVO {
        private int file_no;
        private int pds_no;
        private String filename;
        private String upload_file;
        private String state;

        public int getFile_no() {
            return file_no;
        }

        public int getPds_no() {
            return pds_no;
        }

        public String getFilename() {
            return filename;
        }

        public String getUpload_file() {
            return upload_file;
        }

        public String getState() {
            return state;
        }
    }
}
