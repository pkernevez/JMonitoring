package org.jmonitoring.console.gwt.client.methodcall.search;

import it.pianetatecno.gwt.utility.client.table.Column;

import org.jmonitoring.console.gwt.shared.method.MethodCallSearchExtractDTO;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.Image;

public class ColumnMethodPositionAction extends Column<MethodCallSearchExtractDTO>
{
    Image image;

    public ColumnMethodPositionAction()
    {
        super();
    }

    public ColumnMethodPositionAction(String pTitle, String pActionName, Image pImage,
        HorizontalAlignmentConstant pAlignement)
    {
        super(pTitle, null, false, pAlignement, pActionName);
        image = pImage;
    }

    @Override
    public HTML getValue(MethodCallSearchExtractDTO pValue)
    {
        return new HTML(image.getElement().getString() + pValue.getPosition());
    }

    @Override
    public String getStyle()
    {
        return "table-td-icon";
    }
}
