package org.jmonitoring.console.gwt.client.methodcall;

import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MethodCallDetail extends Composite implements Editor<MethodCallDTO>
{

    private static MethodCallDetailUiBinder uiBinder = GWT.create(MethodCallDetailUiBinder.class);

    interface MethodCallDetailUiBinder extends UiBinder<Widget, MethodCallDetail>
    {
    }

    private MethodCallDetailActivity presenter;

    public MethodCallDetail()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public MethodCallDetail setPresenter(MethodCallDetailActivity pActivity)
    {
        presenter = pActivity;
        return this;
    }

}
