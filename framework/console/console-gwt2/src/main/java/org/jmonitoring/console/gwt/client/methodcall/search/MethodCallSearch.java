package org.jmonitoring.console.gwt.client.methodcall.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class MethodCallSearch extends Composite implements HasText
{

    private static MethodCallSearchUiBinder uiBinder = GWT.create(MethodCallSearchUiBinder.class);

    interface MethodCallSearchUiBinder extends UiBinder<Widget, MethodCallSearch>
    {
    }

    public MethodCallSearch()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    Button button;

    private MethodCallSearchActivity presenter;

    public MethodCallSearch(String firstName)
    {
        initWidget(uiBinder.createAndBindUi(this));
        button.setText(firstName);
        Window.setTitle("Method call detail");
    }

    @UiHandler("button")
    void onClick(ClickEvent e)
    {
        Window.alert("Hello!");
    }

    public void setText(String text)
    {
        button.setText(text);
    }

    public String getText()
    {
        return button.getText();
    }

    public MethodCallSearch setPresenter(MethodCallSearchActivity pActivity)
    {
        presenter = pActivity;
        return this;
    }

}
