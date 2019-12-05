package kr.co.bravecompany.modoogong.android.stdapp.view;

import android.content.Intent;
import android.view.View;

import org.sufficientlysecure.htmltextview.ClickableTableSpan;

import kr.co.bravecompany.modoogong.android.stdapp.activity.WebViewDialogActivity;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;

/**
 * Created by BraveCompany on 2017. 3. 2..
 */

public class ClickableTableSpanImpl extends ClickableTableSpan {
    @Override
    public ClickableTableSpan newInstance() {
        return new ClickableTableSpanImpl();
    }

    @Override
    public void onClick(View widget) {
        Intent intent = new Intent(widget.getContext(), WebViewDialogActivity.class);
        intent.putExtra(Tags.TAG_HTML, getTableHtml());
        widget.getContext().startActivity(intent);
    }
}
