package org.jmonitoring.console.gwt.client.panel.flow;

import org.jmonitoring.console.gwt.client.JMonitoring;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **************************************************************************/

public class ImportPanel extends FormPanel
{
    public ImportPanel()
    {
        setAction("/ImportXml");

        // Because we're going to add a FileUpload widget, we'll need to set the
        // form to use the POST method, and multipart MIME encoding.
        setEncoding(FormPanel.ENCODING_MULTIPART);
        setMethod(FormPanel.METHOD_POST);
        VerticalPanel panel = new VerticalPanel();
        setWidget(panel);

        // Create a FileUpload widget.
        FileUpload upload = new FileUpload();
        upload.setName("file");
        panel.add(upload);

        // Add a 'submit' button.
        panel.add(new Button("Submit", new ClickListener()
        {
            public void onClick(Widget sender)
            {
                submit();
            }
        }));

        // Add an event handler to the form.
        addFormHandler(new FormHandler()
        {
            public void onSubmit(FormSubmitEvent event)
            {
            }

            public void onSubmitComplete(FormSubmitCompleteEvent event)
            {
                JMonitoring.setContentMain(new HTML("<h2 class=\"info\">Flow successfully imported</h2><BR/>" + "Id="
                    + event.getResults()));
            }
        });

    }

}
