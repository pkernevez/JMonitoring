package org.jmonitoring.console.gwt.client.common.message;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Message extends Composite
{

    private static MessageUiBinder uiBinder = GWT.create(MessageUiBinder.class);

    @UiField
    Label message;

    interface MessageUiBinder extends UiBinder<Widget, Message>
    {
    }

    public Message()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setMessage(String pMessage)
    {
        message.setText(pMessage);
    }

}
