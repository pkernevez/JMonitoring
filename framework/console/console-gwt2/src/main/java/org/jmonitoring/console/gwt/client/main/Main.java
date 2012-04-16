package org.jmonitoring.console.gwt.client.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class Main extends Composite
{

    private static MainUiBinder uiBinder = GWT.create(MainUiBinder.class);
    @UiField SimplePanel content;



    interface MainUiBinder extends UiBinder<Widget, Main>
    {
    }

    public Main()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }


    public Main(String firstName)
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public SimplePanel getContent()
    {
        return content;
    }


    public void setContent(SimplePanel content)
    {
        this.content = content;
    }

}
