package org.jmonitoring.console.gwt.client.methodcall.search;

import it.pianetatecno.gwt.utility.client.table.Column;

import org.jmonitoring.console.gwt.shared.method.MethodCallSearchExtractDTO;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.Image;

public class ColumnFlowIdAction extends Column<MethodCallSearchExtractDTO>
{
    Image image;

    public ColumnFlowIdAction()
    {
        super();
    }

    public ColumnFlowIdAction(String pTitle, String pActionName, Image pImage, HorizontalAlignmentConstant pAlignement,
        String pPropertyName)
    {
        super(pTitle, pPropertyName, true, pAlignement, pActionName);
        image = pImage;
    }

    @Override
    public HTML getValue(MethodCallSearchExtractDTO pValue)
    {
        return new HTML(image.getElement().getString() + pValue.getFlowId());
    }

    @Override
    public String getStyle()
    {
        return "table-td-icon";
    }
}
